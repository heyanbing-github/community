package com.jpx.dao.impl;

import com.jpx.dao.AdminDao;
import com.jpx.dto.Pager;
import com.jpx.dto.TextDto;
import com.jpx.dto.UserDto;
import com.jpx.entity.Admin;
import com.jpx.entity.Goods;
import com.jpx.entity.Notice;
import com.jpx.entity.User;
import com.jpx.entity.*;
import com.jpx.utils.SqlUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminDaoImpl implements AdminDao {


    @Override
    public int selectUserCount() {
        String sql="select count(*) c from user left join score on user.uid=score.uid where state=0";
        List<Map<String, Object>> maps = SqlUtils.select(sql);
        return Integer.parseInt(maps.get(0).get("c")+"");
    }

    @Override
    public List<UserDto> selectUser(Pager<UserDto> pager) {
        String sql="select user.uid, username,nickName,email,sex,score from user left join score on user.uid=score.uid where state=0  limit ?,?";
        Class classes[]={UserDto.class, User.class};
        List<List> lists = SqlUtils.select(classes, sql,(pager.getPageNow()-1)*pager.getPageSize(),pager.getPageSize());
        List<UserDto> userDtos = new ArrayList<>();
        for (List list:lists){
            UserDto userDto= (UserDto) list.get(0);
            User user= (User) list.get(1);
            userDto.setUser(user);
            userDtos.add(userDto);
        }
        return userDtos;
    }

    @Override
    public int deleteUser(String uid) {
        String sql="update user set state=1 where uid=?";
        return SqlUtils.update(sql,uid);
    }

    @Override
    public int deleteScore(String uid) {
        String sql="delete from score where uid=?";
        return SqlUtils.update(sql,uid);
    }

    @Override
    public int updateUser(User user) {
        String sql="update user set username=?,nickName=?,email=?,sex=? where uid=?";
        return SqlUtils.update(sql,user.getUsername(),user.getNickName(),user.getEmail(),user.getSex(),user.getUid());
    }

    @Override
    public int updateScore(String uid, int score) {
        String sql="update score set score=? where uid=?";
        return SqlUtils.update(sql,score,uid);
    }

    @Override
    public Admin loginByAdmin(String username, String password) {
        String sql = "select mid ,username, password ,phone,email,sex,uphoto from admin where username=? and password =?";
        return SqlUtils.selectOne(Admin.class,sql,username,password);
    }

    @Override
    public int insertNotice(Notice not) {
        String sql = "insert into notice (nid, mid, sendTime, content) values (?,?,?,?)";
        return SqlUtils.update(sql, not.getNid(), not.getAdmin().getMid(), not.getSendTime(), not.getContent());
    }

    @Override
    public List<List> getFourNewNotice() {
        String sql = "select admin.username,content,sendTime from admin right join notice on admin.mid=notice.mid order by sendTime desc limit 0,4";
        Class[] clazzs = {Admin.class, Notice.class};
        List<List> lists = SqlUtils.select(clazzs, sql);

        return lists;
    }

    @Override
    public int selectTotalNoticeCount() {
        String sql = "select count(nid) c from notice";
        List<Map<String,Object>>list = SqlUtils.select(sql);
        if (list.get(0).get("c") == null){
            return 0;
        }
        return Integer.parseInt(list.get(0).get("c")+"");
    }

    @Override
    public List<Notice> selectNotice(Pager<Notice> pager) {
        String sql = "select notice.*,admin.username,admin.mid from notice left join admin on notice.mid=admin.mid order by sendTime desc limit ?,?";
        Class[] classes = {Notice.class,Admin.class};
        List<Notice> notices = new ArrayList<>();
        List<List> lists = SqlUtils.select(classes,sql,(pager.getPageNow()-1)*pager.getPageSize(),pager.getPageSize());
        for (List list:lists) {
            Notice notice = (Notice) list.get(0);
            Admin admin = (Admin) list.get(1);
            notice.setAdmin(admin);
            notices.add(notice);
        }
        return notices;
    }

    @Override
    public boolean deleteNotice(String nid) {
        String sql = "delete from notice where nid=?";
        return SqlUtils.update(sql,nid)>0;
    }

    @Override
    public boolean updateNotice(String nid, String mid, String content) {
        String sql = "update notice set content=?,mid=? where nid=?";
        return SqlUtils.update(sql, content, mid, nid)>0;
    }

    @Override
    public int selectTotalNoticeCountByKey(String searchText) {
        String sql = "select count(nid) c from notice where content like ?";
        List<Map<String,Object>>list = SqlUtils.select(sql, "%"+searchText+"%");
        if (list.get(0).get("c") == null){
            return 0;
        }
        return Integer.parseInt(list.get(0).get("c")+"");
    }

    @Override
    public List<Notice> selectNoticeByKey(Pager<Notice> pager, String searchText) {
        String sql = "select notice.*,admin.username,admin.mid from notice left join admin on notice.mid=admin.mid  where content like ? order by sendTime desc limit ?,?";
        Class[] classes = {Notice.class,Admin.class};
        List<Notice> notices = new ArrayList<>();
        List<List> lists = SqlUtils.select(classes,sql,"%"+searchText+"%",(pager.getPageNow()-1)*pager.getPageSize(),pager.getPageSize());

        for (List list:lists) {
            Notice notice = (Notice) list.get(0);
            Admin admin = (Admin) list.get(1);
            notice.setAdmin(admin);
            notices.add(notice);
        }
        return notices;
    }

    @Override
    public int insertGoods(Goods goods) {
        String sql = "insert into goods (gid,mid,gname,gphoto,gnum,price,count,score,state) values (?,?,?,?,?,?,?,1,0)";
        return SqlUtils.update(sql, goods.getGid(),goods.getAdmin().getMid(),goods.getGname(),goods.getGphoto(),goods.getGnum(),goods.getPrice(),goods.getCount());
    }

    @Override
    public List<UserDto> searchUser(String username) {
        String sql="select user.uid, username,nickName,email,sex,score from user left join score on user.uid=score.uid where state=0  and username=?";
        Class classes[]={UserDto.class, User.class};
        List<List> lists = SqlUtils.select(classes, sql,username);
        if(lists.size()==0){
            return  null;
        }else {
            List<UserDto> userDtos = new ArrayList<>();
            for (List list:lists){
                UserDto userDto= (UserDto) list.get(0);
                User user= (User) list.get(1);
                userDto.setUser(user);
                userDtos.add(userDto);
            }
            return userDtos;
        }

    }

    @Override
    public int selectGoodsCount() {
        String sql="select count(*) c from goods left join admin on goods.gid=admin.mid ";
        List<Map<String, Object>> maps = SqlUtils.select(sql);
        return Integer.parseInt(maps.get(0).get("c")+"");
    }

    @Override
    public List<Goods> selectGoods(Pager<Goods> pager) {
        String sql="select gid,gnum,gname,gphoto,score,price,count,state,admin.username from goods left join admin on goods.mid=admin.mid limit ?,?";
        Class classes[]={Goods.class, Admin.class};
        List<List> lists = SqlUtils.select(classes, sql,(pager.getPageNow()-1)*pager.getPageSize(),pager.getPageSize());
        List<Goods> goodses = new ArrayList<>();
        for (List list:lists){
          Goods goods= (Goods) list.get(0);
          Admin admin= (Admin) list.get(1);
          goods.setAdmin(admin);
          goodses.add(goods);
        }
        return goodses;
    }

    @Override
    public int updateGoods(String gid, int state, String mid) {
        String sql="update goods set mid=?,state=? where gid=?";
        return SqlUtils.update(sql,mid,state,gid);
    }

    @Override
    public int searchGoodsCount(String gname) {
        String sql="select count(*) c from goods left join admin on goods.mid=admin.mid where gname like ? ";
        List<Map<String, Object>> maps = SqlUtils.select(sql,"%"+gname+"%");
        return Integer.parseInt(maps.get(0).get("c")+"");
    }

    @Override
    public List<Goods> searchGoods(Pager<Goods> pager, String gname) {

        String sql="select gid,gnum,gname,gphoto,score,price,count,state,admin.username from goods left join admin on goods.mid=admin.mid where gname like ? limit ?,?";
        Class classes[]={Goods.class, Admin.class};
        List<List> lists = SqlUtils.select(classes, sql,"%"+gname+"%",(pager.getPageNow()-1)*pager.getPageSize(),pager.getPageSize());
        List<Goods> goodses = new ArrayList<>();
        for (List list:lists){
            Goods goods= (Goods) list.get(0);
            Admin admin= (Admin) list.get(1);
            goods.setAdmin(admin);
            goodses.add(goods);
        }
        return goodses;
    }

    @Override
    public int selectTextCount() {

        String sql="select count(*) c from text where  state=0 and ispass=1";
        List<Map<String, Object>> maps = SqlUtils.select(sql);
        return Integer.parseInt(maps.get(0).get("c")+"");
    }

    @Override
    public List<TextDto> showTextPass(Pager<TextDto> pager) {
        String sql="select text.tid, title,sendTime,score,content,kinds  from text left join kt on text.tid=kt.tid left join  kinds on kt.kid=kinds.kid where state=0 and ispass=1  limit ?,?";
        Class classes[]={TextDto.class, Text.class};
        List<List> lists = SqlUtils.select(classes, sql,(pager.getPageNow()-1)*pager.getPageSize(),pager.getPageSize());
       if(lists.size()==0){
           return  null;
       }else {
           List<TextDto> textDtos = new ArrayList<>();
           for (List list:lists){
               TextDto textDto= (TextDto) list.get(0);
               Text text= (Text) list.get(1);
               textDto.setText(text);
               textDtos.add(textDto);
           }
           return textDtos;
       }
    }

    @Override
    public int updateIspass(String tid, int ispass) {

        String sql="update text set ispass=? where tid=?";
        return SqlUtils.update(sql,ispass,tid);
    }

    @Override
    public int searchTextByKindsCount(String kinds) {
        String sql="select count(*) c  from text left join kt on text.tid=kt.tid left join  kinds on kt.kid=kinds.kid where state=0 and ispass=1 and kinds like ?";
        List<Map<String, Object>> maps = SqlUtils.select(sql,"%"+kinds+"%");
        return Integer.parseInt(maps.get(0).get("c")+"");
    }

    @Override
    public List<TextDto> searchTextByKinds(Pager<TextDto> pager, String kinds) {
        String sql="select text.tid, title,sendTime,score,content,kinds from text left join kt on text.tid=kt.tid left join  kinds on kt.kid=kinds.kid where state=0 and ispass=1 and kinds like ?  limit ?,?";
        Class classes[]={TextDto.class, Text.class};
        List<List> lists = SqlUtils.select(classes, sql,"%"+kinds+"%",(pager.getPageNow()-1)*pager.getPageSize(),pager.getPageSize());
        if(lists.size()==0){
            return  null;
        }else {
            List<TextDto> textDtos = new ArrayList<>();
            for (List list:lists){
                TextDto textDto= (TextDto) list.get(0);
                Text text= (Text) list.get(1);
                textDto.setText(text);
                textDtos.add(textDto);
                System.out.println(text);
            }
            return textDtos;
        }
    }

    @Override
    public int showFilePassCount() {
        String sql="select count(*) c from file where state=0 and ispass=1";
        List<Map<String, Object>> maps = SqlUtils.select(sql);
        return Integer.parseInt(maps.get(0).get("c")+"");
    }

    @Override
    public List<File> showFilePass(Pager<File> pager) {
        String sql="select fid, fname, context, size, score,sendTime from file where state=0 and ispass=1  limit ?,?";
        List<File> list = SqlUtils.select(File.class, sql, (pager.getPageNow() - 1) * pager.getPageSize(), pager.getPageSize());
        return  list;
    }

    @Override
    public int updateFilepass(String fid, int ispass) {
        String sql="update file set ispass=? where fid=?";
        return SqlUtils.update(sql,ispass,fid);
    }

    @Override
    public int editGoods(String gid, int price, int score, int count) {
        String sql="update goods set price=?,score=?,count=? where gid=?";
        return SqlUtils.update(sql,price,score,count,gid);
    }

    @Override
    public Text getTextByTid(String tid) {
        String sql = "select * from text left join user on text.uid=user.uid where text.state=0 and text.ispass=1 and tid=?";
        Class[] classes = {Text.class, User.class};
        List<List> lists = SqlUtils.select(classes,sql,tid);
        Text text = (Text) lists.get(0).get(0);
        User user = (User) lists.get(0).get(1);
        text.setUser(user);
        return text;
    }
}
