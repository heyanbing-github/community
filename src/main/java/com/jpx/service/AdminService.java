package com.jpx.service;

import com.jpx.dto.Pager;
import com.jpx.dto.TextDto;
import com.jpx.dto.UserDto;
import com.jpx.entity.Admin;
import com.jpx.entity.Goods;
import com.jpx.entity.Notice;
import com.jpx.entity.User;
import com.jpx.entity.Admin;
import com.jpx.entity.Notice;
import com.jpx.entity.User;
import com.jpx.entity.*;

import java.util.List;

public interface AdminService {

    List<UserDto> selectUser();

    Pager<UserDto> selectUserPager(Pager<UserDto> pager);

    boolean deleteUser(String uid);


    boolean updateUser(User user, int score);


    Admin loginByAdmin(String username, String password);


    boolean sendNotice(String notice, Admin c_admin);

    List<Notice> getFourNewNotice();

    Pager<Notice> selectNotice(Pager<Notice> pager);

    boolean deleteNotice(String nid);

    boolean updateNotice(String nid, String mid, String content);

    Pager<Notice> selectNoticeByKey(Pager<Notice> pager, String searchText);

    boolean sendGoods(Goods goods);

    Pager<UserDto> searchUser(String username);

    Pager<Goods> selectGoods(Pager<Goods> pager);

    boolean updateGoods(String gid, int state, String mid);

    Pager<Goods> searchGoods(Pager<Goods> pager, String gname);

    Pager<TextDto> showTextPass(Pager<TextDto> pager);

    boolean updateIspass(String tid, int parseInt);

    Pager<TextDto> searchTextByKinds(Pager<TextDto> pager, String kinds);

    Pager<File> showFilePass(Pager<File> pager);

    boolean updateFilepass(String fid, int parseInt);

    boolean editGoods(String gid, int price, int score, int count);

    Text getTextByTid(String tid);
}
