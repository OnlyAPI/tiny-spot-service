package com.yifan.admin.api.webSocket.msg.resp;

import com.yifan.admin.api.enums.WsMsgTypeEnum;
import com.yifan.admin.api.webSocket.msg.WebsocketMsg;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/11/27 18:29
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RespAiChatMsg extends WebsocketMsg {

    private String msg;

    private String data;


    public RespAiChatMsg(String data) {
        this.setType(WsMsgTypeEnum.RESP_AI_CHAT_MSG.getType());
        this.setMsg("OK");
        this.setData(data);
    }
}
