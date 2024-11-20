package com.batch.excel;

import java.util.ArrayList;
import java.util.List;

public class ObjectList {

    public List<Object[]> getObjectList(int listSize) {
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
