package com.kthorpe.urlshortener.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kthorpe.urlshortener.exception.DecodingException;
import com.kthorpe.urlshortener.exception.EncodingException;
import com.kthorpe.urlshortener.service.DecoderService;
import com.kthorpe.urlshortener.service.EncoderService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@WebMvcTest(UrlController.class)
public class UrlControllerTests {

    @Autowired private MockMvc mockMvc;

    @MockBean private EncoderService encoderService;

    @MockBean private DecoderService decoderService;

    @Test
    public void testEncodeUrl_passValidation() throws Exception {
        List<String> validUrls =
                List.of(
                        "https://example.com/hello",
                        "https://sub.valid-domain.io/valid-path",
                        "https://www.exaMple.com/hello/",
                        "http://aAa.com?lang=fr",
                        "http://www.exwithport.ui:455",
                        "http://www.exfragment.ui?lang=fr#one",
                        "https://www.example.com:443/path/to/page?query1=value1&query2=value2#section");

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
    public void testEncodeUrl_encodingException() throws Exception {
        when(encoderService.encodeUrl(anyString())).thenThrow(new EncodingException("Unable to generate unique URL"));
        String jsonRequest = "{\"url\":\"https://example.com/hello\"}";

        mockMvc.perform(
                        post("/encode")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                .andExpect(status().isRequestTimeout());
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
    public void testEncodeUrl_invalidUrlFailsValidation() throws Exception {
        List<String> invalidUrls =
                List.of(
                        "notAUrl",
                        "no.http.com",
                        "http://a",
                        "https://www.doubleslashes.com//",
                        "https://www.doubleslashes.com/path//",
                        "http://example.com/path/@invalid*char");

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
    public void testDecodeUrl_notFound() throws Exception {
        when(decoderService.decodeUrl(anyString())).thenThrow(DecodingException.class);

        String jsonRequest = "{\"url\":\"http://example.com\"}";

        mockMvc.perform(
                        post("/decode")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                .andExpect(status().isNotFound());
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
