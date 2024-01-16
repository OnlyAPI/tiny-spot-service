package com.yifan.admin.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yifan.admin.api.entity.AiRobot;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/12/4 18:16
 */
@Repository
public interface AiRobotMapper extends BaseMapper<AiRobot> {

    @Select("select * from ts_ai_robot where id = #{robotId} and status = 1")
    AiRobot selectUsableRobotById(@Param("robotId") Long robotId);
}
