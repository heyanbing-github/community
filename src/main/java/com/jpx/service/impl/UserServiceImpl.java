
package com.jpx.service.impl;

import com.jpx.dao.UserDao;
import com.jpx.dao.impl.UserDaoImpl;
import com.jpx.dto.*;
import com.jpx.dto.UserGoodsDto;
import com.jpx.entity.Goods;
import com.jpx.entity.Text;
import com.jpx.entity.File;
import com.jpx.entity.User;
import com.jpx.service.UserService;
import com.jpx.utils.StringUtils;
import com.jpx.dto.SumDto;

import java.util.ArrayList;
import java.util.List;

import java.util.HashMap;
import java.util.Map;

public class UserServiceImpl implements UserService {
    UserDao userDao =  new UserDaoImpl();

    @Override
    public boolean checkNickname(String nickname) {
        return userDao.checkNickname(nickname);
    }

    @Override
    public boolean checkPhone(String phone) {
        return userDao.checkPhone(phone);
    }

    @Override
    public boolean checkEmail(String email) {
        return userDao.checkEmail(email);
    }

    @Override
    public boolean regDo(User user) {
        String uid = StringUtils.getUUIDInOrderId().toString();
        user.setUid(uid);
        return userDao.insertUser(user);
    }

    @Override
    public User login(String username, String password) {
        return userDao.login(username,password);
    }

    @Override
    public Map<String, Object> getTopMess(String uid) {
        Map<String, Object> map = new HashMap<>();
        //获取帖子数
        int textCount = userDao.getTextCount(uid);
        //获取资源数
        int fileCount = userDao.getFileCount(uid);
        //获取点赞评论量
        int fabulousNum = userDao.getfabulousNum(uid);
        int commentsNum = userDao.getcommentsNum(uid);
        //获取用户积分
        int score = userDao.getScore(uid);
        //获取文件的下载量
        int loadCount = userDao.getloadCount(uid);

        map.put("textCount",textCount);
        map.put("fileCount",fileCount);
        map.put("fabulousNum",fabulousNum);
        map.put("commentsNum",commentsNum);
        map.put("score",score);
        map.put("loadCount",loadCount);
        return map;
    }


    @Override

    public List<Text> getNewText() {
        List<List> lists = userDao.getNewText();
        List<Text> textLists = new ArrayList<>();
        for (List list:lists) {
            User user = (User) list.get(0);
            Text text = (Text) list.get(1);
            text.setUser(user);
            textLists.add(text);
        }
        return textLists;
    }

    @Override
    public List<SumDto> getGoodUser() {
        List<List> lists = userDao.getGoodUser();
        List<SumDto> sumDtos = new ArrayList<>();
        for (List list:lists) {
            SumDto sum = (SumDto) list.get(0);
            User user = (User) list.get(1);
            sum.setUser(user);
            sumDtos.add(sum);
        }
        return sumDtos;
    }

    @Override
    public List<KindsSortDto> getUserAddr() {
        return userDao.getUserAddr();


    }

    @Override
    public int getFileCount() {
        List<File> totalFile=userDao.getTotalFile();
        int count=totalFile.size();
        return count;
    }

    @Override
    public int getUserCount() {
        List<User> totalUser=userDao.getTotalUser();
        int count=totalUser.size();
        return count;
    }

    @Override
    public int getPostCount() {
        List<Text> totalText=userDao.getTotalText();
        int count=totalText.size();
        return count;
    }


    @Override

    public boolean delTextByTid(String tid) {
        return userDao.delTextByTid(tid)>0;
    }

    @Override
    public Pager<TextDto> selectTextByUidAndSearchText(Pager<TextDto> pager, String uid, String searchText) {
        //总条数
        pager.setTotalCount(userDao.selectTotalTextCountByUidAndSearchText(uid,searchText));
        if (pager.getTotalCount()==0){
            return pager;
        }
        //总页数
        pager.setPageCount((pager.getTotalCount()-1)/pager.getPageSize() +1);
        //帖子数据
        List<Text> list = userDao.selectTextByUidAndSearchText(pager, uid ,searchText);

        List<TextDto> textDtos = new ArrayList<>();

        for (Text text:list) {
            TextDto textDto =new TextDto();
            textDto.setText(text);
            textDto.setKinds(userDao.selectTextTypeByTid(text.getTid()));
            textDtos.add(textDto);
        }

        pager.setData(textDtos);
        return pager;
    }

    @Override
    public Pager<File> selectFileByUid(Pager<File> pager, String uid) {
        //总条数
        pager.setTotalCount(userDao.selectTotalFileCountByUid(uid));
        if (pager.getTotalCount()==0){
            return pager;
        }
        //总页数
        pager.setPageCount((pager.getTotalCount()-1)/pager.getPageSize() +1);
        //帖子数据
        List<File> list = userDao.selectFileByUid(pager, uid);

        pager.setData(list);
        return pager;
    }

