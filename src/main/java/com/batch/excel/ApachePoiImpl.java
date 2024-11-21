package com.batch.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.*;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;
import java.util.logging.Logger;

public class ApachePoiImpl {

    private static final Logger logger = Logger.getLogger(ApachePoiImpl.class.getName());

    public static String generateExcel(List<Object[]> data, int rowAccessWindows) {
        logger.info("Generating Excel...");
        Workbook workbook = new SXSSFWorkbook(rowAccessWindows);
        Sheet sheet = workbook.createSheet("sheet 1");

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        int rowNum = 0;
        for (Object[] rowData : data) {
            Row row = sheet.createRow(rowNum++);

            boolean isHeaderRow = rowNum == 1;
            for (int colNum = 0; colNum < rowData.length; colNum++) {
                Cell cell = row.createCell(colNum);
                Object value = rowData[colNum];

                cell.setCellValue((value.toString()));
                if (isHeaderRow) {
                    cell.setCellStyle(headerStyle);
                } else {
                    cell.setCellStyle(cellStyle);
                }
            }
        }

        String filePath = "excelFile.xlsx";
        logger.info("Generating Excel file locally at " + filePath);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            workbook.write(fileOutputStream);
            workbook.close();
            logger.info("Excel file generated locally at " + filePath);
            logger.info("Encode file to base64...");
            File file = new File(filePath);
            byte[] fileBytes = Files.readAllBytes(file.toPath());
            String base64Encoded = Base64.getEncoder().encodeToString(fileBytes);
            logger.info("File encoded to Base64.");
            file.delete();
            logger.info("File deleted.");
            return base64Encoded;
        }  catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
