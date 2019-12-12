package com.netcracker.testerritto.comparators;

import com.netcracker.testerritto.models.ObjectEav;

import java.util.Comparator;

public class ObjectEavIdComparator<T extends ObjectEav> implements Comparator<T> {
    @Override
    public int compare(T o1, T o2) {
        return o1.getId().compareTo(o2.getId());
    }
}
