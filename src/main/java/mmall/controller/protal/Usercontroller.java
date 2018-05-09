package mmall.controller.protal;


import mmall.common.Const;
import mmall.common.ServiceReponse;
import mmall.pojo.User;
import mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * 中间层，将方法具体操封装，这里只调用它对外暴露的方法，面向对象编程
 */
@Controller
@RequestMapping("/user/")
public class Usercontroller {
    @Autowired
    private IUserService iUserService;

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @param session
     * @return
     */
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceReponse<User> Login(String username, String password, HttpSession session) {
        ServiceReponse<User> reponse = iUserService.login(username, password);
        if (reponse.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, reponse.getData());
        }
        return reponse;
    }

    /**
     * 退出登录
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "logout.do", method = RequestMethod.GET)
    @ResponseBody
    public ServiceReponse<String> Logout(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return ServiceReponse.createBySuccess();
    }

    /**
     * 注册
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "register.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceReponse<String> register(User user) {
        return iUserService.register(user);
    }

    /**
     * 校验接口
     */
    @RequestMapping(value = "check_valid.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceReponse<String> checkValid(String str, String type) {
        return iUserService.chekValid(str, type);
    }

    /**
     * 是否登录
     */
    @RequestMapping(value = "get_user_info.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceReponse<User> getUserInfo(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null) {
            return ServiceReponse.createBySuccess(user);
        }
        return ServiceReponse.createByErrorMessage("用户未登录，无法获取当前用户信息");
    }

    /**
     * 密码提示问题获取
     */
    @RequestMapping(value = "forget_get_qusetion.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceReponse<String> forgetGetQuestion(String username) {
        return iUserService.serviceReponse(username);
    }

    /**
     * 校验密码问题是否正确
     */
    @RequestMapping(value = "forget_check_answer.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceReponse<String> forgetCheckAnswer(String username, String question, String answer) {
        return iUserService.chekAnswer(username, question, answer);
    }

    /**
     * 修改密码
     *
     * @param username
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    @RequestMapping(value = "forget_rest_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceReponse<String> forgetRestPassword(String username, String passwordNew, String forgetToken) {

        return iUserService.forgetRestPasword(username, passwordNew, forgetToken);
    }

    /**
     * 登录状态修改密码
     */
    @RequestMapping(value = "rest_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceReponse<String> resetPasword(HttpSession session, String password, String passwordNew) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceReponse.createByErrorMessage("用户未登录");
        }
        return iUserService.resetPassword(password, passwordNew, user);
    }
}
