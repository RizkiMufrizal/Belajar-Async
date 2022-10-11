package org.rizki.mufrizal.async.service.impl;

import java.io.FileOutputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.rizki.mufrizal.async.domain.Calculate;
import org.rizki.mufrizal.async.service.CalculateService;

@Service
@Slf4j
public class CalculateServiceImpl implements CalculateService {

    private List<List<Calculate>> calculates;

    @Async("asyncTaskExecutor")
    @Override
    public CompletableFuture<Void> generateExcelParalel(int max, List<Calculate> calculates) {
        this.calculates = new ArrayList<>();
        int totalRaw = calculates.size();
        countParalel(max, totalRaw, calculates);
        log.info("calculates {}", this.calculates);

        ForkJoinPool customThreadPool = new ForkJoinPool(10);
        try {
            customThreadPool.submit(() -> this.calculates.parallelStream().forEach(c -> {
                preparedExcel(c);
            })).get();
        } catch (InterruptedException | ExecutionException ex) {
            log.error(ex.getMessage());
        } finally {
            customThreadPool.shutdown();
        }
        return CompletableFuture.completedFuture(null);
    }

    public void countParalel(int max, int totalRaw, List<Calculate> calculates) {
        log.info("max-0 {}, {}, {}", max, totalRaw, (totalRaw > max));
        if (totalRaw > max) {
            log.info("max-1 {}, {}", max, totalRaw);
            this.calculates.add(calculates.subList(totalRaw - max, totalRaw));
            log.info("one {}", calculates.subList(totalRaw - max, totalRaw));
            countParalel(max, totalRaw - max, calculates);
        } else {
            log.info("max-2 {}, {}", 0, totalRaw);
            this.calculates.add(calculates.subList(0, totalRaw));
            log.info("two {}", calculates.subList(0, totalRaw));
        }
    }

    public CompletableFuture<XSSFWorkbook> preparedExcel(List<Calculate> calculates) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("calculate");
        createHeader(workbook, sheet);
        createData(workbook, sheet, calculates);

        FileOutputStream outputStream;
        try {
            log.info("generate excel");
            outputStream = new FileOutputStream("/home/rizki/Documents/Project/spring-boot-project/Belajar-Async/excel/" + UUID.randomUUID() + ".xlsx");
            workbook.write(outputStream);
            workbook.close();
            log.info("end");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return CompletableFuture.completedFuture(workbook);
    }

    private void createData(XSSFWorkbook workbook, XSSFSheet sheet, List<Calculate> calculates) {
        log.info("create data");
        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (Calculate calculate : calculates) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            createCell(row, columnCount++, calculate.getName(), style, sheet);
            createCell(row, columnCount++, calculate.getAngka1(), style, sheet);
            createCell(row, columnCount++, calculate.getAngka2(), style, sheet);
            createCell(row, columnCount++, calculate.getTotal(), style, sheet);
        }
    }

    private void createHeader(XSSFWorkbook workbook, XSSFSheet sheet) {
        log.info("create header");
        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        createCell(row, 0, "Name", style, sheet);
        createCell(row, 1, "Angka 1", style, sheet);
        createCell(row, 2, "Angka 2", style, sheet);
        createCell(row, 3, "Total", style, sheet);
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style, XSSFSheet sheet) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

}
