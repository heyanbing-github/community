package com.jpx.service.impl;

import com.jpx.dao.AdminDao;
import com.jpx.dao.impl.AdminDaoImpl;
import com.jpx.dto.Pager;
import com.jpx.dto.TextDto;
import com.jpx.dto.UserDto;
import com.jpx.entity.Admin;
import com.jpx.entity.Goods;
import com.jpx.entity.Notice;
import com.jpx.entity.Admin;
import com.jpx.entity.Notice;
import com.jpx.entity.*;
import com.jpx.entity.User;
import com.jpx.service.AdminService;
import com.jpx.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class AdminServiceImpl implements AdminService {

    private AdminDao adminDao=new AdminDaoImpl();


    @Override
    public List<UserDto> selectUser() {
        return null;
    }

    @Override
    public Pager<UserDto> selectUserPager(Pager<UserDto> pager) {
        int count=adminDao.selectUserCount();
        pager.setTotalCount(count);
        pager.setPageCount((count-1)/pager.getPageSize()+1);
        pager.setData(adminDao.selectUser(pager));
        return pager;
    }

    @Override
    public boolean deleteUser(String uid) {
         int a=adminDao.deleteUser(uid);
         int b=adminDao.deleteScore(uid);
         return a+b>=1;
    }



    @Override
    public boolean updateUser(User user, int score) {

       int a= adminDao.updateUser(user);
       int b=adminDao.updateScore(user.getUid(),score);
       return a+b>=2;
    }

    @Override
    public Admin loginByAdmin(String username, String password) {
        return adminDao.loginByAdmin(username,password);
    }

    @Override
    public boolean sendNotice(String notice, Admin c_admin) {
        String sendTime = StringUtils.getCurrentTime();
        String nid = StringUtils.getUUIDInOrderId()+"";
        Notice not = new Notice();
        not.setContent(notice);
        not.setAdmin(c_admin);
        not.setNid(nid);
        not.setSendTime(sendTime);
        return adminDao.insertNotice(not) > 0;
    }

    @Override
    public List<Notice> getFourNewNotice() {
        List<List> lists = adminDao.getFourNewNotice();
        List<Notice> noticeLists = new ArrayList<>();
        for (List list:lists
        ) {
            Admin admin = (Admin) list.get(0);
            Notice notice = (Notice) list.get(1);
            notice.setAdmin(admin);
            noticeLists.add(notice);
        }
        return noticeLists;
    }

    @Override
    public Pager<Notice> selectNotice(Pager<Notice> pager) {
        //总条数
        pager.setTotalCount(adminDao.selectTotalNoticeCount());
        if (pager.getTotalCount()==0){
            return pager;
        }
        //总页数
        pager.setPageCount((pager.getTotalCount()-1)/pager.getPageSize() +1);
        //公告数据
        List<Notice> list = adminDao.selectNotice(pager);

        pager.setData(list);
        return pager;
    }


    @Override
    public boolean deleteNotice(String nid) {
        return adminDao.deleteNotice(nid);
    }

    @Override
    public boolean updateNotice(String nid, String mid, String content) {
        return adminDao.updateNotice(nid, mid, content);
    }


    @Override
    public Pager<UserDto> searchUser(String username) {

        List<UserDto> userDtos= adminDao.searchUser(username);
        if(userDtos==null){
            return  null;
        }else {
            Pager<UserDto> pager = new Pager<>(1, 1, 1, 5, userDtos);
            return pager;
        }

    }

    @Override
    public Pager<Notice> selectNoticeByKey(Pager<Notice> pager, String searchText) {
        //总条数
        pager.setTotalCount(adminDao.selectTotalNoticeCountByKey(searchText));
        if (pager.getTotalCount()==0){
            return pager;
        }
        //总页数
        pager.setPageCount((pager.getTotalCount()-1)/pager.getPageSize() +1);
        //公告数据
        List<Notice> list = adminDao.selectNoticeByKey(pager, searchText);

        pager.setData(list);
        return pager;
    }
    @Override
    public boolean sendGoods(Goods goods) {
        goods.setGid(StringUtils.getUUIDInOrderId()+"");
        return adminDao.insertGoods(goods) > 0;
    }

    @Override
    public Pager<Goods> selectGoods(Pager<Goods> pager) {

        int count=adminDao.selectGoodsCount();
        pager.setTotalCount(count);
        pager.setPageCount((count-1)/pager.getPageSize()+1);
        pager.setData(adminDao.selectGoods(pager));
        return pager;
    }

    @Override
    public boolean updateGoods(String gid, int state, String mid) {

        return  adminDao.updateGoods(gid,state,mid)>0;
    }

    @Override
    public Pager<Goods> searchGoods(Pager<Goods> pager, String gname) {

        int count=adminDao.searchGoodsCount(gname);
        pager.setTotalCount(count);
        pager.setPageCount((count-1)/pager.getPageSize()+1);
        pager.setData(adminDao.searchGoods(pager,gname));
        return pager;
    }

    @Override
    public Pager<TextDto> showTextPass(Pager<TextDto> pager) {
        int count=adminDao.selectTextCount();
        pager.setTotalCount(count);
        System.out.println(count);
        pager.setPageCount((count-1)/pager.getPageSize()+1);
        List<TextDto> textDtos = adminDao.showTextPass(pager);
        pager.setData(textDtos);
        return pager;
    }

    @Override
    public boolean updateIspass(String tid, int ispass) {

        return adminDao.updateIspass(tid,ispass)>0;
    }

    @Override
    public Pager<TextDto> searchTextByKinds(Pager<TextDto> pager, String kinds) {

        int count=adminDao.searchTextByKindsCount(kinds);
        pager.setTotalCount(count);
        pager.setPageCount((count-1)/pager.getPageSize()+1);
        pager.setData(adminDao.searchTextByKinds(pager,kinds));
        return pager;
    }

    @Override
    public Pager<File> showFilePass(Pager<File> pager) {

        int count=adminDao.showFilePassCount();
        pager.setTotalCount(count);
        pager.setPageCount((count-1)/pager.getPageSize()+1);
        pager.setData(adminDao.showFilePass(pager));
        return pager;
    }

    @Override
    public boolean updateFilepass(String fid, int ispass) {

        return adminDao.updateFilepass(fid, ispass)>0;
    }

    @Override
    public boolean editGoods(String gid, int price, int score, int count) {

        return adminDao.editGoods(gid,  price,score,  count)>0;
    }

    @Override
    public Text getTextByTid(String tid) {
        return adminDao.getTextByTid(tid);
    }


}
