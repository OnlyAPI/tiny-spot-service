package com.yifan.admin.api.utils;

import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/11/13 16:44
 */
public class CollectionsUtil {

    private static final Random RANDOM = new Random(System.currentTimeMillis());

    private CollectionsUtil() {
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return collection != null && !collection.isEmpty();
    }

    public static <T> boolean isNull(T[] array) {
        return array == null;
    }

    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }

    public static <T> boolean isNotEmpty(T[] array) {
        return array != null && array.length > 0;
    }

    public static <T> T getRandom(T[] array) {
        return isEmpty(array) ? null : array[RANDOM.nextInt(array.length)];
    }

    public static <T> T getRandom(List<T> list) {
        return isEmpty(list) ? null : list.get(RANDOM.nextInt(list.size()));
    }

}
