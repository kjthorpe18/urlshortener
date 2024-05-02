package com.kthorpe.urlshortener.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.kthorpe.urlshortener.entity.UrlEntity;
import com.kthorpe.urlshortener.exception.DecodingException;
import com.kthorpe.urlshortener.repository.IUrlRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class DecoderServiceTests {

    @Autowired DecoderService decoderService;
    @MockBean IUrlRepository urlRepository;

    @Test
    public void decodeUrl_inDb() throws DecodingException {
        UrlEntity savedUrl = new UrlEntity("https://example.com/hello", "http://short.ly/HUgtSC");
        when(urlRepository.findByEncodedUrl(anyString())).thenReturn(savedUrl);

        String originalUrl = decoderService.decodeUrl("http://short.ly/HUgtSC");

        verify(urlRepository).findByEncodedUrl(savedUrl.getEncodedUrl());
        assertEquals(originalUrl, savedUrl.getUrl());
    }

    @Test
    public void decodeUrl_notInDbThrowsException() {
        when(urlRepository.findByEncodedUrl(anyString())).thenReturn(null);

        assertThrows(
                DecodingException.class, () -> decoderService.decodeUrl("http://short.ly/HUgtSC"));
        verify(urlRepository).findByEncodedUrl(anyString());
    }
}
