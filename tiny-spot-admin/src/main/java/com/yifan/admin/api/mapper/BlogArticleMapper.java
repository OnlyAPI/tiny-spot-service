package com.yifan.admin.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yifan.admin.api.entity.BlogArticle;
import com.yifan.admin.api.model.dto.LineChartDataDTO;
import com.yifan.admin.api.model.params.OpenBlogParam;
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
public interface BlogArticleMapper extends BaseMapper<BlogArticle> {

    @Select("SELECT DATE_FORMAT( create_time, #{timeFormat} ) AS crd_date_time, count( 1 ) AS total " +
            "FROM ts_blog_article " +
            "where create_time BETWEEN #{beginTime} and #{endTime} " +
            "GROUP BY crd_date_time")
    List<LineChartDataDTO> getArticleEveryDayData(@Param("beginTime") String beginTime,
                                                  @Param("endTime") String endTime,
                                                  @Param("timeFormat") String timeFormat);


    @Select({
            "<script>",
            "SELECT * FROM ts_blog_article",
            "<where>",
            "  <if test='param.keyword != null and param.keyword !=  \"\"'>",
            "    AND title LIKE CONCAT('%', #{param.keyword}, '%')",
            "  </if>",
            "  <if test='tagCount > 0'>",
            "    AND id IN (",
            "      SELECT article_id FROM ts_blog_article_tag",
            "      WHERE tag_id IN",
            "      <foreach collection='tagsList' item='tagId' open='(' separator=',' close=')'>",
            "        #{tagId}",
            "      </foreach>",
            "      GROUP BY article_id",
            "      HAVING COUNT(DISTINCT tag_id) = #{tagCount}",
            "    )",
            "  </if>",
            "</where>",
            "LIMIT #{pageIndex}, #{size}",
            "</script>"
    })
    List<BlogArticle> getSearchList(@Param("param") OpenBlogParam param,
                                    @Param("pageIndex") int pageIndex,
                                    @Param("size") int size,
                                    @Param("tagsList") List<String> tagsList,
                                    @Param("tagCount") int tagCount);

    @Select({
            "<script>",
            "SELECT count(1) FROM ts_blog_article",
            "<where>",
            "  <if test='param.keyword != null and param.keyword != \"\"'>",
            "    AND title LIKE CONCAT('%', #{param.keyword}, '%')",
            "  </if>",
            "  <if test='tagCount > 0'>",
            "    AND id IN (",
            "      SELECT article_id FROM ts_blog_article_tag",
            "      WHERE tag_id IN",
            "      <foreach collection='tagsList' item='tagId' open='(' separator=',' close=')'>",
            "        #{tagId}",
            "      </foreach>",
            "      GROUP BY article_id",
            "      HAVING COUNT(DISTINCT tag_id) = #{tagCount}",
            "    )",
            "  </if>",
            "</where>",
            "</script>"
    })
    int getSearchTotal(@Param("param") OpenBlogParam param,
                       @Param("tagsList") List<String> tagsList,
                       @Param("tagCount") int tagCount);
}
