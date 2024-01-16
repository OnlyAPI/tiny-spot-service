package com.yifan.admin.api.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yifan.admin.api.entity.*;
import com.yifan.admin.api.service.*;
import com.yifan.admin.api.annotition.Log;
import com.yifan.admin.api.annotition.Right;
import com.yifan.admin.api.context.RequestContext;
import com.yifan.admin.api.model.params.BlogBaseParam;
import com.yifan.admin.api.model.params.SaveOrUpdateArticleParam;
import com.yifan.admin.api.model.vo.BlogArticleVO;
import com.yifan.admin.api.enums.BusinessTypeEnum;
import com.yifan.admin.api.enums.StatusEnum;
import com.yifan.admin.api.result.BaseResult;
import com.yifan.admin.api.result.CommonPage;
import com.yifan.admin.api.utils.DateTimeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 博客-文章控制器
 *
 * @author TaiYi
 * @ClassName
 * @date 2023/5/19 14:00
 */
@Api(tags = "博客-文章管理")
@RestController
@RequestMapping("/blog")
public class BlogArticleController {

    @Resource
    private BlogArticleService blogArticleService;
    @Resource
    private BlogTagService blogTagService;
    @Resource
    private BlogArticleTagService blogArticleTagService;
    @Resource
    private BlogArticleClassifyService blogArticleClassifyService;
    @Resource
    private BlogClassifyService blogClassifyService;

    @ApiOperation("根据标题或副标题分页获取文章列表")
    @Right(rightsOr = "sys:blog:list")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public BaseResult<CommonPage<BlogArticle>> pageList(BlogBaseParam param) {
        Page<BlogArticle> page = new Page<>(param.getPageNum(), param.getPageSize());
        LambdaQueryWrapper<BlogArticle> queryWrapper = new QueryWrapper<BlogArticle>().lambda();
        queryWrapper.orderByAsc(BlogArticle::getSort);
        if (StringUtils.isNotBlank(param.getKeyword())) {
            queryWrapper.like(BlogArticle::getTitle, param.getKeyword())
                    .or()
                    .like(BlogArticle::getSubTitle, param.getKeyword());
        }
        if (StringUtils.isNotBlank(param.getBeginTime()) && StringUtils.isNotBlank(param.getEndTime())) {
            queryWrapper.between(BlogArticle::getCreateTime, param.getBeginTime(), param.getEndTime());
        }
        if (param.getSortOrder().contains("desc")) {
            queryWrapper.orderByDesc(BlogArticle::getCreateTime);
        } else {
            queryWrapper.orderByAsc(BlogArticle::getCreateTime);
        }
        List<Long> list1 = new ArrayList<>();
        List<Long> list2 = new ArrayList<>();
        if (param.getClassifyId() != null) {
            List<BlogArticleClassify> classifies = blogArticleClassifyService.list(new QueryWrapper<BlogArticleClassify>().lambda().eq(BlogArticleClassify::getClassifyId, param.getClassifyId()));
            list1 = classifies.stream().map(BlogArticleClassify::getArticleId).collect(Collectors.toList());
            if (list1.isEmpty()){
                return BaseResult.ok(CommonPage.empty());
            }
        }
        if (param.getTagId() != null) {
            List<BlogArticleTag> tags = blogArticleTagService.list(new QueryWrapper<BlogArticleTag>().lambda().eq(BlogArticleTag::getTagId, param.getTagId()));
            list2 = tags.stream().map(BlogArticleTag::getArticleId).collect(Collectors.toList());
            if (list2.isEmpty()){
                return BaseResult.ok(CommonPage.empty());
            }
        }
        Set<Long> articleIds = getListIntersection(list1, list2);
        if (param.getClassifyId() != null || param.getTagId() != null) {
            if (articleIds.isEmpty()) {
                return BaseResult.ok(CommonPage.empty());
            }
            queryWrapper.in(BlogArticle::getId, articleIds);
        }
        Page<BlogArticle> articlePage = blogArticleService.page(page, queryWrapper);
        return BaseResult.ok(CommonPage.restPage(articlePage));
    }

    /**
     * 获取2个集合的交集
     * @param articleIdByTag
     * @param articleIdByclassifyId
     * @return
     */
    private Set<Long> getListIntersection(List<Long> articleIdByTag, List<Long> articleIdByclassifyId) {
        Set<Long> articleIds = new HashSet<>();
        if (articleIdByTag.isEmpty() && articleIdByclassifyId.isEmpty()) {
            return articleIds;
        }
        if (!articleIdByTag.isEmpty() && !articleIdByclassifyId.isEmpty()) {
            articleIdByTag.forEach( e -> {
                if (articleIdByclassifyId.contains(e)){
                    articleIds.add(e);
                }
            });
            return articleIds;
        }
        articleIds.addAll((articleIdByTag.isEmpty() ? articleIdByclassifyId : articleIdByTag));
        return articleIds;
    }

