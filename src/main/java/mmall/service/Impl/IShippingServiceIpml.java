package mmall.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import mmall.common.ServiceReponse;
import mmall.dao.ShippingMapper;
import mmall.pojo.Shipping;
import mmall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("iShippingService")
public class IShippingServiceIpml implements IShippingService {

    @Autowired
    private ShippingMapper shippingMapper;

    /**
     * 新增地址
     *
     * @param userId
     * @param shipping
     * @return
     */
    @Override
    public ServiceReponse add(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        //添加
        int rowCount = shippingMapper.insert(shipping);
        if (rowCount > 0) {
            Map result = Maps.newHashMap();
            result.put("shippingId", shipping.getId());
            return ServiceReponse.createBySuccess(result);
        }
        return ServiceReponse.createByErrorMessage("新增地址失败");
    }

    /**
     * 删除地址
     *
     * @param userId
     * @param shippingId
     * @return
     */
    public ServiceReponse del(Integer userId, Integer shippingId) {
        int result = shippingMapper.deleteByShippingIdUserId(userId, shippingId);
        if (result > 0) {
            return ServiceReponse.createBySuccess("删除地址成功");
        }
        return ServiceReponse.createByErrorMessage("删除地址失败");
    }

    /**
     * 更新地址
     *
     * @param userId
     * @param shipping
     * @return
     */
    public ServiceReponse updata(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        int rowCount = shippingMapper.updateByShipping(shipping);
        if (rowCount > 0) {
            return ServiceReponse.createBySuccess("更新地址成功");
        }
        return ServiceReponse.createByErrorMessage("更新地址失败");
    }

    /**
     * 查询地址
     *
     * @param userId
     * @param shippingId
     * @return
     */
    public ServiceReponse select(Integer userId, Integer shippingId) {
        Shipping shipping = shippingMapper.selectByShippingIdUserId(userId, shippingId);
        if (shipping == null) {
            return ServiceReponse.createByErrorMessage("无法查询到该地址");
        }
        return ServiceReponse.createBySuccess("查询地址成功", shipping);
    }

    /**
     * 地址分页
     *
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    public ServiceReponse list(Integer userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Shipping> shippingList = shippingMapper.selectByUserId(userId);
        PageInfo pageInfo = new PageInfo(shippingList);
        return ServiceReponse.createBySuccess(pageInfo);
    }

}
