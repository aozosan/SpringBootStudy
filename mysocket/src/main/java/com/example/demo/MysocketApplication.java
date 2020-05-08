package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@SpringBootApplication
@RestController
public class MysocketApplication {
	protected final static Logger log = LoggerFactory.getLogger(MysocketApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(MysocketApplication.class, args);
	}

	@Configuration
	@EnableWebSocketMessageBroker
	private static class StompConfig implements WebSocketMessageBrokerConfigurer {

		@Override
		public void registerStompEndpoints(StompEndpointRegistry registry) {
			// WebSocket�̐ڑ���B
			registry.addEndpoint("endpoint");
		}

		@Override
		public void configureMessageBroker(MessageBrokerRegistry registry) {
			registry.setApplicationDestinationPrefixes("/app"); // Controller�ɏ��������鈶���Prefix
			registry.enableSimpleBroker("/topic"); // queue�܂���topic��L���ɂ���(������)�Bqueue��1��1(P2P)�Atopic��1�Α�(Pub-Sub)
		}
	}

	@MessageMapping(value = "/greet" /* ���於 */) // Controller����@MessageMapping�A�m�e�[�V�������������\�b�h���A���b�Z�[�W���󂯕t����
	@SendTo(value = "/topic/greetings") // �������ʂ̑����
	String greet(String name) {
		log.info("received {}", name);
		return "Hello " + name;
	}
}
