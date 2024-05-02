package com.kthorpe.urlshortener.service;

import static org.apache.commons.text.CharacterPredicates.DIGITS;
import static org.apache.commons.text.CharacterPredicates.LETTERS;

import com.kthorpe.urlshortener.entity.UrlEntity;
import com.kthorpe.urlshortener.repository.IUrlRepository;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EncoderService {

    private static final RandomStringGenerator generator =
            new RandomStringGenerator.Builder()
                    .withinRange('0', 'z')
                    .filteredBy(LETTERS, DIGITS)
                    .get();

    @Value("${base.url}")
    private String baseUrl;

    @Autowired private IUrlRepository urlRepository;

    /**
     * Given an un-encoded URL, find a previously generated shortened URL or generate a new one
     *
     * @param url The original URL
     * @return The shortened URL for the given URL
     */
    public String encodeUrl(String url) {
        UrlEntity urlEntity = urlRepository.findByUrl(url);

        if (urlEntity != null) {
            log.info("URL found in repository. Returning previously generated short URL.");
            return urlEntity.getEncodedUrl();
        }

        log.info("URL not found in repository. Generating URL and storing");

        String encoded = generateShortUrl();
        urlEntity = new UrlEntity(url, encoded);
        urlRepository.save(urlEntity);

        return encoded;
    }

    /**
     * Generates a shortened URL
     *
     * @return The generated URL string
     */
    private String generateShortUrl() {
        return baseUrl.concat(generator.generate(6));
    }
}
