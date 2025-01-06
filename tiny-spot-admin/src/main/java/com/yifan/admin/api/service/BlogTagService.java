package com.yifan.admin.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yifan.admin.api.entity.BlogTag;
import com.yifan.admin.api.model.vo.open.OpenBlogTagVO;

import java.util.List;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/5/19 16:45
 */
public interface BlogTagService extends IService<BlogTag> {

    List<OpenBlogTagVO> getOpenBlogTagByArticleId(Long articleId);
}
