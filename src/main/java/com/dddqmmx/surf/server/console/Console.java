package com.dddqmmx.surf.server.console;

import com.dddqmmx.surf.server.socket.connect.ConnectList;

import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Console extends Thread{
    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String command = scanner.nextLine();
            switch (command){
                case "userSessionList":
                    Set<Map.Entry<Integer, String>> entrySet = ConnectList.userSessionMap.entrySet();
                    for (Map.Entry<Integer, String> e : entrySet){
                        //entry表示一对键和值，同样提供了获取对应键和值的方法
                        //获取entry对象中的键
                        Integer key= e.getKey();
                        //获取entry对象中的value值
                        String value = e.getValue();
                        System.out.println(key+value);
                    }
                    break;
                default:
                    System.out.println("未知指令");
            }
        }
    }
}