    @ApiOperation("根据ID获取文章详情")
    @Right(rightsOr = "sys:blog:list")
    @RequestMapping(value = "/getById/{articleId}", method = RequestMethod.GET)
    public BaseResult<BlogArticleVO> getById(@PathVariable(value = "articleId") Long articleId) {
        BlogArticle articleServiceById = blogArticleService.getById(articleId);
        if (articleServiceById == null) {
            return BaseResult.failed("未找到对应文章");
        }

        BlogArticleVO articleVO = new BlogArticleVO(articleServiceById);

        LambdaQueryWrapper<BlogArticleTag> lambda = new QueryWrapper<BlogArticleTag>().lambda();
        lambda.eq(BlogArticleTag::getArticleId, articleId);
        List<BlogArticleTag> list = blogArticleTagService.list(lambda);
        if (!list.isEmpty()) {
            List<Long> tagIds = list.stream().map(BlogArticleTag::getTagId).collect(Collectors.toList());
            articleVO.setTagIds(tagIds);
        }
        LambdaQueryWrapper<BlogArticleClassify> wrapper = new QueryWrapper<BlogArticleClassify>().lambda()
                .eq(BlogArticleClassify::getArticleId, articleId);
        BlogArticleClassify articleClassify = blogArticleClassifyService.getOne(wrapper);
        if (articleClassify != null) {
            articleVO.setClassifyId(articleClassify.getClassifyId());
        }

        return BaseResult.ok(articleVO);
    }

    @ApiOperation("添加文章")
    @Right(rightsOr = "sys:blog:add")
    @Log(title = "文章管理", businessType = BusinessTypeEnum.INSERT)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BaseResult<Void> save(@RequestBody @Valid SaveOrUpdateArticleParam param) {
        BlogArticle article = new BlogArticle();
        BeanUtils.copyProperties(param, article);
        article.setCreateBy(RequestContext.getUser().getId());
        article.setCreateTime(DateTimeUtil.currentDate());
        article.setUpdateTime(DateTimeUtil.currentDate());
        blogArticleService.save(article);

        if (!param.getTagIds().isEmpty()) {
            blogArticleTagService.saveBatch(this.getBlogArticleTagList(article.getId(), param.getTagIds()));
        }
        if (param.getClassifyId() != null) {
            blogArticleClassifyService.save(this.getBlogArticleClassify(article.getId(), param.getClassifyId()));
        }

        return BaseResult.ok();
    }

    @ApiOperation("修改文章")
    @Right(rightsOr = "sys:blog:update")
    @Log(title = "文章管理", businessType = BusinessTypeEnum.UPDATE)
    @RequestMapping(value = "/update/{articleId}", method = RequestMethod.POST)
    public BaseResult<Void> update(@PathVariable(value = "articleId") Long articleId,
                             @RequestBody @Valid SaveOrUpdateArticleParam param) {
        BlogArticle blogArticle = blogArticleService.getById(articleId);
        if (blogArticle == null) {
            return BaseResult.failed("未找到对应文章");
        }
        BeanUtils.copyProperties(param, blogArticle);
        blogArticle.setUpdateTime(DateTimeUtil.currentDate());
        boolean update = blogArticleService.updateById(blogArticle);

        blogArticleTagService.remove(new QueryWrapper<BlogArticleTag>().lambda()
                .eq(BlogArticleTag::getArticleId, articleId));
        if (!param.getTagIds().isEmpty()) {
            blogArticleTagService.saveBatch(this.getBlogArticleTagList(articleId, param.getTagIds()));
        }

        blogArticleClassifyService.remove(new QueryWrapper<BlogArticleClassify>().lambda()
                .eq(BlogArticleClassify::getArticleId, articleId));
        if (param.getClassifyId() != null) {
            blogArticleClassifyService.save(this.getBlogArticleClassify(articleId, param.getClassifyId()));
        }

        return BaseResult.ok();
    }

    @ApiOperation("删除文章")
    @Right(rightsOr = "sys:blog:remove")
    @Log(title = "文章管理", businessType = BusinessTypeEnum.DELETE)
    @RequestMapping(value = "/delete/{articleId}", method = RequestMethod.DELETE)
    public BaseResult<Boolean> delete(@PathVariable(value = "articleId") Long articleId) {
        boolean remove = blogArticleService.removeById(articleId);

        blogArticleTagService.remove(new QueryWrapper<BlogArticleTag>().lambda()
                .eq(BlogArticleTag::getArticleId, articleId));

        blogArticleClassifyService.remove(new QueryWrapper<BlogArticleClassify>().lambda()
                .eq(BlogArticleClassify::getArticleId, articleId));

        return BaseResult.ok(remove);
    }

