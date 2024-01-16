package com.yifan.admin.api.controller;

import com.google.code.kaptcha.Producer;
import com.yifan.admin.api.service.CacheService;
import com.yifan.admin.api.result.BaseResult;
import com.yifan.admin.api.utils.UuidUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/2/9 14:41
 */
@RestController
@Api(tags = "系统验证码")
@RequestMapping("/captcha")
public class CaptchaController {

    @Autowired
    private Producer producer;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    CacheService cacheService;


    @ApiOperation(value = "获取验证码")
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public BaseResult<Map<String, String>> captcha() {

        //生成文字验证码
        final String text = producer.createText();
        //生成图片验证码
        BufferedImage image = producer.createImage(text);

        //redis 60秒
        // 保存验证码信息
        final String uuid = UuidUtils.randomUUID();
        cacheService.cacheCaptcha(uuid, text);

        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();

        try {
            ImageIO.write(image, "jpg", os);
        } catch (IOException e) {
            return BaseResult.failed(e.getMessage());
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("uuid", uuid);
        map.put("img", Base64.getEncoder().encodeToString(os.toByteArray()));

        return BaseResult.ok(map);
    }

}
