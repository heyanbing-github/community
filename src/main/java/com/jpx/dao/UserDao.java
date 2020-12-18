package com.jpx.dao;

import com.jpx.dto.*;

import com.jpx.entity.File;

import com.jpx.entity.Goods;
import com.jpx.entity.Text;
import com.jpx.entity.User;

import java.util.List;

public interface UserDao {

    int getScore(String uid);

    int selectTotalTextCountByUid(String uid);

    boolean checkNickname(String nickname);

    boolean checkPhone(String phone);

    boolean checkEmail(String email);

    boolean insertUser(User user);


    User login(String username, String password);



    int getTextCount(String uid);

    int getFileCount(String uid);

    int getfabulousNum(String uid);

    int getcommentsNum(String uid);

    int getloadCount(String uid);

    List<Text> selectTextByUid(Pager<TextDto> pager, String uid);

    String selectTextTypeByTid(String tid);

    List<List> getFourNewText(String uid);

    List<List> getFourNewFile(String uid);


    List<List> getNewText();

    List<List> getGoodUser();

    List<KindsSortDto> getUserAddr();

    List<File> getTotalFile();

    List<User> getTotalUser();

    List<Text> getTotalText();


    int delTextByTid(String tid);

    int selectTotalTextCountByUidAndSearchText(String uid, String searchText);

    List<Text> selectTextByUidAndSearchText(Pager<TextDto> pager, String uid, String searchText);

    int selectTotalFileCountByUid(String uid);

    List<File> selectFileByUid(Pager<File> pager, String uid);

    int delUserFileFid(String fid);

    int selectTotalFileCountByUidAndSearchFile(String uid, String searchFile);

    List<File> selectFileByUidAndSearchFile(Pager<File> pager, String uid, String searchFile);

    boolean changeMessage(User user, String username);

    int selectTotalUserGoodsCountByUid(String uid);

    List<UserGoodsDto> selectUserGoodsByUid(Pager<UserGoodsDto> pager, String uid);

    int delUserGoods(String guId);

    int selectTotalUserGoodsCountByUidAndSearch(String uid, String searchGoods);

    List<UserGoodsDto> selectUserGoodsByUidAndSearch(Pager<UserGoodsDto> pager, String uid, String searchGoods);

    List<UserGoodsDto> selectAllUserGoodsByUid(String uid);

    boolean updateUserInfo(User user);

    List<KindsSortDto> getKindsSort();

    List<TopGoodsDto> getTopGoods();

    TextDto selectTextBytid(String tid);

    List<Goods> pushMessage(String kinds);
}
