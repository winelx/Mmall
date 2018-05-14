package mmall.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import mmall.common.ResponseCode;
import mmall.common.ServiceReponse;
import mmall.dao.CategoryMapper;
import mmall.dao.ProductMapper;
import mmall.pojo.Category;
import mmall.pojo.Product;
import mmall.service.IProductService;
import mmall.util.DateTimeUtil;
import mmall.util.PropertiesUtil;
import mmall.vo.ProductListVo;
import mmall.vo.ProductdetailVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * model
 */
@Service("iProductService")
public class IProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    //新增或更新产品
    public ServiceReponse saveOtUpdataProduct(Product product) {
        if (product != null) {
            if (StringUtils.isBlank(product.getSubImages())) {
                String[] subImageArrray = product.getSubImages().split(",");
                if (subImageArrray.length > 0) {
                    product.setMainImage(subImageArrray[0]);
                }
                if (product.getId() != null) {
                    int resultCount = productMapper.updateByPrimaryKey(product);
                    if (resultCount > 0) {
                        return ServiceReponse.createBySuccess("更新产品成功");
                    }
                    return ServiceReponse.createByErrorMessage("更新产品失败");
                } else {
                    int resultCount = productMapper.insert(product);
                    if (resultCount > 0) {
                        return ServiceReponse.createBySuccess("新增产品成功");
                    }
                    return ServiceReponse.createByErrorMessage("新增产品失败");
                }
            }
        }
        return ServiceReponse.createByErrorMessage("新增或更新产品参数不正确");
    }

    /**
     * 修改产品状态，上下架
     */
    public ServiceReponse<String> setSaleStatus(Integer productId, Integer status) {
        if (productId == null || status == null) {
            return ServiceReponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int rowCount = productMapper.updateByPrimaryKeySelective(product);
        if (rowCount > 0) {
            return ServiceReponse.createBySuccess("修改产品状态成功");
        }
        return ServiceReponse.createByErrorMessage("修改产品状态失败");
    }


    /**
     * 获取产品详情
     */

    public ServiceReponse<ProductdetailVo> mangerProductDetail(Integer productId) {
        if (productId == null) {
            return ServiceReponse.createByErrorMessage("产品已下架或者删除");
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServiceReponse.createByErrorMessage("产品已下架或者删除");
        }
        ProductdetailVo productdetailVo = assembleProducteDetailVo(product);
        return ServiceReponse.createBySuccess(productdetailVo);
    }


    /**
     * 数据分页处理
     */
    public ServiceReponse<PageInfo> getProductList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Product> productList = productMapper.selectList();
        List<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product ProductItme : productList) {
            ProductListVo productListVo = assembleProductListVo(ProductItme);
            productListVoList.add(productListVo);
        }
        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productListVoList);
        return ServiceReponse.createBySuccess(pageResult);
    }

    /**
     * 搜索
     *
     * @param proedctname
     * @param productId
     * @param pageNum
     * @param pageSize
     * @return
     */
    public ServiceReponse<PageInfo> SearchProduct(String proedctname, Integer productId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        if (StringUtils.isNotBlank(proedctname)) {
            proedctname = new StringBuffer().append("%").append(proedctname).append("%").toString();
        }
        List<Product> productList = productMapper.selectByNameAndProductId(proedctname, productId);
        List<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product productItem : productList
                ) {
            ProductListVo productListVo = assembleProductListVo(productItem);
            productListVoList.add(productListVo);
        }
        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productListVoList);
        return ServiceReponse.createBySuccess(pageResult);
    }


    private ProductListVo assembleProductListVo(Product product) {
        ProductListVo productdetailVo = new ProductListVo();
        productdetailVo.setId(product.getId());
        productdetailVo.setName(product.getName());
        productdetailVo.setCategoryId(product.getCategoryId());
        productdetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.happymmall.com/"));
        productdetailVo.setMainImage(product.getMainImage());
        productdetailVo.setPrice(product.getPrice());
        productdetailVo.setSubtitle(product.getSubtitle());
        productdetailVo.setStatus(product.getStatus());
        return productdetailVo;
    }


    private ProductdetailVo assembleProducteDetailVo(Product product) {
        ProductdetailVo productdetailVo = new ProductdetailVo();
        productdetailVo.setId(product.getId());
        productdetailVo.setSubtitle(product.getSubtitle());
        productdetailVo.setPrice(product.getPrice());
        productdetailVo.setMainImage(product.getMainImage());
        productdetailVo.setSubImages(product.getSubImages());
        productdetailVo.setCategoryId(product.getCategoryId());
        productdetailVo.setDetail(product.getDetail());
        productdetailVo.setName(product.getName());
        productdetailVo.setStatus(product.getStatus());
        productdetailVo.setStock(product.getStock());

        productdetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.happymmall.com/"));

        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category == null) {
            productdetailVo.setParentCategoryId(0);//默认根节点
        } else {
            productdetailVo.setParentCategoryId(category.getParentId());
        }
        productdetailVo.setCreateTime(DateTimeUtil.DateToStr(product.getCreateTime()));
        productdetailVo.setUpdataTime(DateTimeUtil.DateToStr(product.getUpdateTime()));
        return productdetailVo;
    }

}
