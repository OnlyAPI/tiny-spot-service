package com.yifan.admin.api.model.xunfei;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/9/20 11:59
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class InHeader {

    /**
     * 应用appid
     */
    private String appId;

    /**
     * 每个用户的id，用于区分不同用户
     */
    private String uid;

}

