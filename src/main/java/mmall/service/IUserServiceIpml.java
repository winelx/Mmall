package mmall.service;

import mmall.common.Const;
import mmall.common.ServiceReponse;
import mmall.dao.UserMapper;
import mmall.pojo.User;
import mmall.util.MD5Util;
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
        int resultCount = userMapper.checkusername(username);
        if (resultCount == 0) {
            return ServiceReponse.createByErrorMessage("用户名不存在");
        }
        //
        String Md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, Md5Password);
        if (user == null) {
            return ServiceReponse.createByErrorMessage("密码错误");
        }
        user.setPassword(org.apache.commons.lang3.StringUtils.EMPTY);

        return ServiceReponse.createBySuccess("登录成功", user);
    }

    public ServiceReponse<String> register(User user) {
        //查询用户名是否存在
        int resultCount = userMapper.checkusername(user.getUsername());
        if (resultCount > 0) {
            return ServiceReponse.createByErrorMessage("用户名已存在");
        }
        //查询邮箱是否存在
        resultCount = userMapper.checkuserEmail(user.getEmail());
        if (resultCount > 0) {
            return ServiceReponse.createByErrorMessage("邮箱已存在");
        }
        //设置用户权限
        user.setRole(Const.Role.ROLE_CUSTOMER);
        //MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        resultCount = userMapper.insert(user);
        if (resultCount == 0) {
            return ServiceReponse.createByErrorMessage("注册失败");
        }
        return ServiceReponse.createBySuccess("登录成功");
    }
}
