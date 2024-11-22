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
    private static final int CHUNK_SIZE = 8192; // 8KB chunks for reading

    public static String generateExcel(List<Object[]> data, int rowAccessWindows, int bytes) {
        logger.info("final impl of base64 buffer 8");
        logger.info("Generating Excel...");
        String filePath = "excelFile.xlsx";
        
        try (SXSSFWorkbook workbook = new SXSSFWorkbook(rowAccessWindows)) {
            createExcelFile(workbook, data, rowAccessWindows, filePath);
            return encodeFileToBase64(filePath, bytes);
        } catch (Exception e) {
            throw new RuntimeException("Error generating Excel: " + e.getMessage(), e);
        } finally {
            deleteFile(filePath);
        }
    }

    private static void createExcelFile(SXSSFWorkbook workbook, List<Object[]> data, int rowAccessWindows, String filePath) throws IOException {
        SXSSFSheet sheet = workbook.createSheet("sheet 1");
        
        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle cellStyle = createCellStyle(workbook);

        logger.info("Writing rows...");
        writeDataToSheet(sheet, data, rowAccessWindows, headerStyle, cellStyle);

        try (FileOutputStream fileOut = new FileOutputStream(filePath);
             BufferedOutputStream bufferedOut = new BufferedOutputStream(fileOut)) {
            workbook.write(bufferedOut);
        }
        logger.info("Excel file generated locally at " + filePath);
    }

    private static CellStyle createHeaderStyle(SXSSFWorkbook workbook) {
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return headerStyle;
    }

    private static CellStyle createCellStyle(SXSSFWorkbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return cellStyle;
    }

    private static void writeDataToSheet(SXSSFSheet sheet, List<Object[]> data, int rowAccessWindows,
                                       CellStyle headerStyle, CellStyle cellStyle) throws IOException {
        int rowNum = 0;
        for (Object[] rowData : data) {
            Row row = sheet.createRow(rowNum++);
            boolean isHeaderRow = rowNum == 1;
            
            for (int colNum = 0; colNum < rowData.length; colNum++) {
                Cell cell = row.createCell(colNum);
                cell.setCellValue(rowData[colNum].toString());
                cell.setCellStyle(isHeaderRow ? headerStyle : cellStyle);
            }

            if (rowNum % rowAccessWindows == 0) {
                sheet.flushRows(rowAccessWindows);
            }
        }
    }

    private static String encodeFileToBase64(String filePath, int bufferSize) throws IOException {
        logger.info("Encode file to base64...");
        File file = new File(filePath);
        
        // Use a fixed buffer size of 8KB for optimal performance
        int actualBufferSize = CHUNK_SIZE;
        byte[] buffer = new byte[actualBufferSize];
        
        StringBuilder base64Content = new StringBuilder();
        Base64.Encoder encoder = Base64.getEncoder().withoutPadding();
        
        try (InputStream inputStream = new BufferedInputStream(new FileInputStream(file), actualBufferSize)) {
            int bytesRead;
            byte[] encodedBytes;
            
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                if (bytesRead > 0) {
                    // Only encode the actual bytes read
                    encodedBytes = encoder.encode(Arrays.copyOf(buffer, bytesRead));
                    base64Content.append(new String(encodedBytes));
                }
            }
        }

        logger.info("Base64 encoding completed. Total length: " + base64Content.toString().length());
        return String.valueOf(base64Content.toString().length());
    }

    private static void deleteFile(String filePath) {
        try {
            Files.delete(new File(filePath).toPath());
            logger.info("File deleted...");
        } catch (IOException e) {
            logger.warning("Could not delete temporary file: " + filePath + ". Error: " + e.getMessage());
        }
    }
}
