package com.batch.excel;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class Controller {
    @PostMapping("/execute")
    public String generateExcel(@RequestBody Request request) {
        List<Object[]> data = new ObjectList().getObjectList(request.getListSize());
        return ApachePoiImpl.generateExcel(data);
    }
}
