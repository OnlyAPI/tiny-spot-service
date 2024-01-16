package com.yifan.admin.api.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/4/7 11:32
 */
@Configuration
@EnableSwagger2
@ConditionalOnProperty(value = "swagger.show", havingValue = "true")
public class SwaggerConfig {

    @Bean
    public Docket api() {

        //在swagger2中设置请求头，携带token
        ParameterBuilder ticketPar = new ParameterBuilder();
        ticketPar.name("token")//
                .description("登录校验")//
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)//required表示是否必填
                .defaultValue("")//defaultvalue表示默认值
                .build();

        List<Parameter> pars = new ArrayList<Parameter>();
        pars.add(ticketPar.build());

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())//Api 信息
                .groupName("f-admin")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.yifan.admin.api.controller"))
                .paths(PathSelectors.any())//路径下所有的接口
                //.paths(PathSelectors.regex("/award/user.*"))//也可以指定路径下的接口
                .build()
                .globalOperationParameters(pars);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("TinySpot接口文档")
                .description("")
                .termsOfServiceUrl("")
                .version("1.0.0")
                .contact(new Contact("f", "", "2842273945@qq.com"))
                .build();
    }

}
