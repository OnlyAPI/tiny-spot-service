package com.yifan.admin.api.webSocket.headler;

import com.yifan.admin.api.utils.JacksonJsonUtil;
import com.yifan.admin.api.webSocket.WebSocketContext;
import com.yifan.admin.api.webSocket.msg.req.ReqSingleChatMsg;
import com.yifan.admin.api.webSocket.msg.resp.RespErrorMsg;
import com.yifan.admin.api.webSocket.msg.resp.RespSingleChatMsg;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.websocket.Session;

/**
 * @description: TODO
 * @author: wyf
 * @date: 2023/11/27
 */
@Component
@Slf4j
public class ReqSingleChatMsgHandler implements WebsocketMsgHandler<ReqSingleChatMsg> {

    @Override
    public void handle(Session session, ReqSingleChatMsg msg) throws Exception {
        final String fromUserId = WebSocketContext.getUId(session);
        if (StringUtils.isBlank(fromUserId)) {
            log.error("from userId is null.");
            return;
        }
        if (StringUtils.equals(fromUserId, msg.getMark())) {
            log.error("The fromUserId is same toUserId, fromUserId: {}, toUserId: {}", fromUserId, msg.getMark());
            WebSocketContext.sendMsg(session, JacksonJsonUtil.toJson(new RespErrorMsg("不能给自己发消息")));
            return;
        }
        Session toUserSession = WebSocketContext.getSession(msg.getMark());
        if (toUserSession != null && toUserSession.isOpen()) {
            WebSocketContext.sendMsg(toUserSession, JacksonJsonUtil.toJson(new RespSingleChatMsg(fromUserId, msg.getText())));
        }else {
            log.warn("user offLine, userId: {}, text: {}", msg.getMark(), msg.getText());
        }
    }
}
