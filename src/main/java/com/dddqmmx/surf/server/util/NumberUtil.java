package com.dddqmmx.surf.server.util;

public class NumberUtil {
    public static boolean isNumeric(String str){
        for (int i = 0; i < str.length(); i++){
            System.out.println(str.charAt(i));
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }

    private static int toInt(byte[] bytes) {
        int result = 0;
        for (int i = 0; i < 4; i++) {
            result <<= 8;
            result |= bytes[i] & 0xFF;
        }
        return result;
    }

}
