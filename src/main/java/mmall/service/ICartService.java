package mmall.service;

import mmall.common.ServiceReponse;
import mmall.vo.CartVo;

public interface ICartService {

    //添加购物车
    ServiceReponse<CartVo> add(Integer count, Integer userId, Integer productId);

    //更新购物车
    ServiceReponse<CartVo> update(Integer count, Integer userId, Integer productId);

    //删除购物车
    ServiceReponse<CartVo> delete(Integer userId, String productIds);

    //
    ServiceReponse<CartVo> list(Integer userId);

    ServiceReponse<CartVo> selectOrInSelectAll(Integer userId, Integer checked, Integer productId);

    ServiceReponse<Integer> getCartProductCount(Integer userId);
}
