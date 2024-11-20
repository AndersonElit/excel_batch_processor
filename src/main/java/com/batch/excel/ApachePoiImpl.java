package com.batch.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

public class ApachePoiImpl {

    public static String generateExcel(List<Object[]> data) {
        Workbook workbook = new SXSSFWorkbook(100);
        Sheet sheet = workbook.createSheet("Person Details");

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

        try (ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream()) {
            workbook.write(byteArrayOut);
            byte[] byteArray = byteArrayOut.toByteArray();
            String base64Encoded = Base64.getEncoder().encodeToString(byteArray);
            workbook.close();
            return base64Encoded;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
