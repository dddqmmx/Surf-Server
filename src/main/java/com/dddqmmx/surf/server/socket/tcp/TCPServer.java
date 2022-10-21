package com.dddqmmx.surf.server.socket.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer extends Thread{

    public int port = 2042;

    @Override
    public void run() {
        System.out.println("--------TCP服务端启动--------");
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //2.定义一个循环不断接受客户端的连接请求
        while (true) {
            //3.开始等待接受客户端的Socket管道连接
            Socket accept = null;
            try {
                accept = serverSocket.accept();
                new TCPServerThread(accept).start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
