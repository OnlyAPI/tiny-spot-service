package com.yifan.admin.api.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yifan.admin.api.service.BlogArticleTagService;
import com.yifan.admin.api.service.BlogTagService;
import com.yifan.admin.api.annotition.Log;
import com.yifan.admin.api.annotition.Right;
import com.yifan.admin.api.model.params.BlogBaseParam;
import com.yifan.admin.api.model.params.BlogCommonTitleParam;
import com.yifan.admin.api.model.vo.BlogTagVO;
import com.yifan.admin.api.entity.BlogArticleTag;
import com.yifan.admin.api.entity.BlogTag;
import com.yifan.admin.api.enums.BusinessTypeEnum;
import com.yifan.admin.api.enums.StatusEnum;
import com.yifan.admin.api.result.BaseResult;
import com.yifan.admin.api.result.CommonPage;
import com.yifan.admin.api.utils.DateTimeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: TODO
 * @author: wyf
 * @date: 2023/5/23
 */
@Api(tags = "博客-标签管理")
@RestController
@RequestMapping("/blog/tag")
public class BlogTagController {

    @Resource
    private BlogTagService blogTagService;
    @Resource
    private BlogArticleTagService blogArticleTagService;

    @ApiOperation("获取所有标签")
    @Right(rightsOr = "sys:blogTag:list")
    @GetMapping("/getAll")
    public BaseResult<List<BlogTagVO>> getAll() {
        List<BlogTag> list = blogTagService.list();
        List<BlogTagVO> collect = list.stream().map(e -> {
            BlogTagVO tagVO = new BlogTagVO(e);
            long count = blogArticleTagService.count(new QueryWrapper<BlogArticleTag>().lambda().eq(BlogArticleTag::getTagId, e.getId()));
            tagVO.setArticleNum(count);
            return tagVO;
        }).collect(Collectors.toList());
        return BaseResult.ok(collect);
    }

    @ApiOperation("分页获取标签")
    @Right(rightsOr = "sys:blogTag:list")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public BaseResult<CommonPage<BlogTagVO>> list(BlogBaseParam param) {
        Page<BlogTag> page = new Page<>(param.getPageNum(), param.getPageSize());

        LambdaQueryWrapper<BlogTag> queryWrapper = new QueryWrapper<BlogTag>().lambda();
        if (StringUtils.isNotBlank(param.getKeyword())) {
            queryWrapper.like(BlogTag::getTitle, param.getKeyword());
        }
        if (param.getSortOrder().contains("desc")) {
            queryWrapper.orderByDesc(BlogTag::getCreateTime);
        } else {
            queryWrapper.orderByAsc(BlogTag::getCreateTime);
        }
        if (StringUtils.isNotBlank(param.getBeginTime()) && StringUtils.isNotBlank(param.getEndTime())) {
            queryWrapper.between(BlogTag::getCreateTime, param.getBeginTime(), param.getEndTime());
        }

        Page<BlogTag> tagPage = blogTagService.page(page, queryWrapper);

        return BaseResult.ok(CommonPage.fromAndConvert(tagPage, e -> {
            BlogTagVO tagVO = new BlogTagVO(e);
            long count = blogArticleTagService.count(new QueryWrapper<BlogArticleTag>().lambda().eq(BlogArticleTag::getTagId, e.getId()));
            tagVO.setArticleNum(count);
            return tagVO;
        }));
    }

    @ApiOperation("根据ID获取标签")
    @Right(rightsOr = "sys:blogTag:list")
    @RequestMapping(value = "/getById/{tagId}", method = RequestMethod.GET)
    public BaseResult getById(@PathVariable(value = "tagId") Long tagId) {
        BlogTag blogTag = blogTagService.getById(tagId);
        if (blogTag == null) {
            return BaseResult.failed("未找到对应标签");
        }
        return BaseResult.ok(blogTag);
    }


