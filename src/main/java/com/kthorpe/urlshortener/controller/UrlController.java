package com.kthorpe.urlshortener.controller;

import com.kthorpe.urlshortener.resource.UrlResource;
import com.kthorpe.urlshortener.service.DecoderService;
import com.kthorpe.urlshortener.service.EncoderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UrlController {

    EncoderService encoderService;
    DecoderService decoderService;

    @Autowired
    public UrlController(EncoderService encoderService, DecoderService decoderService) {
        this.encoderService = encoderService;
        this.decoderService = decoderService;
    }

    @RequestMapping("/encode")
    public UrlResource encodeUrl(@RequestBody UrlResource urlResource) {
        String encodedUrl = encoderService.encodeUrl(urlResource.getUrl());

        return new UrlResource(encodedUrl);
    }

    @RequestMapping("/decode")
    public UrlResource decodeUrl(@RequestBody UrlResource urlResource) {
        String decodedUrl = decoderService.decodeUrl(urlResource.getUrl());
        return new UrlResource(decodedUrl);
    }
}
