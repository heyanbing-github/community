package com.jpx.filter;

import com.alibaba.fastjson.JSON;
import com.jpx.dto.Result;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

@WebFilter(value = "/*",initParams = {
        @WebInitParam(name="login",value = "/show/index.html,/show/html/addGoods.html,/show/html/adminInfo.html," +
                "/show/html/adminManagerGoods.html,/show/html/adminManagerUser.html,/show/html/buyGoods.html," +
                "/show/html/change_message.html,/show/html/changePassword.html,/show/html/doNotice.html," +
                "/show/html/lanjie.html,/show/html/managerFile.html,/show/html/managerText.html," +
                "/show/html/teamIntroduce.html,/show/html/userInfo.html,/show/html/userSpread.html," +
                "/show/html/versionNnumber.html,/show/html/waitCheck.html,"),

        @WebInitParam(name="url",value =
                "/show/html/buyGoods.html," +
                "/show/html/change_message.html," +
                "/show/html/changePassword.html," +
                "/show/html/userInfo.html," +
                        "/show/html/managerFile.html," +
                        "/show/html/managerText.html,"
        ),

        @WebInitParam(name="adminUrl",value =
                        "/show/html/addGoods.html," +
                        "/show/html/adminInfo.html," +
                        "/show/html/adminManagerGoods.html," +
                        "/show/html/adminManagerUser.html," +
                                "/show/html/doNotice.html," +
                "/show/html/waitCheck.html,"),
})
public class LoginFilter implements Filter {

    private List<String> logins;
    private List<String> urls;
    private List<String> adminUrls;

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        String uri = request.getRequestURI(); //
        uri = uri.substring(request.getContextPath().length());

        if(logins.contains(uri)){//拦截登录
            Object obj = request.getSession().getAttribute("C_USER");
            Object obj2 = request.getSession().getAttribute("C_ADMIN");
            if(obj == null && obj2==null){
                response.sendRedirect("/community/show/html/login.html");
                return;
            }
        }

        if(urls.contains(uri)){//拦截用户
            Object obj = request.getSession().getAttribute("C_USER");
            if(obj == null){
                response.sendRedirect("/community/show/html/lanjie.html");
                return;
            }

        }

        if(adminUrls.contains(uri)){//拦截管理员
            Object obj = request.getSession().getAttribute("C_ADMIN");
            if(obj == null){
                response.sendRedirect("/community/show/html/lanjie.html");
                return;
            }
        }
        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {
        String url = config.getInitParameter("url");
        urls = Arrays.asList(url.split(","));

        String adminUrl = config.getInitParameter("adminUrl");
        adminUrls = Arrays.asList(adminUrl.split(","));

        String login = config.getInitParameter("login");
        logins = Arrays.asList(login.split(","));

    }

}
