package com.dddqmmx.surf.server.socket.connect;

import com.dddqmmx.surf.server.socket.tcp.TCPServerThread;

import java.util.HashMap;
import java.util.Map;

public class ConnectList {
    //String : session id ; Connect : 对应Connect;
    public static Map<String,Connect> connectList = new HashMap<>();
    //String : session id ; Connect : SocketSession;
    public static Map<String,SocketSession> sessionList = new HashMap<>();
    public static Map<Integer,String> userSession = new HashMap<>();
}
