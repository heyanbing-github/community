package com.jpx.utils;

import java.util.UUID;

public class SecretKeyUtils {

    public static String secretKey(String username){
       String uuid =  UUID.randomUUID().toString();
       String secretKey = uuid + username;
        return secretKey;
    }
}
