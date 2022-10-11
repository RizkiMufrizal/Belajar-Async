package org.rizki.mufrizal.async.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.rizki.mufrizal.async.domain.Calculate;

public interface CalculateService {

    CompletableFuture<Void> generateExcelParalel(int max, List<Calculate> calculates);
}