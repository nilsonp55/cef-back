package com.ath.adminefectivo.auditoria.listener;


import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.PreRemove;
import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

import com.ath.adminefectivo.auditoria.context.AuditData;
import com.ath.adminefectivo.auditoria.context.AuditoriaContext;
import com.ath.adminefectivo.auditoria.utils.AuditReadyEvent;
import com.ath.adminefectivo.auditoria.utils.SpringContext;
import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.entities.audit.EntityChange;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


/**
 * Listener para capturar cambios en entidades (CREATE / UPDATE / DELETE)
 * - Guarda snapshot "antes" (postLoad) por clave class#id
 * - En postUpdate captura la foto "después" de forma robusta (safeSerialize o readFromDB)
 * - NO publica eventos por defecto (el filtro publicará el AuditReadyEvent con todo el AuditData)
 */
public class AuditoriaEntityListener {

    private static final Logger logger = LoggerFactory.getLogger(AuditoriaEntityListener.class);
    
    /**
     * ------------------ INICIALIZACIÓN DEL OBJECT MAPPER DEL AUDIT LISTENER ------------------
     *
     * Este bloque configura un ObjectMapper dedicado al AuditoriaEntityListener.
     * Su propósito principal es capturar de forma confiable el estado "antes" y "después"
     * de cada entidad para auditoría, evitando problemas típicos de serialización con JPA.
     * Se implementa de manera genérica para que funcione con cualquier entidad sin necesidad 
     * de crear MixIns específicos para cada una.
     *
     * Características principales:
     * 1) Serialización de LocalDateTime sin milisegundos/nanosegundos (solo hasta segundos)
     *    para mantener consistencia en los registros de auditoría.
     * 2) Evita timestamps numéricos para fechas, haciendo que la salida sea legible.
     * 3) Configura formato para java.util.Date (si existiera en las entidades).
     * 4) MixIns específicos opcionales para casos puntuales de clases que requieran ajustes.
     * 5) Ignora automáticamente:
     *    - Relaciones JPA (@ManyToOne, @OneToMany, @OneToOne, @ManyToMany)
     *    - Propiedades tipo colección o mapas
     *    - Otras entidades JPA (@Entity)
     *    Esto evita ciclos infinitos, lazy loading y sobrecarga en la auditoría.
     * 6) Ignora proxies/handlers de Hibernate globalmente, evitando errores con lazy loading.
     * -----------------------------------------------------------------------------------------
     */
    
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern(Constantes.FECHA_HORA_PATTERN_DD_MM_YYYY_T_HH_MM_SS);

    private static final ObjectMapper MAPPER;

