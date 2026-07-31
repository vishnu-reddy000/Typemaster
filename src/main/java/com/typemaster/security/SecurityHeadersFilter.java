package com.typemaster.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import java.io.IOException;

/**
 * 2026-07-31 Full-Stack Security Optimization:
 * Intercepts all incoming requests and appends standard security headers
 * to meet modern web application best practices and optimize Lighthouse Best Practices scores.
 */
@Component
public class SecurityHeadersFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // No initialization needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (response instanceof HttpServletResponse) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;

            // 1. Content Security Policy (CSP)
            // Permits self, cdnjs (for html2canvas), Google Fonts, and Google Web Fonts stylesheets/fonts.
            // Also allows inline styles/scripts for custom theme injections and analytics simulators.
            httpResponse.setHeader("Content-Security-Policy", 
                "default-src 'self'; " +
                "script-src 'self' 'unsafe-inline' 'unsafe-eval' https://cdnjs.cloudflare.com; " +
                "style-src 'self' 'unsafe-inline' https://fonts.googleapis.com; " +
                "font-src 'self' https://fonts.gstatic.com https://fonts.googleapis.com; " +
                "img-src 'self' data: https://typemaster.com; " +
                "connect-src 'self' ws: wss:; " +
                "frame-ancestors 'none'; " +
                "base-uri 'self'; " +
                "form-action 'self';");

            // 2. Strict-Transport-Security (HSTS) - force SSL for 2 years (63072000s) including subdomains
            httpResponse.setHeader("Strict-Transport-Security", "max-age=63072000; includeSubDomains; preload");

            // 3. X-Frame-Options - Clickjacking mitigation
            httpResponse.setHeader("X-Frame-Options", "DENY");

            // 4. X-Content-Type-Options - MIME-sniffing mitigation
            httpResponse.setHeader("X-Content-Type-Options", "nosniff");

            // 5. Referrer-Policy - control referrer disclosure
            httpResponse.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");

            // 6. Permissions-Policy - restrict camera, geolocation, microphone
            httpResponse.setHeader("Permissions-Policy", "geolocation=(), microphone=(), camera=(), interest-cohort=()");

            // 7. Cross-Origin-Opener-Policy (COOP) - mitigation for Spectre/attacks
            httpResponse.setHeader("Cross-Origin-Opener-Policy", "same-origin");

            // 8. Cross-Origin-Resource-Policy (CORP)
            httpResponse.setHeader("Cross-Origin-Resource-Policy", "same-origin");
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // No cleanup needed
    }
}
