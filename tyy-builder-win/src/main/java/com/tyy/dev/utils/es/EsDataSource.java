package com.tyy.dev.utils.es;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

public class EsDataSource {

    private String host;

    private String username;

    private String passwd;

    private RestHighLevelClient client;

    // private EsDataSource() {
    // super();
    // }

    private EsDataSource(String host) {
        super();
        this.host = host;
    }

    public static EsDataSource of(String host) {
        if (isEmpty(host)) { throw new RuntimeException("xxx"); }
        return new EsDataSource(host);
    }

    public static EsDataSource of(String host, String username, String passwd) {
        if (isEmpty(host)) { throw new RuntimeException("xxx"); }
        return new EsDataSource(host, username, passwd);
    }

    public EsDataSource(String host, String username, String passwd) {
        super();
        this.host = host;
        this.username = username;
        this.passwd = passwd;
    }

    public RestHighLevelClient getClient() {
        if (client == null) {
            synchronized (this) {
                if (client == null) {
                    client = getClient(host, username, passwd);
                }
            }
        }
        return client;
    }

    private static boolean isEmpty(String v) {
        return v == null || "".equals(v.trim());
    }

    @Override
    public String toString() {
        if (username == null || "".equals(username.trim())) {
            return host;
        } else {
            return "[host=" + host + ", username=" + username + ", passwd=" + passwd + "]";
        }
    }

    public String toKey() {
        return host + username + passwd;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    private static RestHighLevelClient getClient(String esIps, String esUser, String esPwd) {
        RestHighLevelClient client = null;
        String[] ips = esIps.split(";");
        HttpHost[] httpHosts = new HttpHost[ips.length];
        for (int i = 0; i < ips.length; i++) {
            String[] ipPorts = ips[i].split("\\:");
            httpHosts[i] = new HttpHost(ipPorts[0], Integer.parseInt(ipPorts[1]), "http");
        }
        RestClientBuilder builder = initBuilder(httpHosts, esUser, esPwd);
        if (client == null) {
            client = new RestHighLevelClient(builder);
        }
        return client;
    }

    /**
     * <b>初始化
     *
     * @param httpHosts
     * @return
     */
    private static RestClientBuilder initBuilder(HttpHost[] httpHosts, String esUser, String esPwd) {
        RestClientBuilder builder = RestClient.builder(httpHosts);
        builder.setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {

            @Override
            public RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder builder) {
                builder.setConnectTimeout(60000);
                builder.setSocketTimeout(60000);
                builder.setConnectionRequestTimeout(60000);
                return builder;
            }

        });
        builder.setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {

            @Override
            public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpAsyncClientBuilder) {
                httpAsyncClientBuilder.setMaxConnTotal(300);
                httpAsyncClientBuilder.setMaxConnPerRoute(300);
                if (true) {
                    CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                    credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(esUser, esPwd));
                    httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                }
                return httpAsyncClientBuilder;
            }

        });
        return builder;
    }

}
