package mmall.service;

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
}
