package com.yifan.admin.api.model.xunfei;

import com.yifan.admin.api.tool.FunctionDefinition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author https://www.unfbx.com/
 */
@Data
public class InPayload {
    private InMessage message;
    private InFunction functions;

    public InPayload(InMessage message, InFunction functions) {
        this.message = message;
        this.functions = functions;
    }

    public InPayload(InMessage message) {
        this.message = message;
    }

    public InPayload() {
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class InMessage {
        private List<Message> text;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class InFunction {
        private List<FunctionDefinition> text;
    }
}
