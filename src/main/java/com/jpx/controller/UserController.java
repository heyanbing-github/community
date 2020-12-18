package com.jpx.controller;



import com.alibaba.fastjson.JSON;
import com.jpx.dto.*;
import com.jpx.entity.Admin;
import com.jpx.entity.File;
import com.jpx.entity.Text;
import com.jpx.entity.User;
import com.jpx.service.AdminService;
import com.jpx.service.UserService;
import com.jpx.service.impl.AdminServiceImpl;
import com.jpx.service.impl.UserServiceImpl;
import com.jpx.utils.ClassUtils;
import com.jpx.utils.ExcelUtil;
import com.jpx.utils.MD5Utils;
import com.jpx.utils.StringUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import com.jpx.dto.SumDto;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;

import javax.servlet.annotation.WebServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.ws.Response;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.*;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.regex.Pattern;


@WebServlet("/user/*")
public class UserController extends BaseController{
    UserService userService = new UserServiceImpl();
    AdminService adminService = new AdminServiceImpl();

    /**
     * 获取当前登陆用户所有信息
     * 获取当前登陆用户
     * 检验用户名是否重复
     * @param request
     * @param response
     * @throws javax.servlet.ServletException
     * @throws IOException
     */
    public void checkNickName(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("进入checkNickname");
        boolean isExist = userService.checkNickname(request.getParameter("userName"));
        Result result = new Result();
        if(isExist == false){
            result.setMsg("该昵称可用");
            result.setState(Result.SUCCESS);
        }else{
            result.setMsg("该昵称以注册");
            result.setState(Result.ERROR);
        }
        //把result转换为JSON对象
        String json = JSON.toJSONString(result);
        //将json响应给客户端
        System.out.println("结果： " + result.getMsg()+result.getState());
        response.getWriter().write(json);
    }

    /**
     * 检查邮箱是否可用
     * @param request
     * @param response
     * @throws IOException
     */
    public void checkPhone(HttpServletRequest request, HttpServletResponse response) throws IOException {
        boolean isExist = userService.checkPhone(request.getParameter("phone"));
        Result result = new Result();
        if(isExist == false){
            result.setMsg("该号码可用");
            result.setState(Result.SUCCESS);
        }else{
            result.setMsg("该号码已注册");
            result.setState(Result.ERROR);
        }
        //把result转换为JSON对象
        String json = JSON.toJSONString(result);
        //将json响应给客户端
        response.getWriter().write(json);
    }

