package com.batch.excel;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;

@RestController
public class Controller {

    private static final Logger logger = Logger.getLogger(Controller.class.getName());

    @PostMapping("/execute")
    public String generateExcel(@RequestBody Request request) {
        logger.info("Request: " + request.toString());
        List<Object[]> data = new ObjectList().getObjectList(request.getListSize());
        return ApachePoiImpl.generateExcel(data, request.getRowAccessWindows());
    }

}
