package com.tjlcast.updateMessagePlugin;

import com.tjlcast.updateMessagePlugin.common.Constant;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker
public class ConfigSocket extends AbstractWebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(Constant.SOCKET_UPDATEMESSAGE_ENDPOINT).setAllowedOrigins("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes(Constant.SOCKET_UPDATEMESSAGE_APP);
        registry.enableSimpleBroker(Constant.SOCKET_UPDATEMESSAGE_RESPONSE) ;
    }
}
