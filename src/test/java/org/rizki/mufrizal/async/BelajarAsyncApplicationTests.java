package org.rizki.mufrizal.async;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.rizki.mufrizal.async.httpclient.AsyncHttpClient;
import org.rizki.mufrizal.async.httpclient.SyncHttpClient;
import org.rizki.mufrizal.async.mapper.AlbumMapper;
import org.rizki.mufrizal.async.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.rizki.mufrizal.async.domain.Calculate;
import org.rizki.mufrizal.async.service.CalculateService;

@SpringBootTest
@Slf4j
class BelajarAsyncApplicationTests {

    @Autowired
    private AsyncHttpClient asyncHttpClient;

    @Autowired
    private SyncHttpClient syncHttpClient;

    @Autowired
    private CalculateService calculateService;

    @Test
    void asyncHttpClientTest() throws ExecutionException, InterruptedException, JsonProcessingException {
        log.info("Start Test Async Test");
        CompletableFuture<AlbumMapper> albumMapperResponseMapperCompletableFuture = asyncHttpClient.getAlbum();
        CompletableFuture<CategoryMapper> categoryMapperCompletableFuture = asyncHttpClient.getCategory();

        log.info("Result Get Album {}", new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(albumMapperResponseMapperCompletableFuture.get()));
        log.info("Result Get Category {}", new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(categoryMapperCompletableFuture.get()));
        log.info("End Test Async Test");
    }

    @Test
    void syncHttpClientTest() throws ExecutionException, InterruptedException, JsonProcessingException {
        log.info("Start Test Sync Test");
        CompletableFuture<AlbumMapper> albumMapperResponseMapperCompletableFuture = syncHttpClient.getAlbum();
        CompletableFuture<CategoryMapper> categoryMapperCompletableFuture = syncHttpClient.getCategory();

        log.info("Result Get Album {}", new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(albumMapperResponseMapperCompletableFuture.get()));
        log.info("Result Get Category {}", new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(categoryMapperCompletableFuture.get()));
        log.info("End Test Sync Test");
    }

    @Test
    void generateExcelParalel() throws InterruptedException, ExecutionException {
        List<Calculate> calculates = new ArrayList<>();

        for (int i = 1; i <= 100; i++) {
            Calculate calculate = new Calculate();
            calculate.setName("Perjumlahan");
            calculate.setAngka1(i);
            calculate.setAngka2(i);
            calculate.setTotal(i + i);

            calculates.add(calculate);
        }

        calculateService.generateExcelParalel(10, calculates).get();
    }

}
