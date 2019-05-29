package com.chen.myo2o.service.impl;

import com.chen.myo2o.dao.UserShopMapDao;
import com.chen.myo2o.dto.UserShopMapExecution;
import com.chen.myo2o.entity.UserShopMap;
import com.chen.myo2o.service.UserShopMapService;
import com.chen.myo2o.util.PageCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserShopMapServiceImpl implements UserShopMapService {
    @Autowired(required = false)
    private UserShopMapDao userShopMapDao;

    @Override
    public UserShopMapExecution listUserShopMap(
            UserShopMap userShopMapCondition, int pageIndex, int pageSize) {
        if (userShopMapCondition != null && pageIndex != -1 && pageSize != -1) {
            int beginIndex = PageCalculator.calculateRowIndex(pageIndex,
                    pageSize);
            //根据传入的查询条件分页返回用户积分列表信息
            List<UserShopMap> userShopMapList = userShopMapDao
                    .queryUserShopMapList(userShopMapCondition, beginIndex,
                            pageSize);
            //返回总数
            int count = userShopMapDao
                    .queryUserShopMapCount(userShopMapCondition);
            UserShopMapExecution ue = new UserShopMapExecution();
            ue.setUserShopMapList(userShopMapList);
            ue.setCount(count);
            return ue;
        } else {
            return null;
        }

    }

    /**
     * 根据用户id 和店铺id 返回 该用户在摸个店铺的积分情况
     *
     * @param userId
     * @param shopId
     * @return
     */
    @Override
    public UserShopMap getUserShopMap(long userId, long shopId) {
        return userShopMapDao.queryUserShopMap(userId, shopId);
    }
}
