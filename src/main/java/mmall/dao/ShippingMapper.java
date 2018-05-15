package mmall.dao;

import mmall.pojo.Shipping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShippingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);

    //删除地址
    int deleteByShippingIdUserId(@Param("userId") Integer userId, @Param("shipping") Integer shipping);

    //更新地址
    int updateByShipping(Shipping shipping);

    Shipping selectByShippingIdUserId(@Param("userId") Integer userId, @Param("shippingId") Integer shippingId);

    //地址分页
    List<Shipping> selectByUserId(@Param("userId")Integer userId);
}