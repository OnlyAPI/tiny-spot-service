package com.yifan.admin.api.enums;

import com.yifan.admin.api.webSocket.msg.WebsocketMsg;
import com.yifan.admin.api.webSocket.msg.req.ReqAiChatMsg;
import com.yifan.admin.api.webSocket.msg.req.ReqHbMsg;
import com.yifan.admin.api.webSocket.msg.req.ReqSingleChatMsg;
import com.yifan.admin.api.webSocket.msg.resp.*;
import org.apache.commons.lang3.StringUtils;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/11/23 11:52
 */
public enum WsMsgTypeEnum {
    REQ_HB_MSG("req_hb", ReqHbMsg.class),
    REQ_AI_CHAT_MSG("req_ai_chat", ReqAiChatMsg.class),
    REQ_SINGLE_CHAT_MSG("req_single_chat", ReqSingleChatMsg.class),

    RESP_HB_MSG("resp_hb", RespHbMsg.class),
    RESP_ERROR_MSG("resp_error", RespErrorMsg.class),
    RESP_CONN_MSG("resp_conn", RespConnMsg.class),
    RESP_AI_CHAT_MSG("resp_ai_chat", RespAiChatMsg.class),
    RESP_SINGLE_CHAT_MSG("resp_single_chat", RespSingleChatMsg.class),
    ;

    private final String type;
    private final Class<? extends WebsocketMsg> aClass;


    WsMsgTypeEnum(String type, Class<? extends WebsocketMsg> aClass) {
        this.type = type;
        this.aClass = aClass;
    }

    public String getType() {
        return type;
    }

    public Class<? extends WebsocketMsg> getaClass() {
        return aClass;
    }

    public static WsMsgTypeEnum getByType(String type) {
        if (!StringUtils.isBlank(type)) {
            for (WsMsgTypeEnum anEnum : values()) {
                if (StringUtils.equals(type, anEnum.getType())) {
                    return anEnum;
                }
            }
        }
        return null;
    }
    }
