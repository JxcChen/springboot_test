package com.example.demo.config;

import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author: JJJJ
 * @date:2021/4/19 19:46
 * @Description: swagger配置类
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket docket(){
        ParameterBuilder builder = new ParameterBuilder(); // 声明参数构造器
        builder.parameterType("header").name("token") // 需要一个name为token的header
                .description("token值") // 对header的描述
                .required(true)  // 是必须的
                .modelRef(new ModelRef("String")); // 在swagger里显示header

        return new Docket(DocumentationType.SWAGGER_2) // 定义文档类型
                .groupName("ai_test_interface") // 给接口定义组名
                .apiInfo(apiInfo()) // 将定义的api信息传入
                .globalOperationParameters(Lists.newArrayList(builder.build())) //全局参数
                .select() // 选择接口进行页面展示
                .paths(PathSelectors.any()).build();
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder() // 定义api构造器
                .title("ai_test系统") // 标题为ai_test系统
                .description("ai_test接口文档") // 描述
                .contact(new Contact("CHNJX","","360088940@qq.com")) //联系人
                .version("1.0") // api版本好
                .build();
    }
}
