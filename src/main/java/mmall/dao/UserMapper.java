package mmall.dao;

import mmall.pojo.User;
import org.apache.ibatis.annotations.Param;

/**
 * 这里是封装数据库查询的接口类，对外暴露接口实现对数据库的操作
 */
public interface UserMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    //查询用户名，在登录时判断用户名是否存
    int checkusername(String username);

    //查询邮箱
    int checkuserEmail(String email);

    //登录
    User selectLogin(@Param("username") String username,
                     @Param("password") String password);

    //
    String selectQusetionByUsername(String username);

    //
    int chekAnswer(@Param("username") String username, @Param("question") String question, @Param("anwer") String anwer);

    //修改密码
    int updatePasswordByUsername(@Param("username") String username, @Param("passwordNew") String passwordNew);

    //登录状态下修改密码
    int checkPassword(@Param("password") String password, @Param("userId") Integer userId);
    //查询用户邮箱和用户ID
    int checkEmailByUserId(@Param("email")String email,@Param("userId")Integer userId);
}