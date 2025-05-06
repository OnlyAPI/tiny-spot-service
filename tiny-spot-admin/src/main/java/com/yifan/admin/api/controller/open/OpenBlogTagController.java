package com.yifan.admin.api.controller.open;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yifan.admin.api.entity.BlogTag;
import com.yifan.admin.api.enums.StatusEnum;
import com.yifan.admin.api.model.vo.open.OpenBlogTagVO;
import com.yifan.admin.api.result.BaseResult;
import com.yifan.admin.api.service.BlogTagService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "open-标签")
@RestController
@RequestMapping("/open/tag")
public class OpenBlogTagController {
    @Resource
    private BlogTagService blogTagService;

    @GetMapping("list")
    public BaseResult<List<OpenBlogTagVO>> getAllTags(){
        LambdaQueryWrapper<BlogTag> lambda = new QueryWrapper<BlogTag>().lambda();
        lambda.eq(BlogTag::getStatus, StatusEnum.USABLE.getStatus());

        List<BlogTag> blogTags = blogTagService.list(lambda);
        return BaseResult.ok(blogTags.stream().map(OpenBlogTagVO::new).collect(Collectors.toList()));
    }
}
