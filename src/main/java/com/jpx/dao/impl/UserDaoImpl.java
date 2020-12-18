package com.jpx.dao.impl;


import com.jpx.dao.UserDao;
import com.jpx.dto.*;
import com.jpx.entity.Goods;
import com.jpx.entity.Text;
import com.jpx.entity.User;
import com.jpx.entity.File;
import com.jpx.utils.SqlUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserDaoImpl implements UserDao {
    @Override
    public boolean checkNickname(String nickname) {
        String sql = "select uid from user where nickname = ?";
        return SqlUtils.selectOne(User.class,sql,nickname)!=null ? true:false;
    }
    @Override
    public boolean checkPhone(String phone) {
        String sql = "select uid from user where phone = ?";
        return SqlUtils.selectOne(User.class,sql,phone)!=null ? true:false;
    }

    @Override
    public boolean checkEmail(String email) {
        String sql = "select uid from user where email = ?";
        return SqlUtils.selectOne(User.class,sql,email)!=null ? true:false;
    }

    @Override
    public boolean insertUser(User user) {
        return SqlUtils.insert(user)>0;
    }


    @Override
    public User login(String username, String password) {
        String sql = "select uid ,username,nickName,phone,email,sex,uphoto,address,state from user where username=? and password =? and state=0";
        return SqlUtils.selectOne(User.class,sql,username,password);
    }


    @Override
    public int getTextCount(String uid) {
        String sql = "select count(tid) c from text where uid=? and state=0";
        List<Map<String, Object>> list = SqlUtils.select(sql, uid);
        if (list.get(0).get("c") == null){
            return 0;
        }
        return Integer.parseInt(list.get(0).get("c")+"");
    }



    @Override
    public int getFileCount(String uid) {
        String sql = "select count(tid) c from text where uid=? and state=0";
        List<Map<String, Object>> list = SqlUtils.select(sql, uid);
        if (list.get(0).get("c") == null){
            return 0;
        }
        return Integer.parseInt(list.get(0).get("c")+"");
    }

    @Override
    public int getfabulousNum(String uid) {
        String sql = "select sum(fabulousNum) s from text where uid=? and state=0";
        List<Map<String, Object>> list = SqlUtils.select(sql, uid);
        if (list.get(0).get("s") == null){
            return 0;
        }
        return Integer.parseInt(list.get(0).get("s")+"");
    }

    @Override
    public int getcommentsNum(String uid) {
        String sql = "select sum(commentsNum) s from text where uid=? and state=0";
        List<Map<String, Object>> list = SqlUtils.select(sql, uid);
        if (list.get(0).get("s") == null){
            return 0;
        }
        return Integer.parseInt(list.get(0).get("s")+"");
    }

    @Override
    public int getloadCount(String uid) {
        String sql = "select sum(loadCount) s from file where uid=? and state=0";
        List<Map<String, Object>> list = SqlUtils.select(sql, uid);
        if (list.get(0).get("s") == null){
            return 0;
        }
        return Integer.parseInt(list.get(0).get("s")+"");
    }

    @Override
    public int getScore(String uid) {
        String sql = "select score from score where uid=?";
        List<Map<String, Object>> list = SqlUtils.select(sql, uid);
        return Integer.parseInt(list.get(0).get("score") + "");
    }

    @Override
    public int selectTotalTextCountByUid(String uid) {
        String sql = "select count(*) count from text where state=0 and uid=?";
        List<Map<String,Object>>list = SqlUtils.select(sql,uid);
        return Integer.parseInt(list.get(0).get("count")+"");
    }

    @Override
    public List<Text> selectTextByUid(Pager<TextDto> pager, String uid) {
        String sql = "select*from text left join user on text.uid=user.uid where text.state=0 and text.uid=? order by sendTime desc limit ?,?";
        Class[] classes = {Text.class,User.class};
        List<Text> texts = new ArrayList<>();
        List<List> lists = SqlUtils.select(classes,sql,uid,(pager.getPageNow()-1)*pager.getPageSize(),pager.getPageSize());
        for (List list:lists) {
            Text text = (Text) list.get(0);
            User user = (User) list.get(1);
            text.setUser(user);
            texts.add(text);
        }
        return texts;
    }

    @Override
    public String selectTextTypeByTid(String tid) {
        String sql = "select kinds from kt left join kinds on kt.kid=kinds.kid where tid=?";
        return SqlUtils.select(sql,tid).get(0).get("kinds")+"";
    }

    @Override
    public List<List> getFourNewText(String uid) {
        String sql = "select user.uid,title,sendTime,ispass from user right join text on user.uid=text.uid where user.uid=? and text.state=0 and user.state=0 order by sendTime desc limit 0,4";
        Class[] clazzs = {User.class, Text.class};
        List<List> lists = SqlUtils.select(clazzs, sql, uid);

        return lists;
    }

    @Override
    public List<List> getFourNewFile(String uid) {
        String sql = "select user.uid,fname,sendTime,ispass from user right join file on user.uid=file.uid where user.uid=? and file.state=0 and user.state=0 order by sendTime desc limit 0,4";
        Class[] clazzs = {User.class, File.class};
        List<List> lists = SqlUtils.select(clazzs, sql, uid);

        return lists;
    }


    @Override
    public List<List> getNewText() {
        String sql = "select user.nickname nickName,title,sendTime,commentsNum,ispass from user right join text on user.uid=text.uid where text.state=0 and user.state=0 order by sendTime desc limit 0,7";
        Class[] clazzs = {User.class, Text.class};
        List<List> lists = SqlUtils.select(clazzs, sql);
        return lists;
    }

    @Override
    public List<List> getGoodUser() {
        String sql="select user.nickname nickName,email,uphoto,SUM(fabulousNum) sumFabulous from text left join user on user.uid=text.uid where text.state=0 and user.state=0 GROUP BY user.uid ORDER BY SUM(fabulousNum) desc";
        Class[] clazzs = {SumDto.class, User.class};
        List<List> lists = SqlUtils.select(clazzs, sql);
        return lists;
    }

    @Override
    public List<KindsSortDto> getUserAddr() {
        String sql="select address,COUNT(address) value from user GROUP BY address";
        List<Map<String, Object>> maps = SqlUtils.select(sql);

        if(maps.size()==0){
            return  null;
        }else {
            List<KindsSortDto> list = new ArrayList<>();
            for(Map map:maps){
                KindsSortDto kindsSortDto = new KindsSortDto();
                kindsSortDto.setValue(Integer.parseInt(map.get("value")+"") );
                kindsSortDto.setName((String) map.get("address"));
                list.add(kindsSortDto);
            }
            return list;
        }
    }

    @Override
    public List<File> getTotalFile() {
        String sql="select fid,fname,sendTime,state from file where state=0";
        List<File> totalFile=SqlUtils.select(File.class,sql);
        return totalFile;
    }

    @Override
    public List<User> getTotalUser() {
        String sql="select uid,username,nickName from user";
        List<User> totalUser=SqlUtils.select(User.class,sql);
        return totalUser;
    }

    @Override
    public List<Text> getTotalText() {
        String sql="select tid,title,ispass from text where ispass=1";
        List<Text> totalText=SqlUtils.select(Text.class,sql);
        return totalText;
    }


    @Override
    public int delTextByTid(String tid) {
        String sql = "update text set state=1 where tid=?";
        return SqlUtils.update(sql,tid);
    }




    @Override
    public int selectTotalTextCountByUidAndSearchText(String uid, String searchText) {
        String sql = "select count(*) count from text where state=0 and uid=? and title like ? ";
        List<Map<String,Object>>list = SqlUtils.select(sql,uid,"%"+searchText+"%");
        return Integer.parseInt(list.get(0).get("count")+"");
    }

    @Override
    public List<Text> selectTextByUidAndSearchText(Pager<TextDto> pager, String uid, String searchText) {
        String sql = "select*from text left join user on text.uid=user.uid where text.state=0 and text.uid=? and title like ?  order by sendTime desc limit ?,?";
        Class[] classes = {Text.class,User.class};
        List<Text> texts = new ArrayList<>();
        List<List> lists = SqlUtils.select(classes,sql,uid,"%"+searchText+"%",(pager.getPageNow()-1)*pager.getPageSize(),pager.getPageSize());
        for (List list:lists) {
            Text text = (Text) list.get(0);
            User user = (User) list.get(1);
            text.setUser(user);
            texts.add(text);
        }
        return texts;
    }

    @Override
    public int selectTotalFileCountByUid(String uid) {
        String sql = "select count(*) count from file where state=0 and uid=?";
        List<Map<String,Object>>list = SqlUtils.select(sql,uid);
        return Integer.parseInt(list.get(0).get("count")+"");
    }

    @Override
    public List<File> selectFileByUid(Pager<File> pager, String uid) {
        String sql = "select fid,fname,ispass,loadCount,score,sendTime from file where state=0 and uid=? order by sendTime desc limit ?,?";
        return SqlUtils.select(File.class,sql,uid,(pager.getPageNow()-1)*pager.getPageSize(), pager.getPageSize());
    }

    @Override
    public int delUserFileFid(String fid) {
        String sql = "update file set state=1 where fid=?";
        return SqlUtils.update(sql,fid);
    }

    @Override
    public int selectTotalFileCountByUidAndSearchFile(String uid, String searchFile) {
        String sql = "select count(*) count from file where state=0 and uid=? and fname like ?";
        List<Map<String,Object>>list = SqlUtils.select(sql,uid,"%"+searchFile+"%");
        return Integer.parseInt(list.get(0).get("count")+"");
    }

    @Override
    public List<File> selectFileByUidAndSearchFile(Pager<File> pager, String uid, String searchFile) {
        String sql = "select fid,fname,ispass,loadCount,score,sendTime from file where state=0 and uid=? and fname like ? order by sendTime desc limit ?,?";
        return SqlUtils.select(File.class,sql,uid,"%"+searchFile+"%",(pager.getPageNow()-1)*pager.getPageSize(), pager.getPageSize());
    }

    @Override
    public boolean changeMessage(User user, String username) {
        String sql ="update User set nickName= ?,password =? ,phone=? ,email = ? ,sex = ? ,address = ? where username = ? ";
        return SqlUtils.update(sql,user.getNickName(),user.getPassword(),user.getPhone(),user.getEmail(),user.getSex(),user.getAddress(),username)>0;
    }


    @Override
    public int selectTotalUserGoodsCountByUid(String uid) {
        String sql = "select count(*) count from userGoods  where state=0 and uid=?";
        List<Map<String,Object>>list = SqlUtils.select(sql,uid);
        return Integer.parseInt(list.get(0).get("count")+"");
    }

    @Override
    public List<UserGoodsDto> selectUserGoodsByUid(Pager<UserGoodsDto> pager, String uid) {

        String sql = "select guId,gname,gphoto,num,price,buyTime from userGoods left join goods on userGoods.gid=goods.gid where userGoods.state=0 and uid=? order by buyTime desc limit ?,?";


        Class[] classes = {UserGoodsDto.class,Goods.class};

        List<UserGoodsDto> userGoodsDtos = new ArrayList<>();

        List<List> lists = SqlUtils.select(classes,sql,uid,(pager.getPageNow()-1)*pager.getPageSize(),pager.getPageSize());

        for (List list:lists) {
            UserGoodsDto userGoodsDto = (UserGoodsDto) list.get(0);
            Goods goods = (Goods) list.get(1);
            userGoodsDto.setGoods(goods);
            userGoodsDtos.add(userGoodsDto);
        }
        System.out.println(lists);
        return userGoodsDtos;
    }

    @Override
    public int delUserGoods(String guId) {
        String sql = "update userGoods set state=1 where guId=?";
        return SqlUtils.update(sql,guId);
    }

    @Override
    public int selectTotalUserGoodsCountByUidAndSearch(String uid, String searchGoods) {
        String sql = "select count(*) count from userGoods left join goods on userGoods.gid=goods.gid where userGoods.state=0 and uid=? and gname like ?";
        List<Map<String,Object>>list = SqlUtils.select(sql,uid,"%"+searchGoods+"%");
        return Integer.parseInt(list.get(0).get("count")+"");
    }

    @Override
    public List<UserGoodsDto> selectUserGoodsByUidAndSearch(Pager<UserGoodsDto> pager, String uid, String searchGoods) {
        String sql = "select guId,gname,gphoto,num,price,buyTime from userGoods left join goods on userGoods.gid=goods.gid where userGoods.state=0 and uid=? and gname like ? order by buyTime desc limit ?,?";

        Class[] classes = {UserGoodsDto.class,Goods.class};

        List<UserGoodsDto> userGoodsDtos = new ArrayList<>();

        List<List> lists = SqlUtils.select(classes,sql,uid,"%"+searchGoods+"%",(pager.getPageNow()-1)*pager.getPageSize(),pager.getPageSize());

        for (List list:lists) {
            UserGoodsDto userGoodsDto = (UserGoodsDto) list.get(0);
            Goods goods = (Goods) list.get(1);
            userGoodsDto.setGoods(goods);
            userGoodsDtos.add(userGoodsDto);
        }
        return userGoodsDtos;
    }

    @Override
    public List<UserGoodsDto> selectAllUserGoodsByUid(String uid) {
        String sql = "select guId,gname,num,price,buyTime from usergoods left join goods on usergoods.gid=goods.gid where usergoods.state=0 and uid=? order by buyTime desc";
        Class[]classes = {UserGoodsDto.class,Goods.class};
        List<UserGoodsDto> userGoodsDtos = new ArrayList<>();
        List<List> lists= SqlUtils.select(classes,sql,uid);
        for (List l:lists) {
            UserGoodsDto userGoodsDto = (UserGoodsDto) l.get(0);
            Goods goods = (Goods) l.get(1);
            userGoodsDto.setGoods(goods);
            userGoodsDtos.add(userGoodsDto);
        }
        return userGoodsDtos;
    }

    @Override
    public boolean updateUserInfo(User user) {
        return SqlUtils.update(user)>0;
    }

    @Override
    public List<KindsSortDto> getKindsSort() {
        String sql="select count(kinds) value,kinds  from text left join kt on text.tid=kt.tid left join  kinds on kt.kid=kinds.kid where state=0 and ispass=1  GROUP BY kinds";

        List<Map<String, Object>> maps = SqlUtils.select(sql);

        if(maps.size()==0){
            return  null;
        }else {
            List<KindsSortDto> list = new ArrayList<>();
            for(Map map:maps){
                KindsSortDto kindsSortDto = new KindsSortDto();
                kindsSortDto.setValue(Integer.parseInt(map.get("value")+"") );
                kindsSortDto.setName((String) map.get("kinds"));
                list.add(kindsSortDto);
            }
            return list;
        }

    }

    @Override
    public List<TopGoodsDto> getTopGoods() {
        String sql="select gname ,SUM(num) value from usergoods LEFT JOIN goods on goods.gid=usergoods.gid where goods.state=0 GROUP BY gname ORDER BY SUM(num) desc limit 0,5";

        List<TopGoodsDto> topGoods=SqlUtils.select(TopGoodsDto.class,sql);
        return topGoods;
    }

    @Override
    public TextDto selectTextBytid(String tid) {
        String sql = "select * from text left join kt on text.tid=kt.tid left join kinds on kt.kid=kinds.kid where text.tid=?";
        Class[] clazzs = {TextDto.class, Text.class};
        List<List> lists = SqlUtils.select(clazzs, sql, tid);

        for (List list:lists
             ) {
            TextDto textDto = (TextDto) list.get(0);
            Text text= (Text) list.get(1);
            textDto.setText(text);
            return textDto;
        }
        return null;
    }

    @Override
    public List<Goods> pushMessage(String kinds) {
        String sql="select goods.gphoto,goods.gname,goods.gid from goods left join gk on goods.gid=gk.gid left join kinds on gk.kid=kinds.kid where kinds.kinds=?";
        List<Goods> listGoods = SqlUtils.select(Goods.class, sql, kinds);
        return listGoods;
    }

}
