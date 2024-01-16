package com.yifan.admin.api.service.image;

import com.yifan.admin.api.entity.AiConfig;
import com.yifan.admin.api.model.params.BaiduTxt2ImgParams;
import com.yifan.admin.api.model.data.ImageGenerationData;

import java.util.List;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/11/21 14:27
 */
public interface BaiduImageGenerateService extends ImageGenerateService<BaiduTxt2ImgParams>{

    List<String> txt2img(String taskId, AiConfig aiConfig, BaiduTxt2ImgParams params);

    ImageGenerationData queryTxt2ImgResult(String taskId, Long userId);
}
