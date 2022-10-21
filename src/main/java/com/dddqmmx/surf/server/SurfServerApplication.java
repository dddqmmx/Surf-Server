package com.dddqmmx.surf.server;

import com.dddqmmx.surf.server.socket.tcp.TCPServer;
import com.dddqmmx.surf.server.socket.udp.UDPServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SurfServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SurfServerApplication.class, args);
        TCPServer tcpServer = new TCPServer();
        tcpServer.start();
        UDPServer udpServer = new UDPServer();
        udpServer.start();

    }

}
