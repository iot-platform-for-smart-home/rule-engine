package com.tjlcast.server.typeHandler;

import org.apache.ibatis.type.*;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by tangjialiang on 2018/4/23.
 */

@MappedTypes({UUID.class})
@MappedJdbcTypes({JdbcType.VARCHAR})
public class MyUUIDTypeHandler extends BaseTypeHandler<UUID> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, UUID uuid, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, uuid.toString());
    }

    @Override
    public UUID getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return UUID.fromString(resultSet.getString(s)) ;
    }

    @Override
    public UUID getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return UUID.fromString(resultSet.getString(i)) ;
    }

    @Override
    public UUID getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return UUID.fromString(callableStatement.getString(i)) ;
    }
}

