package com.yifan.admin.api.model.xunfei;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

/**
 * @author https://www.unfbx.com/
 */
@Data
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class OutHeader {
    /**
     * 错误码，0表示正常，非0表示出错；详细释义可在接口说明文档最后的错误码说明了解
     */
    private Integer code;

    /**
     * 会话状态，取值为[0,1,2]；0代表首次结果；1代表中间结果；2代表最后一个结果
     */
    private Integer status;

    /**
     * 会话是否成功的描述信息
     */
    private String message;

    /**
     * 会话的唯一id，用于讯飞技术人员查询服务端会话日志使用,出现调用错误时建议留存该字段
     */
    private String sid;


    public boolean isSuccess() {
        return this.code != null && this.code.equals(0);
    }

    public boolean isEnd() {
        return this.status.equals(2);
    }
}
