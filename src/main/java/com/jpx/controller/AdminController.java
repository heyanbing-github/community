package com.jpx.controller;

import com.alibaba.fastjson.JSON;
import com.jpx.dto.Pager;
import com.jpx.dto.Result;
import com.jpx.dto.TextDto;
import com.jpx.dto.UserDto;
import com.jpx.entity.*;
import com.jpx.service.AdminService;
import com.jpx.service.impl.AdminServiceImpl;
import com.jpx.utils.ClassUtils;
import com.jpx.utils.StringUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

@WebServlet("/admin/*")
public class AdminController extends BaseController {

    protected AdminService adminService=new AdminServiceImpl();

    /**
     * 获取所有用户信息和积分
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void manageUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String pageNow = request.getParameter("pageNow");
        String pageSize = request.getParameter("pageSize");
        if (!StringUtils.isNotNull(pageNow)){
            pageNow = "1";
        }
        if (!StringUtils.isNotNull(pageSize)){
            pageSize = "10";
        }
        Pager<UserDto> pager = new Pager<>();

        pager.setPageNow(Integer.parseInt(pageNow));
        pager.setPageSize(Integer.parseInt(pageSize));


        pager = adminService.selectUserPager(pager);

        Result<Pager<UserDto>> result = new Result<>();
        result.setData(pager);
        if(pager.getData()==null){
            result.setState(Result.ERROR);
        }else {
            result.setState(Result.SUCCESS);
        }
        response.getWriter().write(JSON.toJSONString(result));
    }

    /**
     * 删除用户信息
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void deleteUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String uid = request.getParameter("uid");
        System.out.println(uid);
        boolean isSuccess=adminService.deleteUser(uid);
        Result result = new Result<>();
        System.out.println(isSuccess);
        if(isSuccess){
            result.setState(Result.SUCCESS);
        }
        response.getWriter().write(JSON.toJSONString(result));
    }

    protected void updateUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String uid = request.getParameter("uid");
        String username = request.getParameter("username");
        String nickName = request.getParameter("nickName");
        String email = request.getParameter("email");
        String sex = request.getParameter("sex");
        int score = Integer.parseInt(request.getParameter("score"));
        User user = new User();
        user.setUid(uid);
        user.setUsername(username);
        user.setNickName(nickName);
        user.setEmail(email);
        user.setSex(sex);
        boolean isSuccess=adminService.updateUser(user,score);
        //结果处理
        Result result = new Result<>();
        System.out.println(isSuccess);
        if(isSuccess){
            result.setState(Result.SUCCESS);
        }
        response.getWriter().write(JSON.toJSONString(result));
    }

    /**
     * 发送公告
     */
    protected void sendNotice(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        Admin c_admin = (Admin) request.getSession().getAttribute("C_ADMIN");
        String notice = request.getParameter("notice");
        boolean isSuccess = adminService.sendNotice(notice, c_admin);
        Result result = new Result();
        if (isSuccess){
            result.setState(Result.SUCCESS);
            result.setMsg("发送成功");
        }else{
            result.setState(Result.ERROR);
            result.setMsg("发送失败");
        }

        String json = JSON.toJSONString(result);
        response.getWriter().write(json);
    }

