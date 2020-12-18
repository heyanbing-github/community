
package com.jpx.service;

import com.jpx.dto.*;
import com.jpx.entity.File;
import com.jpx.entity.Text;
import com.jpx.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService {



    boolean checkNickname(String nickname);

    //获取用户的积分
    int getScoreByUid(String uid);

    Pager<TextDto> selectTextByUid(Pager<TextDto> pager, String uid);

    //获取用户最新的前四条文章
    List<Text> getFourNewText(String uid);

    //获取用户最新的前四个文件
    List<File> getFourNewFile(String uid);

    boolean checkPhone(String phone);

    boolean checkEmail(String email);

    boolean regDo(User user);


    User login(String username, String password);


    Map<String, Object> getTopMess(String uid);

    //获取最新的七条帖子
    List<Text> getNewText();

    List<SumDto> getGoodUser();

    List<KindsSortDto> getUserAddr();

    int getFileCount();

    int getUserCount();

    int getPostCount();


    boolean delTextByTid(String tid);

    Pager<TextDto> selectTextByUidAndSearchText(Pager<TextDto> pager, String uid, String searchText);

    Pager<File> selectFileByUid(Pager<File> pager, String uid);

    boolean delUserFileFid(String fid);

    Pager<File> selectFileByUidAndSearchFile(Pager<File> pager, String uid, String searchFile);


    boolean changeMessage(User user, String username);

    Pager<UserGoodsDto> selectUserGoodsByUid(Pager<UserGoodsDto> pager, String uid);

    boolean delUserGoods(String guId);

    Pager<UserGoodsDto> selectUserGoodsAndSearch(Pager<UserGoodsDto> pager, String uid, String searchGoods);

    List<UserGoodsDto> selectAllUserGoodsByUid(String uid);

    boolean updateUserInfo(User user);
    List<KindsSortDto> getKindsSort();

    List<TopGoodsDto> getTopGoods();

    List pushMessage(String tid);
}

