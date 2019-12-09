
package com.netcracker.testerritto.properties;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public enum ListsAttr {
    OPEN(BigInteger.valueOf(1), "open"),
    ONE_ANSWER(BigInteger.valueOf(2), "one-answer test"),
    MULTIPLE_ANSWER(BigInteger.valueOf(3),"multiple answer"),
    NEGATIVE_TEXT_ANSWER(BigInteger.valueOf(4),"нет, это совсем не так"),
    POSITIVE_TEXT_ANSWER(BigInteger.valueOf(5),"пожалуй так"),
    RIGHT_TEXT_ANSWER(BigInteger.valueOf(6),"верно"),
    PASSED(BigInteger.valueOf(7), "Passed"),
    NOT_PASSED(BigInteger.valueOf(8), "Not passed");

    private final BigInteger id;
    private final String value;
    private static Map<BigInteger, ListsAttr> idToEnum;

    ListsAttr(BigInteger id, String value) {
        this.id = id;
        this.value = value;
    }

    public BigInteger getid() {
        return id;
    }

    public String getvalue() {
        return value;
    }

    public static ListsAttr getvalueByid(BigInteger id) {

        if (idToEnum.containsKey(id)) {
            return idToEnum.get(id);
        }
        return null;
    }

    static {
        idToEnum = new HashMap<>();
        for (ListsAttr list : values()) {
            idToEnum.put(list.id, list);
        }
    }
}
