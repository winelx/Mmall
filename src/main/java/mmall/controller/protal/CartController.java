package mmall.controller.protal;

import mmall.common.Const;
import mmall.common.ResponseCode;
import mmall.common.ServiceReponse;
import mmall.pojo.User;
import mmall.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private ICartService iCartService;


    @RequestMapping("list.do")
    @ResponseBody
    public ServiceReponse list(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceReponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.
                    getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        return iCartService.list(user.getId());
    }


    //添加购物车
    @RequestMapping("cartvo.do")
    @ResponseBody
    public ServiceReponse add(HttpSession session, Integer count, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceReponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.
                    getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        return iCartService.add(count, user.getId(), productId);
    }

    //更新购物车
    @RequestMapping("update.do")
    @ResponseBody
    public ServiceReponse update(HttpSession session, Integer count, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceReponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.
                    getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        return iCartService.update(count, user.getId(), productId);
    }

    //更新购物车
    @RequestMapping("detele.do")
    @ResponseBody
    public ServiceReponse detele(HttpSession session, String productIds) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceReponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.
                    getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return iCartService.delete(user.getId(), productIds);
    }

    //全选

    @RequestMapping("select_all.do")
    @ResponseBody
    public ServiceReponse selectall(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceReponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.
                    getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return iCartService.selectOrInSelectAll(user.getId(), null, Const.Cart.CHECKED);
    }

    //全反选
    @RequestMapping("un_select_all.do")
    @ResponseBody
    public ServiceReponse unSelectall(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceReponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.
                    getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return iCartService.selectOrInSelectAll(user.getId(), null, Const.Cart.UN_CHECKED);
    }

    //单独选

    @RequestMapping("uselect.do")
    @ResponseBody
    public ServiceReponse lSelectl(HttpSession session, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceReponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.
                    getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return iCartService.selectOrInSelectAll(user.getId(), productId, Const.Cart.CHECKED);
    }


    //单独反选
    @RequestMapping("un_select.do")
    @ResponseBody
    public ServiceReponse unSelectl(HttpSession session, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceReponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.
                    getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return iCartService.selectOrInSelectAll(user.getId(), productId, Const.Cart.UN_CHECKED);
    }

    //查询用户购车网数量
    @RequestMapping("get_cart_product_count.do")
    @ResponseBody
    public ServiceReponse getcartproductcount(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceReponse.createBySuccess(0);
        }
        return iCartService.getCartProductCount(user.getId());
    }
}
