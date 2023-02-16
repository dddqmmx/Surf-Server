package com.dddqmmx.surf.server.socket.connect;

import com.dddqmmx.surf.server.socket.tcp.TCPServerThread;
import com.dddqmmx.surf.server.socket.udp.UDPServer;

public class Connect {

    private TCPServerThread tcpServerThread;

    //没做udp这只是个摆设
    private UDPServer udpServer;

    public TCPServerThread getTcpServerThread() {
        return tcpServerThread;
    }
    public void setTcpServerThread(TCPServerThread tcpServerThread) {
        this.tcpServerThread = tcpServerThread;
    }

    public UDPServer getUdpServer() {
        return udpServer;
    }

    public void setUdpServer(UDPServer udpServer) {
        this.udpServer = udpServer;
    }
}