    @ApiOperation("修改发布状态")
    @Right(rightsOr = "sys:blog:update")
    @Log(title = "文章管理", businessType = BusinessTypeEnum.UPDATE)
    @RequestMapping(value = "/updateStatus/{articleId}", method = RequestMethod.GET)
    public BaseResult<Boolean> updateStatus(@PathVariable(value = "articleId") Long articleId,
                                   @RequestParam int status) {

        LambdaUpdateWrapper<BlogArticle> lambda = new UpdateWrapper<BlogArticle>().lambda();
        lambda.eq(BlogArticle::getId, articleId);
        lambda.set(BlogArticle::getStatus, (Objects.equals(status, 0) ? 0 : 1));
        lambda.set(BlogArticle::getUpdateTime, DateTimeUtil.currentDate());
        boolean update = blogArticleService.update(lambda);
        return BaseResult.ok(update);
    }

    @ApiOperation("修改置顶状态")
    @Right(rightsOr = "sys:blog:update")
    @Log(title = "文章管理", businessType = BusinessTypeEnum.UPDATE)
    @RequestMapping(value = "/updateTopStatus/{articleId}", method = RequestMethod.GET)
    public BaseResult<Boolean> updateTopStatus(@PathVariable(value = "articleId") Long articleId,
                                      @RequestParam int status) {

        LambdaUpdateWrapper<BlogArticle> lambda = new UpdateWrapper<BlogArticle>().lambda();
        lambda.eq(BlogArticle::getId, articleId);
        lambda.set(BlogArticle::getIsTop, (Objects.equals(status, 0) ? 0 : 1));
        lambda.set(BlogArticle::getUpdateTime, DateTimeUtil.currentDate());
        boolean update = blogArticleService.update(lambda);
        return BaseResult.ok(update);
    }

    @ApiOperation("修改评论状态")
    @Right(rightsOr = "sys:blog:update")
    @Log(title = "文章管理", businessType = BusinessTypeEnum.UPDATE)
    @RequestMapping(value = "/updateCommentStatus/{articleId}", method = RequestMethod.GET)
    public BaseResult<Boolean> updateCommentStatus(@PathVariable(value = "articleId") Long articleId,
                                          @RequestParam int status) {

        LambdaUpdateWrapper<BlogArticle> lambda = new UpdateWrapper<BlogArticle>().lambda();
        lambda.eq(BlogArticle::getId, articleId);
        lambda.set(BlogArticle::getAllowComment, (Objects.equals(status, 0) ? 0 : 1));
        lambda.set(BlogArticle::getUpdateTime, DateTimeUtil.currentDate());
        boolean update = blogArticleService.update(lambda);
        return BaseResult.ok(update);
    }

    private List<BlogArticleTag> getBlogArticleTagList(Long articleId, List<Object> tagIds) {
        if (tagIds.isEmpty()) {
            return Collections.emptyList();
        }
        List<BlogArticleTag> articleTags = new ArrayList<>();

        tagIds.forEach(e -> {
            BlogArticleTag articleTag = new BlogArticleTag(articleId);
            if (e instanceof String) {
                //创建新标签
                BlogTag tag = new BlogTag();
                tag.setTitle((String) e);
                tag.setStatus(StatusEnum.USABLE.getStatus());
                tag.setCreateTime(DateTimeUtil.currentDate());
                tag.setUpdateTime(DateTimeUtil.currentDate());
                blogTagService.save(tag);

                articleTag.setTagId(tag.getId());
            } else {
                articleTag.setTagId((long) (int) e);
            }

            articleTags.add(articleTag);
        });
        return articleTags;
    }

    private BlogArticleClassify getBlogArticleClassify(Long articleId, Object classifyId) {
        BlogArticleClassify articleClassify = new BlogArticleClassify(articleId);
        if (classifyId instanceof String) {
            BlogClassify blogClassify = new BlogClassify();
            blogClassify.setTitle((String) classifyId);
            blogClassify.setStatus(StatusEnum.USABLE.getStatus());
            blogClassify.setCreateTime(DateTimeUtil.currentDate());
            blogClassify.setUpdateTime(DateTimeUtil.currentDate());

            blogClassifyService.save(blogClassify);
            articleClassify.setClassifyId(blogClassify.getClassifyId());
        } else {
            articleClassify.setClassifyId((long) (int) classifyId);
        }
        return articleClassify;
    }
}
