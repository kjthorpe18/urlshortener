package com.kthorpe.urlshortener.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.kthorpe.urlshortener.exception.DecodingException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashMap;

@SpringBootTest
public class DecoderServiceTests {

    @Autowired DecoderService decoderService;
    @MockBean(name = "encodedToUrlCache") HashMap<String, String> encodedToUrlCache;

    @Test
    public void decodeUrl_inCache() throws DecodingException {
        when(encodedToUrlCache.get(anyString())).thenReturn("https://example.com/hello");

        String originalUrl = decoderService.decodeUrl("http://short.ly/HUgtSC");

        verify(encodedToUrlCache).get("http://short.ly/HUgtSC");
        assertEquals(originalUrl, "https://example.com/hello");
    }

    @Test
    public void decodeUrl_notInCacheThrowsException() {
        when(encodedToUrlCache.get(anyString())).thenReturn(null);

        assertThrows(
                DecodingException.class, () -> decoderService.decodeUrl("http://short.ly/HUgtSC"));
        verify(encodedToUrlCache).get(anyString());
    }
}
