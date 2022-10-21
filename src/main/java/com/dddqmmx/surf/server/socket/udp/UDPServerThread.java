package com.dddqmmx.surf.server.socket.udp;

import java.net.DatagramPacket;
import java.net.InetAddress;

public class UDPServerThread extends Thread {


    //前台传过来的信息
    DatagramPacket datagramPacket = null;
    String info = null;
    InetAddress inetAddress = null;
    int port = 0;

    //构造方法对UDPServer传进来的消息进行赋值
    public UDPServerThread(DatagramPacket packet, String info, InetAddress inetAddress, int port) {
        this.datagramPacket = packet;
        this.info = info;
        this.inetAddress = inetAddress;
        this.port = port;
    }

    //具体地处理逻辑
    @Override
    public void run() {
        String host = inetAddress.getHostAddress();
        System.out.println("UDP : "+info);
        /*JSONObject jsonObject = new JSONObject(info);
        //获取客户端提交json的命令
        String command = jsonObject.getString("command");
        if (command.equals("setIpPort")){
            int setPort = jsonObject.getInt("port");
            String session = RandomCharacters.random(50);
            while (ConnectList.hasSession(host,session)) {
                session = RandomCharacters.random(50);
            }
            ConnectList.setPost(host,session,setPort);
            JSONObject returnJson = new JSONObject();
            returnJson.put("command","setSession");
            returnJson.put("session",session);
            send(returnJson.toString());
        }*/
    }
}