package com.yifan.admin.api.model.vo;

import com.yifan.admin.api.entity.AiRobot;
import lombok.Data;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/12/4 18:48
 */
@Data
public class AiRobotVO {
    private Long id;
    private String name;
    private String description;
    private String avatar;

    public AiRobotVO(AiRobot aiRobot) {
        if (aiRobot != null) {
            this.id = aiRobot.getId();
            this.name = aiRobot.getName();
            this.description = aiRobot.getDescription();
            this.avatar = aiRobot.getAvatar();
        }
    }


}
