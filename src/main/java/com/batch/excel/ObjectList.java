package com.batch.excel;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ObjectList {

    private static final Logger logger = Logger.getLogger(ObjectList.class.getName());

    public List<Object[]> getObjectList(int listSize) {
        logger.info("Generating list of size: " + listSize);
        List<Object[]> list = new ArrayList<>();
        int cont = 0;
        while (listSize > cont) {
            list.add(getObject());
            cont++;
        }
        return list;
    }

    private Object[] getObject() {
        return new Object[] {
                "John",
                "Doe",
                30,
                "123 Main St",
                "john.doe@example.com",
                "123-456-7890",
                "987-654-3210",
                "New York",
                "NY",
                "USA",
                "John",
                "Doe",
                30,
                "123 Main St",
                "john.doe@example.com",
                "123-456-7890",
                "987-654-3210",
                "New York",
                "NY",
                "USA",
                "John",
                "Doe",
                30,
                "123 Main St",
                "john.doe@example.com",
                "123-456-7890",
                "987-654-3210",
                "New York",
                "NY",
                "USA",
                "John",
                "Doe",
                30,
                "123 Main St",
                "john.doe@example.com",
                "123-456-7890",
                "987-654-3210",
                "New York",
                "NY",
                "USA",
                "John",
                "Doe",
                30,
                "123 Main St",
                "john.doe@example.com",
                "123-456-7890",
                "987-654-3210",
                "New York",
                "NY",
                "USA",
                "John",
                "Doe",
                30,
                "123 Main St",
                "john.doe@example.com",
                "123-456-7890",
                "987-654-3210",
                "New York",
                "NY",
                "USA",
                "John",
                "Doe",
                30,
                "123 Main St",
                "john.doe@example.com",
                "123-456-7890",
                "987-654-3210",
                "New York",
                "NY",
                "USA",
                "John",
                "Doe",
                30,
                "123 Main St",
                "john.doe@example.com",
                "123-456-7890",
                "987-654-3210",
                "New York",
                "NY",
                "USA",
                "John",
                "Doe",
                30,
                "123 Main St",
                "john.doe@example.com",
                "123-456-7890",
                "987-654-3210",
                "New York",
                "NY",
                "USA",
                "John",
                "Doe",
                30,
                "123 Main St",
                "john.doe@example.com",
                "123-456-7890",
                "987-654-3210",
                "New York",
                "NY",
                "USA"
        };
    }

}
