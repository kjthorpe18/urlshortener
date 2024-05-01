package com.kthorpe.urlshortener.controller;

import com.kthorpe.urlshortener.service.DecoderService;
import com.kthorpe.urlshortener.service.EncoderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UrlController.class)
public class UrlControllerTests {

    @Autowired private MockMvc mockMvc;

    @MockBean private EncoderService encoderService;

    @MockBean private DecoderService decoderService;

    @Test
    public void testEncodeUrl() throws Exception {
        List<String> validUrls =
                List.of(
                        "https://example.com/hello",
                        "http://exampLe.com/hello",
                        "https://www.exaMple.com/hello",
                        "http://aaa.com?lang=fr",
                        "http://www.example456.ui");

        when(encoderService.encodeUrl(anyString())).thenReturn("http://aDb.cn/aLiw4");

        for (String url : validUrls) {
            String jsonRequest = String.format("{\"url\":\"%s\"}", url);
            mockMvc.perform(
                            post("/encode")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(jsonRequest))
                    .andExpect(status().isOk())
                    .andExpect(content().string("{\"url\":\"http://aDb.cn/aLiw4\"}"));
        }
    }

    @Test
    public void testEncodeUrl_invalidField() throws Exception {
        String jsonRequest = "{\"invalidField\":\"example.com\"}";

        mockMvc.perform(
                        post("/encode")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testEncodeUrl_invalidRequestBody() throws Exception {
        String jsonRequest = "notJson";

        mockMvc.perform(
                        post("/encode")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testEncodeUrl_invalidURL() throws Exception {
        List<String> invalidUrls = List.of("notAUrl", "no.http.com", "http://a", "https://aaa");

        for (String url : invalidUrls) {
            String jsonRequest = String.format("{\"url\":\"%s\"}", url);
            mockMvc.perform(
                            post("/encode")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(jsonRequest))
                    .andExpect(status().isBadRequest());
        }
    }

    @Test
    public void testDecodeUrl() throws Exception {
        List<String> validUrls =
                List.of("http://aDb.cn/aLiw4", "https://ags.if", "http://aaa.com?lang=fr");

        when(decoderService.decodeUrl(anyString())).thenReturn("https://example.com/hello");

        for (String url : validUrls) {
            String jsonRequest = String.format("{\"url\":\"%s\"}", url);
            mockMvc.perform(
                            post("/decode")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(jsonRequest))
                    .andExpect(status().isOk())
                    .andExpect(content().string("{\"url\":\"https://example.com/hello\"}"));
        }
    }

    @Test
    public void testDecodeUrl_invalidField() throws Exception {
        String jsonRequest = "{\"invalidField\":\"example.com\"}";

        mockMvc.perform(
                        post("/decode")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDecodeUrl_invalidRequestBody() throws Exception {
        String jsonRequest = "notJson";

        mockMvc.perform(
                        post("/decode")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDecodeUrl_invalidURL() throws Exception {
        List<String> invalidUrls = List.of("notAUrl", "no.http.com", "http://a", "https://aaa");

        for (String url : invalidUrls) {
            String jsonRequest = String.format("{\"url\":\"%s\"}", url);
            mockMvc.perform(
                            post("/decode")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(jsonRequest))
                    .andExpect(status().isBadRequest());
        }
    }
}
