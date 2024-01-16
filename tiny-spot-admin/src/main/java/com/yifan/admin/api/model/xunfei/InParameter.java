package com.yifan.admin.api.model.xunfei;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/9/20 12:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class InParameter {

    private Chat chat;


    @Data
    @JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class Chat {

        /**
         * 指定访问的领域,
         * general指向V1.5版本
         * generalv2指向V2版本。
         * 注意：不同的取值对应的url也不一样！
         */
        private String domain;

        /**
         * 核采样阈值。用于决定结果随机性，取值越高随机性越强即相同的问题得到的不同答案的可能性越高
         */
        private Float temperature = 0.5f;

        /**
         * 取值为[1,4096]，默认为2048
         */
        private Integer maxTokens = 2048;


        public Chat(String domain) {
            this.domain = domain;
        }
    }

}
