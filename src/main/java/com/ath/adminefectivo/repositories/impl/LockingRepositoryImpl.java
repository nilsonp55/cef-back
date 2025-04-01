package com.ath.adminefectivo.repositories.impl;

import com.ath.adminefectivo.repositories.ILockingRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
public class LockingRepositoryImpl implements ILockingRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public boolean tryAcquireLock(long lockId) {
        Boolean result = (Boolean) entityManager.createNativeQuery("SELECT pg_try_advisory_lock(:lockId)")
                .setParameter("lockId", lockId)
                .getSingleResult();
        return result != null && result;
    }

    @Override
    @Transactional
    public boolean releaseLock(long lockId) {
        Boolean result = (Boolean) entityManager.createNativeQuery("SELECT pg_advisory_unlock(:lockId)")
        .setParameter("lockId", lockId)
        .getSingleResult();
        return result != null && result;
    }
}
