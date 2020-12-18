package com.jpx.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class StringUtils {


    private static String str = "abcdefghijkmnopqrstuvwxyzABCDEFGHIJKLMNPQRSTUVWXYZ0123456789";

    /**
     * 获取一个指定长度的随机字符串
     * @param len
     * @return
     */
    public static String randomString(int len){
        StringBuilder sb = new StringBuilder();
        for (int i = 0 ; i < len;i++){
            int result = (int) (Math.random()*str.length());
            sb.append(str.charAt(result));
        }
        return sb.toString();
    }

    /**
     * 获取当前时间
     * @return
     */
    public static  String getCurrentTime(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    /**
     * 随机生成一个uuid
     * @return
     */
    public static String randomUUID(){
        return UUID.randomUUID().toString();
    }

    /**
     * 判断字符串是否为空
     * @param str
     * @return
     */
    public static boolean isNotNull(String str){
        return str != null && !"".equals(str);
    }

    /**
     * 产生纯数字的账号
     * @return
     */
    public static Integer getUUIDInOrderId(){
        Integer orderId=UUID.randomUUID().toString().hashCode();
        orderId = orderId < 0 ? -orderId : orderId; //String.hashCode() 值会为空
        return orderId;
    }

    /**
     * 获取到具体哪一天
     * @return
     */
    public static  String getDate(){
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    public static boolean isNull(String instr) {
        return instr == null || "".equals(instr);
    }
}
