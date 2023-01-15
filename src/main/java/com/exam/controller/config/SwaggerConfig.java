package com.exam.controller.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * swagger配置
 *
 * @author wangpeng
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(swaggerApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.exam.controller"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(this.getParameterList())
                ;
    }

    /**
     * 添加令牌参数
     *
     * @return
     */
    private List<Parameter> getParameterList() {
        ParameterBuilder builder = new ParameterBuilder();
        List<Parameter> par = new ArrayList<>();
        builder.name("token").description("令牌").modelRef(new ModelRef("string"))
                .parameterType("header").required(false).build();
        par.add(builder.build());
        return par;
    }

    private ApiInfo swaggerApiInfo() {
        return new ApiInfoBuilder()
                .title("Car Rental Api Document")
                .version("1.0.0")
                .build();
    }
}