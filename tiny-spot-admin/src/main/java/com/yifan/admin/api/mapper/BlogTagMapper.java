package com.yifan.admin.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yifan.admin.api.entity.BlogTag;
import com.yifan.admin.api.model.vo.open.OpenBlogTagVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/5/19 16:46
 */
@Repository
public interface BlogTagMapper extends BaseMapper<BlogTag> {


    @Select("SELECT " +
            "   bt.id as 'tagId', " +
            "   bt.title " +
            "from ts_blog_article_tag bat " +
            "   left join ts_blog_tag bt " +
            "   on bat.tag_id = bt.id " +
            "   where bat.article_id = #{articleId} ")
    List<OpenBlogTagVO> getOpenBlogTagByArticleId(@Param(value = "articleId") Long articleId);
}
