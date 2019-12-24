package com.netcracker.testerritto.properties;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public enum Status {
    PASSED(BigInteger.valueOf(7), "Passed"),
    NOT_PASSED(BigInteger.valueOf(8), "Not passed");

    private final BigInteger id;
    private final String value;
    private static Map<BigInteger, Status> idToEnum;

    Status(BigInteger id, String value) {
        this.id = id;
        this.value = value;
    }

    public BigInteger getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public static Status getValueById(BigInteger id) {

        if (idToEnum.containsKey(id)) {
            return idToEnum.get(id);
        }
        return null;
    }

    static {
        idToEnum = new HashMap<>();
        for (Status attribute : values()) {
            idToEnum.put(attribute.id, attribute);
        }
    }
}
