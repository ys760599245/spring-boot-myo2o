package com.chen.myo2o.service;

import com.chen.myo2o.dto.UserProductMapExecution;
import com.chen.myo2o.entity.UserProductMap;

public interface UserProductMapService {
    /**
     * 通过传入的查询条件分页列出用户信息列表
     *
     * @param shopId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    UserProductMapExecution listUserProductMap(UserProductMap userProductCondition, Integer pageIndex,
                                               Integer pageSize);

    /**
     * @param userProductMap
     * @return
     * @throws RuntimeException
     */
    UserProductMapExecution addUserProductMap(UserProductMap userProductMap) throws RuntimeException;

}