    static {
        MAPPER = new ObjectMapper();

        // 1) JavaTimeModule + serializer para LocalDateTime (sin millis/nanos)
        JavaTimeModule jtm = new JavaTimeModule();
        jtm.addSerializer(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
            @Override
            public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeString(DATE_TIME_FORMATTER.format(value));
            }
        });
        MAPPER.registerModule(jtm);

        // 2) evitar timestamps numéricos
        MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // 3) Formato para java.util.Date (si existiera en entidades)
        MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        // 4) MixIn específico si se necesita para clases puntuales
        //MAPPER.addMixIn(Usuario.class, UsuarioAuditMixIn.class);

        // 5) Registrar un SimpleModule que remueva propiedades relacionadas JPA antes de serializar
        SimpleModule relationsModule = new SimpleModule("JpaRelationsAuditModule");
        relationsModule.setSerializerModifier(new BeanSerializerModifier() {
            @Override
            public List<BeanPropertyWriter> changeProperties(SerializationConfig config,
                                                             BeanDescription beanDesc,
                                                             List<BeanPropertyWriter> beanProperties) {
                List<BeanPropertyWriter> result = new ArrayList<>(beanProperties.size());
                for (BeanPropertyWriter bpw : beanProperties) {
                    boolean skip = false;

                    // 5.a) Si el miembro (getter/field) tiene anotaciones JPA -> skip
                    AnnotatedMember member = bpw.getMember();
                    if (member != null) {
                        if (member.hasAnnotation(ManyToOne.class)
                                || member.hasAnnotation(OneToMany.class)
                                || member.hasAnnotation(OneToOne.class)
                                || member.hasAnnotation(ManyToMany.class)
                                || member.hasAnnotation(Transient.class)) {
                            skip = true;
                        }
                    }

                    // 5.b) Si el tipo de la propiedad es colección o mapa -> skip (probable relación)
                    JavaType jt = bpw.getType();
                    Class<?> raw = jt.getRawClass();
                    if (!skip) {
                        if (Collection.class.isAssignableFrom(raw) || Map.class.isAssignableFrom(raw)) {
                            skip = true;
                        }
                    }

                    // 5.c) Si el tipo de la propiedad es otra entidad JPA (anotada con @Entity) -> skip
                    if (!skip) {
                        try {
                            if (raw.isAnnotationPresent(Entity.class)) {
                                skip = true;
                            }
                        } catch (Exception ignored) {
                            // seguridad: si algo falla en introspección, no rompemos la serialización
                        }
                    }

                    if (!skip) {
                        result.add(bpw);
                    } else {
                        // opcional: logger.debug("Audit: omitiendo propiedad {} de {}", bpw.getName(), beanDesc.getBeanClass().getSimpleName());
                    }
                }
                return result;
            }
        });
        MAPPER.registerModule(relationsModule);

        // 6) Ignorar proxies/handler de Hibernate globalmente en este mapper
        MAPPER.addMixIn(Object.class, HibernateMixIn.class);
    }

    // MixIn para evitar hibernateLazyInitializer y handler (ya lo tenías)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private abstract static class HibernateMixIn {}

    /**
     * ------------------ FIN DE INICIALIZACIÓN DEL OBJECT MAPPER DEL AUDIT LISTENER ------------------
     */

    private static final Map<String, String> ORIGINAL_STATES_BY_ID = new ConcurrentHashMap<>();
    /**
     * Si lo pones en true, el listener publicará eventos por cada cambio. 
     * Recomiendo mantenerlo en false y dejar que el filtro publique el evento final
     * (así garantizas que estadoHttp, peticion y respuesta estén presentes).
     */
    private static final boolean PUBLISH_EVENTS_FROM_ENTITY_LISTENER = false;

    private static ApplicationEventPublisher publisher;

    public static void setPublisher(ApplicationEventPublisher p) {
        publisher = p;
    }

    @PostLoad
    public void postLoad(Object entity) {
        AuditData audit = AuditoriaContext.get();
        if (audit == null) return;

        String id = extractId(entity);
        if (id == null) return;

        String key = keyFor(entity.getClass(), id);
        String snapshot = safeSerialize(entity);
        if (snapshot != null) {
            ORIGINAL_STATES_BY_ID.put(key, snapshot);
            logger.debug("postLoad: stored snapshot for key={} (len={})", key, snapshot.length());
        } else {
            logger.debug("postLoad: snapshot null for key={}", key);
        }
    }

    /**
     * PostUpdate: capturamos la foto "después" con más garantías.
     */
    @PostUpdate
    public void postUpdate(Object entity) {
        AuditData audit = AuditoriaContext.get();
        if (audit == null) return;

        Class<?> realClass = getRealClass(entity);
        String id = extractId(entity);
        String key = id != null ? keyFor(realClass, id) : null;

        // Foto "antes" desde el snapshot guardado en postLoad (si existe)
        String before = key != null ? ORIGINAL_STATES_BY_ID.get(key) : null;

        // Foto "después": primero intentamos serializar directamente la entidad
        String after = safeSerialize(entity);

        // Si safeSerialize falla, intentamos leer desde BD (fallback más fiable)
        if (after == null && id != null) {
            after = readFromDB(realClass, id);
            if (after != null) {
                logger.debug("postUpdate: obtenido 'after' desde DB para {}#{}", realClass.getSimpleName(), id);
            } else {
                logger.warn("postUpdate: no se pudo obtener 'after' para {}#{}", realClass.getSimpleName(), id);
            }
        }

        String tableName = getTableName(realClass);
        audit.addChange(new EntityChange(tableName, id, before, after, Constantes.ACTUALIZAR));

        if (key != null && after != null) {
            ORIGINAL_STATES_BY_ID.put(key, after);
        }

        logger.debug("postUpdate: change added for {}#{} beforeLen={} afterLen={}",
                realClass.getSimpleName(), id, before != null ? before.length() : 0, after != null ? after.length() : 0);

        if (PUBLISH_EVENTS_FROM_ENTITY_LISTENER) publishAuditIfReady(audit);
    }

    @PostPersist
    public void postPersist(Object entity) {
        AuditData audit = AuditoriaContext.get();
        if (audit == null) return;

        Class<?> realClass = getRealClass(entity);
        String id = extractId(entity);
        String key = id != null ? keyFor(realClass, id) : null;

        String after = safeSerialize(entity);
        if (after == null && id != null) {
            after = readFromDB(realClass, id);
        }

        String tableName = getTableName(realClass);
        audit.addChange(new EntityChange(tableName, id, null, after, Constantes.CREAR));
        
        if (key != null && after != null) {
            ORIGINAL_STATES_BY_ID.put(key, after);
        }

        logger.debug("postPersist: change added for {}#{} afterLen={}", realClass.getSimpleName(),
                id, after != null ? after.length() : 0);

        if (PUBLISH_EVENTS_FROM_ENTITY_LISTENER) publishAuditIfReady(audit);
    }

    @PreRemove
    public void preRemove(Object entity) {
        AuditData audit = AuditoriaContext.get();
        if (audit == null) return;

        Class<?> realClass = getRealClass(entity);
        String id = extractId(entity);
        String before = safeSerialize(entity);
     
        String tableName = getTableName(realClass);
        audit.addChange(new EntityChange(tableName, id, before, null, Constantes.ELIMINAR));
        
        logger.debug("preRemove: change added for {}#{} beforeLen={}", realClass.getSimpleName(),
                id, before != null ? before.length() : 0);

        if (PUBLISH_EVENTS_FROM_ENTITY_LISTENER) publishAuditIfReady(audit);
    }

    // ---------------- helpers ----------------

    private void publishAuditIfReady(AuditData audit) {
        try {
            if (publisher == null) {
                publisher = SpringContext.getPublisher(); // fallback si no se setea
            }
            if (publisher != null) {
                // Intentamos clonar para evitar mutaciones posteriores
                AuditData snapshot = null;
                try {
                    String j = MAPPER.writeValueAsString(audit);
                    snapshot = MAPPER.readValue(j, AuditData.class);
                } catch (Exception e) {
                    logger.warn("publishAuditIfReady: no pudo clonar audit, usando original: {}", e.getMessage());
                    snapshot = audit;
                }
                publisher.publishEvent(new AuditReadyEvent(this, snapshot));
                logger.debug("publishAuditIfReady: evento publicado {}", audit);
            } else {
                logger.error("publishAuditIfReady: ApplicationEventPublisher no disponible");
            }
        } catch (Exception e) {
            logger.error("publishAuditIfReady: error publicando evento: {}", e.getMessage(), e);
        }
    }

    private String safeSerialize(Object o) {
        if (o == null) return null;
        try {
            return MAPPER.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            logger.debug("safeSerialize: error serializando {}", e.getMessage());
            return null;
        }
    }

    private String extractId(Object entity) {
        if (entity == null) return null;
        Class<?> clazz = getRealClass(entity);
        while (clazz != null && !clazz.equals(Object.class)) {
            for (Field f : clazz.getDeclaredFields()) {
                if (f.isAnnotationPresent(Id.class)) {
                    try {
                        f.setAccessible(true);
                        Object val = f.get(entity);
                        return val != null ? String.valueOf(val) : null;
                    } catch (Exception ex) {
                        logger.debug("extractId: error leyendo id {}", ex.getMessage());
                        return null;
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }
        return null;
    }

    private Object convertIdToType(Class<?> entityClass, String idString) {
        if (idString == null) return null;
        Class<?> current = entityClass;
        while (current != null && !current.equals(Object.class)) {
            for (Field f : current.getDeclaredFields()) {
                if (f.isAnnotationPresent(Id.class)) {
                    Class<?> type = f.getType();
                    try {
                        if (type.equals(Long.class) || type.equals(long.class)) return Long.valueOf(idString);
                        if (type.equals(Integer.class) || type.equals(int.class)) return Integer.valueOf(idString);
                        if (type.equals(String.class)) return idString;
                        if (type.equals(java.util.UUID.class)) return java.util.UUID.fromString(idString);
                        if (type.equals(BigInteger.class)) return new BigInteger(idString);
                        if (type.equals(Short.class) || type.equals(short.class)) return Short.valueOf(idString);
                        return idString;
                    } catch (Exception ex) {
                        logger.warn("convertIdToType: no pudo convertir id '{}' a tipo {}", idString, type);
                        return idString;
                    }
                }
            }
            current = current.getSuperclass();
        }
        return idString;
    }

    private String readFromDB(Class<?> entityClass, String id) {
        try {
            EntityManagerFactory emf = SpringContext.getBean(EntityManagerFactory.class);
            EntityManager em = emf.createEntityManager();
            try {
                Object dbEntity = em.find(entityClass, convertIdToType(entityClass, id));
                return safeSerialize(dbEntity);
            } finally {
                if (em.isOpen()) em.close();
            }
        } catch (Exception ex) {
            logger.warn("readFromDB: error {}#{} -> {}", entityClass.getSimpleName(), id, ex.getMessage());
            return null;
        }
    }

    private String keyFor(Class<?> cls, String id) {
        return getRealClassName(cls) + "#" + id;
    }

    private Class<?> getRealClass(Object entity) {
        if (entity == null) return null;
        Class<?> cls = entity.getClass();
        if (cls.getName().contains("$$")) {
            return cls.getSuperclass();
        }
        return cls;
    }

    private String getRealClassName(Class<?> cls) {
        if (cls == null) return "unknown";
        String name = cls.getName();
        if (name.contains("$$")) return cls.getSuperclass().getName();
        return name;
    }
    
    private String getTableName(Class<?> cls) {
        if (cls.isAnnotationPresent(javax.persistence.Table.class)) {
            return cls.getAnnotation(javax.persistence.Table.class).name();
        }
        return cls.getSimpleName();
    }
}