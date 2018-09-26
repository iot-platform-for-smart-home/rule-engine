package com.tjlcast.server;

import com.tjlcast.server.common.Constant;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * Created by tangjialiang on 2018/5/24.
 */
@Configuration
@EnableWebSocketMessageBroker
public class ConfigSocket extends AbstractWebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(Constant.SOCKET_METRIC_ENDPOINT).setAllowedOrigins("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes(Constant.SOCKET_METRIC_APP);
        registry.enableSimpleBroker(Constant.SOCKET_METRIC_RESPONSE) ;
    }
}
