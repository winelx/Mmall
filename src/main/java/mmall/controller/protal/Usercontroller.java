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

}
