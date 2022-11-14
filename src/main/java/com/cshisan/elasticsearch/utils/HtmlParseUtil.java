package com.cshisan.elasticsearch.utils;

import com.cshisan.elasticsearch.entity.JdProduct;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author CShisan
 */
public class HtmlParseUtil {
    private static final String JD_SEARCH_URL = "https://search.jd.com/Search?keyword=";

    public static List<JdProduct> parseJdSearch(String keyword) {
        if (ObjectUtils.isEmpty(keyword)) {
            return new ArrayList<>();
        }
        try {
            String url = JD_SEARCH_URL.concat(keyword);
            Document document = Jsoup.parse(new URL(url), 30000);
            Element goodList = document.getElementById("J_goodsList");
            Elements lis = Optional.ofNullable(goodList).map(item -> item.getElementsByTag("li")).orElse(new Elements());
            return lis.stream().map(li -> {
                String spu = Optional.of(li.attr("data-spu")).orElse("1");
                String sku = Optional.of(li.attr("data-sku")).orElse("1");
                String img = Optional.of(li.getElementsByTag("img"))
                        .map(item -> item.eq(0))
                        .map(item -> item.attr("data-lazy-img"))
                        .orElse("");
                String price = Optional.of(li.getElementsByClass("p-price"))
                        .map(item -> item.eq(0)).map(Elements::text)
                        .map(item -> item.replace("￥", ""))
                        .orElse("");
                String name = Optional.of(li.getElementsByClass("p-name"))
                        .map(item -> item.eq(0)).map(Elements::text)
                        .orElse("");
                String shop = Optional.of(li.getElementsByClass("curr-shop"))
                        .map(item -> item.attr("title"))
                        .orElse("");
                long id = ObjectUtils.isEmpty(spu) ? 1L : Long.parseLong(sku);
                JdProduct product = JdProduct.builder().sku(id).img(img).name(name).price(new BigDecimal(price)).shop(shop).build();
                product.setId(id);
                return product;
            }).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
