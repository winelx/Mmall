package mmall.controller.backend;

import mmall.common.Const;
import mmall.common.ResponseCode;
import mmall.common.ServiceReponse;
import mmall.pojo.Product;
import mmall.pojo.User;
import mmall.service.IProductService;
import mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/product")
public class ProductManageController {
    @Autowired
    private IUserService iUserService;
    @Autowired
    private IProductService iProductService;

    /**
     * 新增更新产品
     */
    @RequestMapping("save.do")
    @ResponseBody
    public ServiceReponse productSave(HttpSession session, Product product) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceReponse.createByError(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请先登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //增加产品的登录逻辑
            return iProductService.saveOtUpdataProduct(product);
        } else {
            return ServiceReponse.createByErrorMessage("无权限操作");
        }
    }

    /**
     * 产品上下架状态
     */

    @RequestMapping("set_sale_status.do")
    @ResponseBody
    public ServiceReponse setSaleStatus(HttpSession session, Integer poductId, Integer status) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceReponse.createByError(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请先登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //修改产品状态，是否
            return iProductService.setSaleStatus(poductId, status);
        } else {
            return ServiceReponse.createByErrorMessage("无权限操作");
        }
    }


    /**
     * 获取产品详情
     * @param session
     * @param poductId
     * @return
     * 已验证
     */
    @RequestMapping("detail.do")
    @ResponseBody
    public ServiceReponse getDetail(HttpSession session, Integer poductId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceReponse.createByError(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请先登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //填充业务
            return iProductService.mangerProductDetail(poductId);
        } else {
            return ServiceReponse.createByErrorMessage("无权限操作");
        }
    }

    /**
     * 界面数据（做分页处理）
     *
     * @param session
     * @param pageNum
     * @param pageSize
     * @return
     * 已验证
     */
    @RequestMapping("list.do")
    @ResponseBody
    public ServiceReponse getList(HttpSession session, @RequestParam(value = "pageNum", defaultValue = "1")
            int pageNum, @RequestParam(value = "pageNum", defaultValue = "1") int pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceReponse.createByError(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请先登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //填充业务
            return iProductService.getProductList(pageNum, pageSize);
        } else {
            return ServiceReponse.createByErrorMessage("无权限操作");
        }
    }

    /**
     * 搜索
     * @param session
     * @param productName
     * @param poductId
     * @param pageNum
     * @param pageSize
     * @return
     * 已验证
     */
    @RequestMapping(value = "search.do", method = RequestMethod.GET)
    @ResponseBody
    public ServiceReponse productSearch(HttpSession session, String productName, Integer poductId,
                                        @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                        @RequestParam(value = "pageNum", defaultValue = "1") int pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceReponse.createByError(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请先登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //填充业务
            return iProductService.SearchProduct(productName, poductId, pageNum, pageSize);
        } else {
            return ServiceReponse.createByErrorMessage("无权限操作");
        }
    }
}
