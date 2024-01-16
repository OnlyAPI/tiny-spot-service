package com.yifan.admin.api.service.image;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yifan.admin.api.context.RequestContext;
import com.yifan.admin.api.entity.AiConfig;
import com.yifan.admin.api.entity.AiImageTask;
import com.yifan.admin.api.enums.ImageGenerateSizeEnum;
import com.yifan.admin.api.enums.StatusEnum;
import com.yifan.admin.api.exception.ApiException;
import com.yifan.admin.api.model.baidu.Txt2ImgRequest;
import com.yifan.admin.api.model.baidu.Txt2ImgResponse;
import com.yifan.admin.api.model.params.BaiduTxt2ImgParams;
import com.yifan.admin.api.model.data.ImageGenerationData;
import com.yifan.admin.api.result.ResultCode;
import com.yifan.admin.api.service.AbstractBaiduService;
import com.yifan.admin.api.service.AiImageTaskService;
import com.yifan.admin.api.service.storage.FileStorageService;
import com.yifan.admin.api.utils.DateTimeUtil;
import com.yifan.admin.api.utils.JacksonJsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RedissonClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/11/20 17:52
 */
@Slf4j
@Service
public class BaiduImageGenerateServiceImpl extends AbstractBaiduService implements BaiduImageGenerateService {

    @Resource
    private FileStorageService fileStorageService;
    @Resource
    private AiImageTaskService aiImageTaskService;

    private final ExecutorService executorService;

