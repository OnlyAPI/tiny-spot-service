package com.yifan.admin.api.model.xunfei;

import com.yifan.admin.api.utils.XunFeiAuthUtils;
import com.yifan.admin.api.webSocket.XunFeiWebSocketListener;
import lombok.Data;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

/**
 * 星火大模型客户端
 *
 * @author https://www.unfbx.com/
 */
@Data
public class SparkDeskClient {

    private String host;
    private String appid;
    private String apiKey;
    private String apiSecret;
    private OkHttpClient okHttpClient;


    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String host;
        private String appid;
        private String apiKey;
        private String apiSecret;
        private OkHttpClient okHttpClient;

        private Builder() {
        }

        public static Builder aSparkDeskClient() {
            return new Builder();
        }

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder appid(String appid) {
            this.appid = appid;
            return this;
        }

        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder apiSecret(String apiSecret) {
            this.apiSecret = apiSecret;
            return this;
        }

        public Builder okHttpClient(OkHttpClient okHttpClient) {
            this.okHttpClient = okHttpClient;
            return this;
        }

        public SparkDeskClient build() {
            SparkDeskClient sparkDeskClient = new SparkDeskClient();
            sparkDeskClient.host = this.host;
            this.okHttpClient = new OkHttpClient.Builder().build();
            sparkDeskClient.okHttpClient = this.okHttpClient;
            sparkDeskClient.apiKey = this.apiKey;
            sparkDeskClient.appid = this.appid;
            sparkDeskClient.apiSecret = this.apiSecret;
            return sparkDeskClient;
        }
    }

    @SneakyThrows
    public <T extends XunFeiWebSocketListener> WebSocket chat(T chatListener) {
        String authUrl = XunFeiAuthUtils.getAuthUrl(host, apiKey, apiSecret);
        String url = authUrl.replace("http://", "ws://").replace("https://", "wss://");
        Request request = new Request.Builder().url(url).build();
        return this.okHttpClient.newWebSocket(request, chatListener);
    }
}
