package com.fish.birdProducted.config;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
//import org.assertj.core.util.Lists;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author:fish
 * @date: 2024/4/1-15:19
 * @content: Elasticsearch配置
 */

@Configuration
public class ElasticsearchConfig {

    @Value("${spring.elasticsearch.username}")
    private String username;

    @Value("${spring.elasticsearch.password}")
    private String password;

/*    @Value("${spring.elasticsearch.uris}")
    private String uris;*/


//    @Value("${elasticsearch.connTimeout}")
//    private int connTimeout;

    @Value("${spring.elasticsearch.socketTimeout}")
    private int socketTimeout;

//    @Value("${elasticsearch.connectionRequestTimeout}")
//    private int connectionRequestTimeout;

    @Bean
    public ElasticsearchClient elasticsearchClient() {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
//            RestClient restClient = RestClient.builder(new HttpHost(hostname, port, scheme))
            RestClient restClient = RestClient.builder(new HttpHost("localhost", 9200, "http"))
                .setRequestConfigCallback(requestConfigBuilder -> requestConfigBuilder
                        .setConnectTimeout(1000)
                        .setSocketTimeout(socketTimeout)
                        .setConnectionRequestTimeout(5000)
                ).setHttpClientConfigCallback(f -> f.setDefaultCredentialsProvider(credentialsProvider)).build();
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());

        System.out.println("ElasticSearch connection is okkk!!!");
        return new ElasticsearchClient(transport);

    }


}
