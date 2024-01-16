package com.yifan.admin.api.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yifan.admin.api.service.BlogArticleClassifyService;
import com.yifan.admin.api.service.BlogClassifyService;
import com.yifan.admin.api.annotition.Log;
import com.yifan.admin.api.annotition.Right;
import com.yifan.admin.api.model.params.BlogBaseParam;
import com.yifan.admin.api.model.params.BlogCommonTitleParam;
import com.yifan.admin.api.model.vo.BlogClassifyVO;
import com.yifan.admin.api.entity.BlogArticleClassify;
import com.yifan.admin.api.entity.BlogClassify;
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
@Api(tags = "博客-分类管理")
@RestController
@RequestMapping("/blog/classify")
public class BlogClassifyController {

    @Resource
    private BlogClassifyService blogClassifyService;
    @Resource
    private BlogArticleClassifyService blogArticleClassifyService;

    @ApiOperation("获取所有分类")
    @Right(rightsOr = "sys:blogClassify:list")
    @GetMapping("/getAll")
    public BaseResult<List<BlogClassifyVO>> getAll() {
        List<BlogClassify> list = blogClassifyService.list();
        List<BlogClassifyVO> collect = list.stream().map(e -> {
            BlogClassifyVO classifyVO = new BlogClassifyVO(e);
            long count = blogArticleClassifyService.count(new QueryWrapper<BlogArticleClassify>().lambda().eq(BlogArticleClassify::getClassifyId, e.getClassifyId()));
            classifyVO.setArticleNum(count);
            return classifyVO;
        }).collect(Collectors.toList());
        return BaseResult.ok(collect);
    }

    @ApiOperation("分页获取分类")
    @Right(rightsOr = "sys:blogClassify:list")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public BaseResult<CommonPage<BlogClassifyVO>> list(BlogBaseParam param) {
        Page<BlogClassify> page = new Page<>(param.getPageNum(), param.getPageSize());

        LambdaQueryWrapper<BlogClassify> queryWrapper = new QueryWrapper<BlogClassify>().lambda();
        if (StringUtils.isNotBlank(param.getKeyword())) {
            queryWrapper.like(BlogClassify::getTitle, param.getKeyword());
        }
        if (param.getSortOrder().contains("desc")) {
            queryWrapper.orderByDesc(BlogClassify::getCreateTime);
        } else {
            queryWrapper.orderByAsc(BlogClassify::getCreateTime);
        }
        if (StringUtils.isNotBlank(param.getBeginTime()) && StringUtils.isNotBlank(param.getEndTime())) {
            queryWrapper.between(BlogClassify::getCreateTime, param.getBeginTime(), param.getEndTime());
        }

        Page<BlogClassify> tagPage = blogClassifyService.page(page, queryWrapper);

        return BaseResult.ok(CommonPage.fromAndConvert(tagPage, e -> {
            BlogClassifyVO classifyVO = new BlogClassifyVO(e);
            long count = blogArticleClassifyService.count(new QueryWrapper<BlogArticleClassify>().lambda().eq(BlogArticleClassify::getClassifyId, e.getClassifyId()));
            classifyVO.setArticleNum(count);
            return classifyVO;
        }));
    }

    @ApiOperation("根据ID获取标签")
    @Right(rightsOr = "sys:blogClassify:list")
    @RequestMapping(value = "/getById/{classifyId}", method = RequestMethod.GET)
    public BaseResult getById(@PathVariable(value = "classifyId") Long classifyId) {
        BlogClassify classify = blogClassifyService.getById(classifyId);
        if (classify == null) {
            return BaseResult.failed("未找到对应分类");
        }
        return BaseResult.ok(classify);
    }


    @ApiOperation("添加分类")
    @Right(rightsOr = "sys:blogClassify:add")
    @Log(title = "分类管理", businessType = BusinessTypeEnum.INSERT)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BaseResult save(@RequestBody @Valid BlogCommonTitleParam param) {
        BlogClassify blogTag = new BlogClassify();
        blogTag.setTitle(param.getTitle());
        blogTag.setStatus(param.getStatus());
        blogTag.setCreateTime(DateTimeUtil.currentDate());
        blogTag.setUpdateTime(DateTimeUtil.currentDate());
        return BaseResult.ok(blogClassifyService.save(blogTag));
    }

