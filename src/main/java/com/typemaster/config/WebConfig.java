package com.typemaster.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

/**
 * Enterprise Full-Stack Optimization Configuration:
 * - Static assets (CSS, JS, Images, Fonts) are cached with public max-age=365 days + immutable.
 * - HTML, Service Worker, Manifest, and Metadata files use no-cache with ETags for revalidation.
 * - Register ShallowEtagHeaderFilter to support HTTP ETags.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 1. Static Assets (CSS, JS, Images, Fonts) -> Cache for 365 days (1 year) immutable
        registry.addResourceHandler("/assets/**")
                .addResourceLocations("classpath:/static/assets/")
                .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS).cachePublic().getHeaderValue() != null ?
                        CacheControl.maxAge(365, TimeUnit.DAYS).cachePublic() : CacheControl.maxAge(365, TimeUnit.DAYS));

        // 2. Root metadata, service worker, manifest, and HTML files -> Revalidate with ETags
        registry.addResourceHandler("/*.html", "/sitemap.xml", "/robots.txt", "/favicon.ico", "/manifest.json", "/sw.js")
                .addResourceLocations("classpath:/static/")
                .setCacheControl(CacheControl.noCache().mustRevalidate());
    }

    @Bean
    public ShallowEtagHeaderFilter shallowEtagHeaderFilter() {
        return new ShallowEtagHeaderFilter();
    }
}

