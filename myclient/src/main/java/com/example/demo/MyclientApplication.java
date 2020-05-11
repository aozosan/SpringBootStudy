package com.example.demo;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.net.ssl.SSLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@RestController
@SpringBootApplication
public class MyclientApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyclientApplication.class, args);
	}

	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping("/1")
	public String get1() {
		HttpHeaders headers = new HttpHeaders();
		setBasicAuthHeader(headers, "hello", "hello");
		HttpEntity<String> entity = new HttpEntity<String>(headers);

		ResponseEntity<String> response = this.restTemplate.exchange("https://localhost:9000/hello", HttpMethod.GET,
				entity, String.class);
		return response.getBody();
	}

	@RequestMapping("/2")
	public Mono<String> get2() throws SSLException {
		SslContext sslContext = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE)
				.build();

		HttpClient httpClient = HttpClient.create().secure(sslContextSpec -> sslContextSpec.sslContext(sslContext));
		ClientHttpConnector httpConnector = new ReactorClientHttpConnector(httpClient);
		WebClient webClient = WebClient.builder().clientConnector(httpConnector).build();

		return webClient.get().uri("https://localhost:9000/hello")
				.headers(headers -> headers.setBasicAuth("hello", "hello")).retrieve().bodyToMono(String.class);
	}

	@Autowired
	private WebClient.Builder webClientBuilder;

	@RequestMapping("/3")
	public Mono<String> get3() throws SSLException {
		// return this.webClientBuilder.build().get().uri("https://localhost:9000/hello")
		return this.webClientBuilder.build().get().uri("https://MYSERVICE/hello")
				.headers(headers -> headers.setBasicAuth("hello", "hello")).retrieve().bodyToMono(String.class);
	}

	private void setBasicAuthHeader(HttpHeaders headers, String user, String password) {
		String base64Creds = Base64.getEncoder()
				.encodeToString((user + ":" + password).getBytes(StandardCharsets.UTF_8));
		headers.add("Authorization", "Basic " + base64Creds);
	}
}
