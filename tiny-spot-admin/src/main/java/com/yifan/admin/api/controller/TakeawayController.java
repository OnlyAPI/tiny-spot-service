package com.yifan.admin.api.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yifan.admin.api.service.TakeawayService;
import com.yifan.admin.api.annotition.Log;
import com.yifan.admin.api.annotition.Right;
import com.yifan.admin.api.model.params.SaveOrUpdateToolTakeawayParam;
import com.yifan.admin.api.model.params.TakeawayParam;
import com.yifan.admin.api.entity.Takeaway;
import com.yifan.admin.api.enums.BusinessTypeEnum;
import com.yifan.admin.api.result.BaseResult;
import com.yifan.admin.api.result.CommonPage;
import com.yifan.admin.api.utils.DateTimeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @description: TODO
 * @author: wyf
 * @date: 2023/5/14
 */
@RestController
@Api(tags = "系统工具-好评管理")
@RequestMapping("/takeaway")
public class TakeawayController {


    @Resource
    private TakeawayService takeawayService;

    @ApiOperation("好评-分页列表")
    @Right(rightsOr = {"sys:takeaway:list"})
    @RequestMapping(value = "/pageList", method = RequestMethod.GET)
    public BaseResult<CommonPage<Takeaway>> pageList(TakeawayParam param){
        Page<Takeaway> page = new Page<>(param.getPageNum(), param.getPageSize());

        LambdaQueryWrapper<Takeaway> queryWrapper = new QueryWrapper<Takeaway>().lambda();
        if (param.getKeyworkNum() > 0){
            queryWrapper.ge(Takeaway::getKeyworkNum, param.getKeyworkNum());
        }
        if (param.getSortOrder().contains("desc")){
            queryWrapper.orderByDesc(Takeaway::getCreateTime);
        }else {
            queryWrapper.orderByAsc(Takeaway::getCreateTime);
        }
        if (StringUtils.isNotBlank(param.getBeginTime()) && StringUtils.isNotBlank(param.getEndTime())){
            queryWrapper.between(Takeaway::getCreateTime, param.getBeginTime(), param.getEndTime());
        }
        return BaseResult.ok(CommonPage.restPage(takeawayService.page(page, queryWrapper)));
    }

    @ApiOperation("好评-保存内容")
    @Right(rightsOr = {"sys:takeaway:add"})
    @Log(title = "好评管理", businessType = BusinessTypeEnum.INSERT)
    @PostMapping("/add")
    public BaseResult<Boolean> save(@RequestBody @Valid SaveOrUpdateToolTakeawayParam param){
        Takeaway content = new Takeaway();
        content.setContentText(param.getContentText());
        content.setNiceNum(0);
        content.setKeyworkNum(param.getContentText().length());
        content.setCreateTime(DateTimeUtil.currentDate());
        boolean save = takeawayService.save(content);
        return BaseResult.ok(save);
    }

    @ApiOperation("好评-修改内容")
    @Right(rightsOr = {"sys:takeaway:update"})
    @Log(title = "好评管理", businessType = BusinessTypeEnum.UPDATE)
    @PostMapping("/update/{takeawayId}")
    public BaseResult<Boolean> update(@PathVariable(value = "takeawayId" ) Long takeawayId,
                                    @RequestBody @Valid SaveOrUpdateToolTakeawayParam param){
        Takeaway context = new Takeaway();
        context.setId(takeawayId);
        context.setKeyworkNum(param.getContentText().length());
        context.setContentText(param.getContentText());
        return BaseResult.ok(takeawayService.updateById(context));
    }

    @ApiOperation("好评-删除内容")
    @Right(rightsOr = {"sys:takeaway:delete"})
    @Log(title = "好评管理", businessType = BusinessTypeEnum.DELETE)
    @DeleteMapping("/remove/{takeawayId}")
    public BaseResult<Boolean> remove(@PathVariable(value = "takeawayId" ) Long takeawayId){
        boolean removeById = takeawayService.removeById(takeawayId);
        return BaseResult.ok(removeById);
    }

    @ApiOperation("根据id获取")
    @Right(rightsOr = {"sys:takeaway:list"})
    @GetMapping("/getById/{takeawayId}")
    public BaseResult<Takeaway> getById(@PathVariable(value = "takeawayId" ) Long takeawayId){
        Takeaway contextServiceById = takeawayService.getById(takeawayId);
        return BaseResult.ok(contextServiceById);
    }

}
