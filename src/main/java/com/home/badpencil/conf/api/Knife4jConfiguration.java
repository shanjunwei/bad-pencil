package com.home.badpencil.conf.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class Knife4jConfiguration {
    @Bean(value = "defaultApi2")
    public Docket defaultApi2() {
        Docket docket=new Docket(DocumentationType.OAS_30)
                .apiInfo(new ApiInfoBuilder()
                        .description("# 烂笔头 学城 面试助力系统")
                        .termsOfServiceUrl("http://localhost/doc.html#")
                        .contact(new Contact("dts","http://localhost/doc.html#","UnKnow"))
                        .version("1.0")
                        .build())
                //分组名称
                .groupName("bad-pencil")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.home.badpencil.controller"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }
}
