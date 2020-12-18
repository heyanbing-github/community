package com.jpx.dao.impl;

import com.jpx.dao.EmailDao;
import com.jpx.utils.SqlUtils;

public class EmailDaoImpl implements EmailDao {

    @Override
    public boolean changePassword(String username, String newPassword) {
        String sql = "update user set password = ? where username = ?";
        return SqlUtils.update(sql,newPassword,username)>0;
    }
}
