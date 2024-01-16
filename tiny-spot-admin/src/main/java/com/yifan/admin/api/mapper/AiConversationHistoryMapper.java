package com.yifan.admin.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yifan.admin.api.entity.AiConversationHistory;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description: TODO
 * @author: wyf
 * @date: 2023/12/8
 */
@Repository
public interface AiConversationHistoryMapper extends BaseMapper<AiConversationHistory> {


    @Select("select * from ts_ai_conversation_history where user_id = #{userId} and conversation_id = #{cid} and status = 1 order by create_time desc limit 1")
    AiConversationHistory queryLastConversationHistory(@Param("userId") Long userId, @Param("cid") String cid);

    @Select("select * from ts_ai_conversation_history where user_id = #{userId} and conversation_id = #{cid} and status = 1 order by create_time asc")
    List<AiConversationHistory> queryAiConversationHistoryListDesc(@Param("userId") Long userId, @Param("cid") String cid);

    @Update("update ts_ai_conversation_history set status = 2 where user_id = #{userId} and conversation_id = #{cid}")
    int delConversationHistoryByCid(@Param("userId") Long userId, @Param("cid") String cid);
}
