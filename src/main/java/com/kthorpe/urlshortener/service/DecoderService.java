package com.kthorpe.urlshortener.service;

import com.kthorpe.urlshortener.exception.DecodingException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Slf4j
@Service
public class DecoderService {

    // Hash table for looking up an original URL by an encoded URL
    @Autowired private HashMap<String, String> encodedToUrlCache;

    /**
     * Given an encoded URL, search the cache for its original version
     *
     * @param encodedUrl The encoded URL
     * @return The corresponding original URL
     * @throws DecodingException if an encoded URL has not been seen before
     */
    public String decodeUrl(String encodedUrl) throws DecodingException {
        String originalUrl = encodedToUrlCache.get(encodedUrl);

        if (originalUrl == null) {
            log.error("Encoded URL not found in cache");
            throw new DecodingException("URL not found");
        }

        log.info("Encoded URL found in cache. Returning original URL");
        return originalUrl;
    }
}
