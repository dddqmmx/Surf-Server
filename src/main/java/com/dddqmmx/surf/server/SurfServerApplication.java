package com.dddqmmx.surf.server;

import com.dddqmmx.surf.server.console.Console;
import com.dddqmmx.surf.server.socket.tcp.TCPServer;
import com.dddqmmx.surf.server.socket.udp.UDPServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

@SpringBootApplication
public class SurfServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SurfServerApplication.class, args);
/*        try {
            String encode = Base64.getEncoder().encodeToString(Files.readAllBytes(Paths.get("surf/image/watame.jpg")));
            System.out.println(encode);
            System.out.println(encode.length());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        new File("surf/image").mkdir();*/
        Console console = new Console();
        console.start();
        TCPServer tcpServer = new TCPServer();
        tcpServer.start();
        UDPServer udpServer = new UDPServer();
        udpServer.start();

    }

}
