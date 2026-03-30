package com.dikiytechies.ger.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// I have no idea why stando deleted this
public class JavaUtil {
    @SafeVarargs
    public static <T> List<T> listOf(T... elements) {
        List<T> list = new ArrayList<>();
        for (T e : elements)
            list.add(e);
        return Collections.unmodifiableList(list);
    }
}
