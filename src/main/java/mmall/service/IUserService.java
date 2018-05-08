package mmall.service;

import mmall.common.ServiceReponse;
import mmall.pojo.User;

//接口类
public interface IUserService {
    //登录接口
    ServiceReponse<User> login(String username, String password);
}
