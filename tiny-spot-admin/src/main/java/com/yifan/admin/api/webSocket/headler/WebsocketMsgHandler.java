package com.yifan.admin.api.webSocket.headler;


import com.yifan.admin.api.webSocket.msg.WebsocketMsg;

import javax.websocket.Session;

public interface WebsocketMsgHandler<T extends WebsocketMsg> {

    void handle(Session session, T msg) throws Exception;

}
