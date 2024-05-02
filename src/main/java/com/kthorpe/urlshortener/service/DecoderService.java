package com.kthorpe.urlshortener.service;

import com.kthorpe.urlshortener.entity.UrlEntity;
import com.kthorpe.urlshortener.exception.DecodingException;
import com.kthorpe.urlshortener.repository.IUrlRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DecoderService {

    @Autowired IUrlRepository urlRepository;

    /**
     * Given an encoded URL, search the DB for its original version
     *
     * @param encodedUrl The encoded URL
     * @return The corresponding original URL
     * @throws DecodingException if an encoded URL has not been seen before
     */
    public String decodeUrl(String encodedUrl) throws DecodingException {
        UrlEntity urlEntity = urlRepository.findByEncodedUrl(encodedUrl);

        if (urlEntity == null) {
            log.error("Encoded URL not found in repository");
            throw new DecodingException("URL not found");
        }

        log.info("Encoded URL found in repository. Returning original URL");
        return urlEntity.getUrl();
    }
}
