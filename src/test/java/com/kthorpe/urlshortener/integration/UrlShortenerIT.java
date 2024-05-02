package com.kthorpe.urlshortener.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kthorpe.urlshortener.resource.UrlResource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UrlShortenerIT {

    @Autowired private MockMvc mvc;

    ObjectMapper om = new ObjectMapper();

    @Test
    public void testEncodeUrl() throws Exception {
        List<String> validUrls =
                List.of(
                        "https://example.com/hello",
                        "http://exampLe.com/hello",
                        "https://www.exaMple.com/hello",
                        "http://aaa.com?lang=fr",
                        "http://www.example456.ui");

        for (String url : validUrls) {
            String jsonRequest = String.format("{\"url\":\"%s\"}", url);
            mvc.perform(
                            post("/encode")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(jsonRequest))
                    .andExpect(status().isOk());
        }
    }

    @Test
    public void testDecodeUrl() throws Exception {
        String jsonRequest = "{\"url\":\"https://example.com/hello\"}";

        // Encode a URL
        MvcResult encodeResult =
                mvc.perform(
                                post("/encode")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(jsonRequest))
                        .andExpect(status().isOk())
                        .andReturn();

        UrlResource encodedResponseUrl =
                om.readValue(encodeResult.getResponse().getContentAsString(), UrlResource.class);

        String encodedUrl = encodedResponseUrl.getUrl();

        // Decode the URL from the first request
        MvcResult decodedResult =
                mvc.perform(
                                post("/decode")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content("{\"url\":\"%s\"}".formatted(encodedUrl)))
                        .andExpect(status().isOk())
                        .andReturn();

        UrlResource decodedResponseUrl =
                om.readValue(decodedResult.getResponse().getContentAsString(), UrlResource.class);

        // Decoded response should
        assertEquals("https://example.com/hello", decodedResponseUrl.getUrl());
    }
}
