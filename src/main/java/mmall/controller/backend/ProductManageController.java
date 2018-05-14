package mmall.controller.backend;

import com.google.common.collect.Maps;
import mmall.common.Const;
import mmall.common.ResponseCode;
import mmall.common.ServiceReponse;
import mmall.pojo.Product;
import mmall.pojo.User;
import mmall.service.IFileService;
import mmall.service.IProductService;
import mmall.service.IUserService;
import mmall.util.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/manage/product")
public class ProductManageController {
    @Autowired
    private IUserService iUserService;
    @Autowired
    private IProductService iProductService;
    @Autowired
    private IFileService iFileService;

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
     *
     * @param session
     * @param poductId
     * @return 已验证
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
     * @return 已验证
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
     *
     * @param session
     * @param productName
     * @param poductId
     * @param pageNum
     * @param pageSize
     * @return 已验证
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

    /**
     * 文件上传
     *
     * @param session
     * @param file
     * @param request
     * @return
     */
    @RequestMapping("upload.do")
    @ResponseBody
    public ServiceReponse upload(HttpSession session, @RequestParam(value = "upload_file", required = false) MultipartFile file, HttpServletRequest request) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceReponse.createByError(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请先登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //填充业务
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = iFileService.upload(file, path);
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;
            Map fileMap = Maps.newHashMap();
            fileMap.put("uri", targetFileName);
            fileMap.put(url, url);
            return ServiceReponse.createBySuccess(fileMap);
        } else {
            return ServiceReponse.createByErrorMessage("无权限操作");
        }
    }

    /**
     * 富文本上传
     */
    @RequestMapping("richtext_img_upload.do")
    @ResponseBody
    public Map richtextImgUpload(HttpSession session,
                                 @RequestParam(value = "upload_file", required = false) MultipartFile file,
                                 HttpServletRequest request,
                                 HttpServletResponse servletResponse) {
        Map resultMap = Maps.newHashMap();
        User user = (User) session.getAttribute(Const.CURRENT_USER);

        if (user == null) {
            resultMap.put("success", false);
            resultMap.put("msg", "请登录管理员");
            return resultMap;
        }
        //富文本中对于返回有自己的要求，我们使用的是simditor所以安卓simditor的要求
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //填充业务
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = iFileService.upload(file, path);
            if (StringUtils.isBlank(targetFileName)) {
                resultMap.put("success", false);
                resultMap.put("msg", "上传失败");
                return resultMap;
            }
            String url = PropertiesUtil.getProperty("ftp.server.http.perfix") + targetFileName;
            resultMap.put("success", true);
            resultMap.put("msg", "上传成功");
            resultMap.put("file_path", url);
            servletResponse.addHeader("Accesscontrol-Headers", "X-File-Name");
            return resultMap;
        } else {
            resultMap.put("success", false);
            resultMap.put("msg", "无权限操作");
            return resultMap;
        }
    }

}
