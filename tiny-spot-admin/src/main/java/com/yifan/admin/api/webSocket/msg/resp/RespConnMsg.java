package com.yifan.admin.api.webSocket.msg.resp;

import com.yifan.admin.api.enums.WsMsgTypeEnum;
import com.yifan.admin.api.webSocket.msg.WebsocketMsg;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/11/23 14:47
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RespConnMsg extends WebsocketMsg {

    private String msg;

    public RespConnMsg() {
        this.setType(WsMsgTypeEnum.RESP_CONN_MSG.getType());
        this.setMsg("OK");
    }
}
