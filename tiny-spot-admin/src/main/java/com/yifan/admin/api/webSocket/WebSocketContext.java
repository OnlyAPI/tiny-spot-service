package com.yifan.admin.api.webSocket;

import com.yifan.admin.api.constant.SysConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.websocket.Session;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/11/23 10:39
 */
@Slf4j
public final class WebSocketContext {

    private static final Map<String, Session> SESSION_MAP = new ConcurrentHashMap<>();

    private WebSocketContext() {
    }

    public static void addSession(String uId, Session session) {
        SESSION_MAP.put(uId, session);
        session.getUserProperties().put(SysConstant.WS_USER_ID, uId);
        log.info("[addSession] sessionId: {}", uId);
    }


    public static void sendMsg(Session session, String jsonMsg) {
        try {
            synchronized (session){
//            session.getAsyncRemote().sendText(jsonMsg);
                session.getBasicRemote().sendText(jsonMsg);
            }
        }catch (Exception e){
            log.error("send message error.", e);
        }
    }

    public static <T> T getSessionProperty(String uId, String property) {
        return (T) getSession(uId).getUserProperties().get(property);
    }

    public static Session getSession(String uId) {
        return SESSION_MAP.get(uId);
    }

    public static String getUId(Session session) {
        return (String) session.getUserProperties().get(SysConstant.WS_USER_ID);
    }

    public static boolean hasSession(String uId) {
        return !StringUtils.isBlank(uId) && SESSION_MAP.get(uId) != null;
    }

    public static boolean hasSession(Session session) {
        return hasSession(getUId(session));
    }

    public static void removeSession(String uId) {
        SESSION_MAP.remove(uId);
        log.info("[removeSession] sessionId: {}", uId);
    }

    public static void removeSession(Session session) {
        removeSession(getUId(session));
    }
}
