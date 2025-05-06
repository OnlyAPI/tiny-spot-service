package com.yifan.admin.api.controller.open;


import com.yifan.admin.api.entity.BlogArticle;
import com.yifan.admin.api.entity.SysUser;
import com.yifan.admin.api.model.params.OpenBlogParam;
import com.yifan.admin.api.model.vo.open.OpenBlogArticleVO;
import com.yifan.admin.api.model.vo.open.OpenBlogTagVO;
import com.yifan.admin.api.result.BaseResult;
import com.yifan.admin.api.result.CommonPage;
import com.yifan.admin.api.service.BlogArticleService;
import com.yifan.admin.api.service.BlogTagService;
import com.yifan.admin.api.service.SysUserService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

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
    public BaseResult<CommonPage<OpenBlogArticleVO>> getList(OpenBlogParam param) {

        List<BlogArticle> list = blogArticleService.getSearchList(param);

        List<OpenBlogArticleVO> articleVOS = list.stream().map(t -> {
            List<OpenBlogTagVO> openBlogTagVOS = blogTagService.getOpenBlogTagByArticleId(t.getId());
            SysUser sysUser = userService.getById(t.getCreateBy());
            return new OpenBlogArticleVO(t, openBlogTagVOS, sysUser.getNickName());
        }).collect(Collectors.toList());

        int total = blogArticleService.getSearchListTotal(param);

        CommonPage<OpenBlogArticleVO> commonPage = new CommonPage<>();
        commonPage.setPageSize(param.getPageSize());
        commonPage.setPageNum(param.getPageNum());
        commonPage.setTotal(total);
        commonPage.setList(articleVOS);

        return BaseResult.ok(commonPage);
    }

}