    @Override
    public boolean delUserFileFid(String fid) {
        return userDao.delUserFileFid(fid)>0;
    }

    @Override
    public Pager<File> selectFileByUidAndSearchFile(Pager<File> pager, String uid, String searchFile) {
        //总条数
        pager.setTotalCount(userDao.selectTotalFileCountByUidAndSearchFile(uid,searchFile));
        if (pager.getTotalCount()==0){
            return pager;
        }
        //总页数
        pager.setPageCount((pager.getTotalCount()-1)/pager.getPageSize() +1);
        //文件数据
        List<File> list = userDao.selectFileByUidAndSearchFile(pager, uid ,searchFile);

        pager.setData(list);
        return pager;
    }

    @Override
    public boolean changeMessage(User user, String username) {

        return userDao.changeMessage(user,username);
    }

    @Override
    public Pager<UserGoodsDto> selectUserGoodsByUid(Pager<UserGoodsDto> pager, String uid) {
        //总条数
        pager.setTotalCount(userDao.selectTotalUserGoodsCountByUid(uid));

        if (pager.getTotalCount()==0){
            return pager;
        }

        //总页数
        pager.setPageCount((pager.getTotalCount()-1)/pager.getPageSize() +1);
        //商品数据
        List<UserGoodsDto> list = userDao.selectUserGoodsByUid(pager, uid);


        for (UserGoodsDto userGoodsDto:list) {
            userGoodsDto.setUid(uid);
        }

        pager.setData(list);
        return pager;
    }

    @Override
    public boolean delUserGoods(String guId) {
        return userDao.delUserGoods(guId)>0;
    }

    @Override
    public Pager<UserGoodsDto> selectUserGoodsAndSearch(Pager<UserGoodsDto> pager, String uid, String searchGoods) {
        //总条数
        pager.setTotalCount(userDao.selectTotalUserGoodsCountByUidAndSearch(uid,searchGoods));

        if (pager.getTotalCount()==0){
            return pager;
        }

        //总页数
        pager.setPageCount((pager.getTotalCount()-1)/pager.getPageSize() +1);
        //商品数据
        List<UserGoodsDto> list = userDao.selectUserGoodsByUidAndSearch(pager, uid ,searchGoods);


        for (UserGoodsDto userGoodsDto:list) {
            userGoodsDto.setUid(uid);
        }

        pager.setData(list);
        return pager;
    }

    @Override
    public List<UserGoodsDto> selectAllUserGoodsByUid(String uid) {
        return userDao.selectAllUserGoodsByUid(uid);
    }

    @Override
    public boolean updateUserInfo(User user) {
        return userDao.updateUserInfo(user);
    }

    @Override
    public List<KindsSortDto> getKindsSort() {
        List<KindsSortDto> result=userDao.getKindsSort();
        return result;
    }

    @Override
    public List<TopGoodsDto> getTopGoods() {
        List<TopGoodsDto> result=userDao.getTopGoods();
        return result;
    }

    @Override
    public List pushMessage(String tid) {
        //查询文章
        TextDto textDto = userDao.selectTextBytid(tid);
        //查询商品
        List<Goods> listGoods=userDao.pushMessage(textDto.getKinds());

        List<Object> list = new ArrayList<>();
        list.add(textDto);
        list.add(listGoods);
        return list;
    }

    @Override
    public int getScoreByUid(String uid) {
        return userDao.getScore(uid);
    }

    @Override
    public Pager<TextDto> selectTextByUid(Pager<TextDto> pager, String uid) {
        //总条数
        pager.setTotalCount(userDao.selectTotalTextCountByUid(uid));
        if (pager.getTotalCount()==0){
            return pager;
        }
        //总页数
        pager.setPageCount((pager.getTotalCount()-1)/pager.getPageSize() +1);
        //帖子数据
        List<Text> list = userDao.selectTextByUid(pager, uid);

        List<TextDto> textDtos = new ArrayList<>();

        for (Text text:list) {
            TextDto textDto =new TextDto();
            textDto.setText(text);
            textDto.setKinds(userDao.selectTextTypeByTid(text.getTid()));
            textDtos.add(textDto);
        }

        pager.setData(textDtos);
        return pager;
    }

    @Override
    public List<Text> getFourNewText(String uid) {
        List<List> lists = userDao.getFourNewText(uid);
        List<Text> textLists = new ArrayList<>();
        for (List list:lists
        ) {
            User user = (User) list.get(0);
            Text text = (Text) list.get(1);
            text.setUser(user);
            textLists.add(text);
        }
        return textLists;
    }

    @Override
    public List<File> getFourNewFile(String uid) {
        List<List> lists = userDao.getFourNewFile(uid);
        List<File> fileLists = new ArrayList<>();
        for (List list:lists
        ) {
            User user = (User) list.get(0);
            File file = (File) list.get(1);
            file.setUser(user);
            fileLists.add(file);
        }
        return fileLists;
    }


}


