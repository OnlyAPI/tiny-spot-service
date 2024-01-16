package com.yifan.admin.api.model.xunfei;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

/**
 * @author https://www.unfbx.com/
 */
@Data
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OutPayload {

    /**
     * 问答信息
     */
    private Choices choices;

    /**
     * Token 信息
     */
    private Usage usage;


    @Data
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Choices {
        /**
         * 文本响应状态，取值为[0,1,2]; 0代表首个文本结果；1代表中间文本结果；2代表最后一个文本结果
         */
        private Integer status;

        /**
         * 返回的数据序号，取值为[0,9999999]
         */
        private Integer seq;

        /**
         * 返回信息
         */
        private List<Message> text;
    }

    @Data
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Usage {

        private OutText text;


        @Data
        @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class OutText {
            /**
             * 保留字段，可忽略
             */
            private Integer questionTokens;

            /**
             * 包含历史问题的总tokens大小
             */
            private Integer promptTokens;

            /**
             * 回答的tokens大小
             */
            private Integer completionTokens;

            /**
             * prompt_tokens和completion_tokens的和，也是本次交互计费的tokens大小
             */
            private Integer totalTokens;
        }

    }


}
