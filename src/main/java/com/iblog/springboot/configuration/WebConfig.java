package com.iblog.springboot.configuration;

import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(
            ResourceHandlerRegistry registry) {

        String uploadPath =
                Paths.get(
                        System.getProperty("user.dir"),
                        "uploads",
                        "blogs"
                )
                .toUri()
                .toString();

        registry.addResourceHandler(
                "/img/blogs/**"
        ).addResourceLocations(
                uploadPath
        );
    }
}
