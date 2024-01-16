package com.yifan.admin.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yifan.admin.api.entity.AiConfig;
import com.yifan.admin.api.enums.AiConfigTypeEnum;
import com.yifan.admin.api.enums.StatusEnum;
import com.yifan.admin.api.mapper.AiConfigMapper;
import com.yifan.admin.api.service.AiConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/11/13 15:08
 */
@Slf4j
@Service
public class AiConfigServiceImpl extends ServiceImpl<AiConfigMapper, AiConfig> implements AiConfigService {

    @Override
    public List<AiConfig> getUsableConfigByType(AiConfigTypeEnum configTypeEnum) {
        LambdaQueryWrapper<AiConfig> queryWrapper = new QueryWrapper<AiConfig>()
                .lambda()
                .eq(AiConfig::getConfigType, configTypeEnum.getName())
                .eq(AiConfig::getStatus, StatusEnum.USABLE.getStatus());
        return baseMapper.selectList(queryWrapper);
    }
}