    /**
     * 获取最新的四条公告
     */
    protected void getNewNotice(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{


        List<Notice> lists = adminService.getFourNewNotice();

        Result result = new Result();
        result.setData(lists);

        String json = JSON.toJSONString(result);
        response.getWriter().write(json);
    }

    /**
     * 获取公告
     */
    protected void getNotice(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

        Result<Pager<Notice>> result = new Result<>();

        String pageNow = request.getParameter("pageNow");
        String pageSize = request.getParameter("pageSize");
        if (!StringUtils.isNotNull(pageNow)){
            pageNow = "1";
        }
        if (!StringUtils.isNotNull(pageSize)){
            pageSize = "10";
        }

        Pager<Notice> pager = new Pager<>();
        pager.setPageNow(Integer.parseInt(pageNow));
        pager.setPageSize(Integer.parseInt(pageSize));
        pager = adminService.selectNotice(pager);

        //该用户没有帖子数据
        if (pager.getTotalCount()==0){
            result.setState(Result.ERROR);
            result.setMsg("还没有公告");
            result.setData(pager);
        }else {
            result.setState(Result.SUCCESS);
            result.setMsg("查询成功");
            result.setData(pager);
        }

        response.getWriter().write(JSON.toJSONString(result));
    }

    /**
     * 删除公告
     */
    protected void deleteNotice(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String nid = request.getParameter("nid");
        boolean isSuccess = adminService.deleteNotice(nid);
        Result result = new Result();
        if (isSuccess){
            result.setState(Result.SUCCESS);
            result.setMsg("删除成功");
        }else{
            result.setState(Result.ERROR);
            result.setMsg("删除失败");
        }
        response.getWriter().write(JSON.toJSONString(result));
    }

    /**
     * 通过关机字搜索公告
     */
    protected void getNoticeByKey(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        Result<Pager<Notice>> result = new Result<>();

        String pageNow = request.getParameter("pageNow");
        String pageSize = request.getParameter("pageSize");
        String searchText = request.getParameter("searchText");

        if (!StringUtils.isNotNull(pageNow)){
            pageNow = "1";
        }
        if (!StringUtils.isNotNull(pageSize)){
            pageSize = "10";
        }

        Pager<Notice> pager = new Pager<>();
        pager.setPageNow(Integer.parseInt(pageNow));
        pager.setPageSize(Integer.parseInt(pageSize));
        pager = adminService.selectNoticeByKey(pager, searchText);

        //该用户没有帖子数据
        if (pager.getTotalCount()==0){
            result.setState(Result.ERROR);
            result.setMsg("没找到相关公告");
            result.setData(pager);
        }else {
            result.setState(Result.SUCCESS);
            result.setMsg("查询成功");
            result.setData(pager);
        }

        response.getWriter().write(JSON.toJSONString(result));
    }

    /**
     * 修改公告
     */
    protected void updateNotice(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String nid = request.getParameter("nid");
        String mid = request.getParameter("mid");
        String content = request.getParameter("content");

        boolean isSuccess = adminService.updateNotice(nid,mid,content);
        Result result = new Result();
        if (isSuccess){
            result.setState(Result.SUCCESS);
            result.setMsg("修改成功");
        }else{
            result.setState(Result.ERROR);
            result.setMsg("修改失败");
        }
        response.getWriter().write(JSON.toJSONString(result));
    }

    /**
     * 添加商品
     */
    protected void sendGoods(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, FileUploadException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Admin c_admin = (Admin) request.getSession().getAttribute("C_ADMIN");

        Goods goods = new Goods();
        goods.setAdmin(c_admin);

        FileItemFactory fileItemFactory = new DiskFileItemFactory();
        ServletFileUpload servletFileUpload = new ServletFileUpload(fileItemFactory);
        //所有的请求参数
        List<FileItem> fileItems = servletFileUpload.parseRequest(request);
        for (FileItem fileItem:fileItems
        ) {
            //普通元素
            if (fileItem.isFormField()){
                //获取参数名
                String name = fileItem.getFieldName();
                //获取参数值
                String value = fileItem.getString();
                //处理内容乱码
                value = new String(value.getBytes("ISO-8859-1"),"UTF-8");
                Method method =  ClassUtils.getSetMethod(Goods.class,name);
                if(method != null){
                    //执行以下set方法
                    ClassUtils.invokeMethod(method,goods,value);
                }
            }else{
                //获取字段名
                String name = fileItem.getFieldName();
                //文件名
                String fileName = fileItem.getName();
                //获取后缀，使用uuid重新给定文件名
                String suffx = fileName.substring(fileName.lastIndexOf("."));
                fileName = StringUtils.randomUUID() + suffx;

                //将文件写入磁盘保存
                String path = "E:\\communitydata\\images" + File.separator + fileName;
                FileUtils.copyInputStreamToFile(fileItem.getInputStream(), new File(path));

                goods.setGphoto(fileName);
            }
        }
        boolean isSuccess = adminService.sendGoods(goods);
        Result result = new Result();
        if (isSuccess){
            response.sendRedirect("/community/show/html/adminManagerGoods.html");
        }else{
        }
    }


    /**
     * 查询用户
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void searchUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

        String username = request.getParameter("search");

        Pager<UserDto> pager=adminService.searchUser(username);
        Result<Pager<UserDto>> result = new Result();
        if(pager==null){
            result.setState(Result.ERROR);
        }else {
            result.setState(Result.SUCCESS);
            result.setData(pager);
        }
        String json = JSON.toJSONString(result);
        response.getWriter().write(json);
    }


    /**
     * 查询出所有商品
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void selectGoods(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

        String pageNow = request.getParameter("pageNow");
        String pageSize = request.getParameter("pageSize");


        if (!StringUtils.isNotNull(pageNow)){
            pageNow = "1";
        }
        if (!StringUtils.isNotNull(pageSize)){
            pageSize = "5";
        }
        Pager<Goods> pager = new Pager<>();

        pager.setPageNow(Integer.parseInt(pageNow));
        pager.setPageSize(Integer.parseInt(pageSize));

        pager = adminService.selectGoods(pager);

        Result<Pager<Goods>> result = new Result<>();
        result.setData(pager);
        if(pager.getData()==null){
            result.setState(Result.ERROR);
        }else {
            result.setState(Result.SUCCESS);
        }
        response.getWriter().write(JSON.toJSONString(result));
    }


    /**
     * 修改商品属性
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void editGoods(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

        String gid = request.getParameter("gid");
        String price = request.getParameter("price");
        String score = request.getParameter("score");
        String count = request.getParameter("count");

        System.out.println(gid+price+score+count);
        //换成session的管理员
        String mid = request.getParameter("mid");

        boolean isSuccess=adminService.editGoods(gid,Integer.parseInt(price),Integer.parseInt(score),Integer.parseInt(count));
        //结果处理
        Result result = new Result<>();

        if(isSuccess){
            result.setState(Result.SUCCESS);
        }
        response.getWriter().write(JSON.toJSONString(result));
    }
    /**
     * 改变商品状态
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void updateGoods(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String gid = request.getParameter("gid");
        String mid = request.getParameter("mid");
        String state = request.getParameter("state");
        if(state==null||mid==null||gid==null){
            //相应的错误处理方式
        }
        boolean isSuccess= adminService.updateGoods(gid,Integer.parseInt(state),mid);
        Result result = new Result<>();
        if(isSuccess){
            result.setState(Result.SUCCESS);
        }else {
            result.setState(Result.ERROR);
        }
        response.getWriter().write(JSON.toJSONString(result));
    }

    /**
     * 搜索商品
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void searchGoods(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

        String gname = request.getParameter("search");

        String pageNow = request.getParameter("pageNow");
        String pageSize = request.getParameter("pageSize");

        if (!StringUtils.isNotNull(pageNow)){
            pageNow = "1";
        }
        if (!StringUtils.isNotNull(pageSize)){
            pageSize = "5";
        }

        Pager<Goods> pager = new Pager<>();

        pager.setPageNow(Integer.parseInt(pageNow));
        pager.setPageSize(Integer.parseInt(pageSize));

        pager = adminService.searchGoods(pager,gname);

        Result<Pager<Goods>> result = new Result<>();

        result.setData(pager);
        if(pager.getData()==null){
            result.setState(Result.ERROR);
        }else {
            result.setState(Result.SUCCESS);
        }
        response.getWriter().write(JSON.toJSONString(result));
    }


    /**
     * 显示待审核的帖子
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void showTextPass(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

        String pageNow = request.getParameter("pageNow");
        String pageSize = request.getParameter("pageSize");

        if (!StringUtils.isNotNull(pageNow)){
            pageNow = "1";
        }
        if (!StringUtils.isNotNull(pageSize)){
            pageSize = "5";
        }

        Pager<TextDto> pager = new Pager<>();

        pager.setPageNow(Integer.parseInt(pageNow));
        pager.setPageSize(Integer.parseInt(pageSize));


        pager = adminService.showTextPass(pager);

        Result<Pager<TextDto>> result = new Result<>();

        result.setData(pager);
        if(pager==null){
            result.setState(Result.ERROR);
        }else {
            result.setState(Result.SUCCESS);
        }
        response.getWriter().write(JSON.toJSONString(result));
    }

    /**
     * 审核帖子
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doTextPass(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

        String tid = request.getParameter("tid");
        String ispass = request.getParameter("ispass");
        Result<Pager<TextDto>> result = new Result<>();
        if(ispass==null ||"".equals(ispass)){
            result.setState(Result.ERROR);
        }else {
         boolean isSuccess= adminService.updateIspass(tid,Integer.parseInt(ispass));
         if(isSuccess){
             result.setState(Result.SUCCESS);
         }else {
             result.setState(Result.ERROR);
         }
        }
        response.getWriter().write(JSON.toJSONString(result));
    }


    /**
     * 按种类搜索待审核的帖子
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void searchText(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

        String pageNow = request.getParameter("pageNow");
        String pageSize = request.getParameter("pageSize");
        String kinds = request.getParameter("kinds");
        Result<Pager<TextDto>> result = new Result<>();
        if(kinds==null || "".equals(kinds)){
            result.setState(Result.ERROR);
        }

        if (!StringUtils.isNotNull(pageNow)){
            pageNow = "1";
        }
        if (!StringUtils.isNotNull(pageSize)){
            pageSize = "5";
        }

        Pager<TextDto> pager = new Pager<>();

        pager.setPageNow(Integer.parseInt(pageNow));
        pager.setPageSize(Integer.parseInt(pageSize));

        pager = adminService.searchTextByKinds(pager,kinds);

        result.setData(pager);

        if(pager==null){
            result.setState(Result.ERROR);
        }else {
            result.setState(Result.SUCCESS);
        }
        response.getWriter().write(JSON.toJSONString(result));
    }


    /**
     * 显示待审核的文件
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void showFilePass(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

        String pageNow = request.getParameter("pageNow");

        String pageSize = request.getParameter("pageSize");

        Result<Pager<com.jpx.entity.File>> result = new Result<>();

        if (!StringUtils.isNotNull(pageNow)){
            pageNow = "1";
        }
        if (!StringUtils.isNotNull(pageSize)){
            pageSize = "5";
        }

        Pager<com.jpx.entity.File> pager = new Pager<>();

        pager.setPageNow(Integer.parseInt(pageNow));

        pager.setPageSize(Integer.parseInt(pageSize));

        pager = adminService.showFilePass(pager);

        result.setData(pager);

        if(pager==null){
            result.setState(Result.ERROR);
        }else {
            result.setState(Result.SUCCESS);
        }
        response.getWriter().write(JSON.toJSONString(result));
    }


    /**
     * 审核文件
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */

    protected void doFilePass(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

        String fid = request.getParameter("fid");

        String ispass = request.getParameter("ispass");

        Result<Pager<TextDto>> result = new Result<>();
        if(ispass==null ||"".equals(ispass)){
            result.setState(Result.ERROR);
        }else {
            boolean isSuccess= adminService.updateFilepass(fid,Integer.parseInt(ispass));
            if(isSuccess){
                result.setState(Result.SUCCESS);
            }else {
                result.setState(Result.ERROR);
            }
        }
        response.getWriter().write(JSON.toJSONString(result));
    }
    protected void getThisText(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String tid = request.getParameter("tid");
        Text text = adminService.getTextByTid(tid);
        Result result = new Result();
        result.setState(Result.SUCCESS);
        result.setData(text);
        response.getWriter().write(JSON.toJSONString(result));
    }

}
