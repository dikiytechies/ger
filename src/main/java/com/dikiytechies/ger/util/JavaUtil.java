package com.dikiytechies.ger.util;

import java.util.*;

// I have no idea why stando deleted this
public class JavaUtil {
    @SafeVarargs
    public static <T> List<T> listOf(T... elements) {
        List<T> list = new ArrayList<>();
        for (T e : elements)
            list.add(e);
        return Collections.unmodifiableList(list);
    }

    @SafeVarargs
    public static <T> Set<T> setOf(T... elements) {
        Set<T> set = new HashSet<>();
        for (T e : elements)
            set.add(e);
        return Collections.unmodifiableSet(set);
    }

    @SafeVarargs
    public static <T> Set<T> modifiableSetOf(T... elements) {
        Set<T> set = new HashSet<>();
        for (T e : elements)
            set.add(e);
        return set;
    }
}
