package mmall.service.Impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import mmall.common.Const;
import mmall.common.ResponseCode;
import mmall.common.ServiceReponse;
import mmall.dao.CartMapper;
import mmall.dao.ProductMapper;
import mmall.pojo.Cart;
import mmall.pojo.Product;
import mmall.service.ICartService;
import mmall.util.BigDecimlUtil;
import mmall.util.PropertiesUtil;
import mmall.vo.CartProductVo;
import mmall.vo.CartVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service("iCartService")
public class ICartServiceIpml implements ICartService {

    @Autowired
    CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;

    public ServiceReponse<CartVo> add(Integer count, Integer userId, Integer productId) {
        if (productId == null || count == null) {
            return ServiceReponse.createByErrorMessage("参数错误");
        }
        Cart cart = cartMapper.selectByuerIdProductId(userId, productId);
        if (cart == null) {
            Cart cartItem = new Cart();
            cartItem.setQuantity(count);
            cartItem.setChecked(Const.Cart.CHECKED);
            cartItem.setProductId(productId);
            cartItem.setUserId(userId);
            //插入数据库
            cartMapper.insert(cartItem);
        } else {
            count = cart.getQuantity() + count;
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKeySelective(cart);
        }

        return this.list(userId);
    }

    public ServiceReponse<CartVo> update(Integer count, Integer userId, Integer productId) {
        if (productId == null || count == null) {
            return ServiceReponse.createByErrorMessage("参数错误");
        }
        Cart cart = cartMapper.selectByuerIdProductId(userId, productId);
        if (cart != null) {
            cart.setQuantity(count);
        }
        cartMapper.updateByPrimaryKeySelective(cart);
        return this.list(userId);
    }

    public ServiceReponse<CartVo> delete(Integer userId, String productIds) {
        List<String> productList = Splitter.on(",").splitToList(productIds);
        if (CollectionUtils.isNotEmpty(productList)) {
            return ServiceReponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        cartMapper.selecByUserIdProductIds(userId, productList);
        return this.list(userId);
    }

    public ServiceReponse<CartVo> list(Integer userId) {
        CartVo cartVo = this.getCartVoLimit(userId);

        return ServiceReponse.createBySuccess(cartVo);
    }

    //全选反选
    public ServiceReponse<CartVo> selectOrInSelectAll(Integer userId, Integer checked, Integer productId) {
        cartMapper.checkOrUncheckedAllProduct(userId, productId, checked);
        return this.list(userId);
    }


    //查询购物车商品数量
    public ServiceReponse<Integer> getCartProductCount(Integer userId) {
        if (userId == null) {
            return ServiceReponse.createBySuccess(0);
        }
        return ServiceReponse.createBySuccess(cartMapper.selectCartProductCount(userId));
    }


    //购物车查询数据方法封装
    private CartVo getCartVoLimit(Integer userId) {
        CartVo cartVo = new CartVo();
        List<Cart> cartList = cartMapper.selectCartByuserId(userId);
        List<CartProductVo> cartProductVoList = Lists.newArrayList();
        BigDecimal cartTotalPrice = new BigDecimal("0");
        if (CollectionUtils.isNotEmpty(cartList)) {
            for (Cart catrItme : cartList
                    ) {
                CartProductVo cartProductVo = new CartProductVo();
                cartProductVo.setId(catrItme.getId());
                cartProductVo.setUserId(userId);
                cartProductVo.setProductId(catrItme.getProductId());
                Product product = productMapper.selectByPrimaryKey(catrItme.getProductId());
                if (product != null) {
                    cartProductVo.setProductMainImage(product.getMainImage());
                    cartProductVo.setProductName(product.getName());
                    cartProductVo.setProductSubtitle(product.getSubtitle());
                    cartProductVo.setProductStatus(product.getStatus());
                    cartProductVo.setProductPrice(product.getPrice());
                    cartProductVo.setProductStock(product.getStock());
                    //判断库存
                    int buyLimitCount = 0;
                    if (product.getStock() >= catrItme.getQuantity()) {
                        buyLimitCount = catrItme.getQuantity();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
                    } else {
                        buyLimitCount = product.getStock();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_FAIL);
                        //购物车中更新有效库存
                        Cart cartForQuantity = new Cart();
                        cartForQuantity.setUserId(catrItme.getId());
                        cartForQuantity.setQuantity(buyLimitCount);
                        cartMapper.updateByPrimaryKeySelective(cartForQuantity);
                    }
                    cartProductVo.setQuantity(buyLimitCount);
                    //计算总价
                    cartProductVo.setProductPrice(BigDecimlUtil.mul(product.getPrice().doubleValue(), cartProductVo.getQuantity()));
                    cartProductVo.setProductChecked(catrItme.getChecked());

                }
                if (catrItme.getChecked() == Const.Cart.CHECKED) {
                    cartTotalPrice = BigDecimlUtil.add(cartTotalPrice.doubleValue(), cartProductVo.getProductPrice().doubleValue());

                }
                cartProductVoList.add(cartProductVo);
            }
        }
        cartVo.setCartTotalPrice(cartTotalPrice);
        cartVo.setCartProductVoList(cartProductVoList);
        cartVo.setAllChecked(this.getAllchekStatus(userId));
        cartVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
        return cartVo;
    }

    private boolean getAllchekStatus(Integer userId) {
        if (userId == null) {
            return false;
        }
        return cartMapper.selectCartProductCheckStatusByUserId(userId) == 0;
    }
}
