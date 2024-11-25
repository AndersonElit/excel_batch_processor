package com.batch.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.logging.Logger;

public class ApachePoiImpl {

    private static final Logger logger = Logger.getLogger(ApachePoiImpl.class.getName());
    private static final int CHUNK_SIZE = 8192; // 8KB chunks for reading
    private static final int DEFAULT_BATCH_SIZE = 1000;  // Default batch size for processing

    public static String generateExcel(List<Object[]> data, int rowAccessWindows, int bytes) {
        logger.info("let only 20 columns");
        logger.info("Generating Excel...");
        String filePath = "excelFile.xlsx";
        
        try (SXSSFWorkbook workbook = new SXSSFWorkbook(rowAccessWindows)) {
            createExcelFile(workbook, data, rowAccessWindows, filePath);
            logger.info("Excel file generated locally at " + filePath);
            
            // Get base64 content
            String base64Content = encodeFileToBase64(filePath, bytes);
            
            // Verify the file was properly encoded
            logger.info("Verifying base64 content...");
            if (base64Content == null || base64Content.isEmpty()) {
                throw new RuntimeException("Base64 encoding failed - content is empty");
            }
            
            return base64Content;
            
        } catch (Exception e) {
            logger.severe("Error in Excel generation: " + e.getMessage());
            throw new RuntimeException("Failed to generate Excel: " + e.getMessage());
        } finally {
            deleteFile(filePath);
            logger.info("File deleted...");
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
        int totalRows = data.size();
        int batchSize = DEFAULT_BATCH_SIZE;
        int processedRows = 0;
        
        // Process header row separately
        if (!data.isEmpty()) {
            Object[] headerData = data.get(0);
            Row headerRow = sheet.createRow(rowNum++);
            for (int colNum = 0; colNum < headerData.length; colNum++) {
                Cell cell = headerRow.createCell(colNum);
                cell.setCellValue(headerData[colNum].toString());
                cell.setCellStyle(headerStyle);
            }
            processedRows++;
        }

        // Process data in batches
        for (int i = 1; i < totalRows; i++) {
            Object[] rowData = data.get(i);
            Row row = sheet.createRow(rowNum++);
            
            for (int colNum = 0; colNum < rowData.length; colNum++) {
                Cell cell = row.createCell(colNum);
                cell.setCellValue(rowData[colNum].toString());
                cell.setCellStyle(cellStyle);
            }
            
            processedRows++;
            
            // Flush when reaching batch size or row access window
            if (processedRows % batchSize == 0 || processedRows % rowAccessWindows == 0) {
                sheet.flushRows(rowAccessWindows);
                logger.info(String.format("Processed %d/%d rows (%.2f%%)", 
                    processedRows, totalRows, (processedRows * 100.0) / totalRows));
                    
                // Request garbage collection after processing each batch
                System.gc();
                logger.info("Requested garbage collection after batch processing");
            }
        }
        
        // Final flush for any remaining rows
        if (processedRows % rowAccessWindows != 0) {
            sheet.flushRows(rowAccessWindows);
        }
        
        logger.info("Completed processing all " + processedRows + " rows");
    }

    private static String encodeFileToBase64(String filePath, int bufferSize) {
        logger.info("Encode file to base64...");
        
        try {
            // Read the entire file into a byte array
            Path path = Paths.get(filePath);
            byte[] fileContent = Files.readAllBytes(path);
            
            // Encode the entire file at once to ensure proper padding
            String base64Content = Base64.getEncoder().encodeToString(fileContent);
            
            logger.info("Base64 encoding completed. Total length: " + base64Content.length());
            return base64Content;
            
        } catch (IOException e) {
            logger.severe("Error reading file for base64 encoding: " + e.getMessage());
            throw new RuntimeException("Failed to read file for base64 encoding", e);
        }
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
