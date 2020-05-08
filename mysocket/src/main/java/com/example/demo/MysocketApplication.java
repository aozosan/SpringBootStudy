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
			// WebSocketの接続先。
			registry.addEndpoint("endpoint");
		}

		@Override
		public void configureMessageBroker(MessageBrokerRegistry registry) {
			registry.setApplicationDestinationPrefixes("/app"); // Controllerに処理させる宛先のPrefix
			registry.enableSimpleBroker("/topic"); // queueまたはtopicを有効にする(両方可)。queueは1対1(P2P)、topicは1対多(Pub-Sub)
		}
	}

	@MessageMapping(value = "/greet" /* 宛先名 */) // Controller内の@MessageMappingアノテーションをつけたメソッドが、メッセージを受け付ける
	@SendTo(value = "/topic/greetings") // 処理結果の送り先
	String greet(String name) {
		log.info("received {}", name);
		return "Hello " + name;
	}
}
