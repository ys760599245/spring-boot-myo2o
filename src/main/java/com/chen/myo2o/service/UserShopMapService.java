package com.chen.myo2o.service;

import com.chen.myo2o.dto.UserShopMapExecution;
import com.chen.myo2o.entity.UserShopMap;

public interface UserShopMapService {
    /**
     * 根据传入的查询信息 分页查询用户积分列表
     *
     * @param userShopMapCondition
     * @param pageIndex
     * @param pageSize
     * @return
     */
    UserShopMapExecution listUserShopMap(UserShopMap userShopMapCondition, int pageIndex, int pageSize);

    /**
     * 根据用户id 和店铺id 返回 该用户在摸个店铺的积分情况
     *
     * @param userId
     * @param shopId
     * @return
     */
    UserShopMap getUserShopMap(long userId, long shopId);

}
