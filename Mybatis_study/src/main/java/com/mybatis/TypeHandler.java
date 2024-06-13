package com.mybatis;

import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface TypeHandler<T> {
    void setParam(PreparedStatement statement,int index,T value) throws SQLException;
    Object  getParam(ResultSet resultSet, String cloumn) throws SQLException;
}