    @ApiOperation("修改分类")
    @Right(rightsOr = "sys:blogClassify:update")
    @Log(title = "分类管理", businessType = BusinessTypeEnum.UPDATE)
    @RequestMapping(value = "/update/{classifyId}", method = RequestMethod.PUT)
    public BaseResult updateById(@PathVariable(value = "classifyId") Long classifyId,
                                 @RequestBody @Valid BlogCommonTitleParam param) {
        BlogClassify classify = blogClassifyService.getById(classifyId);
        if (classify == null) {
            return BaseResult.failed("未找到对应分类");
        }
        LambdaUpdateWrapper<BlogClassify> lambda = new UpdateWrapper<BlogClassify>().lambda();
        lambda.set(BlogClassify::getTitle, param.getTitle());
        lambda.set(BlogClassify::getStatus, param.getStatus());
        lambda.set(BlogClassify::getUpdateTime, DateTimeUtil.currentDate());
        lambda.eq(BlogClassify::getClassifyId, classifyId);

        return BaseResult.ok(blogClassifyService.update(lambda));
    }

    @ApiOperation("分类删除")
    @Right(rightsOr = "sys:blogClassify:delete")
    @Log(title = "分类管理", businessType = BusinessTypeEnum.DELETE)
    @RequestMapping(value = "/delete/{classifyId}", method = RequestMethod.DELETE)
    public BaseResult removeTag(@PathVariable(value = "classifyId") Long classifyId) {
        BlogClassify classify = blogClassifyService.getById(classifyId);
        if (classify == null) {
            return BaseResult.failed("未找到对应分类");
        }
        boolean removeTagResult = blogClassifyService.removeById(classifyId);

        LambdaQueryWrapper<BlogArticleClassify> lambda = new QueryWrapper<BlogArticleClassify>().lambda();
        lambda.eq(BlogArticleClassify::getClassifyId, classifyId);
        boolean removeLinkResult = blogArticleClassifyService.remove(lambda);

        return BaseResult.ok(removeLinkResult && removeTagResult);
    }

    @ApiOperation("启用分类")
    @Right(rightsOr = "sys:blogClassify:enable")
    @Log(title = "分类管理", businessType = BusinessTypeEnum.UPDATE)
    @RequestMapping(value = "/enable", method = RequestMethod.GET)
    public BaseResult enableTag(@RequestParam(value = "classifyId") Long classifyId) {
        BlogClassify classify = blogClassifyService.getById(classifyId);
        if (classify == null) {
            return BaseResult.failed("未找到对应分类");
        }
        LambdaUpdateWrapper<BlogClassify> lambda = new UpdateWrapper<BlogClassify>().lambda();
        lambda.set(BlogClassify::getStatus, StatusEnum.USABLE.getStatus());
        lambda.set(BlogClassify::getUpdateTime, DateTimeUtil.currentDate());
        lambda.eq(BlogClassify::getClassifyId, classifyId);
        return BaseResult.ok(blogClassifyService.update(lambda));
    }

    @ApiOperation("禁用分类")
    @Right(rightsOr = "sys:blogClassify:disable")
    @Log(title = "分类管理", businessType = BusinessTypeEnum.UPDATE)
    @RequestMapping(value = "/disable", method = RequestMethod.GET)
    public BaseResult disableTag(@RequestParam(value = "classifyId") Long classifyId) {
        BlogClassify classify = blogClassifyService.getById(classifyId);
        if (classify == null) {
            return BaseResult.failed("未找到对应分类");
        }
        LambdaUpdateWrapper<BlogClassify> lambda = new UpdateWrapper<BlogClassify>().lambda();
        lambda.set(BlogClassify::getStatus, StatusEnum.DISABLE.getStatus());
        lambda.set(BlogClassify::getUpdateTime, DateTimeUtil.currentDate());
        lambda.eq(BlogClassify::getClassifyId, classifyId);
        return BaseResult.ok(blogClassifyService.update(lambda));
    }

}
