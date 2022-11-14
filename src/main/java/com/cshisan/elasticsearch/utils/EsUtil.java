package com.cshisan.elasticsearch.utils;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import co.elastic.clients.util.ObjectBuilder;
import com.cshisan.elasticsearch.config.EsConfig;
import com.cshisan.elasticsearch.entity.BaseEntity;
import com.cshisan.elasticsearch.entity.SearchEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author CShisan
 */
@Slf4j
@Component
public class EsUtil {
    private final ElasticsearchClient client;

    @Autowired
    public EsUtil(EsConfig config) {
        RestClient restClient = RestClient.builder(new HttpHost(config.getHost(), config.getPort())).build();
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        this.client = new ElasticsearchClient(transport);
    }

    /**
     * 插入当个文档
     *
     * @param index  index
     * @param entity entity
     * @return response
     */
    public IndexResponse index(String index, BaseEntity entity) {
        try {
            return client.index(i -> i.index(index).id(String.valueOf(entity.getId())).document(entity));
        } catch (IOException e) {
            throw new RuntimeException("文档更新错误, e=" + e.getMessage());
        }
    }

    /**
     * 批量插入文档
     *
     * @param index index
     * @param list  list
     * @return response
     */
    public BulkResponse bulk(String index, List<? extends BaseEntity> list) {
        try {
            return client.bulk(br -> {
                Optional.ofNullable(list).orElse(new ArrayList<>())
                        .forEach(item -> br.operations(op -> op.index(idx ->
                                idx.index(index).id(String.valueOf(item.getId())).document(item)))
                        );
                return br;
            });
        } catch (IOException e) {
            throw new RuntimeException("文档更新错误, e=" + e.getMessage());
        }
    }

    public <T> List<Hit<T>> search(String index, String fieldName, SearchEntity search, Class<T> clazz) {
        try {
            String keyword = search.getKeyword();
            Integer current = Optional.ofNullable(search.getCurrent()).orElse(1);
            Integer size = Optional.ofNullable(search.getSize()).orElse(10);
            SearchResponse<T> response = client.search(s -> {
                // 查询条件
                s.index(index).query(q -> q.match(t -> t.field(fieldName).query(keyword)));
                // 分页
                s.from(current).size(size);
                // 高亮
                s.highlight(h -> h.fields("name", f -> f));
                return s;
            }, clazz);
            return response.hits().hits();
        } catch (IOException e) {
            throw new RuntimeException("es搜索失败, e=" + e.getMessage());
        }
    }
}
