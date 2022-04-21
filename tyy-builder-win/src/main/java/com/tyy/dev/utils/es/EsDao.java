package com.tyy.dev.utils.es;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tyy.dev.utils.db.IdGenerator;

public class EsDao implements AutoCloseable {

    public static int DEF_PAGESIZE = 10000;

    private long lastUseTime = 0;

    private EsDataSource ds;

    public RestHighLevelClient client = null;

    private volatile boolean running = false;

    public boolean isRunning() {
        return running;
    }

    public JSONObject queryById(EsIndex idx, Object id) {
        take();
        GetRequest getRequest = new GetRequest(idx.getIndex(), idx.getType(), id.toString());
        try {
            GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
            return new JSONObject(getResponse.getSourceAsMap());
        } catch (IOException e) {
            return null;
            // throw new RuntimeException(e.getMessage());
        }
    }

    public Integer clearAllByQ(EsIndex idx, QueryBuilder query) {
        if (query == null) {
            query = QueryBuilders.boolQuery();
        }
        Integer flag = 1;
        RestClient lowClient = client.getLowLevelClient();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(query);
        HttpEntity entity = new NStringEntity(sourceBuilder.toString(), ContentType.APPLICATION_JSON);
        try {
            String url = "/" + idx.getIndex() + "/" + idx.getType() + "/_delete_by_query?conflicts=proceed&refresh=" + true;
            Request request = new Request("POST", url);
            request.setEntity(entity);
            Response res = lowClient.performRequest(request);
            if (res.getStatusLine().getStatusCode() != 200) {
                flag = 0;
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return flag;
    }

    public Integer save(EsIndex index, Object obj) {
        return saveList(index, Arrays.asList(obj));
    }

    public Integer saveList(EsIndex index, List<?> list) {
        take();
        Integer flag = 1;
        BulkRequest bulkRequest = new BulkRequest();

        for (int i = 0; (!list.isEmpty() && i < list.size()); i++) {
            JSONObject obj = list.get(i) instanceof JSONObject ? (JSONObject) list.get(i) : (JSONObject) JSON.toJSON(list.get(i));
            if (!obj.containsKey("id")) {
                obj.put("id", IdGenerator.createGenerator().getID());
            }
            UpdateRequest uRequest = new UpdateRequest(index.getIndex(), index.getType(), obj.get("id").toString());
            uRequest.doc(obj);
            uRequest.docAsUpsert(true);
            bulkRequest.add(uRequest);
        }
        bulkRequest.timeout(TimeValue.timeValueMinutes(2));
        bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        try {
            BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
            if (bulkResponse.hasFailures()) {
                flag = 0;
            }
        } catch (IOException e) {
            flag = 0;
            throw new RuntimeException(e.getMessage());
        }
        return flag;
    }

    public JSONArray queryList(EsIndex idx) {
        return queryList(idx, QueryBuilders.matchAllQuery());
    }

    public JSONArray queryList(EsIndex idx, QueryBuilder query) {
        JSONArray rs = new JSONArray();
        try {
            SearchResponse response = client.search(buildReq(idx, query, 0, DEF_PAGESIZE), RequestOptions.DEFAULT);
            SearchHit[] hits = response.getHits().getHits();
            for (SearchHit hit : hits) {
                String recordStr = hit.getSourceAsString();
                JSONObject result = JSON.parseObject(recordStr);
                rs.add(result);
            }
            return rs;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private SearchRequest buildReq(EsIndex idx, QueryBuilder query, int from, int size) {
        take();
        SearchRequest searchRequest = new SearchRequest(idx.getIndex());
        searchRequest.types(idx.getType());
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.from(0);
        sourceBuilder.size(10000);
        sourceBuilder.query(query);
        searchRequest.source(sourceBuilder);
        return searchRequest;
    }

    public <T> List<T> queryList(EsIndex idx, QueryBuilder query, Class<T> clazz) {
        try {
            SearchResponse response = client.search(buildReq(idx, query, 0, DEF_PAGESIZE), RequestOptions.DEFAULT);
            // long totalCount = response.getHits().getTotalHits();
            SearchHit[] hits = response.getHits().getHits();
            JSONArray rs = new JSONArray();
            for (SearchHit hit : hits) {
                String recordStr = hit.getSourceAsString();
                JSONObject result = JSON.parseObject(recordStr);
                rs.add(result);
            }
            return rs.toJavaList(clazz);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void init(EsDataSource ds) {
        this.ds = ds;
        client = ds.getClient();
        running = true;
        take();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (client != null) {
                try {
                    client.close();
                } catch (IOException e) {
                }
            }
        }));
    }

    private void take() {
        lastUseTime = System.currentTimeMillis();
    }

    public long getLastUseTime() {
        return lastUseTime;
    }

    @Override
    public void close() throws Exception {
        running = false;
        if (client != null) {
            try {
                client.close();
            } catch (IOException e) {
            }
        }
    }

    public EsDataSource getDs() {
        return ds;
    }

}
