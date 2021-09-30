package com.north.base.configuration;

import com.north.websocket.handler.HardwareInfoWebSocketHandler;
import com.north.websocket.handler.NotificationWebSocketHandler;
import com.north.websocket.handler.SpringWebSocketHandlerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * @author Northzx
 * @version 1.0
 * @since 2021-03-15
 */
@EnableWebSocket
@Configuration
public class WebSocketConfiguration implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry
                .addHandler(new HardwareInfoWebSocketHandler(), "/hardwareInfo")
                .addInterceptors(new SpringWebSocketHandlerInterceptor()).setAllowedOrigins("*")
                .addHandler(new NotificationWebSocketHandler(), "/notificationMessage")
                .addInterceptors(new SpringWebSocketHandlerInterceptor()).setAllowedOrigins("*");
    }

}
