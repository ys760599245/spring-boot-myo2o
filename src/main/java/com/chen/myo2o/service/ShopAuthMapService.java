package com.chen.myo2o.service;

import com.chen.myo2o.dto.ShopAuthMapExecution;
import com.chen.myo2o.entity.ShopAuthMap;

public interface ShopAuthMapService {
    /**
     * 根据店铺id分页显示该店铺的授权信息
     *
     * @param shopId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    ShopAuthMapExecution listShopAuthMapByShopId(Long shopId, Integer pageIndex, Integer pageSize);

    /**
     * 添加授权信息
     *
     * @param shopAuthMap
     * @return
     * @throws RuntimeException
     */
    ShopAuthMapExecution addShopAuthMap(ShopAuthMap shopAuthMap) throws RuntimeException;

    /**
     * 更新授权信息，包括职位 状态等
     *
     * @param shopAuthId
     * @param title
     * @param titleFlag
     * @param enableStatus
     * @return
     * @throws RuntimeException
     */
    ShopAuthMapExecution modifyShopAuthMap(ShopAuthMap shopAuthMap) throws RuntimeException;

    /**
     * 移除授权信息
     *
     * @param shopAuthMapId
     * @return
     * @throws RuntimeException
     */
    ShopAuthMapExecution removeShopAuthMap(Long shopAuthMapId) throws RuntimeException;

    /**
     * 根据ShopAuthId 返回对应的授权信息
     *
     * @param shopAuthId
     * @return
     */
    ShopAuthMap getShopAuthMapById(Long shopAuthId);

}
