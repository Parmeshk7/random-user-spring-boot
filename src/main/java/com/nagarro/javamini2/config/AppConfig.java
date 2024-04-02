package com.nagarro.javamini2.config;

import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class AppConfig {

    @Bean
    public WebClient userApiClient(){
        return WebClient.builder().baseUrl("https://randomuser.me/api/").clientConnector(new ReactorClientHttpConnector(HttpClient.newConnection()
                .responseTimeout(Duration.ofMillis(2000))
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(2000, TimeUnit.MILLISECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(2000, TimeUnit.MILLISECONDS))
                )
        )).build();
    }

    @Bean
    public WebClient nationalityApiClient() {
        return WebClient.builder().baseUrl("https://api.nationalize.io/").clientConnector(new ReactorClientHttpConnector(HttpClient.newConnection()
                .responseTimeout(Duration.ofMillis(1000))
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(1000, TimeUnit.MILLISECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(1000, TimeUnit.MILLISECONDS))

                )

        )).build();
    }

    @Bean
    public WebClient genderApiClient() {
        return WebClient.builder().baseUrl("https://api.genderize.io/").clientConnector(new ReactorClientHttpConnector(HttpClient.newConnection()
                .responseTimeout(Duration.ofMillis(1000))
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(1000, TimeUnit.MILLISECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(1000, TimeUnit.MILLISECONDS))
                )
        )).build();
    }

}
