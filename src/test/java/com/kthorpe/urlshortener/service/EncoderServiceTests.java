package com.kthorpe.urlshortener.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.kthorpe.urlshortener.entity.UrlEntity;
import com.kthorpe.urlshortener.repository.IUrlRepository;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class EncoderServiceTests {

    @Autowired EncoderService encoderService;
    @MockBean IUrlRepository urlRepository;
    @Captor ArgumentCaptor<UrlEntity> urlEntityArgumentCaptor;

    @Test
    public void testEncodeUrl_notInDb() {
        // Set up
        UrlEntity urlMock = mock(UrlEntity.class);
        when(urlRepository.findByUrl(anyString())).thenReturn(null);
        when(urlRepository.save(any())).thenReturn(urlMock);

        // Execute
        String encodedUrl = encoderService.encodeUrl("https://example.com/hello");

        // Assert
        verify(urlRepository).findByUrl("https://example.com/hello");
        verify(urlRepository).save(urlEntityArgumentCaptor.capture());
        assertNotNull(encodedUrl);
        assertEquals(encodedUrl, urlEntityArgumentCaptor.getValue().getEncodedUrl());
        assertEquals("https://example.com/hello", urlEntityArgumentCaptor.getValue().getUrl());
    }

    @Test
    public void testEncodeUrl_collisionRepeatsUntilNotFound() {
        UrlEntity urlMock = mock(UrlEntity.class);
        when(urlRepository.findByUrl(anyString())).thenReturn(null);
        when(urlRepository.save(any())).thenReturn(urlMock);

        // First two generations will collide, third will not
        when(urlRepository.findByEncodedUrl(anyString()))
                .thenReturn(urlMock)
                .thenReturn(urlMock)
                .thenReturn(null);

        String encodedUrl = encoderService.encodeUrl("https://example.com/hello");

        verify(urlRepository).findByUrl("https://example.com/hello");
        verify(urlRepository, times(3)).findByEncodedUrl(anyString());
        verify(urlRepository).save(urlEntityArgumentCaptor.capture());

        assertNotNull(encodedUrl);
        assertEquals(encodedUrl, urlEntityArgumentCaptor.getValue().getEncodedUrl());
        assertEquals("https://example.com/hello", urlEntityArgumentCaptor.getValue().getUrl());
    }

    @Test
    public void testEncodeUrl_inDb() {
        UrlEntity savedUrl = new UrlEntity("https://example.com/hello", "http://short.ly/HUgtSC");
        when(urlRepository.findByUrl(anyString())).thenReturn(savedUrl);

        String encodedUrl = encoderService.encodeUrl("https://example.com/hello");

        verify(urlRepository).findByUrl("https://example.com/hello");
        verify(urlRepository, never()).save(any(UrlEntity.class));
        assertEquals(encodedUrl, savedUrl.getEncodedUrl());
    }
}
