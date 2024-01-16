package com.yifan.admin.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yifan.admin.api.entity.AiRobot;
import com.yifan.admin.api.model.vo.AiRobotVO;

import java.util.List;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/12/4 18:15
 */
public interface AiRobotService extends IService<AiRobot> {
    List<AiRobotVO> getAiRobotList(String name);

    AiRobot getRobotById(Long robotId);
}
