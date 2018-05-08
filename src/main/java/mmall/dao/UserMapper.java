package mmall.dao;

import mmall.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    //查询用户名，在登录时判断用户名是否存
    int checkusername(String username);

    User selectLogin(@Param("username") String username,
                     @Param("password") String password);
}