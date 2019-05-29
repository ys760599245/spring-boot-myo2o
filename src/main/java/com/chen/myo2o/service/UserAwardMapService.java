package com.chen.myo2o.service;

import com.chen.myo2o.dto.UserAwardMapExecution;
import com.chen.myo2o.entity.UserAwardMap;

public interface UserAwardMapService {

    /**
     * @param userAwardCondition
     * @param pageIndex
     * @param pageSize
     * @return
     */
    UserAwardMapExecution listUserAwardMap(UserAwardMap userAwardCondition, Integer pageIndex, Integer pageSize);

    /**
     * 根据传入的id获取映射信息
     *
     * @param userAwardMapId
     * @return
     */
    UserAwardMap getUserAwardMapById(long userAwardMapId);

    /**
     * 领取奖品 添加映射关系
     *
     * @param userAwardMap
     * @return
     * @throws RuntimeException
     */
    UserAwardMapExecution addUserAwardMap(UserAwardMap userAwardMap) throws RuntimeException;

    /**
     * 修改映射信息  这里主要是修改奖品临朐状态
     *
     * @param userAwardMap
     * @return
     * @throws RuntimeException
     */
    UserAwardMapExecution modifyUserAwardMap(UserAwardMap userAwardMap) throws RuntimeException;

}
