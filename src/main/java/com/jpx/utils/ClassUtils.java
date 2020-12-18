package com.jpx.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Description
 * @ClassName ClassUtils
 * @Author KingLee
 * @date 2020.07.22 14:18
 */
public class ClassUtils {
    /**
     * 从一个类中去获取对应名字的字段
     * @param clazz
     * @param fieldName
     * @return 如果不存在返回null
     */
    public static Field getField(Class clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            return null;
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过名字获取对应的set方法
     * @param clazz
     * @param name
     * @return
     * @throws NoSuchMethodException
     */
    public static Method getSetMethod(Class clazz, String name) throws NoSuchMethodException {
        Field field = getField(clazz,name);
        if(field == null){
            return null;
        }
        String first = name.charAt(0) + "";
        //获取set方法的名字
        String methodName = "set"+ first.toUpperCase() + name.substring(1);
        Method method = clazz.getDeclaredMethod(methodName,field.getType());
        return method;
    }

    /**
     * 通过反射执行一个方法
     * @param method
     * @param target
     * @param value
     */
    public static void invokeMethod (Method method,Object target,Object value) throws InvocationTargetException, IllegalAccessException {

        Class [] types = method.getParameterTypes();

            if(types[0] == Integer.class || types[0] == int.class ){
                method.invoke(target,Integer.parseInt(value + ""));
            } else if(types[0] == String.class){
                method.invoke(target,value);
            } else if(types[0] == Double.class || types[0] == double.class){
                method.invoke(target,Double.parseDouble(value + ""));
            }
    }

}
