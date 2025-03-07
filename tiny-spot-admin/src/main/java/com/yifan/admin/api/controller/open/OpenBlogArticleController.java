package com.yifan.admin.api.controller.open;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yifan.admin.api.entity.BlogArticle;
import com.yifan.admin.api.entity.SysUser;
import com.yifan.admin.api.enums.StatusEnum;
import com.yifan.admin.api.model.params.BlogBaseParam;
import com.yifan.admin.api.model.vo.open.OpenBlogArticleVO;
import com.yifan.admin.api.model.vo.open.OpenBlogTagVO;
import com.yifan.admin.api.result.BaseResult;
import com.yifan.admin.api.result.CommonPage;
import com.yifan.admin.api.service.BlogArticleService;
import com.yifan.admin.api.service.BlogTagService;
import com.yifan.admin.api.service.SysUserService;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "open-文章")
@RestController
@RequestMapping("/open/blog")
public class OpenBlogArticleController {

    @Resource
    private BlogArticleService blogArticleService;
    @Resource
    private BlogTagService blogTagService;
    @Resource
    private SysUserService userService;


    @GetMapping("list")
    public BaseResult<CommonPage<OpenBlogArticleVO>> getList(BlogBaseParam param) {

        Page<BlogArticle> page = new Page<>(param.getPageNum(), param.getPageSize());

        LambdaQueryWrapper<BlogArticle> queryWrapper = new QueryWrapper<BlogArticle>().lambda();
        queryWrapper.eq(BlogArticle::getStatus, StatusEnum.USABLE.getStatus());
        queryWrapper.orderByAsc(BlogArticle::getSort);
        queryWrapper.orderByDesc(BlogArticle::getCreateTime);
        if (StringUtils.isNotBlank(param.getKeyword())) {
            queryWrapper.like(BlogArticle::getTitle, param.getKeyword());
        }

        Page<BlogArticle> paged = blogArticleService.page(page, queryWrapper);

        return BaseResult.ok(CommonPage.fromAndConvert(paged, t -> {
            List<OpenBlogTagVO> openBlogTagVOS = blogTagService.getOpenBlogTagByArticleId(t.getId());
            SysUser sysUser = userService.getById(t.getCreateBy());
            return new OpenBlogArticleVO(t, openBlogTagVOS, sysUser.getNickName());
        }));
    }

}