    /**
     * 检查电话是否可用
     * @param request
     * @param response
     * @throws IOException
     */
    public void checkEmail(HttpServletRequest request, HttpServletResponse response) throws IOException {
        boolean isExist = userService.checkEmail(request.getParameter("email"));
        Result result = new Result();
        if(isExist == false){
            result.setMsg("该邮箱可用");
            result.setState(Result.SUCCESS);
        }else{
            result.setMsg("该邮箱已注册");
            result.setState(Result.ERROR);
        }
        //把result转换为JSON对象
        String json = JSON.toJSONString(result);
        //将json响应给客户端
        response.getWriter().write(json);
    }
    /**
     * 执行注册
     * @param request
     * @param response
     * @throws IOException
     */
    public void regDo(HttpServletRequest request, HttpServletResponse response) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException {
        User  user = resolveEntity(request,User.class);//从参数中解析给对象
        Result result = new Result();
        //参数验证
        //电话验证
        String phone = user.getPhone();
        String pattern_phone ="^[0-9]{11}$";
        boolean phone_ = Pattern.compile(pattern_phone).matcher(phone).matches();
        System.out.println(phone_+"  ");
        //邮箱验证
        String email = user.getEmail();
        String pattern_email = "^[A-Za-z0-9]+([_\\.][A-Za-z0-9]+)*@([A-Za-z0-9\\-]+\\.)+[A-Za-z]{2,6}$";
        boolean email_ = Pattern.compile(pattern_email).matcher(email).matches();
        System.out.println(email_+"  ");
        //密码验证
        String password = user.getPassword();
        String pattern_password = "^[_a-zA-Z0-9]{6,16}$";
//        password = MD5Utils.digest16(password);
        boolean password_ = Pattern.compile(pattern_password).matcher(password).matches();
        System.out.println(password_+"  ");
        //昵称验证
        String nickName = user.getNickName();
        String pattern_nickName = "^[\\u4E00-\\u9FA5A-Za-z0-9]{0,7}$";
        boolean nickName_ = Pattern.compile(pattern_nickName).matcher(nickName).matches();
        System.out.println(nickName_+"  ");
        //产生账号
        String account = StringUtils.getUUIDInOrderId().toString();
        System.out.println(account);
        user.setUsername(account);
        if (phone_&&email_&&password_&&nickName_){
            boolean isSuccess = userService.regDo(user);
            if (isSuccess){
                result.setState(Result.SUCCESS);
                result.setMsg("注册成功");
                result.setData(user.getUsername());
            } else{
                result.setState(Result.ERROR);
                result.setMsg("注册失败");
            }
        }else{
            result.setState(Result.ERROR);
            result.setMsg("（昵称 或者 密码 或者 邮箱 或者 手机号）错误");
        }
        response.getWriter().write(JSON.toJSONString(result));
    }
    /**
     * 执行登錄
     * @param request
     * @param response
     * @throws IOException
     */
    public void loginDo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("进入执行登录函数");
        Result result = new Result();
        HttpSession session = request.getSession();
        String sessionVal  = (String) session.getAttribute("val");
        String val      = request.getParameter("val");
        if(!sessionVal.equalsIgnoreCase(val)){//验证码不正确的时候
            result.setMsg("验证码不正确");
            result.setState(Result.ERROR);
            response.getWriter().write(JSON.toJSONString(result));
            return;
        }
        //  处理登录
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String whichOne = request.getParameter("whichOne");
//        password = MD5Utils.digest16(password);
        System.out.println(username+  "   "+password + "  "+val +"   "+whichOne);
        if (whichOne.equals("用户登录")){
            User user = userService.login(username,password);
            System.out.println(user);
            if(user != null){//登录成功
                session.setAttribute("C_USER",user);
                result.setMsg("登录成功");
                result.setState(Result.SUCCESS);
                result.setWhichOne(Result.ERROR);
            }else{
                result.setMsg("账号或密码错误");
                result.setState(Result.ERROR);
            }
        }else if(whichOne.equals("管理员登录")){
            System.out.println("是管理员登录");
            Admin admin = adminService.loginByAdmin(username,password);
            System.out.println(admin);
            if(admin != null){//登录成功
                session.setAttribute("C_ADMIN",admin);
                result.setMsg("登录成功");
                result.setState(Result.SUCCESS);
                result.setWhichOne(Result.SUCCESS);
            }else{
                result.setMsg("账号或密码错误");
                result.setState(Result.ERROR);
            }
        }
        System.out.println("开始返回给登录界面");
        response.getWriter().write(JSON.toJSONString(result));
    }

    /**
     * 执行登錄
     * @param request
     * @param response
     * @throws IOException
     */
    public void changeMessage(HttpServletRequest request, HttpServletResponse response) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        User  user = resolveEntity(request,User.class);//从参数中解析给对象
        System.out.println(user);

        User user2 = (User) request.getSession().getAttribute("C_USER");
        String username =user2.getUsername();
        user.setUid(user2.getUid());
        user.setUsername(username);
        user.setUphoto(user2.getUphoto());
        user.setState(user2.getState());
        request.getSession().removeAttribute("C_USER");

        if(user.getNickName()==null){
            user.setNickName(user2.getNickName());
        }
        if (user.getPassword()==null){
            user.setPassword(user2.getPassword());
        }
        if (user.getEmail()==null){
            user.setEmail(user2.getEmail());
        }
        if (user.getAddress()==null){
            user.setAddress(user2.getAddress());
        }
        request.getSession().setAttribute("C_USER",user);
        boolean isSucceed = userService.changeMessage(user,username);
        System.out.println(isSucceed);
        Result result = new Result();
        if (isSucceed){
            result.setState(Result.SUCCESS);
            result.setMsg("更新成功");
        }else{
            result.setState(Result.ERROR);
            result.setMsg("更新失败");
        }

        response.getWriter().write(JSON.toJSONString(result));
    }



    /**
     * 图片验证码
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void validate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置响应类型为图片
        response.setContentType("image/jpeg");
        //创建一个图片的对象
        BufferedImage image = new BufferedImage(100,37, BufferedImage.TYPE_INT_RGB);
        //得到画笔
        Graphics2D g = image.createGraphics();
        // 背景颜色 0-127   文字颜色 128-255
        //随机一个背景颜色
        int br = (int)(Math.random() * 128);
        int bg = (int)(Math.random() * 128);
        int bb = (int)(Math.random() * 128);
        //得到一个随机的颜色
        Color backgroundColor = new Color(br,bg,bb);
        g.setColor(backgroundColor);
        //填充一个矩形
        g.fillRect(0,0,100,37);
        String str = StringUtils.randomString(4);
        System.out.println(str);
        //将字符串存入session
        request.getSession().setAttribute("val",str);
        //设置绘制的字体
        g.setFont(new Font("黑体",Font.PLAIN,23));
        //把字符串绘制到图片上
        for(int i = 0 ; i < str.length();i++){
            int fr = (int)(Math.random() * 128) + 128;
            int fg = (int)(Math.random() * 128) + 128;
            int fb = (int)(Math.random() * 128) + 128;
            Color fColor = new Color(fr,fg,fb);
            g.setColor(fColor);
            //绘制每一个字符
            double du = (Math.random()*(-30)+15)/180*Math.PI;
            g.rotate(du,i*20+5,25);
            g.drawString(str.charAt(i)+"",i*20+5,25);
            g.rotate(-du,i*20+5,25);
        }

        g.setColor(Color.WHITE);
        //销毁画笔
        g.dispose();
        //把图片响应给客户端
        ImageIO.write(image,"jpg",response.getOutputStream());
    }


    /**
     * 获取当前登陆用户
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void getLoginUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //从session中获取登陆用户的所有信息
        HttpSession session = request.getSession();
        User c_user = (User) session.getAttribute("C_USER");
        System.out.println(c_user);
        //将积分和用户绑定
        UserDto userDto = new UserDto();
        userDto.setUser(c_user);
        userDto.setScore(userService.getScoreByUid(c_user.getUid()));

        //封装到结果对象
        Result result = new Result();
        result.setData(userDto);
        result.setState(Result.SUCCESS);

        //将与用户相关的数据通过json传输
        String json = JSON.toJSONString(result);
        response.getWriter().write(json);
    }
    /**
     * 获取当前登陆管理员所有信息
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void getLoginAdmin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //从session中获取登陆管理员的所有信息
        HttpSession session = request.getSession();
        Admin c_admin = (Admin) session.getAttribute("C_ADMIN");

        //封装结果集
        Result result = new Result();
        result.setState(Result.SUCCESS);
        result.setData(c_admin);

        //将与用户相关的数据通过json传输
        String json = JSON.toJSONString(result);
        response.getWriter().write(json);
    }

    /**
     * 获取最新的七条帖子
     * @param request
     * @param response
     */
    private void getNewText(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Text> lists = userService.getNewText();

        Result result = new Result();
        result.setState(Result.SUCCESS);
        result.setData(lists);
        String json = JSON.toJSONString(result);
        response.getWriter().write(json);
    }
    /**
     * 获取当前用户的最新四条帖子
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void getLoginUserNewText(HttpServletRequest request, HttpServletResponse response) throws  IOException {
         User c_user = (User)request.getSession().getAttribute("C_USER");
        //获取用户的最新4条文章
        List<Text> lists = userService.getFourNewText(c_user.getUid());

        //封装结果集
        Result result = new Result();
        result.setState(Result.SUCCESS);
        result.setData(lists);

        //将与用户相关的数据通过json传输
        String json = JSON.toJSONString(result);
        response.getWriter().write(json);
    }

    /**
     * 获取当前用户的最新四个文件
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void getLoginUserNewFile(HttpServletRequest request, HttpServletResponse response) throws  IOException {
         User c_user = (User)request.getSession().getAttribute("C_USER");
        //获取用户的最新4条文章
        List<File> lists = userService.getFourNewFile(c_user.getUid());

        //封装结果集
        Result result = new Result();
        result.setState(Result.SUCCESS);
        result.setData(lists);

        //将与用户相关的数据通过json传输
        String json = JSON.toJSONString(result);
        response.getWriter().write(json);
    }

    /**
     * 得到用户的帖子目录
     */
    private void getUserText(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        User user = (User) request.getSession().getAttribute("C_USER");

        Result<Pager<TextDto>>result = new Result<>();
        //没有登录，不能查看目录 !!!!过滤器登录
        if (user == null){
            result.setMsg("你需要先登录！");
            result.setState(Result.NOT_LOGIN);
            //跳转登录界面
            response.getWriter().write(JSON.toJSONString(result));
            return;
        }
        //已经登录，获取数据
        String pageNow = request.getParameter("pageNow");
        String pageSize = request.getParameter("pageSize");
        if (!StringUtils.isNotNull(pageNow)){
            pageNow = "1";
        }
        if (!StringUtils.isNotNull(pageSize)){
            pageSize = "10";
        }
        Pager<TextDto> pager = new Pager<>();
        pager.setPageNow(Integer.parseInt(pageNow));
        pager.setPageSize(Integer.parseInt(pageSize));
        pager = userService.selectTextByUid(pager,user.getUid());

        //该用户没有帖子数据
        if (pager.getTotalCount()==0){
            result.setState(Result.ERROR);
            result.setMsg("你还没有发布过帖子");
            result.setData(pager);
        }else {
            result.setState(Result.SUCCESS);
            result.setMsg("查询成功");
            result.setData(pager);
        }

        response.getWriter().write(JSON.toJSONString(result));
    }
    /**
     * 删除帖子
     */
    private void delUserText(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String tid = request.getParameter("tid");
        boolean delIsSuccess = userService.delTextByTid(tid);
        Result result = new Result();
        if (delIsSuccess){
            result.setState(Result.SUCCESS);
        }else {
            result.setState(Result.ERROR);
            result.setMsg("删除失败");
        }
        response.getWriter().write(JSON.toJSONString(result));
    }
    /**
     * 搜索帖子
     */
    private void searchText(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        User user = (User) request.getSession().getAttribute("C_USER");

        //关键字
        String searchText = request.getParameter("searchText");

        //已经登录，获取数据
        String pageNow = "1";
        String pageSize = "10";

        Result<Pager<TextDto>>result = new Result<>();

        Pager<TextDto> pager = new Pager<>();
        pager.setPageNow(Integer.parseInt(pageNow));
        pager.setPageSize(Integer.parseInt(pageSize));

        pager = userService.selectTextByUidAndSearchText(pager,user.getUid(),searchText);

        //该用户没有帖子数据
        if (pager.getTotalCount()==0){
            result.setState(Result.ERROR);
            result.setMsg("你还没有发布过该帖子");
            result.setData(pager);
        }else {
            result.setState(Result.SUCCESS);
            result.setMsg("查询成功");
            result.setData(pager);
        }

        response.getWriter().write(JSON.toJSONString(result));
    }
    /**
     * 得到用户上传的文件目录
     */
    private void getUserFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = (User) request.getSession().getAttribute("C_USER");

        Result<Pager<File>>result = new Result<>();

        //已经登录，获取数据
        String pageNow = request.getParameter("pageNow");
        String pageSize = request.getParameter("pageSize");
        if (!StringUtils.isNotNull(pageNow)){
            pageNow = "1";
        }
        if (!StringUtils.isNotNull(pageSize)){
            pageSize = "10";
        }

        Pager<File> pager = new Pager<>();
        pager.setPageNow(Integer.parseInt(pageNow));
        pager.setPageSize(Integer.parseInt(pageSize));
        pager = userService.selectFileByUid(pager,user.getUid());

        //该用户没有帖子数据
        if (pager.getTotalCount()==0){
            result.setState(Result.ERROR);
            result.setMsg("你还没有上传过文件");
            result.setData(pager);
        }else {
            result.setState(Result.SUCCESS);
            result.setMsg("查询成功");
            result.setData(pager);
        }

        response.getWriter().write(JSON.toJSONString(result));
    }

    /**
     *
     * 删除用户文件目录
     */
    private void delUserFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String fid = request.getParameter("fid");
        boolean delIsSuccess = userService.delUserFileFid(fid);
        Result result = new Result();
        if (delIsSuccess){
            result.setState(Result.SUCCESS);
        }else {
            result.setState(Result.ERROR);
            result.setMsg("删除失败");
        }
        response.getWriter().write(JSON.toJSONString(result));
    }
    /**
     * 搜索文件目录
     */
    private void searchFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        User user = (User) request.getSession().getAttribute("C_USER");

        //关键字
        String searchFile = request.getParameter("searchFile");

        //已经登录，获取数据
        String pageNow = "1";
        String pageSize = "10";

        Result<Pager<File>>result = new Result<>();

        Pager<File> pager = new Pager<>();
        pager.setPageNow(Integer.parseInt(pageNow));
        pager.setPageSize(Integer.parseInt(pageSize));

        pager = userService.selectFileByUidAndSearchFile(pager,user.getUid(),searchFile);

        //该用户没有帖子数据
        if (pager.getTotalCount()==0){
            result.setState(Result.ERROR);
            result.setMsg("你没有上传过该文件");
            result.setData(pager);
        }else {
            result.setState(Result.SUCCESS);
            result.setMsg("查询成功");
            result.setData(pager);
        }

        response.getWriter().write(JSON.toJSONString(result));
    }


    /**
     * 得到用户购买的商品
     */
    private void getUserGoods(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = (User) request.getSession().getAttribute("C_USER");

        Result<Pager<UserGoodsDto>>result = new Result<>();

        //已经登录，获取数据
        String pageNow = request.getParameter("pageNow");
        String pageSize = request.getParameter("pageSize");
        if (!StringUtils.isNotNull(pageNow)){
            pageNow = "1";
        }
        if (!StringUtils.isNotNull(pageSize)){
            pageSize = "10";
        }

        Pager<UserGoodsDto> pager = new Pager<>();

        pager.setPageNow(Integer.parseInt(pageNow));
        pager.setPageSize(Integer.parseInt(pageSize));

        pager = userService.selectUserGoodsByUid(pager,user.getUid());

        //该用户没有帖子数据
        if (pager.getTotalCount()==0){
            result.setState(Result.ERROR);
            result.setMsg("你还没有购买过商品");
            result.setData(pager);
        }else {
            result.setState(Result.SUCCESS);
            result.setMsg("查询成功");
            result.setData(pager);
        }

        response.getWriter().write(JSON.toJSONString(result));
    }

    /**
     * 删除用户购买的商品
     */
    private void delUserGoods(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String guId = request.getParameter("guId");
        boolean delIsSuccess = userService.delUserGoods(guId);
        Result result = new Result();
        if (delIsSuccess){
            result.setState(Result.SUCCESS);
        }else {
            result.setState(Result.ERROR);
            result.setMsg("删除失败");
        }
        response.getWriter().write(JSON.toJSONString(result));
    }

    /**
     * 查询商品
     */
    private void searchUserGoods(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = (User) request.getSession().getAttribute("C_USER");

        //关键字
        String searchGoods = request.getParameter("searchGoods");
        System.out.println(searchGoods);
        //已经登录，获取数据
        String pageNow = "1";
        String pageSize = "10";

        Result<Pager<UserGoodsDto>>result = new Result<>();

        Pager<UserGoodsDto> pager = new Pager<>();
        pager.setPageNow(Integer.parseInt(pageNow));
        pager.setPageSize(Integer.parseInt(pageSize));

        pager = userService.selectUserGoodsAndSearch(pager,user.getUid(),searchGoods);

        //该用户没有帖子数据
        if (pager.getTotalCount()==0){
            result.setState(Result.ERROR);
            result.setMsg("你没有购买过该商品");
            result.setData(pager);
        }else {
            result.setState(Result.SUCCESS);
            result.setMsg("查询成功");
            result.setData(pager);
        }

        response.getWriter().write(JSON.toJSONString(result));
    }

    /**
     * 导出Excel
     */
    private void outExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel;charset=utf-8");

        User user = (User) request.getSession().getAttribute("C_USER");

        List<UserGoodsDto> userGoodsDtos = userService.selectAllUserGoodsByUid(user.getUid());

        String fileName = URLEncoder.encode(StringUtils.randomUUID()+user.getNickName()+"订单Excel文件.xlsx","UTF-8");
        java.io.File[] files = java.io.File.listRoots();
        String pathDir = files[files.length-1]+java.io.File.separator+"communitydata";
        java.io.File fileDir = new java.io.File(pathDir);
        boolean exits = fileDir.exists();
        if (exits){

        }else {
            fileDir.mkdir();
        }
        String path = files[files.length-1]+java.io.File.separator+"communitydata"+ java.io.File.separator+fileName+".xlsx";
        if (!userGoodsDtos.isEmpty()){
            java.io.File file = new java.io.File(path);
            boolean isSuccess = file.createNewFile();
            if (isSuccess){
                //创建成功
                List<String> head = Arrays.asList("单号","商品名","数量","总价格","购买日期");
                List<List<Object>> datas = new ArrayList<>();
                Iterator<UserGoodsDto> it = userGoodsDtos.iterator();
                while (it.hasNext()){
                    UserGoodsDto userGoodsDto = it.next();
                    datas.add(Arrays.<Object>asList(userGoodsDto.getGuId()+"",userGoodsDto.getGoods().getGname(),userGoodsDto.getNum()+"",(Double.parseDouble(userGoodsDto.getGoods().getPrice()+"")*userGoodsDto.getNum())+"",userGoodsDto.getBuyTime()));
                }
                ExcelUtil.writeBySimple(path,datas,head);
            }

            //下载文件
            response.setHeader("Content-Disposition","attchment;filename="+URLEncoder.encode(user.getNickName()+"商品订单Excel文件.xlsx","UTF-8"));
            InputStream is = new FileInputStream(path);

            OutputStream os = response.getOutputStream();
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = is.read(bytes))!=-1){
                os.write(bytes,0,len);
            }
            os.close();
            is.close();

        }else {//没有订单文件
            return;
        }

    }

    /**
     * 获取帖子的点赞和评论数，注册时间，文件下载量
     */
    private void getTopMess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         User c_user = (User)request.getSession().getAttribute("C_USER");
        Map<String, Object> map= userService.getTopMess(c_user.getUid());

        //封装结果集
        Result result = new Result();
        result.setState(Result.SUCCESS);
        result.setData(map);

        //将与用户相关的数据通过json传输
        String json = JSON.toJSONString(result);
        response.getWriter().write(json);
    }

    /**
     * 修改个人信息
     */
    protected void updateUserInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, FileUploadException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        User c_user = (User)request.getSession().getAttribute("C_USER");

        FileItemFactory fileItemFactory = new DiskFileItemFactory();
        ServletFileUpload servletFileUpload = new ServletFileUpload(fileItemFactory);

        List<FileItem> fileItems = servletFileUpload.parseRequest(request);
        for (FileItem fileItem:fileItems
        ) {
            if (fileItem.isFormField()){
                String name = fileItem.getFieldName();
                String value = fileItem.getString();
                value = new String(value.getBytes("ISO-8859-1"),"UTF-8");
                Method method =  ClassUtils.getSetMethod(User.class,name);
                if(method != null){
                    ClassUtils.invokeMethod(method,c_user,value);
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
                String path = "E:\\communitydata\\images" + java.io.File.separator + fileName;
                FileUtils.copyInputStreamToFile(fileItem.getInputStream(), new java.io.File(path));

                c_user.setUphoto(fileName);
            }
        }
        boolean isSuccess = userService.updateUserInfo(c_user);
        request.getSession().removeAttribute("C_USER");
        request.getSession().setAttribute("C_USER",c_user);
        Result result = new Result();
        if (isSuccess){
            response.sendRedirect("/community/show/html/userInfo.html");
        }else{
        }
    }

    private void logOut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User c_user = (User) request.getSession().getAttribute("C_USER");
        Admin c_admin = (Admin) request.getSession().getAttribute("C_ADMIN");
        if (c_user != null){
            request.getSession().removeAttribute("C_USER");
        }else if (c_admin != null){
            request.getSession().removeAttribute("C_ADMIN");
        }
        request.getSession().invalidate();
        response.sendRedirect("/community/show/font/main.html");
    }
    /**
     * 根据点赞数排名得到前四优质用户
     * @param request
     * @param response
     */
    private void getGoodUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<SumDto> lists = userService.getGoodUser();

        Result result = new Result();
        result.setState(Result.SUCCESS);
        result.setData(lists);
        String json = JSON.toJSONString(result);

        response.getWriter().write(json);
    }

    /**
     * 统计用户的地址分布
     * @param request
     * @param response
     */
    private void getUserAddr(HttpServletRequest request, HttpServletResponse response) throws IOException{
        List<KindsSortDto> userAddr = userService.getUserAddr();

        String json = JSON.toJSONString(userAddr);

        response.getWriter().write(json);
    }

    /**
     * 获得总资源数
     * @param request
     * @param response
     * @throws IOException
     */
    private void getFileCount(HttpServletRequest request, HttpServletResponse response) throws IOException{
        int fileCount=userService.getFileCount();
        String json = JSON.toJSONString(fileCount);
        response.getWriter().write(json);
    }

    /**
     * 获得总用户数
     * @param request
     * @param response
     * @throws IOException
     */
    private void getUserCount(HttpServletRequest request, HttpServletResponse response) throws IOException{
        int userCount=userService.getUserCount();
        String json = JSON.toJSONString(userCount);
        response.getWriter().write(json);
    }

    /**
     * 获得总帖子数
     * @param request
     * @param response
     * @throws IOException
     */
    private void getPostCount(HttpServletRequest request, HttpServletResponse response) throws IOException{
        int postCount=userService.getPostCount();
        String json = JSON.toJSONString(postCount);
        response.getWriter().write(json);
    }

    /**
     * 统计帖子种类分布
     * @param request
     * @param response
     * @throws IOException
     */
    private void getKindsSort(HttpServletRequest request, HttpServletResponse response)throws IOException{
        List<KindsSortDto> resultList=userService.getKindsSort();
        String json = JSON.toJSONString(resultList);
        response.getWriter().write(json);
    }

    /**
     * 获取商品热销前五
     * @param request
     * @param response
     * @throws IOException
     */
    private void getTopGoods(HttpServletRequest request, HttpServletResponse response)throws IOException{
        List<TopGoodsDto> topGoods=userService.getTopGoods();
        System.out.println(topGoods);

        String json = JSON.toJSONString(topGoods);
        response.getWriter().write(json);
    }

    /**
     * 获取商品热销前五
     * @param request
     * @param response
     * @throws IOException
     */
    private void pushMessage(HttpServletRequest request, HttpServletResponse response)throws IOException{
        response.setContentType("application/json;charset=utf-8");
        String tid = request.getParameter("tid");
        List list = userService.pushMessage(tid);
        Result result = new Result();
        result.setData(list);

        String json = JSON.toJSONString(result);
        response.getWriter().write(json);
    }

    private void showindex(HttpServletRequest request, HttpServletResponse response)throws IOException{
        response.sendRedirect("/community/show/index.html");
    }
}
