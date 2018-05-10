package mmall.service;

import mmall.common.ServiceReponse;
import mmall.pojo.User;

//接口类
public interface IUserService {
    //登录接口
    ServiceReponse<User> login(String username, String password);

    //注册
    ServiceReponse<String> register(User user);

    //校验用户名，邮箱
    ServiceReponse<String> checkValid(String str, String type);

    //找回密码问题
    ServiceReponse serviceReponse(String username);

    //密码问题答案校验
    ServiceReponse<String> chekAnswer(String username, String question, String answere);

    //修改密码
    ServiceReponse<String> forgetRestPasword(String username, String passwordNew, String forgetToken);
    //登录状态修改密码
    ServiceReponse<String> resetPassword(String password, String passwordNew, User user);
    //更新个人信息
    ServiceReponse<User> updataInformation(User user);
    //获取个人信息
    ServiceReponse<User> getInfomation(Integer userId);
    //恔验是否是管理员
    ServiceReponse checkAdminRole(User user);

}
