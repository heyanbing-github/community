package com.jpx.controller;

import com.jpx.utils.ClassUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;

@WebServlet(name = "BaseController")
public class BaseController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//访问注册界面   /weibo/user/reg   /weibo/user
        String uri = request.getRequestURI();
        System.out.println(uri);
        //获取处理后的uri
        uri = uri.substring(uri.lastIndexOf("/")+1);

        try {
            //通过反射找到方法
            Method method = getClass().getDeclaredMethod(uri,HttpServletRequest.class,HttpServletResponse.class);
            //因为方法是私有的 需要配置可以访问
            method.setAccessible(true);
            //执行这个方法
            method.invoke(this,request,response);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    /**
     * 反射解析参数为实体
     * @param request
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T resolveEntity(HttpServletRequest request,Class<T> clazz) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Enumeration<String> enums = request.getParameterNames();
        T t = clazz.newInstance();
        while(enums.hasMoreElements()){
            String name = enums.nextElement();//获取到每一个参数名
            Method method = ClassUtils.getSetMethod(clazz,name);
            if(method != null){
                //参数值获取到
                String value = request.getParameter(name);
                //先获取一下属性
                Field field = ClassUtils.getField(clazz,name);
                if(field != null){
                    if(field.getType() == Integer.class || field.getType() == int.class ){
                        method.invoke(t,Integer.parseInt(value));
                    } else if(field.getType() == String.class){
                        method.invoke(t,value);
                    } else if(field.getType() == Double.class || field.getType() == double.class){
                        method.invoke(t,Double.parseDouble(value));
                    }
                }

            }
        }
        return t;
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
