package com.yifan.admin.api.result;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分页数据封装类
 */
@Data
public class CommonPage<T> {

    private long pageNum = 1;

    private long pageSize = 10;

    private long total = 0;

    private List<T> list = Collections.emptyList();

    /**
     * 将MyBatis Plus 分页结果转化为通用结果
     */
    public static <T> CommonPage<T> restPage(Page<T> pageResult) {
        CommonPage<T> result = new CommonPage<>();
        result.pageNum = pageResult.getCurrent();
        result.pageSize = pageResult.getSize();
        result.total = pageResult.getTotal();
        result.list = pageResult.getRecords();
        return result;
    }

    public static <T, E> CommonPage<T> fromAndConvert(IPage<E> page, Function<E, T> func) {
        CommonPage<T> pageInfo = new CommonPage<>();
        pageInfo.pageNum = page.getCurrent();
        pageInfo.pageSize = page.getSize();
        pageInfo.total = page.getTotal();
        pageInfo.list = page.getRecords().stream().map(func).collect(Collectors.toList());
        return pageInfo;
    }

    public static <T, E> CommonPage<T> fromAndConvert2(IPage<E> page, Function<List<E>, List<T>> func){
        CommonPage<T> pageInfo = new CommonPage<>();
        pageInfo.pageNum = page.getCurrent();
        pageInfo.pageSize = page.getSize();
        pageInfo.total = page.getTotal();
        pageInfo.list = func.apply(page.getRecords());
        return pageInfo;
    }

    public static <T> CommonPage<T> empty() {
        return new CommonPage<>();
    }

}
