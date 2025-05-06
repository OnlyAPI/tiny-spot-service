package com.yifan.admin.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yifan.admin.api.entity.BlogArticle;
import com.yifan.admin.api.model.params.OpenBlogParam;

import java.util.List;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/5/19 16:45
 */
public interface BlogArticleService extends IService<BlogArticle> {
    List<BlogArticle> getSearchList(OpenBlogParam param);

    int getSearchListTotal(OpenBlogParam param);
}
