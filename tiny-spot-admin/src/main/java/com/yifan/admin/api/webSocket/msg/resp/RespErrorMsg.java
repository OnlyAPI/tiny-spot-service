package com.yifan.admin.api.webSocket.msg.resp;

import com.yifan.admin.api.enums.WsMsgTypeEnum;
import com.yifan.admin.api.webSocket.msg.WebsocketMsg;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/11/23 14:51
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RespErrorMsg extends WebsocketMsg {

    private String msg;

    public RespErrorMsg(String msg) {
        this.setType(WsMsgTypeEnum.RESP_ERROR_MSG.getType());
        this.setMsg(msg);
    }
}
