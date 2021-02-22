package org.rizki.mufrizal.async.httpclient;

import lombok.extern.slf4j.Slf4j;
import org.rizki.mufrizal.async.mapper.AlbumMapper;
import org.rizki.mufrizal.async.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class AsyncHttpClient {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OAuth2RestOperations oAuth2RestOperations;

    @Autowired
    private Environment environment;

    @Async("asyncTaskExecutor")
    public CompletableFuture<AlbumMapper> getAlbum() {
        log.info("Start Get Album");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", "Bearer " + oAuth2RestOperations.getAccessToken().getValue());
        HttpEntity<HttpHeaders> entity = new HttpEntity<>(httpHeaders);

        ResponseEntity<AlbumMapper> albumMapperResponseEntity = restTemplate
                .exchange(environment.getRequiredProperty("service.getAllNewRelease.url"), HttpMethod.GET, entity, AlbumMapper.class);
        log.info("End Get Album");
        return CompletableFuture.completedFuture(albumMapperResponseEntity.getBody());
    }

    @Async("asyncTaskExecutor")
    public CompletableFuture<CategoryMapper> getCategory() {
        log.info("Start Get Category");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", "Bearer " + oAuth2RestOperations.getAccessToken().getValue());
        HttpEntity<HttpHeaders> entity = new HttpEntity<>(httpHeaders);

        ResponseEntity<CategoryMapper> categoryMapperResponseEntity = restTemplate
                .exchange(environment.getRequiredProperty("service.getCategories.url"), HttpMethod.GET, entity, CategoryMapper.class);
        log.info("End Get Category");
        return CompletableFuture.completedFuture(categoryMapperResponseEntity.getBody());
    }

}