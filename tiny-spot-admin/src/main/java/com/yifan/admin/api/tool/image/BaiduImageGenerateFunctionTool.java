package com.yifan.admin.api.tool.image;

import com.yifan.admin.api.context.RequestContext;
import com.yifan.admin.api.entity.AiConfig;
import com.yifan.admin.api.entity.AiImageTask;
import com.yifan.admin.api.entity.SysUser;
import com.yifan.admin.api.enums.AiConfigTypeEnum;
import com.yifan.admin.api.enums.ImageGenerateSizeEnum;
import com.yifan.admin.api.enums.StatusEnum;
import com.yifan.admin.api.model.params.BaiduTxt2ImgParams;
import com.yifan.admin.api.service.AiConfigService;
import com.yifan.admin.api.service.AiImageTaskService;
import com.yifan.admin.api.service.image.BaiduImageGenerateService;
import com.yifan.admin.api.tool.FunctionOutput;
import com.yifan.admin.api.utils.DateTimeUtil;
import com.yifan.admin.api.utils.JacksonJsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author TaiYi
 * @ClassName
 * @date 2024/1/4 16:34
 */
@Component
@Slf4j
public class BaiduImageGenerateFunctionTool extends AbstractImageGenerateFunctionTool {

    @Resource
    private AiConfigService aiConfigService;
    @Resource
    private AiImageTaskService aiImageTaskService;
    @Resource
    private BaiduImageGenerateService baiduImageGenerateService;

    @Override
    public String getName() {
        return AiConfigTypeEnum.TXT2IMG_BAIDU.getName();
    }

    @Override
    public FunctionOutput apply(String param) {
        ImageGenerationInput imageGenerationInput = JacksonJsonUtil.fromJson(param, ImageGenerationInput.class);
        if (imageGenerationInput != null) {
            String taskId = "func-txt2img-baidu" + RandomStringUtils.random(32, true, true) + DateTimeUtil.currentMilli();
            String prompt = StringUtils.isBlank(imageGenerationInput.getPrompt()) ? imageGenerationInput.getContent() : imageGenerationInput.getPrompt();

            BaiduTxt2ImgParams baiduTxt2ImgParams = new BaiduTxt2ImgParams();
            baiduTxt2ImgParams.setPrompt(prompt);
            baiduTxt2ImgParams.setGenerateNum(imageGenerationInput.getN());
            baiduTxt2ImgParams.setImageSize(ImageGenerateSizeEnum.SIZE_1024_1024.getSize());

            AiConfig aiConfig = aiConfigService.getUsableConfigByType(AiConfigTypeEnum.fromName(getName())).get(0);

            List<String> imageBase64List = baiduImageGenerateService.txt2img(taskId, aiConfig, baiduTxt2ImgParams);
            List<String> imageUrls = baiduImageGenerateService.imgBaseVal2Url(imageBase64List);

            AiImageTask aiImageTask = new AiImageTask();
            SysUser user = RequestContext.getUser();
            aiImageTask.setUserId(user == null ? 0 : user.getId());
            aiImageTask.setTaskId(taskId);
            aiImageTask.setConfigType(aiConfig.getConfigType());
            aiImageTask.setPrompt(baiduTxt2ImgParams.getPrompt());
            aiImageTask.setGenerateNum(baiduTxt2ImgParams.getGenerateNum());
            aiImageTask.setImageSize(baiduTxt2ImgParams.getImageSize());
            aiImageTask.setGenerateDataBase64(String.join(",", imageBase64List));
            aiImageTask.setGenerateDataUrl(String.join(",", imageUrls));
            aiImageTask.setStatus(StatusEnum.COMPLETED.getStatus());
            aiImageTask.setErrorMsg("");
            aiImageTask.setCreateTime(DateTimeUtil.currentDate());
            aiImageTask.setUpdateTime(aiImageTask.getCreateTime());
            aiImageTaskService.save(aiImageTask);

            StringBuilder sb = new StringBuilder();
            for (String image : imageUrls) {
                sb.append("![image](").append(image).append(")");
            }

            return new FunctionOutput(false, sb.toString());
        }
        return null;
    }
}
