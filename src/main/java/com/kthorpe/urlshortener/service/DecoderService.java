package com.kthorpe.urlshortener.service;

import org.springframework.stereotype.Service;

@Service
public class DecoderService {
    public String decodeUrl(String url) {
        return url + "-decoded";
    }
}
