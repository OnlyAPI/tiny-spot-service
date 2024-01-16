package com.yifan.admin.api.webSocket.msg.resp;

import com.yifan.admin.api.enums.WsMsgTypeEnum;
import com.yifan.admin.api.webSocket.msg.WebsocketMsg;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/11/23 11:55
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RespHbMsg extends WebsocketMsg {

    private String msg;

    public RespHbMsg() {
        this.setType(WsMsgTypeEnum.RESP_HB_MSG.getType());
        this.setMsg("OK");
    }
}
