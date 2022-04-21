package com.tyy.dev.dix;

import org.elasticsearch.index.query.QueryBuilders;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tyy.dev.utils.es.EsDao;
import com.tyy.dev.utils.es.EsIndex;

public class DixActions {

    EsDaoGetter getter;

    public static EsIndex dipConfig = EsIndex.of("dix_dipconfig", "dipconfig");

    public DixActions(EsDaoGetter getter) {
        super();
        this.getter = getter;
    }

    public void stopAllDip() {
        JSONArray queryList = esDao().queryList(dipConfig, QueryBuilders.termQuery("dipUserId.keyword", "640"));
        for (Object dc : queryList) {
            JSONObject d = (JSONObject) dc;
            d.put("dipStatus", 1);
            esDao().save(dipConfig, d);
        }
    }

    EsDao esDao() {
        return getter.getEsDao();
    }

}
