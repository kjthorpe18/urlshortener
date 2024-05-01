package com.kthorpe.urlshortener.service;

import org.springframework.stereotype.Service;

@Service
public class EncoderService {

    public String encodeUrl(String url) {
        return url + "-encoded";
    }
}
