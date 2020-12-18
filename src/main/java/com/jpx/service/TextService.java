package com.jpx.service;

import com.jpx.dto.Pager;
import com.jpx.dto.TextDto;

public interface TextService {
    Pager<TextDto> selectTextPager(Pager<TextDto> pager);
}
