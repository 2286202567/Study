package com.mybatis;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StringTypeHandler implements TypeHandler<String>{

    @Override
    public void setParam(PreparedStatement statement, int index, String value) throws SQLException {
        statement.setString(index,value);
    }

    @Override
    public Object getParam(ResultSet resultSet, String cloumn) throws SQLException {
        return resultSet.getString(cloumn);
    }

}
