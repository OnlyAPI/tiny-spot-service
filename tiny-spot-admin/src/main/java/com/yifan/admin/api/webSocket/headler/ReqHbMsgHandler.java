package com.yifan.admin.api.webSocket.headler;


import com.yifan.admin.api.utils.JacksonJsonUtil;
import com.yifan.admin.api.webSocket.WebSocketContext;
import com.yifan.admin.api.webSocket.msg.req.ReqHbMsg;
import com.yifan.admin.api.webSocket.msg.resp.RespHbMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.Session;

/**
 * @author TaiYi
 * @ClassName
 * @date 2022/10/27 11:40
 */
@Slf4j
@Component
public class ReqHbMsgHandler implements WebsocketMsgHandler<ReqHbMsg> {

    @Override
    public void handle(Session session, ReqHbMsg msg) throws Exception {
        log.info("heart beat. sessionId: {}", session.getId());
        WebSocketContext.sendMsg(session, JacksonJsonUtil.toJson(new RespHbMsg()));
    }

}
