package com.cshisan.elasticsearch;

import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import com.cshisan.elasticsearch.entity.JdProduct;
import com.cshisan.elasticsearch.utils.EsUtil;
import com.cshisan.elasticsearch.utils.HtmlParseUtil;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ElasticsearchDemoApplication.class)
class ElasticsearchDemoApplicationTests {
    @Autowired
    private EsUtil esUtil;

    @Test
    void contextLoads() {
        List<JdProduct> products = HtmlParseUtil.parseJdSearch("Java");
        BulkResponse response = esUtil.bulk("test01", products);
        System.out.println(response);
    }

}
