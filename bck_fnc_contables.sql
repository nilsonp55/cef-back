 CREATE OR REPLACE FUNCTION public.fnc_transcciones_contables(p_fecha_inicio character varying, p_fecha_fin character varying, p_tipo_proceso character varying, p_estado integer, p_formato_fecha character varying)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
declare

registro record;

v_fecha_inicio date;

v_fecha_fin date;

v_errores int;

-- se traducen las transacciones internas en transacciones contables temporal
cursor_transacciones_contables_tmp cursor is 
select
	ti.id_transacciones_internas,
	ti.banco_aval,
	ti.fecha,
	ti.consecutivo_dia,
	ti.id_operacion,
	ti.id_generico,
	ti.tipo_transaccion,
	ti.codigo_moneda,
	ti.medio_pago,
	ti.valor,
	cc.cuenta_contable,
	cc.naturaleza,
	ti.tipo_proceso,
	'' as estado
from
	transacciones_internas ti,
	conf_contable_entidades cc
where
	ti.fecha between v_fecha_inicio and v_fecha_fin
	and ti.tipo_transaccion = cc.tipo_transaccion
	and TI.banco_aval = cc.banco_aval
	and ti.tipo_proceso = p_tipo_proceso
	and ti.estado = p_estado
	and (ti.tipo_operacion = cc.tipo_operacion
		or cc.tipo_operacion is null )
	and (ti.TIPO_IMPUESTO = cc.tipo_impuesto
		or cc.tipo_impuesto is null )
	and (ti.codigo_comision = cc.codigo_comision
		or cc.codigo_comision is null )
	and (ti.codigo_tdv = cc.codigo_tdv
		or cc.codigo_tdv is null )
	and (ti.medio_pago = cc.medio_pago
		or cc.medio_pago is null)
	and (ti.codigo_punto_banco_ext = cc.codigo_punto_banco_ext
		or cc.codigo_punto_banco_ext is null);

cursor_errores_contable_nuevos cursor is 
   select
	*
from
	public.errores_contables ec
where
	ec.fecha between v_fecha_inicio and v_fecha_fin;

begin 
	v_fecha_inicio := to_date(p_fecha_inicio, p_formato_fecha);

v_fecha_fin := to_date(p_fecha_fin, p_formato_fecha);

open cursor_transacciones_contables_tmp;
loop fetch cursor_transacciones_contables_tmp into registro;
exit when not found;

	
	raise notice 'registro %',
registro;
-- Se buscan transacciones internas sin débito y se graba error
insert
	into
	errores_contables (id_transacciones_internas,
	fecha,
	mensaje_error)
(
	select
		ti.id_transacciones_internas,
		ti.fecha,
		'Transacción sin Débito'
	from
		transacciones_internas ti
	where
		ti.id_transacciones_internas = registro.id_transacciones_internas
		and registro.naturaleza = (
		select
			valor_texto
		from
			public.dominio
		where
			dominio = 'NATURALEZA'
			and codigo = 'D')
		and ti.fecha between v_fecha_inicio and v_fecha_fin
		and ti.tipo_proceso = p_tipo_proceso
);
---- se buscan transacciones internas sin crédito y se graba error
insert
	into
	errores_contables (id_transacciones_internas,
	fecha,
	mensaje_error)
(
	select
		ti.id_transacciones_internas,
		ti.fecha,
		'Transacción sin Crédito'
	from
		transacciones_internas ti
	where
		ti.id_transacciones_internas = registro.id_transacciones_internas
		and registro.naturaleza = (
		select
			valor_texto
		from
			public.dominio
		where
			dominio = 'NATURALEZA'
			and codigo = 'C')
		and ti.fecha between v_fecha_inicio and v_fecha_fin
		and ti.tipo_proceso = p_tipo_proceso
);

---- se buscan transacciones internas com más de un débito se graba error
insert
	into
	errores_contables (id_transacciones_internas,
	fecha,
	mensaje_error)
(
	select
		ti.id_transacciones_internas,
		ti.fecha,
		'Transacción con más de un Débito'
	from
		transacciones_internas ti
	where
		ti.id_transacciones_internas = registro.id_transacciones_internas
		and registro.naturaleza = (
		select
			valor_texto
		from
			public.dominio
		where
			dominio = 'NATURALEZA'
			and codigo = 'D')
		and ti.fecha between v_fecha_inicio and v_fecha_fin
		and ti.tipo_proceso = p_tipo_proceso
	group by
		ti.id_transacciones_internas,
		ti.fecha
	having
		count(1) > 1 
);
--				
---- se buscan transacciones internas com más de un crédito, se graba error
insert
	into
	errores_contables (id_transacciones_internas,
	fecha,
	mensaje_error)
(
	select
		ti.id_transacciones_internas,
		ti.fecha,
		'Transacción con más de un Crédito'
	from
		transacciones_internas ti
	where
		ti.id_transacciones_internas = registro.id_transacciones_internas
		and registro.naturaleza = (
		select
			valor_texto
		from
			public.dominio
		where
			dominio = 'NATURALEZA'
			and codigo = 'C')
		and ti.fecha between v_fecha_inicio and v_fecha_fin
		and ti.tipo_proceso = p_tipo_proceso
	group by
		ti.id_transacciones_internas,
		ti.fecha
	having
		count(1) > 1 
);

	
	if exists (select
					count(1)
				from
					public.errores_contables ec
				where
					fecha between v_fecha_inicio and v_fecha_fin
					and ec.id_transacciones_internas = registro.id_transacciones_internas) then 

					update public.transacciones_internas set
						estado = 3
					where
						id_transacciones_internas = registro.id_transacciones_internas;
	else 
	
	
		INSERT INTO ctrefc.public.transacciones_contables
		(id_transacciones_internas, banco_aval, fecha, consecutivo_dia, id_operacion, id_generico, tipo_transaccion, codigo_moneda, medio_pago, valor, cuenta_contable, naturaleza, tipo_proceso)
		VALUES(registro.id_transacciones_internas, registro.banco_aval, registro.fecha, registro.consecutivo_dia, registro.id_operacion, registro.id_generico, registro.tipo_transaccion, registro.codigo_moneda, registro.medio_pago, registro.valor, registro.cuenta_contable, registro.naturaleza, registro.tipo_proceso);

	
	
	
	 end if;

end loop;

close cursor_transacciones_contables_tmp;


/*UPDATE
	PUBLIC.TRANSACCIONES_INTERNAS
SET
	ESTADO = 3
WHERE
	ID_TRANSACCIONES_INTERNAS IN (
	SELECT
		DISTINCT EC.ID_TRANSACCIONES_INTERNAS 
	FROM
		PUBLIC.ERRORES_CONTABLES EC
	WHERE
		FECHA BETWEEN V_FECHA_INICIO AND V_FECHA_FIN);*/

return true;
end
$function$
