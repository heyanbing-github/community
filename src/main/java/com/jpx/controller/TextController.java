package com.jpx.controller;

import com.alibaba.fastjson.JSON;
import com.jpx.dto.Pager;
import com.jpx.dto.Result;
import com.jpx.dto.TextDto;
import com.jpx.service.TextService;
import com.jpx.service.impl.TextServiceImpl;
import com.jpx.utils.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 帖子管理
 */
@WebServlet("/text/*")
public class TextController extends BaseController {

    TextService textService = new TextServiceImpl();

    protected void showText(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pageNow = request.getParameter("pageNum");
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

        pager = textService.selectTextPager(pager);

        Result<Pager<TextDto>> result = new Result<>();
        result.setData(pager);
        if(pager.getData()==null){
            result.setState(Result.ERROR);
        }else {
            result.setState(Result.SUCCESS);
        }
        response.getWriter().write(JSON.toJSONString(result));
    }
}
