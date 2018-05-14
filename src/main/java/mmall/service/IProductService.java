package mmall.service;

import com.github.pagehelper.PageInfo;
import mmall.common.ServiceReponse;
import mmall.pojo.Product;
import mmall.vo.ProductdetailVo;

public interface IProductService {

    //增加或更新商品
    ServiceReponse saveOtUpdataProduct(Product product);

    //修改产品状态
    ServiceReponse<String> setSaleStatus(Integer productId, Integer status);

    //获取产品详情
    ServiceReponse<ProductdetailVo> mangerProductDetail(Integer productId);

    //分页
    ServiceReponse<PageInfo> getProductList(int pageNum, int pageSize);

    //搜索
    ServiceReponse<PageInfo> SearchProduct(String proedctname, Integer productId, int pageNum, int pageSize);
}
