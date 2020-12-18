package com.jpx.controller;


import com.alibaba.fastjson.JSON;
import com.jpx.dto.Result;
import com.jpx.service.EmailService;
import com.jpx.service.impl.EmailServiceImpl;
import com.jpx.utils.SecretKeyUtils;

import javax.jws.WebService;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

@WebServlet("/email/*")
public class EmailController extends BaseController{

    EmailService emailService = new EmailServiceImpl();

    /**
     * 找回密码，发送邮件
     * @param request
     * @param response
     * @throws MessagingException
     * @throws ServletException
     * @throws IOException
     */
    public void emailFind(HttpServletRequest request, HttpServletResponse response) throws MessagingException, ServletException, IOException {
       //要找回的账号
        String username = request.getParameter("username");
        //接收页面传过来的邮箱号
        String toEmail    = request.getParameter("email");
        //系统用来发送邮件给用户验证的邮箱号
        String myEmail = "819837460@qq.com";
        //指定发送邮件的主机为smtp.qq.com
        String host = "smtp.qq.com";//qq邮件服务器
        // 获取系统属性
        Properties properties = System.getProperties();
        // 设置邮件服务器
        properties.setProperty("mail.smtp.host", host);
        properties.put("mail.smtp.auth", "true");
        //获取密钥
        String secretKey = SecretKeyUtils.secretKey(username);
        System.out.println(secretKey);
        // 获取默认session对象
        HttpSession session_key = request.getSession();
        session_key.setAttribute("key",secretKey);
        session_key.setAttribute("username",username);
        Session session = Session.getDefaultInstance(properties,new Authenticator(){
            public PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication("819837460@qq.com", "vjefwbvhnswfbeii");
                //发件人邮件用户名、授权码（授权码要与QQ邮箱相对应，可以从邮箱设置里面获得，详细步骤在博客开头）
            }
        });
        // 创建默认的 MimeMessage 对象
        MimeMessage message = new MimeMessage(session);
        // Set myEmail: 头部头字段
        message.setFrom(new InternetAddress(myEmail));
        // Set toEmail: 头部头字段
        message.addRecipient(Message.RecipientType.TO,
                new InternetAddress(toEmail));
        // Set Subject: 头部头字段
        message.setSubject("This is the Subject Line!");
        // 设置消息体
        //message.setText("您的员工管理系统，密码是："+);

        String msgContent = "<!DOCTYPE html>"
                +"<html>"
                +"<head>"
                +"<meta charset='utf-8' />"
                +"<title>欢迎使用员工管理系统</title>"
                +"</head>"
                +"<body>"
                +"亲爱的会员 ，您好，"
                +"<br/><br/> "
                +"您在"
                + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date())
                +"提交找回密码的请求。"
                +"<br/><br/>"
                +"以下是您的密钥："
                +"<br/><br/>"
                + "密钥："+secretKey
                +"<br/> <br/>"
                +"感谢您使用本系统。"
                +"<br/>"
                +"此为自动发送邮件，请勿直接回复！"
                +"</body>"
                +"</html>";
        message.setContent(msgContent, "text/html;charset=utf-8");
        // 发送消息
        Transport.send(message);
        Result result = new Result();
        result.setMsg("发送成功，请注意查收！");
        result.setState(Result.SUCCESS);
        response.getWriter().write(JSON.toJSONString(result));
       // request.setAttribute("MSG3", "发送成功，请注意查收！");
        //转发
       // request.getRequestDispatcher("verify.html").forward(request, response);
    }

    public void emailVerify(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String verify = request.getParameter("verify");
        System.out.println(verify);
        String secretKey = (String) request.getSession().getAttribute("key");
        System.out.println(secretKey);
        Result result = new Result();
        if (verify.equals(secretKey)){
            //转发
           // request.getRequestDispatcher("changePassword.html").forward(request, response);

            result.setMsg("密钥正确");
            result.setState(Result.SUCCESS);
        }else{
            result.setMsg("密钥错误");
            result.setState(Result.ERROR);
        }
        response.getWriter().write(JSON.toJSONString(result));
    }

    public void changeNewPassword(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String newPassword = request.getParameter("newpassword_2");
        System.out.println(newPassword);
        String username = (String) request.getSession().getAttribute("username");
        System.out.println(username);
        boolean isSeccss = emailService.changePassword(username,newPassword);
        System.out.println(isSeccss);
        Result result = new Result();
        if (isSeccss){
            result.setState(Result.SUCCESS);
            result.setMsg("更改成功");
        }else{
            result.setState(Result.ERROR);
            result.setMsg("更改失败");
        }
        response.getWriter().write(JSON.toJSONString(result));
    }

}
