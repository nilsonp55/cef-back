package com.ath.adminefectivo.auditoria.listener;


import com.ath.adminefectivo.auditoria.context.AuditData;
import com.ath.adminefectivo.auditoria.context.AuditoriaContext;
import com.ath.adminefectivo.auditoria.utils.AuditReadyEvent;
import com.ath.adminefectivo.auditoria.utils.SpringContext;
import com.ath.adminefectivo.entities.audit.EntityChange;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

import javax.persistence.PostLoad;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.PostPersist;
import javax.persistence.Id;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityManager;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Listener para capturar cambios en entidades (CREATE / UPDATE / DELETE)
 * - Guarda snapshot "antes" (postLoad) por clave class#id
 * - En preUpdate usa snapshot; si no existe hace fallback a BD por id
 */
public class AuditoriaEntityListener {

    private static final Logger logger = LoggerFactory.getLogger(AuditoriaEntityListener.class);
    private static final ObjectMapper MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());

    private static final Map<String, String> ORIGINAL_STATES_BY_ID = new ConcurrentHashMap<>();
    private static final boolean PUBLISH_EVENTS_FROM_ENTITY_LISTENER = true;

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
        }
    }

    @PreUpdate
    public void preUpdate(Object entity) {
        AuditData audit = AuditoriaContext.get();
        if (audit == null) return;

        Class<?> realClass = getRealClass(entity);
        String id = extractId(entity);
        String key = id != null ? keyFor(realClass, id) : null;

        String before = key != null ? ORIGINAL_STATES_BY_ID.get(key) : null;
        if (before == null && id != null) {
            before = readFromDB(realClass, id);
        }

        String after = safeSerialize(entity);
        audit.addChange(new EntityChange(realClass.getSimpleName(), id, before, after, "UPDATE"));

        if (key != null && after != null) {
            ORIGINAL_STATES_BY_ID.put(key, after);
        }

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

        audit.addChange(new EntityChange(realClass.getSimpleName(), id, null, after, "CREATE"));

        if (key != null && after != null) {
            ORIGINAL_STATES_BY_ID.put(key, after);
        }

        if (PUBLISH_EVENTS_FROM_ENTITY_LISTENER) publishAuditIfReady(audit);
    }

    @PreRemove
    public void preRemove(Object entity) {
        AuditData audit = AuditoriaContext.get();
        if (audit == null) return;

        Class<?> realClass = getRealClass(entity);
        String id = extractId(entity);
        String before = safeSerialize(entity);

        audit.addChange(new EntityChange(realClass.getSimpleName(), id, before, null, "DELETE"));

        if (PUBLISH_EVENTS_FROM_ENTITY_LISTENER) publishAuditIfReady(audit);
    }

    // ---------------- helpers ----------------

    private void publishAuditIfReady(AuditData audit) {
        try {
            if (publisher == null) {
                publisher = SpringContext.getPublisher(); // fallback si no se setea
            }
            if (publisher != null) {
                publisher.publishEvent(new AuditReadyEvent(this, audit));
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
}