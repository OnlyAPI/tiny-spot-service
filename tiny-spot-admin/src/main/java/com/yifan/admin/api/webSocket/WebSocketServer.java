package com.yifan.admin.api.webSocket;

import com.yifan.admin.api.enums.WsMsgTypeEnum;
import com.yifan.admin.api.model.data.StoreWsTokenData;
import com.yifan.admin.api.service.CacheService;
import com.yifan.admin.api.utils.JacksonJsonUtil;
import com.yifan.admin.api.utils.SpringContextUtil;
import com.yifan.admin.api.webSocket.headler.DefaultWebSocketMsgHandler;
import com.yifan.admin.api.webSocket.msg.resp.RespConnMsg;
import com.yifan.admin.api.webSocket.msg.resp.RespErrorMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @description: TODO
 * @author: wyf
 * @date: 2023/11/25
 */
@Component
@Slf4j
@ServerEndpoint("/websocket/{wstoken}")
public class WebSocketServer {

    //记录当前在线连接数
    private static final AtomicInteger onlineSessionClientCount = new AtomicInteger(0);

    /**
     * 连接建立成功调用的方法
     *
     * @param wstoken 用户唯一标识
     * @param session 与客户端关联的会话
     */
    @OnOpen
    public void onOpen(@PathParam("wstoken") String wstoken, Session session) {
        log.info("[onOpen] 连接建立中 ==> sessionId = {}， wstoken = {}", session.getId(), wstoken);

        CacheService cacheService = SpringContextUtil.getBean(CacheService.class);
        StoreWsTokenData wstokenInfo = cacheService.getWsTokenInfo(wstoken);
        if (wstokenInfo != null) {
            cacheService.removeWsToken(wstoken);
            //在线数加1
            if (!WebSocketContext.hasSession(wstokenInfo.getUserId())){
                onlineSessionClientCount.incrementAndGet();
            }
            WebSocketContext.addSession(wstokenInfo.getUserId(), session);
            WebSocketContext.sendMsg(session, JacksonJsonUtil.toJson(new RespConnMsg()));
            log.info("[onOpen] 连接建立成功，当前在线数为：{} ==> 开始监听新连接：sessionId = {}， wstoken = {}", onlineSessionClientCount, session.getId(), wstoken);
        } else {
            log.info(" [onOpen] 连接建立失败，非法wstoken, wstoken = {}", wstoken);
            WebSocketContext.sendMsg(session, JacksonJsonUtil.toJson(new RespErrorMsg("连接失败，wstoken非法")));
            try {
                session.close();
            } catch (Exception e) {
                log.error("[onOpen] websocket错误", e);
            }
        }
    }

    /**
     * 连接关闭方法，由客户端socket.close()触发
     */
    @OnClose
    public void onClose(Session session) {
        if (WebSocketContext.hasSession(session)) {
            WebSocketContext.removeSession(session);
            //在线数减1
            onlineSessionClientCount.decrementAndGet();
        }
        try {
            if (session.isOpen()) {
                session.close();
            }
            log.info("[onClose] 连接关闭成功，当前在线数为：{} ==> 关闭该连接信息：sessionId = {}", onlineSessionClientCount, session.getId());
        } catch (Exception e) {
            log.error("[onClose] websocket关闭链接失败");
        }
    }

    /**
     * 收到客户端消息后调用的方法。由客户端socket.send触发
     * 当服务端执行toSession.getAsyncRemote().sendText(xxx)后，客户端的socket.onmessage得到监听。
     */
    @OnMessage
    public void onMessage(String message, Session session) throws Exception {
        log.info("收到客户端消息 sessionId= {}, msg= {}", session.getId(), message);
        String type = (String) JacksonJsonUtil.fromJson(message, Map.class).getOrDefault("type", "");
        WsMsgTypeEnum typeEnum = WsMsgTypeEnum.getByType(type);
        if (typeEnum == null) {
            log.error("[onMessage] msg type error, message: {}", message);
            return;
        }
        SpringContextUtil.getBean(DefaultWebSocketMsgHandler.class).handle(session, JacksonJsonUtil.fromJson(message, typeEnum.getaClass()));
    }

    /**
     * 发生错误调用的方法
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("[onError] websocket错误", error);
        if (WebSocketContext.hasSession(session)) {
            WebSocketContext.removeSession(session);
            //在线数减1
            onlineSessionClientCount.decrementAndGet();
        }
        try {
            if (session.isOpen()) {
                session.close();
            }
            log.info("[onError] 连接关闭成功，当前在线数为：{} ==> 关闭该连接信息：sessionId = {}", onlineSessionClientCount, session.getId());
        } catch (Exception e) {
            log.error("[onError] websocket关闭链接失败");
        }
    }
}
