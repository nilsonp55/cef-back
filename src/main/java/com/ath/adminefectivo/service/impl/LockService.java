package com.ath.adminefectivo.service.impl;


import com.ath.adminefectivo.repositories.ILockingRepository;
import com.ath.adminefectivo.service.ILockService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LockService implements ILockService {

    @Autowired
    private ILockingRepository lockRepository;
    
    @Override
    public boolean tryAcquireLock(long lockId) {
        return lockRepository.tryAcquireLock(lockId);
    }
    
    @Override
    public boolean releaseLock(long lockId) {
       return lockRepository.releaseLock(lockId);

    }

}
