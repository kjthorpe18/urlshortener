package com.kthorpe.urlshortener.service;

import static org.apache.commons.text.CharacterPredicates.DIGITS;
import static org.apache.commons.text.CharacterPredicates.LETTERS;

import com.kthorpe.urlshortener.exception.EncodingException;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Slf4j
@Service
public class EncoderService {

    // Generator for encoded URLs
    private static final RandomStringGenerator generator =
            new RandomStringGenerator.Builder()
                    .withinRange('0', 'z')
                    .filteredBy(LETTERS, DIGITS)
                    .get();

    @Value("${base.url}")
    private String baseUrl;

    @Value("${path.length}")
    private int pathLength;

    // Hash tables for storing original and encoded URLs for bidirectional lookups
    @Autowired private HashMap<String, String> urlToEncodedCache;
    @Autowired private HashMap<String, String> encodedToUrlCache;

    /**
     * Given an un-encoded URL, find a previously generated shortened URL or generate a new one
     *
     * @param url The original URL
     * @return The shortened URL for the given URL
     */
    public String encodeUrl(String url) throws EncodingException {
        String encoded = urlToEncodedCache.get(url);

        if (encoded != null) {
            log.info("URL found in cache. Returning previously generated short URL.");
            return encoded;
        }

        log.info("URL not found in cache. Generating URL and storing.");
        encoded = generateAndSearch(url);
        urlToEncodedCache.put(url, encoded);

        return encoded;
    }

    /**
     * Generates a unique URL
     *
     * <p>Generates a new URL and checks if it already exists in the cache. Repeat up to 100 times
     * or until one is not found to ensure a generated URL is unique.
     *
     * @param url The unencoded URL
     * @return A URL which is not in the database
     */
    private String generateAndSearch(String url) throws EncodingException {
        String newEncoded;
        String originalUrlSearch;
        int count = 1;

        do {
            log.info("Generating a URL and checking for uniqueness. Searches: {}", count);
            newEncoded = generateShortUrl();
            originalUrlSearch = encodedToUrlCache.get(newEncoded);
            count++;
        } while (originalUrlSearch != null && count <= 100);

        // If we've searched this many times and still haven't found a unique URL, throw an
        // exception
        if (originalUrlSearch != null) {
            throw new EncodingException("Unable to generate unique URL");
        }

        // Add the new pair to the cache
        encodedToUrlCache.put(newEncoded, url);
        return newEncoded;
    }

    /**
     * Generates a random URL
     *
     * @return The generated URL string
     */
    private String generateShortUrl() {
        return baseUrl + "/" + generator.generate(pathLength);
    }
}
