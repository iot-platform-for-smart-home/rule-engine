package com.tjlcast.updateMessagePlugin.mapper;

import com.tjlcast.updateMessagePlugin.data.UpdateMessage;
import org.apache.ibatis.annotations.*;

import java.math.BigInteger;
import java.util.List;

@Mapper
public interface UpdateMessageMapper {
    @Insert("INSERT INTO update_message (message, messageType, ts, tenantId) VALUES (#{message}, #{messageType}, #{ts}, #{tenantId})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int addAMessage(UpdateMessage updateMessage);

    @Select("SELECT * FROM update_message WHERE tenantId = #{tenantId}")
    List<UpdateMessage> getAllMessage(@Param("tenantId") Integer tenantId);

    @Select("SELECT * FROM update_message WHERE messageType = #{messageType} AND tenantId = #{tenantId}")
    List<UpdateMessage> getMessageByType(@Param("messageType") String messageType, @Param("tenantId") Integer tenantId);

    @Select("SELECT * FROM update_message WHERE id = #{id} AND tenantId = #{tenantId}")
    UpdateMessage getMessageById(@Param("id") Integer id, @Param("tenantId") Integer tenantId);

    @Delete("DELETE FROM update_message")
    void removeAllMessage();

    @Delete("DELETE FROM update_message WHERE id = #{id}")
    void removeMessageById(@Param("id") Integer id);

    @Select("SELECT * FROM update_message WHERE messageType = 'fromWeb' AND tenantId = #{tenantId} ORDER BY id DESC LIMIT 20")
    List<UpdateMessage> getFromWebMessage(@Param("tenantId") Integer tenantId);

    @Select("SELECT * FROM update_message WHERE messageType = 'fromModule' AND tenantId = #{tenantId} ORDER BY id DESC LIMIT 20")
    List<UpdateMessage> getFromModuleMessage(@Param("tenantId") Integer tenantId);

    @Select("SELECT * FROM update_message WHERE tenantId = #{tenantId} AND ts BETWEEN #{startTs} AND #{endTs}")
    List<UpdateMessage> getTsMessage(@Param("tenantId") Integer tenantId, @Param("startTs") BigInteger startTs, @Param("endTs") BigInteger endTs);
}