    @ApiOperation("添加标签")
    @Right(rightsOr = "sys:blogTag:add")
    @Log(title = "标签管理", businessType = BusinessTypeEnum.INSERT)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BaseResult save(@RequestBody @Valid BlogCommonTitleParam param) {
        BlogTag blogTag = new BlogTag();
        blogTag.setTitle(param.getTitle());
        blogTag.setStatus(param.getStatus());
        blogTag.setCreateTime(DateTimeUtil.currentDate());
        blogTag.setUpdateTime(DateTimeUtil.currentDate());
        return BaseResult.ok(blogTagService.save(blogTag));
    }

    @ApiOperation("修改标签")
    @Right(rightsOr = "sys:blogTag:update")
    @Log(title = "标签管理", businessType = BusinessTypeEnum.UPDATE)
    @RequestMapping(value = "/update/{tagId}", method = RequestMethod.PUT)
    public BaseResult updateById(@PathVariable(value = "tagId") Long tagId,
                                 @RequestBody @Valid BlogCommonTitleParam param) {
        BlogTag blogTag = blogTagService.getById(tagId);
        if (blogTag == null) {
            return BaseResult.failed("未找到对应标签");
        }
        LambdaUpdateWrapper<BlogTag> lambda = new UpdateWrapper<BlogTag>().lambda();
        lambda.set(BlogTag::getTitle, param.getTitle());
        lambda.set(BlogTag::getStatus, param.getStatus());
        lambda.set(BlogTag::getUpdateTime, DateTimeUtil.currentDate());
        lambda.eq(BlogTag::getId, tagId);

        return BaseResult.ok(blogTagService.update(lambda));
    }

    @ApiOperation("删除标签")
    @Right(rightsOr = "sys:blogTag:delete")
    @Log(title = "标签管理", businessType = BusinessTypeEnum.DELETE)
    @RequestMapping(value = "/delete/{tagId}", method = RequestMethod.DELETE)
    public BaseResult removeTag(@PathVariable(value = "tagId") Long tagId) {
        BlogTag blogTag = blogTagService.getById(tagId);
        if (blogTag == null) {
            return BaseResult.failed("未找到对应标签");
        }
        LambdaQueryWrapper<BlogArticleTag> lambda = new QueryWrapper<BlogArticleTag>().lambda();
        lambda.eq(BlogArticleTag::getTagId, tagId);
        boolean removeLinkResult = blogArticleTagService.remove(lambda);

        boolean removeTagResult = blogTagService.removeById(tagId);

        return BaseResult.ok(removeLinkResult && removeTagResult);
    }

    @ApiOperation("启动标签")
    @Right(rightsOr = "sys:blogTag:enable")
    @Log(title = "标签管理", businessType = BusinessTypeEnum.UPDATE)
    @RequestMapping(value = "/enable", method = RequestMethod.GET)
    public BaseResult enableTag(@RequestParam(value = "tagId") Long tagId) {
        BlogTag blogTag = blogTagService.getById(tagId);
        if (blogTag == null) {
            return BaseResult.failed("未找到对应标签");
        }
        LambdaUpdateWrapper<BlogTag> lambda = new UpdateWrapper<BlogTag>().lambda();
        lambda.set(BlogTag::getStatus, StatusEnum.USABLE.getStatus());
        lambda.set(BlogTag::getUpdateTime, DateTimeUtil.currentDate());
        lambda.eq(BlogTag::getId, tagId);
        return BaseResult.ok(blogTagService.update(lambda));
    }

    @ApiOperation("禁用标签")
    @Right(rightsOr = "sys:blogTag:disable")
    @Log(title = "标签管理", businessType = BusinessTypeEnum.UPDATE)
    @RequestMapping(value = "/disable", method = RequestMethod.GET)
    public BaseResult disableTag(@RequestParam(value = "tagId") Long tagId) {
        BlogTag blogTag = blogTagService.getById(tagId);
        if (blogTag == null) {
            return BaseResult.failed("未找到对应标签");
        }
        LambdaUpdateWrapper<BlogTag> lambda = new UpdateWrapper<BlogTag>().lambda();
        lambda.set(BlogTag::getStatus, StatusEnum.DISABLE.getStatus());
        lambda.set(BlogTag::getUpdateTime, DateTimeUtil.currentDate());
        lambda.eq(BlogTag::getId, tagId);
        return BaseResult.ok(blogTagService.update(lambda));
    }

}
