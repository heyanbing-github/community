package com.jpx.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

    public static String digest16(String instr){
        return digest(instr,16);
    }

    public static String digest(String instr){
        return digest(instr,32);
    }

    private static String digest(String instr,int rang){
        MessageDigest md5 = null;
        if(StringUtils.isNull(instr)){
            return "";
        }
        try {
            md5 = MessageDigest.getInstance("MD5"); //取得算法
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
        char[] chars = instr.toCharArray();
        byte[] bytes = new byte[chars.length];
        for(int i=0;i<chars.length;i++){
            bytes[i] = (byte) chars[i];
        }
        byte[] md5Bytes = md5.digest(bytes); //加密
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<md5Bytes.length;i++){
            int val = ((int)md5Bytes[i]) & 0xff;
            if(val<16){
                sb.append("0");
            }
            sb.append(Integer.toHexString(val));
        }
        if(rang == 32){
            return sb.toString();
        }else{
            return sb.toString().substring(8,24);
        }

    }

    public static void main(String[] args) {

        System.out.println(digest16("111"));
        System.out.println(digest16("222"));
    }

}
