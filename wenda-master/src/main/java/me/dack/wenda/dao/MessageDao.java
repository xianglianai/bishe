package me.dack.wenda.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import me.dack.wenda.model.Message;

@Mapper
public interface MessageDao {

	String TABLE_NAME ="message";
	String INSERT_FIELDS = " from_id, to_id, content, create_time,has_read,conversation_id";
	String SELECT_FIELDS = " id," + INSERT_FIELDS;
	
	@Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,
		") values(#{fromId},#{toId},#{content},#{createTime},#{hasRead},#{conversationId})"})
	public int addMessage(Message message);
	
	
	@Select({"select",SELECT_FIELDS,"from",TABLE_NAME,
		"where conversation_id=#{conversationId} order by create_time desc limit #{offset},#{limit}"})
	public List<Message> getConversationDetail(@Param("conversationId") String conversationId,
			@Param("offset") int offset,@Param("limit")int limit);
	
	@Select({"select",INSERT_FIELDS,",count(id) as id from (select ",SELECT_FIELDS,"from",TABLE_NAME,
		"where from_id=#{userId} or to_id=#{userId} order by create_time desc) t group by conversation_id order by create_time desc"
		, "limit #{offset},#{limit}"})
	public List<Message> getConversationList(@Param("userId")int userId,
			@Param("offset")int offset,@Param("limit")int limit);
	
	@Select({"select count(id) from",TABLE_NAME,"where has_read = 0 and to_id=#{userId} and conversation_id=#{conversationId}"})
	public int getConversationUnReadCount(@Param("userId")int userId,@Param("conversationId")String conversationId);
	
	@Update({"update",TABLE_NAME,"set has_read = 1 where to_id=#{userId} and conversation_id=#{conversationId}"})
	public int updateConversationHasRead(@Param("userId")int userId,@Param("conversationId")String conversationId);
}
