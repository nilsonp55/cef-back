package com.ath.adminefectivo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.entities.BancoSimpleInfoEntity;

public interface IBancoSimpleInfoRepository extends JpaRepository<BancoSimpleInfoEntity, Integer>, QuerydslPredicateExecutor<BancoSimpleInfoEntity>  {
	
	@Query("SELECT new BancoSimpleInfoEntity(b.codigoPunto, b.abreviatura, CONCAT('BANCO ', b.nombreBanco), b.esAval) FROM BancoSimpleInfoEntity b WHERE b.esAval = '1'")
    List<BancoSimpleInfoEntity> findByEsAvalEqualsOne();
}
