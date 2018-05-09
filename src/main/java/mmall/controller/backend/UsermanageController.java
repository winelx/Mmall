package mmall.controller.backend;

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
@RequestMapping("/manage/user")
public class UsermanageController {
    @Autowired
    private IUserService iUserService;
    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    @ResponseBody
    public ServiceReponse<User> login(String username, String password, HttpSession session){
        ServiceReponse<User> reponse=iUserService.login(username, password);
        if (reponse.isSuccess()){
            User user =reponse.getData();
            if (user.getRole()==Const.Role.ROLE_ADMIN){
                session.setAttribute(Const.CURRENT_USER,user);
                return reponse;
            }else {
                return ServiceReponse.createByErrorMessage("不是管理员,无法登录");
            }
        }
        return  reponse;
    }
}
