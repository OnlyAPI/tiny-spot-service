package com.yifan.admin.api;

import com.yifan.admin.api.service.SysUserService;
import com.yifan.admin.api.entity.SysUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/7/21 18:39
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationTest {

    @Resource
    SysUserService userService;

    @Test
    void restoreUserTable() {
        List<SysUser> userList = userService.list();

        List<SysUser> userBakList = userList.stream().map(user -> {
            SysUser userBak = new SysUser();
            BeanUtils.copyProperties(user, userBak);
            return userBak;
        }).collect(Collectors.toList());

        int batchSize = 1000;

        // 使用 lambda 表达式将 userList 每1000个元素分为一组
        List<List<SysUser>> groupedBakUsers = IntStream.range(0, userList.size())
                .boxed()
                .collect(Collectors.groupingBy(index -> index / batchSize)) // 将索引按组分组
                .values()
                .stream()
                .map(indices -> indices.stream()
                        .map(userBakList::get) // 根据索引获取 User 对象
                        .collect(Collectors.toList())) // 每组1000个元素的列表
                .collect(Collectors.toList()); // 所有分组的列表

        List<CompletableFuture<Void>> completableFutureList = new ArrayList<>();
        int i = 1;
        for (List<SysUser> groupedBakUser : groupedBakUsers) {
            int finalI = i;
            CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
                boolean b = userService.saveOrUpdateBatch(groupedBakUser, batchSize);
            });
            i++;
            completableFutureList.add(completableFuture);
        }
        CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[]{})).join();
    }

}
