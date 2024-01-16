package com.yifan.admin.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yifan.admin.api.entity.UserLoginLog;
import com.yifan.admin.api.model.dto.LineChartDataDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 后台用户登录日志表 Mapper 接口
 */
@Repository
public interface UserLoginLogMapper extends BaseMapper<UserLoginLog> {

    @Select("SELECT DATE_FORMAT( create_time, #{timeFormat} ) AS crd_date_time, count( 1 ) AS total " +
            "FROM ts_user_login_log " +
            "where create_time BETWEEN #{beginTime} and #{endTime} " +
            "GROUP BY crd_date_time")
    List<LineChartDataDTO> getUserLoginEveryDayData(@Param("beginTime") String beginTime,
                                                    @Param("endTime") String endTime,
                                                    @Param("timeFormat") String timeFormat);
}
