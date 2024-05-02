package com.kthorpe.urlshortener.controller;

import com.kthorpe.urlshortener.exception.DecodingException;
import com.kthorpe.urlshortener.exception.ValidationException;
import com.kthorpe.urlshortener.resource.UrlResource;
import com.kthorpe.urlshortener.service.DecoderService;
import com.kthorpe.urlshortener.service.EncoderService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RestController
public class UrlController {

    private static String URL_PATTERN =
            "^" // Start of the string
            + "(https?://)" // Matches 'http://' or 'https://'
            + "([a-zA-Z0-9-]+" // Matches the first part of the domain
            + "(\\.[a-zA-Z0-9-]+)+)" // Matches additional domain parts, each prefixed with a dot
            + "(:[0-9]{1,5})?" // Matches a colon followed by 1 to 5 digits (port number), optional
            + "(/[a-zA-Z0-9_/.-]*[^/])?" // Matches a path without a trailing slash, optional
            + "/?" // Allows one optional trailing slash
            + "(\\?[a-zA-Z0-9&=._-]*)?" // Matches a query string starting with '?', optional
            + "(#[a-zA-Z0-9_-]*)?" // Matches a fragment identifier starting with '#', optional
            + "$"; // End of the string


    private static final Pattern pattern = Pattern.compile(URL_PATTERN);

    EncoderService encoderService;
    DecoderService decoderService;

    @Autowired
    public UrlController(EncoderService encoderService, DecoderService decoderService) {
        this.encoderService = encoderService;
        this.decoderService = decoderService;
    }

    @RequestMapping("/encode")
    public UrlResource encodeUrl(@RequestBody UrlResource urlResource) {
        try {
            validateUrl(urlResource);

            String encodedUrl = encoderService.encodeUrl(urlResource.getUrl());
            return new UrlResource(encodedUrl);
        } catch (ValidationException e) {
            log.error("Request failed validation: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (Exception e) {
            log.error("Error while encoding URL", e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Invalid request", e);
        }
    }

    @RequestMapping("/decode")
    public UrlResource decodeUrl(@RequestBody UrlResource urlResource) {
        try {
            validateUrl(urlResource);
            String decodedUrl = decoderService.decodeUrl(urlResource.getUrl());

            return new UrlResource(decodedUrl);
        } catch (ValidationException e) {
            log.error("Request failed validation: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (DecodingException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
        catch (Exception e) {
            log.error("Error while decoding URL", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    /**
     * Validates the incoming request resource, throwing an exception if any check fails.
     *
     * @param urlResource the incoming URL resource
     * @throws ValidationException exception and reason if a check fails
     */
    private void validateUrl(UrlResource urlResource) throws ValidationException {
        // Provided JSON must contain the `url` key
        if (urlResource.getUrl() == null) {
            throw new ValidationException("Null URL in resource");
        }

        // Provided URL should be a valid URL
        Matcher matcher = pattern.matcher(urlResource.getUrl());

        if (!matcher.matches()) {
            throw new ValidationException("Invalid URL");
        }
    }
}
