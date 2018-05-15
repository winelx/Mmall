package mmall.controller.protal;

import mmall.common.Const;
import mmall.common.ResponseCode;
import mmall.common.ServiceReponse;
import mmall.pojo.Shipping;
import mmall.pojo.User;
import mmall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/shipping/")
public class ShippingController {
    @Autowired
    private IShippingService iShippingService;

    /**
     * 添加地址
     *
     * @param session  用户信息，判断用户是登录
     * @param shipping 地址对象
     * @return
     */
    @RequestMapping("add.do")
    @ResponseBody
    public ServiceReponse add(HttpSession session, Shipping shipping) {
        //拿到用户信息
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        //判断用户信息是否为空
        if (user == null) {
            //如果为空
            return ServiceReponse.createByErrorMessage("请先登录");
        }
        return iShippingService.add(user.getId(), shipping);
    }

    /**
     * @param session
     * @param shippingId 地址id
     * @return
     */
    @RequestMapping("del.do")
    @ResponseBody
    public ServiceReponse del(HttpSession session, Integer shippingId) {
        //避免横向越权 //判断用户是否登录
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceReponse.createByError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.del(user.getId(), shippingId);
    }

    /**
     * 更新
     *
     * @param session
     * @param shipping 对象
     * @return
     */
    @RequestMapping("updata.do")
    @ResponseBody
    public ServiceReponse updata(HttpSession session, Shipping shipping) {
        //避免横向越权 //判断用户是否登录
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceReponse.createByError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.updata(user.getId(), shipping);
    }

    /**
     * 查询
     *
     * @param session
     * @param shippingId
     * @return
     */
    @RequestMapping("select.do")
    @ResponseBody
    public ServiceReponse select(HttpSession session, Integer shippingId) {
        //避免横向越权 //判断用户是否登录
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceReponse.createByError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.select(user.getId(), shippingId);
    }

    /**
     * 地址列表
     * @param pageNum
     * @param pageSize
     * @param session
     * @return
     */
    @RequestMapping("list.do")
    @ResponseBody
    public ServiceReponse list(@RequestParam(value = "pageNUm", defaultValue = "1") int pageNum,
                               @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                               HttpSession session) {
        //避免横向越权 //判断用户是否登录
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceReponse.createByError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.list(user.getId(), pageNum, pageSize);

    }
}
