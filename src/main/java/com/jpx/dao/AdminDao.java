package com.jpx.dao;

import com.jpx.dto.Pager;
import com.jpx.dto.TextDto;
import com.jpx.dto.UserDto;
import com.jpx.entity.*;
import com.jpx.entity.Goods;

import java.util.List;

public interface AdminDao {

    List<UserDto> selectUser(Pager<UserDto> pager);

    int selectUserCount();

    int deleteUser(String uid);

    int deleteScore(String uid);

    int updateUser(User user);

    int updateScore(String uid, int score);

    Admin loginByAdmin(String username, String password);

    int insertNotice(Notice not);

    List<List> getFourNewNotice();

    int selectTotalNoticeCount();

    List<Notice> selectNotice(Pager<Notice> pager);

    boolean deleteNotice(String nid);

    boolean updateNotice(String nid, String mid, String content);

    int selectTotalNoticeCountByKey(String searchText);

    List<Notice> selectNoticeByKey(Pager<Notice> pager, String searchText);

    int insertGoods(Goods goods);

    List<UserDto> searchUser(String username);

    int selectGoodsCount();

    List<Goods> selectGoods(Pager<Goods> pager);

    int updateGoods(String gid, int state, String mid);

    int searchGoodsCount(String gname);

    List<Goods> searchGoods(Pager<Goods> pager, String gname);

    int selectTextCount();

    List<TextDto> showTextPass(Pager<TextDto> pager);

    int updateIspass(String tid, int ispass);

    int searchTextByKindsCount(String kinds);

    List<TextDto> searchTextByKinds(Pager<TextDto> pager, String kinds);

    int showFilePassCount();

    List<File> showFilePass(Pager<File> pager);

    int updateFilepass(String fid, int ispass);

    int editGoods(String gid, int price, int score, int count);

    Text getTextByTid(String tid);
}
