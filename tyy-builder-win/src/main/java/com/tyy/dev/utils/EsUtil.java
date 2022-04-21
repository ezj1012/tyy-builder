package com.tyy.dev.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.tyy.dev.utils.es.EsDao;
import com.tyy.dev.utils.es.EsDataSource;

public class EsUtil implements Runnable {

    private static Map<String, EsDao> dao = new HashMap<>();

    private volatile boolean starting = false;

    private static EsUtil single = null;

    private Thread clearThread;

    private EsUtil() {
        if (single != null) { throw new RuntimeException(); }
        single = this;
        starting = true;
        clearThread = new Thread(this);
        this.clearThread.start();
    }

    public static EsDao get(EsDataSource ds) {
        if (single == null) {
            new EsUtil();
        }
        String key = ds.toKey();
        EsDao esDao = dao.get(key);
        if (esDao == null) {
            esDao = new EsDao();
            esDao.init(ds);
            dao.put(key, esDao);
        }
        return esDao;
    }

    public static EsDao remove(EsDataSource ds) {
        return dao.remove(ds.toKey());
    }

    public static void close(EsDataSource ds) {
        EsDao remove = remove(ds);
        if (remove != null) {
            try {
                remove.close();
            } catch (Exception e) {
            }
        }
    }

    public static void close() {
        Set<String> keySet = dao.keySet();
        for (String key : keySet) {
            EsDao remove = dao.remove(key);
            try {
                remove.close();
            } catch (Exception e) {
            }
        }
    }

    public void run() {
        while (starting) {
            Set<String> keySet = dao.keySet();
            for (String key : keySet) {
                EsDao esDao = dao.get(key);
                if (System.currentTimeMillis() - esDao.getLastUseTime() > 60000) {
                    dao.remove(key);
                    try {
                        esDao.close();
                    } catch (Exception e) {
                    }
                }
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
        }
    }

}
