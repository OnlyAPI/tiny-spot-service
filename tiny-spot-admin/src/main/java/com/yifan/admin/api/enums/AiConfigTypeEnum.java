package com.yifan.admin.api.enums;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/11/13 11:30
 */
public enum AiConfigTypeEnum {
    CHAT_XUNFEI("chat-xunfei", "讯飞星火-聊天模型"),

    CHAT_BAIDU("chat-baidu", "百度-ERNIE-Bot-聊天模型"),

    CHAT_ALIYUN("chat-aliyun", "阿里云-通义千问-聊天模型"),

    CHAT_SKYLARK("chat-skylark", "字节-云雀-聊天模型"),

    CHAT_BAICHUAN("chat-baichuan", "百川-聊天模型"),

    TXT2IMG_BAIDU("txt2img-baidu", "文生图-百度-Stable-Diffusion-XL-生图模型")
    ;

    private final String name;

    private final String desc;


    AiConfigTypeEnum(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public static AiConfigTypeEnum fromName(String name) {
        for (AiConfigTypeEnum aiConfigTypeEnum : AiConfigTypeEnum.values()) {
            if (aiConfigTypeEnum.getName().equals(name)) {
                return aiConfigTypeEnum;
            }
        }
        return null;
    }
}
