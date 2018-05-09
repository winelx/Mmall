package mmall.service;

import mmall.common.ServiceReponse;
import mmall.pojo.Category;

import java.util.List;

public interface ICategoryService {

    //添加商品
    ServiceReponse addCategory(String categroyName, Integer parentId);
    //修改商品信息
    ServiceReponse updateCategoryName(Integer categoryId,String categroyName);
    //查询商品
    ServiceReponse<List<Category>> getChildrenparallelCategory(Integer categoryId);
    //递归查询商品（包括子节点0
    ServiceReponse selectCategoryAndChildernById(Integer categoryId);
}
