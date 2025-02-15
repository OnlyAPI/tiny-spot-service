package com.yifan.admin.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yifan.admin.api.mapper.BlogTagMapper;
import com.yifan.admin.api.model.vo.open.OpenBlogTagVO;
import com.yifan.admin.api.service.BlogTagService;
import com.yifan.admin.api.entity.BlogTag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/5/19 16:45
 */
@Slf4j
@Service
public class BlogTagServiceImpl extends ServiceImpl<BlogTagMapper, BlogTag> implements BlogTagService {


    @Override
    public List<OpenBlogTagVO> getOpenBlogTagByArticleId(Long articleId) {
        return baseMapper.getOpenBlogTagByArticleId(articleId);
    }
}
