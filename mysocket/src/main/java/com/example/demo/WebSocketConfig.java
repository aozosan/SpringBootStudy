package com.example.demo;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		// WebSocketの接続先。
		registry.addEndpoint("endpoint");
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		// Controllerに処理させる宛先のPrefixを指定する。
		registry.setApplicationDestinationPrefixes("/app");
		// topic(1対多(Pub-Sub))を指定する。
		registry.enableSimpleBroker("/topic");
	}
}
