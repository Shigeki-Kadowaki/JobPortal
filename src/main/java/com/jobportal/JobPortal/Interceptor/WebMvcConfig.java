package com.jobportal.JobPortal.Interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Bean
    public StudentInterceptor StudentInterceptor() {
        return new StudentInterceptor();
    }
    @Bean
    public TeacherInterceptor TeacherInterceptor() {
        return new TeacherInterceptor();
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(StudentInterceptor())
                .addPathPatterns("/student/**")
                .excludePathPatterns(
                        "/index",
                        "/style/**",
                        "/static/**",
                        "**/style.css",
                        "**.js"
                        );
        registry.addInterceptor(TeacherInterceptor())
                .addPathPatterns("/teacher/**")
                .excludePathPatterns(
                        "/style/**",
                        "/static/**",
                        "**/style.css",
                        "**.js"
                );
    }
}
