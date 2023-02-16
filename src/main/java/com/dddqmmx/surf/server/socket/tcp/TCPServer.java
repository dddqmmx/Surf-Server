package com.dddqmmx.surf.server.socket.tcp;

import com.dddqmmx.surf.server.socket.connect.Connect;
import com.dddqmmx.surf.server.socket.connect.ConnectList;
import com.dddqmmx.surf.server.socket.connect.SocketSession;
import com.dddqmmx.surf.server.util.RandomCharacters;

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
        //定义一个循环不断接受客户端的连接请求
        while (true) {
            Socket accept = null;
            try {
                accept = serverSocket.accept();
                String sessionId;
                sessionId = RandomCharacters.random(32);
                while (ConnectList.connectList.containsKey(sessionId)) {
                    sessionId = RandomCharacters.random(32);
                }
                TCPServerThread tcpServerThread = new TCPServerThread(accept,sessionId);
                tcpServerThread.start();
                SocketSession socketSession = new SocketSession();
                ConnectList.sessionList.put(sessionId,socketSession);
                Connect connect = new Connect();
                connect.setTcpServerThread(tcpServerThread);
                ConnectList.connectList.put(sessionId, connect);
                System.out.println(sessionId);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
