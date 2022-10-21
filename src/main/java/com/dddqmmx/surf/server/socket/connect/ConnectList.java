package com.dddqmmx.surf.server.socket.connect;

import java.util.HashMap;
import java.util.Map;

public class ConnectList {
    private static final Map<String,SocketSession> socketSessionMap = new HashMap<>();
    private static final Map<String,Connect> connectMap = new HashMap<>();
    private static final Map<String,Thread> threadMap = new HashMap<>();

    public static boolean hasSessionId(String sessionId){
        return socketSessionMap.containsKey(sessionId)&&connectMap.containsKey(sessionId);
    }

    public static void setSocketSession(String sessionId,SocketSession socketSession){
        socketSessionMap.put(sessionId,socketSession);
    }

    public static void setConnectMap(String sessionId,Connect connect){
        connectMap.put(sessionId, connect);
    }

    public static void setThread(String sessionId, Thread thread) {
        threadMap.put(sessionId,thread);
    }

    public static Thread getThread(String sessionId){
        return threadMap.get(sessionId);
    }

    public static SocketSession getSocketSession(String sessionId){
        return socketSessionMap.get(sessionId);
    }


}
