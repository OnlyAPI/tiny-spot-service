package com.yifan.admin.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yifan.admin.api.entity.Music;
import com.yifan.admin.api.model.dto.LineChartDataDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description: TODO
 * @author: wyf
 * @date: 2023/5/7
 */
@Repository
public interface MusicMapper extends BaseMapper<Music> {

    @Select("SELECT DATE_FORMAT( create_time, #{timeFormat} ) AS crd_date_time, count( 1 ) AS total " +
            "FROM ts_music " +
            "where create_time BETWEEN #{beginTime} and #{endTime} " +
            "GROUP BY " +
            "crd_date_time ")
    List<LineChartDataDTO> getMusicEveryDayData(@Param("beginTime") String beginTime,
                                                @Param("endTime") String endTime,
                                                @Param("timeFormat") String timeFormat);
}
