package mmall.controller.backend;

import mmall.common.Const;
import mmall.common.ResponseCode;
import mmall.common.ServiceReponse;
import mmall.pojo.User;
import mmall.service.ICategoryService;
import mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICategoryService iCategoryService;


    /**
     * 添加商品
     *
     * @param session
     * @param categoryName
     * @param parentId
     * @return
     */
    @RequestMapping("add_category.do")
    @ResponseBody
    public ServiceReponse addCategory(HttpSession session, String categoryName, @RequestParam(value = "parentId", defaultValue = "0") int parentId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceReponse.createByError(ResponseCode.NEED_LOGIN.getCode(), "用户未登录！");
        }
        //恔验是否是管理员
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //是管理员
            return iCategoryService.addCategory(categoryName, parentId);
        } else {
            return ServiceReponse.createByErrorMessage("没有权限！");
        }
    }

    /**
     * 修改商品信息
     */
    @RequestMapping("set_category_name.do")
    @ResponseBody
    public ServiceReponse setCategoryName(HttpSession session, Integer parentId, String categoryName) {
    User user = (User) session.getAttribute(Const.CURRENT_USER);
    if (user==null){
        return ServiceReponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"还未登录，请先登录");
    }
    if (iUserService.checkAdminRole(user).isSuccess()){
            //更新数据
      return iCategoryService.updateCategoryName(parentId,categoryName);
        }else {
        return ServiceReponse.createByErrorMessage("无权限操作！");
    }

    }

    /**
     * 查询商品列表
     * @param session
     * @param categotyId
     * @return  返回数据
     */
    @RequestMapping("get_category.do")
    @ResponseBody
    public ServiceReponse getChildrenparalleCategory(HttpSession session,@RequestParam(value = "categoryId",defaultValue = "0")Integer categotyId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceReponse.createByError(ResponseCode.NEED_LOGIN.getCode(), "用户未登录！");
        }
        //恔验是否是管理员
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //是管理员 查询子节点的category信息，并且不递归，保持平级
            return iCategoryService.getChildrenparallelCategory(categotyId);
        } else {
            return ServiceReponse.createByErrorMessage("没有权限！");
        }
    }

    /**
     * 根据
     * @return
     */
    @RequestMapping("get_deep_category.do")
    @ResponseBody
    public ServiceReponse getCategoryAndDeepChildrenCategory(HttpSession session,@RequestParam(value = "categoryId",defaultValue = "0")Integer categotyId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceReponse.createByError(ResponseCode.NEED_LOGIN.getCode(), "用户未登录！");
        }
        //恔验是否是管理员
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //是管理员 查询当前节点和子节点的商品，递归查询
                return iCategoryService.selectCategoryAndChildernById(categotyId);
        } else {
            return ServiceReponse.createByErrorMessage("没有权限！");
        }
    }

}

