package com.typemaster.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

/**
 * 2026-07-31 Full-Stack Optimization Configuration:
 * - Configure separate resource caching: assets are cached for 30 days, while HTML and metadata files are served with revalidation headers.
 * - Register ShallowEtagHeaderFilter to support HTTP ETags.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 1. Static Assets (CSS, JS, Images, Fonts) -> Cache for 30 days
        registry.addResourceHandler("/assets/**")
                .addResourceLocations("classpath:/static/assets/")
                .setCacheControl(CacheControl.maxAge(30, TimeUnit.DAYS).cachePublic());

        // 2. Root files (HTML, robots.txt, sitemap.xml, favicon.ico) -> Check with ETags every time
        registry.addResourceHandler("/*.html", "/sitemap.xml", "/robots.txt", "/favicon.ico")
                .addResourceLocations("classpath:/static/")
                .setCacheControl(CacheControl.noCache().mustRevalidate());
    }

    @Bean
    public ShallowEtagHeaderFilter shallowEtagHeaderFilter() {
        return new ShallowEtagHeaderFilter();
    }
}
