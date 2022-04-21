package com.tyy.dev.utils.db;

import java.security.SecureRandom;

public class IdGenerator {

    private long baseId;

    private static IdGenerator idGenerator = null;

    public static IdGenerator createGenerator() {
        if (null == idGenerator) {
            idGenerator = new IdGenerator();
        }
        return idGenerator;
    }

    private IdGenerator() {
        long t = System.currentTimeMillis();
        // 53~45
        baseId = t;
        baseId &= 0x1FF0000000L;
        baseId <<= 16;
        // 30~17
        t &= 0xFFFC000L;
        t <<= 2;
        baseId |= t;
        // 44~31
        SecureRandom ng = new SecureRandom();
        t = ng.nextLong();
        t &= 0x3FFF0000000L;
        t <<= 2;
        baseId |= t;
        // 16~1
        baseId /= 50000;
        baseId *= 50000;
        baseId &= 0x1FFFFFFFFFFFFFL;
    }

    synchronized public long getID() {
        // logger.info("baseId>>>>>"+baseId);
        return baseId++;
    }

    synchronized public String getBatchID(long batch) {
        String idStartEnd = String.valueOf(baseId) + "-" + String.valueOf(baseId + batch - 1);
        baseId = baseId + batch;
        return idStartEnd;
    }

}
