package com.jpx.service.impl;

import com.jpx.dao.EmailDao;
import com.jpx.dao.impl.EmailDaoImpl;
import com.jpx.service.EmailService;

public class EmailServiceImpl implements EmailService {
    EmailDao emailDao = new EmailDaoImpl();
    @Override
    public boolean changePassword(String username, String newPassword) {
        return emailDao.changePassword(username,newPassword);
    }
}
