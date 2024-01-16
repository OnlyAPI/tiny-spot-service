package com.yifan.admin.api.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/11/20 18:07
 */
public enum ImageGenerateSizeEnum {

    SIZE_768_768("768x768"),
    SIZE_768_1024("768x1024"),
    SIZE_1024_768("1024x768"),
    SIZE_576_1024("576x1024"),
    SIZE_1024_576("1024x576"),
    SIZE_1024_1024("1024x1024"),
    ;

    private final String size;

    ImageGenerateSizeEnum(String size) {
        this.size = size;
    }

    public String getSize() {
        return size;
    }

    public static String getBySize(String size) {
        if (StringUtils.isBlank(size)) {
            return SIZE_1024_1024.size;
        }
        for (ImageGenerateSizeEnum value : values()) {
            if (StringUtils.equals(value.size, size)) {
                return value.size;
            }
        }
        return SIZE_1024_1024.size;
    }
}
