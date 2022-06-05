package com.home.badpencil.controller;

import com.home.badpencil.lucence.index.IndexDao;
import com.home.badpencil.lucence.search.IndexSearch;
import com.home.badpencil.pojo.api.ApiResult;
import com.home.badpencil.pojo.api.doc.ResDocs;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "演示口")
@RequestMapping("v1")
public class DemoController {
    private static final Logger logger = LoggerFactory.getLogger(DemoController.class);
    private IndexSearch indexSearch = new IndexSearch();
    private IndexDao indexDao  = new IndexDao();
    @GetMapping("/hello")
    @ApiOperation("hello")
    public ApiResult<String> hello() {
        return ApiResult.call(() -> ApiResult.okWithMsg("hello spring!"), logger);
    }

    @GetMapping("/index")
    @ApiOperation("索引指定目录所有文档")
    public ApiResult<Integer> index(String path) {
        return ApiResult.call(() -> ApiResult.success(indexDao.createIndex(path)), logger);
    }

    @GetMapping("/query")
    @ApiOperation("全文检索")
    public ApiResult<ResDocs> query(String ask) {
        return ApiResult.call(() -> ApiResult.success(indexSearch.search(ask)), logger);
    }
}
