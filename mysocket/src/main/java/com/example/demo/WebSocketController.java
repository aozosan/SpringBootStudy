package com.example.demo;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class WebSocketController {
	// /app/greetに来たメッセージを変換して/topic/greetingsに送信する。
	@MessageMapping("/greet")
	@SendTo("/topic/greetings")
	public String greet(String name) {
		log.info("received {}", name);
		return "Hello " + name;
	}
}
