package com.yifan.admin.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yifan.admin.api.mapper.BlogArticleMapper;
import com.yifan.admin.api.model.params.OpenBlogParam;
import com.yifan.admin.api.service.BlogArticleService;
import com.yifan.admin.api.entity.BlogArticle;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/5/19 16:45
 */
@Slf4j
@Service
public class BlogArticleServiceImpl extends ServiceImpl<BlogArticleMapper, BlogArticle> implements BlogArticleService {


    @Override
    public List<BlogArticle> getSearchList(OpenBlogParam param) {
        int pageIndex = (param.getPageNum() - 1 ) * param.getPageSize();
        int size = param.getPageSize();
        List<String> tagsList = new ArrayList<>();
        if (StringUtils.isNotBlank(param.getTagIdStr())) {
            tagsList = Arrays.asList(param.getTagIdStr().split(","));
        }
        return baseMapper.getSearchList(param, pageIndex, size, tagsList, tagsList.size());
    }

    @Override
    public int getSearchListTotal(OpenBlogParam param) {
        List<String> tagsList = new ArrayList<>();
        if (StringUtils.isNotBlank(param.getTagIdStr())) {
            tagsList = Arrays.asList(param.getTagIdStr().split(","));
        }
        return baseMapper.getSearchTotal(param, tagsList, tagsList.size());
    }
}
