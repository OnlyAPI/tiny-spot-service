package com.yifan.admin.api.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yifan.admin.api.service.UserOperLogService;
import com.yifan.admin.api.annotition.Right;
import com.yifan.admin.api.model.params.MonitorOperLogParam;
import com.yifan.admin.api.entity.UserOperLog;
import com.yifan.admin.api.result.BaseResult;
import com.yifan.admin.api.result.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/2/16 18:20
 */
@RestController
@Api(tags = "后台用户操作日志管理")
@RequestMapping("/monitor/operlog")
public class UserOperLogController {

    @Autowired
    UserOperLogService userOperLogService;

    @ApiOperation("分页获取用户操作日志")
    @Right(rightsOr = "monitor:operlog:list")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public BaseResult<CommonPage<UserOperLog>> list(MonitorOperLogParam param) {
        Page<UserOperLog> adminList = userOperLogService.list(param);
        return BaseResult.ok(CommonPage.restPage(adminList));
    }

}
