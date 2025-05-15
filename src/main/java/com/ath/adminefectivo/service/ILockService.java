package com.ath.adminefectivo.service;

public interface ILockService {

    boolean tryAcquireLock(long lockId);

    boolean releaseLock(long lockId);

}
