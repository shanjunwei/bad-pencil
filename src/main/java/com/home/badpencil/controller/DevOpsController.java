package com.home.badpencil.controller;

import com.home.badpencil.lucence.index.IndexDao;
import com.home.badpencil.pojo.api.ApiResult;
import com.home.badpencil.pojo.doc.DocTree;
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
@Api(tags = "运维")
@RequestMapping("v1")
public class DevOpsController {
    @Autowired
    DocService DocService;

    private static final Logger logger = LoggerFactory.getLogger(DevOpsController.class);

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

    @GetMapping("/trees/")
    @ApiOperation("文档树")
    public ApiResult<DocTree> docTrees() {
        return ApiResult.call(() -> ApiResult.success(DocService.docTrees()), logger);
    }
}
