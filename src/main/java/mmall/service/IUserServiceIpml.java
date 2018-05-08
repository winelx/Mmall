package mmall.service;

import mmall.common.ServiceReponse;
import mmall.dao.UserMapper;
import mmall.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("iUserService")
public class IUserServiceIpml implements IUserService {
        @Autowired
        private UserMapper userMapper;


    //实现接口，并实现接口的方法
    @Override
    public ServiceReponse<User> login(String username, String password) {
        //查询用户名是否存在
        int resultCount =userMapper.checkusername(username);
        if (resultCount==0){
            return ServiceReponse.createByErrorMessage("用户名不存在");
        }
        //
        User user =userMapper.selectLogin(username,password);
        if (user==null){
            return ServiceReponse.createByErrorMessage("密码错误");
        }
            user.setPassword(org.apache.commons.lang3.StringUtils.EMPTY);

        return ServiceReponse.createBySuccess("登录成功",user);
    }
}
