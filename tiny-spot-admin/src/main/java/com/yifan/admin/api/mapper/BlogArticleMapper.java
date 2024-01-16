package com.yifan.admin.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yifan.admin.api.entity.BlogArticle;
import com.yifan.admin.api.model.dto.LineChartDataDTO;
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
}
