package com.cshisan.elasticsearch.controller;

import com.cshisan.elasticsearch.entity.JdProduct;
import com.cshisan.elasticsearch.entity.Result;
import com.cshisan.elasticsearch.entity.SearchEntity;
import com.cshisan.elasticsearch.service.EsService;
import com.cshisan.elasticsearch.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author CShisan
 */
@RestController
@RequestMapping("/es")
public class EsController {
    private final EsService esService;

    @Autowired
    public EsController(EsService esService) {
        this.esService = esService;
    }

    @PostMapping("/crawl/{keyword}")
    public Result<Boolean> crawl(@PathVariable String keyword) {
        return ResultUtil.ok(esService.crawl(keyword));
    }

    @PostMapping("/search")
    public Result<List<JdProduct>> search(@RequestBody SearchEntity search) {
        return ResultUtil.ok(esService.search(search));
    }
}
