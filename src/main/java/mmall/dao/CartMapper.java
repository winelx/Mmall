package mmall.dao;

import mmall.pojo.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    //查询购物车
    Cart selectByuerIdProductId(@Param("UserId") Integer UserId, @Param("productId") Integer productId);

    List<Cart> selectCartByuserId(Integer UserId);

    int selectCartProductCheckStatusByUserId(Integer userIdl);

    int selecByUserIdProductIds(@Param("userId") Integer userId, @Param("productIds") List<String> productIds);

    //全选和全反选
    int checkOrUncheckedAllProduct(@Param("userId") Integer userId, @Param("productId") Integer productId, @Param("checked") Integer checked);

    int  selectCartProductCount(@Param("userId") Integer userId);
}