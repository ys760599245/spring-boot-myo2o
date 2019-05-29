package com.chen.myo2o.service.impl;

import com.chen.myo2o.dao.ShopAuthMapDao;
import com.chen.myo2o.dto.ShopAuthMapExecution;
import com.chen.myo2o.entity.ShopAuthMap;
import com.chen.myo2o.enums.ShopAuthMapStateEnum;
import com.chen.myo2o.service.ShopAuthMapService;
import com.chen.myo2o.util.PageCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ShopAuthMapServiceImpl implements ShopAuthMapService {
    @Autowired(required = false)
    private ShopAuthMapDao shopAuthMapDao;

    @Override
    public ShopAuthMapExecution listShopAuthMapByShopId(Long shopId,
                                                        Integer pageIndex, Integer pageSize) {
        //空值判断
        if (shopId != null && pageIndex != null && pageSize != null) {
            //页转行
            int beginIndex = PageCalculator.calculateRowIndex(pageIndex,
                    pageSize);
            //查询返回该店铺的授权信息列表
            List<ShopAuthMap> shopAuthMapList = shopAuthMapDao
                    .queryShopAuthMapListByShopId(shopId, beginIndex, pageSize);
            //返回总数
            int count = shopAuthMapDao.queryShopAuthCountByShopId(shopId);
            ShopAuthMapExecution se = new ShopAuthMapExecution();
            se.setShopAuthMapList(shopAuthMapList);
            se.setCount(count);
            return se;
        } else {
            return null;
        }

    }

    @Override
    @Transactional
    public ShopAuthMapExecution addShopAuthMap(ShopAuthMap shopAuthMap)
            throws RuntimeException {
        //空值判断 对用户id和店铺id 做检查
        if (shopAuthMap != null && shopAuthMap.getShopId() != null
                && shopAuthMap.getEmployeeId() != null) {
            shopAuthMap.setCreateTime(new Date());
            shopAuthMap.setLastEditTime(new Date());
            shopAuthMap.setEnableStatus(1);
            try {
                //添加授权信息
                int effectedNum = shopAuthMapDao.insertShopAuthMap(shopAuthMap);
                if (effectedNum <= 0) {
                    throw new RuntimeException("添加授权失败");
                }
                return new ShopAuthMapExecution(ShopAuthMapStateEnum.SUCCESS,
                        shopAuthMap);
            } catch (Exception e) {
                throw new RuntimeException("添加授权失败:" + e.toString());
            }
        } else {
            return new ShopAuthMapExecution(
                    ShopAuthMapStateEnum.NULL_SHOPAUTH_INFO);
        }
    }

    @Override
    @Transactional
    public ShopAuthMapExecution modifyShopAuthMap(ShopAuthMap shopAuthMap)
            throws RuntimeException {
        //空值判断 主要是对校检id做判断
        if (shopAuthMap == null || shopAuthMap.getShopAuthId() == null) {
            return new ShopAuthMapExecution(
                    ShopAuthMapStateEnum.NULL_SHOPAUTH_ID);
        } else {
            try {
                int effectedNum = shopAuthMapDao.updateShopAuthMap(shopAuthMap);
                if (effectedNum <= 0) {
                    return new ShopAuthMapExecution(
                            ShopAuthMapStateEnum.INNER_ERROR);
                } else {// 创建成功
                    return new ShopAuthMapExecution(
                            ShopAuthMapStateEnum.SUCCESS, shopAuthMap);
                }
            } catch (Exception e) {
                throw new RuntimeException("updateShopByOwner error: "
                        + e.getMessage());
            }
        }
    }

    @Override
    public ShopAuthMapExecution removeShopAuthMap(Long shopAuthMapId)
            throws RuntimeException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ShopAuthMap getShopAuthMapById(Long shopAuthId) {
        return shopAuthMapDao.queryShopAuthMapById(shopAuthId);
    }

}
