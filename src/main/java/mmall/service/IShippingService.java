package mmall.service;

import mmall.common.ServiceReponse;
import mmall.pojo.Shipping;

public interface IShippingService {
    //添加地址
    ServiceReponse add(Integer userId, Shipping shipping);

    //shanchu8地址
    ServiceReponse del(Integer userId, Integer shippingId);

    //更新地址
    ServiceReponse updata(Integer userId, Shipping shipping);

    //查询地址
    ServiceReponse select(Integer userId, Integer shippingId);

    //地址分页
    ServiceReponse list(Integer userId, int pageNum, int pageSize);
}
