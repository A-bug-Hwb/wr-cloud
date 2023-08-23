package com.wr.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.HttpHandler;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Configuration
public class HttpServer {

    @Autowired
    private HttpHandler httpHandler;

    private WebServer webServer;

    @Value("${http.port}")
    private Integer httpPort;

    @PostConstruct
    public void start() {
        NettyReactiveWebServerFactory factory = new NettyReactiveWebServerFactory(httpPort);
        WebServer webServer = factory.getWebServer(httpHandler);
        webServer.start();
    }

    @PreDestroy
    public void stop() {
        webServer.stop();
    }

}
