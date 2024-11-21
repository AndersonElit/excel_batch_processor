package com.batch.excel;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;

@RestController
public class Controller {

    private static final Logger logger = Logger.getLogger(Controller.class.getName());

    @PostMapping("/execute")
    public ResponseEntity<String> generateExcel(@RequestBody Request request) {
        try {
            logger.info("Request: " + request.toString());
            List<Object[]> data = new ObjectList().getObjectList(request.getListSize());
            String base64Content = ApachePoiImpl.generateExcel(data, request.getRowAccessWindows(), request.getBytes());
            return ResponseEntity.ok(base64Content);
        } catch (Exception e) {
            logger.severe("Error generating Excel: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error generating Excel: " + e.getMessage());
        }
    }
}
