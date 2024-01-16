package com.yifan.admin.api.webSocket.msg.req;

import com.yifan.admin.api.webSocket.msg.WebsocketMsg;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/11/27 18:24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class ReqAiChatMsg extends WebsocketMsg {

   private String mark;

   private String text;

   private String conversationId;

}
