package com.dddqmmx.surf.server.util;

import java.util.Random;

public class RandomCharacters {
    public static String random(int length){
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(3);
            long result;
            switch(number){
                case 0:
                    result=Math.round(Math.random()*25+65);
                    sb.append((char) result);
                    break;
                case 1:
                    result=Math.round(Math.random()*25+97);
                    sb.append((char) result);
                    break;
                case 2:
                    sb.append(new Random().nextInt(10));
                    break;
            }
        }
        return sb.toString();
    }
}
