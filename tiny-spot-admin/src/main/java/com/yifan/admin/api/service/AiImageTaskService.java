package com.yifan.admin.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yifan.admin.api.entity.AiImageTask;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/11/21 16:54
 */
public interface AiImageTaskService extends IService<AiImageTask> {


    AiImageTask getByTaskIdAndUserId(String taskId, Long userId);
}
