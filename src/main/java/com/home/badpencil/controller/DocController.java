package com.home.badpencil.controller;

import com.home.badpencil.lucence.search.IndexSearch;
import com.home.badpencil.pojo.api.ApiResult;
import com.home.badpencil.pojo.doc.HitDoc;
import com.home.badpencil.pojo.doc.ResDocs;
import com.home.badpencil.service.DocService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "文档管理")
@RequestMapping("docs")
public class DocController {
    private static final Logger logger = LoggerFactory.getLogger(DocController.class);
    @Autowired
    DocService docService;
    private IndexSearch indexSearch = new IndexSearch();

    @GetMapping("/query")
    @ApiOperation("全文检索")
    public ApiResult<ResDocs> query(String ask) {
        return ApiResult.call(() -> ApiResult.success(indexSearch.search(ask)), logger);
    }
    @GetMapping("/route")
    @ApiOperation("根据路径查文档")
    public ApiResult<HitDoc> routeDoc(String path) {
        return ApiResult.call(() -> ApiResult.success(docService.getDocByPath(path)), logger);
    }
}
