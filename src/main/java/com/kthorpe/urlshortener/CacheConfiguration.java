package com.kthorpe.urlshortener;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
public class CacheConfiguration {
    @Bean
    public HashMap<String, String> urlToEncodedCache(){
        return new HashMap<>();
    }

    @Bean
    public HashMap<String, String> encodedToUrlCache(){
        return new HashMap<>();
    }
}
