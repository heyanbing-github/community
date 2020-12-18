package com.jpx.dao;

import com.jpx.dto.Pager;
import com.jpx.dto.TextDto;
import com.jpx.entity.Text;

import java.util.List;

public interface TextDao {
    int getTextTotalCount();

    List<Text> selectText(Pager<TextDto> pager);

    String getKinds(String tid);
}
