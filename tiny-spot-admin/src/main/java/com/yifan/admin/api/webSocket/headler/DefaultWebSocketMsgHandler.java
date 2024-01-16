package com.yifan.admin.api.webSocket.headler;

import com.yifan.admin.api.webSocket.msg.WebsocketMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import javax.websocket.Session;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@SuppressWarnings({"unchecked"})
public class DefaultWebSocketMsgHandler {

    private final Map<Class, List<WebsocketMsgHandler>> msgHandlerMap;

    public DefaultWebSocketMsgHandler(List<WebsocketMsgHandler> msgHandlerList) {
        msgHandlerMap = msgHandlerList.stream().collect(Collectors.groupingBy(websocketMsgHandler -> {
            Type type = websocketMsgHandler.getClass().getGenericSuperclass();
            if (!(type instanceof ParameterizedType)) {
                Type[] types = websocketMsgHandler.getClass().getGenericInterfaces();
                for (Type tempType : types) {
                    if (tempType instanceof ParameterizedType && ((ParameterizedType) tempType).getRawType() == WebsocketMsgHandler.class) {
                        type = tempType;
                        break;
                    }
                }
            }
            return (Class<?>) ((ParameterizedType) type).getActualTypeArguments()[0];
        }));
    }

    public void handle(Session session, WebsocketMsg msg) throws Exception {
        List<WebsocketMsgHandler> msgHandlerList = msgHandlerMap.get(msg.getClass());
        if (CollectionUtils.isEmpty(msgHandlerList)) {
            log.error("[DefaultWebSocketMsgHandler - handle] no msg handler for msg.type: {}, msg.class: {}", msg.getType(), msg.getClass());
            return;
        }
        for (WebsocketMsgHandler handler : msgHandlerList) {
            handler.handle(session, msg);
        }
    }

}
