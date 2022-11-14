package com.cshisan.elasticsearch.service;

import co.elastic.clients.elasticsearch.core.search.Hit;
import com.cshisan.elasticsearch.constant.JdConstant;
import com.cshisan.elasticsearch.entity.JdProduct;
import com.cshisan.elasticsearch.entity.SearchEntity;
import com.cshisan.elasticsearch.utils.EsUtil;
import com.cshisan.elasticsearch.utils.HtmlParseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author CShisan
 */
@Service
public class EsService {
    private final EsUtil esUtil;

    @Autowired
    public EsService(EsUtil esUtil) {
        this.esUtil = esUtil;
    }

    /**
     * 根据关键字爬取京东商品信息
     *
     * @param keyword keyword
     * @return status
     */
    public Boolean crawl(String keyword) {
        if (ObjectUtils.isEmpty(keyword) || ObjectUtils.isEmpty(keyword.trim())) {
            throw new RuntimeException("关键字不能为空");
        }
        List<JdProduct> products = HtmlParseUtil.parseJdSearch(keyword);
        esUtil.bulk(JdConstant.ES_INDEX, products);
        return true;
    }

    /**
     * 搜索
     *
     * @param search search
     * @return list
     */
    public List<JdProduct> search(SearchEntity search) {
        List<Hit<JdProduct>> hits = esUtil.search(JdConstant.ES_INDEX, "name", search, JdProduct.class);
        return hits.stream().map(hit -> {
            JdProduct product = hit.source();
            Optional.ofNullable(product).ifPresent(item -> {
                String name = Optional.ofNullable(hit.highlight().get("name"))
                        .map(l -> l.get(0)).orElse(product.getName());
                product.setName(name);
            });
            return product;
        }).collect(Collectors.toList());
    }
}
