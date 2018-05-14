package mmall.service.Impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import mmall.common.ServiceReponse;
import mmall.dao.CategoryMapper;
import mmall.pojo.Category;
import mmall.service.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service("iCategoryService")
public class ICategoryServiceImpl implements ICategoryService {
    private org.slf4j.Logger logger =LoggerFactory.getLogger(ICategoryServiceImpl.class);
    @Autowired
    private CategoryMapper categoryMapper;
    //添加
    public ServiceReponse addCategory(String categroyName,Integer parentId){
        //判断id和名称是是否正确
        if (parentId==null || StringUtils.isBlank(categroyName)){
            return ServiceReponse.createByErrorMessage("添加品类参数错误");
        }
        Category category=new Category();
        category.setName(categroyName);
        category.setParentId(parentId);
        //这个分类是可用的
        category.setStatus(true);
        int  rowCoun=categoryMapper.insert(category);
        if (rowCoun>0){
            return ServiceReponse.createBySuccess("添加品类成功");
        }
            return ServiceReponse.createByErrorMessage("添加失败");
    }
    //修改
    public ServiceReponse updateCategoryName(Integer categoryId,String categroyName){
        //判断id和名称是是否正确
        if (categoryId==null || StringUtils.isBlank(categroyName)){
            return ServiceReponse.createByErrorMessage("添加品类参数错误");
        }
        Category category=new Category();
        category.setId(categoryId);
        category.setName(categroyName);
        int  rowCoun=categoryMapper.insert(category);
        if (rowCoun>0){
            return ServiceReponse.createBySuccess("修改品类成功");
        }
        return ServiceReponse.createByErrorMessage("修改品类失败");
    }
        //查询商品
    public ServiceReponse<List<Category>> getChildrenparallelCategory(Integer categoryId){
        List<Category>  categoryList=categoryMapper.selectCategoryChildrenByParentId(categoryId);
        if (CollectionUtils.isEmpty(categoryList)){
            logger.info("未找到当前分类的子分类");
        }
        return ServiceReponse.createBySuccess(categoryList);
    }
    //递归查询商品，包括子节点
    public ServiceReponse selectCategoryAndChildernById(Integer categoryId){
        Set<Category> categorySet =Sets.newHashSet();
        findChildCategory(categorySet,categoryId);
        List<Integer> categoryIdList=Lists.newArrayList();
        if (categoryId!=null){
            for (Category categoryItem:categorySet) {
                categoryIdList.add(categoryItem.getId());
            }
        }
        return ServiceReponse.createBySuccess(categoryIdList);
    }

    //递归算法，算出子节点
    private Set<Category> findChildCategory(Set<Category> categorySet ,Integer categoryId){
        Category category=categoryMapper.selectByPrimaryKey(categoryId);
            if (category!=null){
                categorySet.add(category);
            }
            //查找子节点，递归算法一定要有一个退出条件
            List<Category> categoryList=categoryMapper.selectCategoryChildrenByParentId(categoryId);
                for (Category category1:categoryList) {
                   findChildCategory(categorySet,category1.getId());
                }
                return categorySet;

    }
}
