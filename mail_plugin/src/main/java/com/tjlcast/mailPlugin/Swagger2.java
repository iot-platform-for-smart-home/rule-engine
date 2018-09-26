package com.tjlcast.mailPlugin;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by tangjialiang on 2018/5/10.
 */
@Configuration
@EnableSwagger2
public class Swagger2 {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.regex("/api.*"))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("mail plugin api")
                .description("<hr / ><hr / >ヽ(ˋДˊ)ノ Day Day UP ！！！ ヽ(ˋ▽ˊ)ノ<hr / ><hr /> ヽ(ˋДˊ)ノ Day Day Wonderful ！！！ ヽ(ˋ▽ˊ)ノ <hr / ><hr / >")
                .version("1.0")
                .build();
    }
}
