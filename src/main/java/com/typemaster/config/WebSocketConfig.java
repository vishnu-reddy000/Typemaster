package com.typemaster.config;

import com.typemaster.websocket.ResultWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * Spring Boot WebSocket configuration registering /ws/results endpoint.
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final ResultWebSocketHandler resultWebSocketHandler;

    @Autowired
    public WebSocketConfig(ResultWebSocketHandler resultWebSocketHandler) {
        this.resultWebSocketHandler = resultWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(resultWebSocketHandler, "/ws/results")
                .setAllowedOrigins("*");
    }
}