    public BaiduImageGenerateServiceImpl(RedissonClient redissonClient, RestTemplate restTemplate) {
        super(redissonClient, restTemplate);
        this.executorService = new ThreadPoolExecutor(50, 100, 60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1000),
                new CustomizableThreadFactory("baidu-request-"),
                new ThreadPoolExecutor.AbortPolicy());
    }

    @PreDestroy
    public void shutdownExecutor() {
        try {
            executorService.shutdown();
            if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (Exception e) {
            log.error("shutdown executorService error", e);
            executorService.shutdownNow();
        }
    }


    @Override
    public void asyncTxt2img(String taskId, AiConfig aiConfig, BaiduTxt2ImgParams params, Consumer<List<String>> resultConsumer, Consumer<Exception> errorCallback) {
        saveTask(taskId, aiConfig, params);
        this.executorService.submit(() -> {
            AiImageTask task = aiImageTaskService.getByTaskIdAndUserId(taskId, RequestContext.getUser().getId());
            if (task == null) {
                log.error("not found task, taskId: {}", taskId);
                throw new ApiException("not found task");
            }
            try {
                List<String> imageBase64ResultList = txt2img(taskId, aiConfig, params);
                List<String> imageUrlReusltList = imgBaseVal2Url(imageBase64ResultList);

                task.setStatus(StatusEnum.COMPLETED.getStatus());
                task.setGenerateDataUrl(String.join(",", imageUrlReusltList));
                task.setGenerateDataBase64(String.join(",", imageBase64ResultList));
                task.setErrorMsg(ResultCode.OK.getMsg());
                task.setUpdateTime(DateTimeUtil.currentDate());

                resultConsumer.accept(imageBase64ResultList);
            } catch (Exception e) {
                errorCallback.accept(e);

                task.setStatus(StatusEnum.ERROR.getStatus());
                task.setErrorMsg(e.getMessage());
                task.setGenerateDataUrl("");
                task.setGenerateDataBase64("");
                task.setUpdateTime(DateTimeUtil.currentDate());
            }
            aiImageTaskService.updateById(task);
        });
    }

    @Override
    public List<String> imgBaseVal2Url(List<String> imgBase64List) {
        log.info("save img total: {}", imgBase64List.size());
        List<String> imgUrlList = new ArrayList<>();
        for (String imgBase64 : imgBase64List) {
            imgUrlList.add(fileStorageService.fileUpload(imgBase64));
        }
        log.info("save img success, total: {}", imgBase64List.size());
        return imgUrlList;
    }

    @Override
    public List<String> txt2img(String taskId, AiConfig aiConfig, BaiduTxt2ImgParams params) {
        log.info("taskId: {}, params: {}", taskId, JacksonJsonUtil.toJson(params));

        Txt2ImgRequest request = new Txt2ImgRequest();
        request.setPrompt(params.getPrompt());
        request.setSize(ImageGenerateSizeEnum.getBySize(params.getImageSize()));
        request.setN(params.getGenerateNum());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Txt2ImgRequest> httpEntity = new HttpEntity<>(request, headers);

        Txt2ImgResponse txt2ImgResponse = restTemplate.postForObject(aiConfig.getServerUrl() +
                "?access_token={1}", httpEntity, Txt2ImgResponse.class, getAccessToken(aiConfig));

        if (txt2ImgResponse != null && txt2ImgResponse.getData() != null && !txt2ImgResponse.getData().isEmpty()) {
            log.info("txt2img success taskId: {}, id: {}, prompt_tokens: {}, total_tokens: {}",
                    taskId, txt2ImgResponse.getId(), txt2ImgResponse.getUsage().getPromptTokens(), txt2ImgResponse.getUsage().getTotalTokens());
            return txt2ImgResponse.getData().stream().map(Txt2ImgResponse.ImageData::getB64Image).collect(Collectors.toList());
        } else {
            log.error("txt2img fail taskId: {}, resp: {}", taskId, JacksonJsonUtil.toJson(txt2ImgResponse));
            String errorMsg = txt2ImgResponse != null ? txt2ImgResponse.getErrorMsg() : "Task failed";
            throw new ApiException(errorMsg);
        }
    }

    @Override
    public ImageGenerationData queryTxt2ImgResult(String taskId, Long userId) {
        LambdaQueryWrapper<AiImageTask> wrapper = new QueryWrapper<AiImageTask>()
                .lambda()
                .eq(AiImageTask::getTaskId, taskId)
                .eq(AiImageTask::getUserId, userId);
        AiImageTask aiImageTask = aiImageTaskService.getOne(wrapper, true);
        if (aiImageTask == null) {
            log.error("not found task, taskId: {}", taskId);
            throw new ApiException("taskId 错误");
        }
        ImageGenerationData data = new ImageGenerationData();
        data.setTaskId(taskId);
        data.setErrMsg(aiImageTask.getErrorMsg());
        data.setSuccess(aiImageTask.getStatus().equals(StatusEnum.COMPLETED.getStatus()));
        data.setImageBase64s(StringUtils.isBlank(aiImageTask.getGenerateDataBase64()) ?
                Collections.emptyList() : Arrays.asList(aiImageTask.getGenerateDataBase64().split(",")));
        data.setImageUrls(StringUtils.isBlank(aiImageTask.getGenerateDataUrl()) ?
                Collections.emptyList() : Arrays.asList(aiImageTask.getGenerateDataUrl().split(",")));

        return data;
    }

    private void saveTask(String taskId, AiConfig aiConfig, BaiduTxt2ImgParams params) {
        AiImageTask aiImageTask = new AiImageTask();
        aiImageTask.setUserId(RequestContext.getUser().getId());
        aiImageTask.setTaskId(taskId);
        aiImageTask.setConfigType(aiConfig.getConfigType());
        aiImageTask.setPrompt(params.getPrompt());
        aiImageTask.setGenerateNum(params.getGenerateNum());
        aiImageTask.setImageSize(params.getImageSize());
        aiImageTask.setStatus(StatusEnum.ACTIVE.getStatus());
        aiImageTask.setCreateTime(DateTimeUtil.currentDate());
        aiImageTask.setUpdateTime(aiImageTask.getCreateTime());
        boolean save = aiImageTaskService.save(aiImageTask);
        if (!save) {
            log.error("save task fail, taskId: {}", taskId);
            throw new ApiException("save task fail");
        }
    }

}
