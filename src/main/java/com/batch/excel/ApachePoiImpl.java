package com.batch.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.logging.Logger;

public class ApachePoiImpl {

    private static final Logger logger = Logger.getLogger(ApachePoiImpl.class.getName());

    public static String generateExcel(List<Object[]> data, int rowAccessWindows, int bytes) {
        logger.info("Generating Excel...");
        SXSSFWorkbook workbook = new SXSSFWorkbook(rowAccessWindows);
        SXSSFSheet sheet = workbook.createSheet("sheet 1");

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

        logger.info("Writing rows...");
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

            if (rowNum % rowAccessWindows == 0) {
                try {
                    sheet.flushRows(rowAccessWindows);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        String filePath = "excelFile.xlsx";
        logger.info("Generating Excel file locally at " + filePath);
        String base64Content;

        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath);
             BufferedOutputStream bufferedOut = new BufferedOutputStream(fileOutputStream)) {

            workbook.write(bufferedOut);
            workbook.close();

            logger.info("Excel file generated locally at " + filePath);
            logger.info("Encode file to base64...");

            // Use 8KB buffer size for optimal performance
            byte[] buffer = new byte[bytes];
            File file = new File(filePath);
            long fileSize = file.length();

            // Pre-calculate base64 size to avoid StringBuilder resizing
            int base64Size = (int)(fileSize * 1.4); // Base64 is roughly 4/3 of original size
            StringBuilder base64Builder = new StringBuilder(base64Size);

            try (InputStream inputStream = new BufferedInputStream(new FileInputStream(filePath))) {
                Base64.Encoder encoder = Base64.getEncoder();
                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    if (bytesRead > 0) {
                        byte[] readData = bytesRead < buffer.length ?
                                Arrays.copyOf(buffer, bytesRead) : buffer;
                        base64Builder.append(encoder.encodeToString(readData));
                    }
                }
            }
            base64Content = base64Builder.toString();
            logger.info("File encoded to Base64.");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            Files.delete(new File(filePath).toPath());
            logger.info("File deleted...");
        } catch (IOException e) {
            logger.warning("Could not delete temporary file: " + filePath + ". Error: " + e.getMessage());
        }

        return base64Content;
    }
}
