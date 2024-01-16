package com.yifan.admin.api.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yifan.admin.api.context.RequestContext;
import com.yifan.admin.api.entity.Plan;
import com.yifan.admin.api.enums.StatusEnum;
import com.yifan.admin.api.model.vo.PlanVO;
import com.yifan.admin.api.result.BaseResult;
import com.yifan.admin.api.service.PlanService;
import com.yifan.admin.api.utils.DateTimeUtil;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/11/17 11:18
 */
@Api(tags = "计划管理")
@RestController
@RequestMapping("/plan")
public class PlanController {

    @Resource
    private PlanService planService;


    @GetMapping("/list")
    public BaseResult<List<PlanVO>> getPlanList(@RequestParam(value = "planDate") String planDate) {
        LambdaQueryWrapper<Plan> lambda = new QueryWrapper<Plan>()
                .lambda()
                .eq(Plan::getUserId, RequestContext.getUser().getId())
                .eq(Plan::getPlanDate, planDate)
                .ne(Plan::getStatus, StatusEnum.DELETED.getStatus());
        List<Plan> planList = planService.list(lambda);
        if (planList.isEmpty()) {
            return BaseResult.ok(Collections.emptyList());
        }
        return BaseResult.ok(planList.stream().map(PlanVO::new).collect(Collectors.toList()));
    }

    @GetMapping("/add")
    public BaseResult<Boolean> addPlan(@RequestParam(value = "text") String text,
                                       @RequestParam(value = "planDate") String planDate) {
        Plan plan = new Plan();
        plan.setPlanDate(planDate);
        plan.setUserId(RequestContext.getUser().getId());
        plan.setPlanDesc(text);
        plan.setStatus(StatusEnum.ACTIVE.getStatus());
        plan.setCreateTime(DateTimeUtil.currentDate());
        plan.setUpdateTime(DateTimeUtil.currentDate());
        boolean save = planService.save(plan);
        return BaseResult.ok(save);
    }

    @PutMapping("/edit/{id}")
    public BaseResult<Boolean> editPlan(@PathVariable(value = "id") Long id,
                                        @RequestParam(value = "text") String text,
                                        @RequestParam(value = "status") Integer status) {
        if (StringUtils.isBlank(text)){
            return deletePlan(id);
        }
        LambdaQueryWrapper<Plan> wrapper = new QueryWrapper<Plan>().lambda()
                .eq(Plan::getId, id)
                .eq(Plan::getUserId, RequestContext.getUser().getId())
                .ne(Plan::getStatus, StatusEnum.DELETED.getStatus());
        Plan plan = planService.getOne(wrapper, false);
        if (plan == null) {
            return BaseResult.failed("invalid id");
        }
        plan.setPlanDesc(text);
        plan.setUpdateTime(DateTimeUtil.currentDate());
        plan.setStatus(status.equals(StatusEnum.ACTIVE.getStatus()) ? StatusEnum.ACTIVE.getStatus() : StatusEnum.COMPLETED.getStatus());
        boolean update = planService.updateById(plan);
        return BaseResult.ok(update);
    }

    @DeleteMapping("/delete/{id}")
    public BaseResult<Boolean> deletePlan(@PathVariable(value = "id") Long id) {
        LambdaQueryWrapper<Plan> wrapper = new QueryWrapper<Plan>().lambda()
                .eq(Plan::getId, id)
                .eq(Plan::getUserId, RequestContext.getUser().getId());
        Plan plan = planService.getOne(wrapper);
        if (plan == null){
            return BaseResult.failed("invalid id");
        }
        plan.setStatus(StatusEnum.DELETED.getStatus());
        plan.setUpdateTime(DateTimeUtil.currentDate());
        boolean update = planService.updateById(plan);
        return BaseResult.ok(update);
    }
}
