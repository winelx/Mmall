package mmall.service.Impl;

import mmall.common.Const;
import mmall.common.ServiceReponse;
import mmall.common.TokenCache;
import mmall.dao.UserMapper;
import mmall.pojo.User;
import mmall.service.IUserService;
import mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * 这里是接口拿到数据后具体的操作，所以这里是modle
 */
@Service("iUserService")
public class IUserServiceIpml implements IUserService {
    @Autowired
    private UserMapper userMapper;

    /**
     * 实现接口，并实现接口的方法
     */
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

    /**
     * 注册
     */
    public ServiceReponse<String> register(User user) {
        //查询用户名是否存在
        ServiceReponse validReponse = this.chekValid(user.getUsername(), Const.USERNAME);
        if (!validReponse.isSuccess()) {
            return validReponse;
        }
        //查询邮箱是否存在
        validReponse = this.chekValid(user.getEmail(), Const.EMAIL);
        if (!validReponse.isSuccess()) {
            return validReponse;
        }
        //设置用户权限
        user.setRole(Const.Role.ROLE_CUSTOMER);
        //MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int resultCount = userMapper.insert(user);
        if (resultCount == 0) {
            return ServiceReponse.createByErrorMessage("注册失败");
        }
        return ServiceReponse.createBySuccess("登录成功");
    }

    /**
     * 校验的具体实现
     *
     * @param str
     * @param type
     * @return
     */
    public ServiceReponse<String> chekValid(String str, String type) {
        if (org.apache.commons.lang3.StringUtils.isNoneBlank(type)) {
            //开始用户名校验
            if (Const.USERNAME.equals(type)) {
                int resultCount = userMapper.checkusername(str);
                if (resultCount > 0) {
                    return ServiceReponse.createByErrorMessage("用户名已存在");
                }
            }
            //开始校验邮箱
            if (Const.EMAIL.equals(type)) {
                int resultCount = userMapper.checkuserEmail(str);
                if (resultCount > 0) {
                    return ServiceReponse.createByErrorMessage("email已存在");
                }
            }
        } else {
            return ServiceReponse.createByErrorMessage("参数错误");
        }
        return ServiceReponse.createByErrorMessage("校验成功");
    }

    /**
     * 找回密码问题
     *
     * @param username
     * @return
     */
    public ServiceReponse serviceReponse(String username) {
        ServiceReponse validResponse = this.chekValid(username, Const.USERNAME);
        if (validResponse.isSuccess()) {
            return ServiceReponse.createByErrorMessage("用户不存在");
        }
        String questiog = userMapper.selectQusetionByUsername(username);
        if (StringUtils.isNoneBlank(questiog)) {
            return ServiceReponse.createBySuccess(questiog);
        }
        return ServiceReponse.createByErrorMessage("找回密码问题为空");
    }

    /**
     * 密码问题校验
     */
    public ServiceReponse<String> chekAnswer(String username, String question, String answere) {
        int resultCount = userMapper.chekAnswer(username, question, answere);
        if (resultCount > 0) {
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX + username, forgetToken);
            return ServiceReponse.createBySuccess(forgetToken);
        }
        return ServiceReponse.createByErrorMessage("问题答案错误");
    }

    /**
     * 修改密码
     */
    public ServiceReponse<String> forgetRestPasword(String username, String passwordNew, String forgetToken) {
        if (StringUtils.isBlank(forgetToken)) {
            return ServiceReponse.createByErrorMessage("参数错误，Token需要传递");
        }
        ServiceReponse validResponse = this.chekValid(username, Const.USERNAME);
        if (validResponse.isSuccess()) {
            return ServiceReponse.createByErrorMessage("用户不存在");
        }
        String token = TokenCache.getkey(TokenCache.TOKEN_PREFIX + username);
        if (StringUtils.isBlank(token)) {
            return ServiceReponse.createByErrorMessage("token无效或者过期");
        }
        if (StringUtils.equals(forgetToken, token)) {
            String MD5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            int rowCount = userMapper.updatePasswordByUsername(username, MD5Password);
            if (rowCount > 0) {
                return ServiceReponse.createBySuccess("密码修改成功");
            }
        } else {
            return ServiceReponse.createByErrorMessage("token错误，请重新获取token");
        }
        return ServiceReponse.createByErrorMessage("修改密码失败");
    }

    /**
     * 登录状态下修改密码
     */
    public ServiceReponse<String> resetPassword(String password, String passwordNew, User user) {
        //防止横向越权，需要校验一下这个用户的旧密码。一定要指定是这个用户，因为我们会查询一个count（1）
        //如果不指定Id，那么结果就是true
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(password), user.getId());
        if (resultCount == 0) {
            return ServiceReponse.createByErrorMessage("旧密码错误");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount > 0) {
            return ServiceReponse.createBySuccess("密码更新成功");
        }
        return ServiceReponse.createByErrorMessage("密码更新失败");
    }
    /**
     * 修改用户信息
     */
    public  ServiceReponse<User> updataInformation(User user){
        //username是不能更新的
        //email需要需要进行恔烟，恔验新的email是不是已经存在，并且存在的email如果相同，也是不能使用的
        int resultCount=userMapper.checkEmailByUserId(user.getEmail(),user.getId());
        if (resultCount>0){
            return ServiceReponse.createByErrorMessage("邮箱已存在");
        }
        User updateUser=new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        int updataCount =userMapper.updateByPrimaryKeySelective(updateUser);
        if (updataCount>0){
            return ServiceReponse.createBySuccess("更新成功");
        }
        return ServiceReponse.createByErrorMessage("更新失败");

    }
    /**
     * 获取用户信息
     *
     */
    public ServiceReponse<User> getInfomation(Integer userId){
        User user =userMapper.selectByPrimaryKey(userId);
        if (user==null){
           return ServiceReponse.createByErrorMessage("找不到当前用户");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServiceReponse.createBySuccess(user);
    }
    /**
     * 恔验是否是管理员
     */
    public  ServiceReponse checkAdminRole(User user){
        if (user!=null&& user.getRole().intValue()==Const.Role.ROLE_ADMIN){
            return ServiceReponse.createBySuccess();
        }
        return ServiceReponse.createByError();
    }
}
