package com.jpx.dao.impl;

import com.jpx.dao.TextDao;
import com.jpx.dto.Pager;
import com.jpx.dto.TextDto;
import com.jpx.entity.Text;
import com.jpx.entity.User;
import com.jpx.utils.SqlUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TextDaoImpl implements TextDao {
    @Override
    public int getTextTotalCount() {
        String sql = "select count(tid) c from text";
        List<Map<String, Object>> maps = SqlUtils.select(sql);
        return Integer.parseInt(maps.get(0).get("c")+"");
    }

    @Override
    public List<Text> selectText(Pager<TextDto> pager) {
        String sql = "select * from text left join user on text.uid=user.uid where text.state=0 and text.ispass=1 order by sendTime desc limit ?,?";
        Class[] classes = {Text.class, User.class};
        List<Text> texts = new ArrayList<>();
        List<List> lists = SqlUtils.select(classes,sql,(pager.getPageNow()-1)*pager.getPageSize(),pager.getPageSize());
        for (List list:lists) {
            Text text = (Text) list.get(0);
            User user = (User) list.get(1);
            text.setUser(user);
            texts.add(text);
        }
        return texts;
    }

    @Override
    public String getKinds(String tid) {
        String sql = "select kinds from kt left join kinds on kt.kid=kinds.kid where tid=?";
        List<Map<String, Object>> select = SqlUtils.select(sql, tid);
        return select.get(0).get("kinds")+"";
    }
}
