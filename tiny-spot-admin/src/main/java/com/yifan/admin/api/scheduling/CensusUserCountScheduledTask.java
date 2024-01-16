package com.yifan.admin.api.scheduling;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yifan.admin.api.service.SysUserRoleService;
import com.yifan.admin.api.service.SysRoleService;
import com.yifan.admin.api.entity.SysUserRole;
import com.yifan.admin.api.entity.SysRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 定时统计用户数量
 *
 * @author TaiYi
 * @ClassName
 * @date 2023/2/10 10:50
 */
@Slf4j
@Component
public class CensusUserCountScheduledTask {

    @Autowired
    SysUserRoleService sysUserRoleService;
    @Autowired
    SysRoleService sysRoleService;

    @Scheduled(cron = "0 0 0/5 * * ?")
    public void censusUserCount() {
        List<SysRole> roleList = sysRoleService.list();
        if (roleList.isEmpty()) {
            log.info("[censusUserCount] role list is empty.");
            return;
        }
        roleList.forEach(r -> {
            long count = sysUserRoleService.count(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getRoleId, r.getId()));
            if (r.getUserCount() != count) {
                r.setUserCount((int) count);
                boolean update = sysRoleService.update(new UpdateWrapper<SysRole>().set("user_count", count).eq("id", r.getId()));
                log.info("[censusUserCount] update user_count result: {}, ums_role table is: {}.", update, r.getId());
            }
        });


    }

}
