package com.kthorpe.urlshortener.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.kthorpe.urlshortener.exception.EncodingException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashMap;

@SpringBootTest
public class EncoderServiceTests {

    @Autowired EncoderService encoderService;
    @MockBean(name = "urlToEncodedCache") HashMap<String, String> urlToEncodedCache;
    @MockBean(name = "encodedToUrlCache") HashMap<String, String> encodedToUrlCache;

    @Test
    public void testEncodeUrl_notInCache() throws EncodingException {
        // Set up
        when(urlToEncodedCache.get(anyString())).thenReturn(null);
        when(urlToEncodedCache.put(anyString(), anyString())).thenReturn(null);

        // Execute
        String encodedUrl = encoderService.encodeUrl("https://example.com/hello");

        // Assert
        verify(urlToEncodedCache).get("https://example.com/hello");
        verify(urlToEncodedCache).put(eq("https://example.com/hello"), anyString());
        assertNotNull(encodedUrl);
    }

    @Test
    public void testEncodeUrl_collisionRepeatsUntilNotFound() throws EncodingException {
        String oldValue = "https://example.com/some/old/value";
        when(urlToEncodedCache.get(anyString())).thenReturn(null);
        when(urlToEncodedCache.put(anyString(), anyString())).thenReturn(oldValue);

        // First two generations will collide, third will not
        when(encodedToUrlCache.get(anyString()))
                .thenReturn(oldValue)
                .thenReturn(oldValue)
                .thenReturn(null);

        String encodedUrl = encoderService.encodeUrl("https://example.com/hello");

        verify(urlToEncodedCache).get("https://example.com/hello");
        verify(encodedToUrlCache, times(3)).get(anyString());
        verify(urlToEncodedCache).put(eq("https://example.com/hello"), anyString());

        assertNotNull(encodedUrl);
    }

    @Test
    public void testEncodeUrl_inCache() throws EncodingException {
        when(urlToEncodedCache.get("https://example.com/hello")).thenReturn("http://short.ly/HUgtSC");

        String encodedUrl = encoderService.encodeUrl("https://example.com/hello");

        verify(urlToEncodedCache).get("https://example.com/hello");
        verify(urlToEncodedCache, never()).put(anyString(), anyString());
        assertEquals(encodedUrl, "http://short.ly/HUgtSC");
    }
}
