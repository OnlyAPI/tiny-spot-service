package com.yifan.admin.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yifan.admin.api.entity.AiConversation;
import com.yifan.admin.api.model.dto.AiConversationDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/12/8 15:28
 */
@Repository
public interface AiConversationMapper extends BaseMapper<AiConversation> {

    @Select("SELECT c.conversation_id ,c.conversation_title,sr.avatar,sr.config_type " +
            "FROM ts_ai_conversation c LEFT JOIN ts_ai_robot sr ON c.robot_id = sr.id " +
            "WHERE c.user_id = #{userId} and c.status = 1 AND sr.status = 1 order by c.update_time desc")
    List<AiConversationDTO> queryAiConversationsByUserId(@Param("userId") Long userId);

    @Select("SELECT c.conversation_id, c.conversation_title, sr.avatar, sr.config_type " +
            "FROM ts_ai_conversation c LEFT JOIN ts_ai_robot sr ON c.robot_id = sr.id " +
            "WHERE c.user_id = #{userId} and c.conversation_id = #{cId} and c.status = 1 AND sr.status = 1 order by c.update_time desc")
    AiConversationDTO queryAiConversationByUserIdAndCId(@Param("userId") Long userId, @Param("cId") String cId);

    @Select("select * from ts_ai_conversation where user_id = #{userId} and conversation_id = #{cid} and status = 1")
    AiConversation getUsableConversationByUserIdAndCid(@Param("userId") Long userId, @Param("cid") String cid);

    @Update("update ts_ai_conversation set status = 2 where user_id = #{userId} and conversation_id = #{cid}")
    int delConversationByCid(@Param("userId") Long userId, @Param("cid") String cid);
}
