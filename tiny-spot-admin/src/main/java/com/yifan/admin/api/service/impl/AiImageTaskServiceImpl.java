package com.yifan.admin.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yifan.admin.api.entity.AiImageTask;
import com.yifan.admin.api.mapper.AiTaskImageMapper;
import com.yifan.admin.api.service.AiImageTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/11/21 16:55
 */
@Slf4j
@Service
public class AiImageTaskServiceImpl extends ServiceImpl<AiTaskImageMapper, AiImageTask> implements AiImageTaskService {


    @Override
    public AiImageTask getByTaskIdAndUserId(String taskId, Long userId) {
        LambdaQueryWrapper<AiImageTask> wrapper = new QueryWrapper<AiImageTask>()
                .lambda()
                .eq(AiImageTask::getTaskId, taskId)
                .eq(AiImageTask::getUserId, userId);
        return baseMapper.selectOne(wrapper);
    }
}
