package com.ath.adminefectivo.repositories;

public interface ILockingRepository {

    boolean tryAcquireLock(long lockId);
    
    boolean releaseLock(long lockId);

}