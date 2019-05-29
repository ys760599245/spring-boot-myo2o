package com.chen.myo2o.service.impl;

import com.chen.myo2o.dao.UserAwardMapDao;
import com.chen.myo2o.dao.UserShopMapDao;
import com.chen.myo2o.dto.UserAwardMapExecution;
import com.chen.myo2o.entity.UserAwardMap;
import com.chen.myo2o.entity.UserShopMap;
import com.chen.myo2o.enums.UserAwardMapStateEnum;
import com.chen.myo2o.service.UserAwardMapService;
import com.chen.myo2o.util.PageCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class UserAwardMapServiceImpl implements UserAwardMapService {
    @Autowired(required = false)
    private UserAwardMapDao userAwardMapDao;
    @Autowired(required = false)
    private UserShopMapDao userShopMapDao;

    @Override
    public UserAwardMapExecution listUserAwardMap(
            UserAwardMap userAwardCondition, Integer pageIndex, Integer pageSize) {
        if (userAwardCondition != null && pageIndex != null && pageSize != null) {
            int beginIndex = PageCalculator.calculateRowIndex(pageIndex,
                    pageSize);
            //根据查询条件分页返回用户与奖品的映射信息列表 用户领取奖品的信息列表
            List<UserAwardMap> userAwardMapList = userAwardMapDao
                    .queryUserAwardMapList(userAwardCondition, beginIndex,
                            pageSize);
            int count = userAwardMapDao
                    .queryUserAwardMapCount(userAwardCondition);
            UserAwardMapExecution ue = new UserAwardMapExecution();
            ue.setUserAwardMapList(userAwardMapList);
            ue.setCount(count);
            return ue;
        } else {
            return null;
        }

    }

    @Override
    public UserAwardMap getUserAwardMapById(long userAwardMapId) {
        return userAwardMapDao.queryUserAwardMapById(userAwardMapId);
    }

    @Override
    @Transactional
    public UserAwardMapExecution addUserAwardMap(UserAwardMap userAwardMap)
            throws RuntimeException {
        //空值判断主要是检查传入的userAwardID以及领取状态是否为空
        if (userAwardMap != null && userAwardMap.getUserId() != null
                && userAwardMap.getShopId() != null) {
            userAwardMap.setCreateTime(new Date());
            try {
                int effectedNum = 0;
                //若该奖品需要消耗积分 则将tb_user_shop_map 对应的用户积分抵扣
                if (userAwardMap.getPoint() != null
                        && userAwardMap.getPoint() > 0) {
                    //根据用户id和店铺id获取该用户在店铺的积分
                    UserShopMap userShopMap = userShopMapDao.queryUserShopMap(
                            userAwardMap.getUserId(), userAwardMap.getShopId());
                    //判断该用户在店铺里面是否有积分
                    if (userShopMap != null) {
                        //若有积分 必须确保店铺积分大于本次要段欢奖品需要的积分
                        if (userShopMap.getPoint() >= userAwardMap.getPoint()) {
                            //积分抵押
                            userShopMap.setPoint(userShopMap.getPoint()
                                    - userAwardMap.getPoint());
                            //更新可用状态 更新积分信息
                            effectedNum = userShopMapDao
                                    .updateUserShopMapPoint(userShopMap);
                            if (effectedNum <= 0) {
                                throw new RuntimeException("更新积分信息失败");
                            }
                        } else {
                            throw new RuntimeException("积分不足无法领取");
                        }

                    } else {
                        // 在店铺没有积分 则抛出异常
                        throw new RuntimeException("在本店铺没有积分，无法对换奖品");
                    }
                }
                //插入礼品兑换信息
                effectedNum = userAwardMapDao.insertUserAwardMap(userAwardMap);
                if (effectedNum <= 0) {
                    throw new RuntimeException("领取奖励失败");
                }

                return new UserAwardMapExecution(UserAwardMapStateEnum.SUCCESS,
                        userAwardMap);
            } catch (Exception e) {
                throw new RuntimeException("领取奖励失败:" + e.toString());
            }
        } else {
            return new UserAwardMapExecution(
                    UserAwardMapStateEnum.NULL_USERAWARD_INFO);
        }
    }

    @Override
    @Transactional
    public UserAwardMapExecution modifyUserAwardMap(UserAwardMap userAwardMap)
            throws RuntimeException {
        if (userAwardMap == null || userAwardMap.getUserAwardId() == null
                || userAwardMap.getUsedStatus() == null) {
            return new UserAwardMapExecution(
                    UserAwardMapStateEnum.NULL_USERAWARD_ID);
        } else {
            try {
                int effectedNum = userAwardMapDao
                        .updateUserAwardMap(userAwardMap);
                if (effectedNum <= 0) {
                    return new UserAwardMapExecution(
                            UserAwardMapStateEnum.INNER_ERROR);
                } else {
                    return new UserAwardMapExecution(
                            UserAwardMapStateEnum.SUCCESS, userAwardMap);
                }
            } catch (Exception e) {
                throw new RuntimeException("modifyUserAwardMap error: "
                        + e.getMessage());
            }
        }
    }

}
