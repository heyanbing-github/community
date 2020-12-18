package com.jpx.service.impl;

import com.jpx.dao.TextDao;
import com.jpx.dao.impl.TextDaoImpl;
import com.jpx.dto.Pager;
import com.jpx.dto.TextDto;
import com.jpx.entity.Text;
import com.jpx.service.TextService;

import java.util.ArrayList;
import java.util.List;

public class TextServiceImpl implements TextService {

    TextDao textDao = new TextDaoImpl();

    @Override
    public Pager<TextDto> selectTextPager(Pager<TextDto> pager) {
        //总数量
        int totalCount = textDao.getTextTotalCount();
        pager.setTotalCount(totalCount);
        pager.setPageCount((totalCount-1)/pager.getPageSize()+1);

        List<TextDto> textDtos = new ArrayList<>();

        List<Text> texts = textDao.selectText(pager);
        for (Text text : texts){
            TextDto textDto = new TextDto();
            textDto.setText(text);
            textDto.setKinds(textDao.getKinds(text.getTid()));
            textDtos.add(textDto);
        }
        pager.setData(textDtos);

        return pager;
    }
}
