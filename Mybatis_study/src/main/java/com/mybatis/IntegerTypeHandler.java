package com.mybatis;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IntegerTypeHandler implements TypeHandler<Integer>{

    @Override
    public void setParam(PreparedStatement statement, int index, Integer value) throws SQLException {
        statement.setInt(index,value);
    }

    @Override
    public Object getParam(ResultSet resultSet, String cloumn) throws SQLException {
        return resultSet.getInt(cloumn);
    }

}
