package com.dddqmmx.surf.server.socket.connect;

import java.util.HashMap;
import java.util.Map;

public class SocketSession {

    private final Map<Object,Object> sessionMap;

    public SocketSession() {
        sessionMap = new HashMap<>();
    }

    public void set(Object key, Object value){
        sessionMap.put(key, value);
    }
    public Object get(Object key){
        return sessionMap.get(key);
    }
}
