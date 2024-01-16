package com.yifan.admin.api.webSocket.msg.resp;

import com.yifan.admin.api.enums.WsMsgTypeEnum;
import com.yifan.admin.api.webSocket.msg.WebsocketMsg;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @description: TODO
 * @author: wyf
 * @date: 2023/11/27
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RespSingleChatMsg extends WebsocketMsg {
    private String fromUserId;
    private String text;

    public RespSingleChatMsg(String fromUserId, String text) {
        this.setText(text);
        this.setFromUserId(fromUserId);
        this.setType(WsMsgTypeEnum.RESP_SINGLE_CHAT_MSG.getType());
    }
}
