package com.yifan.admin.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yifan.admin.api.entity.AiRobot;
import com.yifan.admin.api.enums.StatusEnum;
import com.yifan.admin.api.mapper.AiRobotMapper;
import com.yifan.admin.api.model.vo.AiRobotVO;
import com.yifan.admin.api.service.AiRobotService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/12/4 18:15
 */
@Service
@Slf4j
public class AiRobotServiceImpl extends ServiceImpl<AiRobotMapper, AiRobot> implements AiRobotService {


    @Override
    public List<AiRobotVO> getAiRobotList(String name) {
        log.info("[getAiRobotList] name: {}", name);
        LambdaQueryWrapper<AiRobot> lambda = new QueryWrapper<AiRobot>().lambda()
                .eq(AiRobot::getStatus, StatusEnum.USABLE.getStatus());
        if (StringUtils.isNotBlank(name)) {
            lambda.like(AiRobot::getName, name);
        }
        return baseMapper.selectList(lambda).stream().map(AiRobotVO::new).collect(Collectors.toList());

    }

    @Override
    public AiRobot getRobotById(Long robotId) {
        return baseMapper.selectUsableRobotById(robotId);
    }
}
