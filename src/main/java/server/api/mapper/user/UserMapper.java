package server.api.mapper.user;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import server.framework.custom.CustomMap;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    CustomMap selectUser00000(@Param("userId") String userId) throws Exception;

    @Select("SELECT username, password, role FROM user WHERE username = #{username}")
    CustomMap findByUsername(@Param("username") String username) throws Exception;
}
