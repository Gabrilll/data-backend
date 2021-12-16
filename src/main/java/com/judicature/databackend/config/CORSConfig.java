package com.judicature.databackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CORSConfig {
    private static final String[] originsVal = new String[]{
            "localhost:8080",
            "127.0.0.1:8080",
            "127.0.0.1",
            "localhost",
            "172.19.144.143",
            "172.19.144.143:8080",
            "42.192.9.211"
    };

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 1
//        addAllowedOrigins(corsConfiguration);
//        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedOriginPattern("*");
        // 2
        corsConfiguration.addAllowedHeader("*");
        // 3
        corsConfiguration.addAllowedMethod("*");
        // 跨域session共享
        corsConfiguration.setAllowCredentials(true);
        // 4
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }

    private void addAllowedOrigins(CorsConfiguration corsConfiguration) {
        for (String origin : originsVal) {
            corsConfiguration.addAllowedOrigin("http://" + origin);
            corsConfiguration.addAllowedOrigin("https://" + origin);
        }
    }
}
