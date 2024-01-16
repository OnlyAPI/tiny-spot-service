package com.yifan.admin.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yifan.admin.api.entity.AiConfig;
import com.yifan.admin.api.enums.AiConfigTypeEnum;

import java.util.List;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/11/13 15:07
 */
public interface AiConfigService extends IService<AiConfig> {

    List<AiConfig> getUsableConfigByType(AiConfigTypeEnum configTypeEnum);
}
