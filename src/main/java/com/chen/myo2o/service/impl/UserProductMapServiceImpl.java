package com.chen.myo2o.service.impl;

import com.chen.myo2o.dao.PersonInfoDao;
import com.chen.myo2o.dao.ShopDao;
import com.chen.myo2o.dao.UserProductMapDao;
import com.chen.myo2o.dao.UserShopMapDao;
import com.chen.myo2o.dto.UserProductMapExecution;
import com.chen.myo2o.entity.PersonInfo;
import com.chen.myo2o.entity.Shop;
import com.chen.myo2o.entity.UserProductMap;
import com.chen.myo2o.entity.UserShopMap;
import com.chen.myo2o.enums.UserProductMapStateEnum;
import com.chen.myo2o.service.UserProductMapService;
import com.chen.myo2o.util.PageCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class UserProductMapServiceImpl implements UserProductMapService {
    @Autowired(required = false)
    private UserProductMapDao userProductMapDao;
    @Autowired(required = false)
    private UserShopMapDao userShopMapDao;
    @Autowired(required = false)
    private PersonInfoDao personInfoDao;
    @Autowired(required = false)
    private ShopDao shopDao;

    @Override
    public UserProductMapExecution listUserProductMap(
            UserProductMap userProductCondition, Integer pageIndex,
            Integer pageSize) {
        if (userProductCondition != null && pageIndex != null
                && pageSize != null) {
            int beginIndex = PageCalculator.calculateRowIndex(pageIndex,
                    pageSize);
            List<UserProductMap> userProductMapList = userProductMapDao
                    .queryUserProductMapList(userProductCondition, beginIndex,
                            pageSize);
            int count = userProductMapDao
                    .queryUserProductMapCount(userProductCondition);
            UserProductMapExecution se = new UserProductMapExecution();
            se.setUserProductMapList(userProductMapList);
            se.setCount(count);
            return se;
        } else {
            return null;
        }

    }

    /**
     * 添加消费记录
     *
     * @param userProductMap
     * @return
     * @throws RuntimeException
     */
    @Override
    @Transactional
    public UserProductMapExecution addUserProductMap(
            UserProductMap userProductMap) throws RuntimeException {
        //空值判断 主要确保顾客id  店铺id以及操作员id
        if (userProductMap != null && userProductMap.getUserId() != null
                && userProductMap.getShopId() != null) {
            //设置默认值
            userProductMap.setCreateTime(new Date());
            try {
                //添加消费记录
                int effectedNum = userProductMapDao
                        .insertUserProductMap(userProductMap);
                if (effectedNum <= 0) {
                    throw new RuntimeException("添加消费记录失败");
                }
                //若本次消费能够积分
                if (userProductMap.getPoint() != null
                        && userProductMap.getPoint() > 0) {
                    //查看该顾客是否在店铺消费过
                    UserShopMap userShopMap = userShopMapDao.queryUserShopMap(
                            userProductMap.getUserId(),
                            userProductMap.getShopId());
                    if (userShopMap != null) {
                        if (userShopMap.getPoint() >= userProductMap.getPoint()) {
                            userShopMap.setPoint(userShopMap.getPoint()
                                    + userProductMap.getPoint());
                            effectedNum = userShopMapDao
                                    .updateUserShopMapPoint(userShopMap);
                            if (effectedNum <= 0) {
                                throw new RuntimeException("更新积分信息失败");
                            }
                        }

                    } else {
                        // 在店铺没有过消费记录，添加一条积分信息 就想初始化会员
                        userShopMap = compactUserShopMap4Add(
                                userProductMap.getUserId(),
                                userProductMap.getShopId(),
                                userProductMap.getPoint());
                        effectedNum = userShopMapDao
                                .insertUserShopMap(userShopMap);
                        if (effectedNum <= 0) {
                            throw new RuntimeException("积分信息创建失败");
                        }
                    }
                }
                return new UserProductMapExecution(
                        UserProductMapStateEnum.SUCCESS, userProductMap);
            } catch (Exception e) {
                throw new RuntimeException("添加授权失败:" + e.toString());
            }
        } else {
            return new UserProductMapExecution(
                    UserProductMapStateEnum.NULL_USERPRODUCT_INFO);
        }
    }

    /**
     * 封装顾客积分信息
     *
     * @param userId
     * @param shopId
     * @param point
     * @return
     */
    private UserShopMap compactUserShopMap4Add(Long userId, Long shopId,
                                               Integer point) {
        UserShopMap userShopMap = null;
        if (userId != null && shopId != null) {
            userShopMap = new UserShopMap();
            PersonInfo personInfo = personInfoDao.queryPersonInfoById(userId);
            Shop shop = shopDao.queryByShopId(shopId);
            userShopMap.setUserId(userId);
            userShopMap.setShopId(shopId);
            userShopMap.setUserName(personInfo.getName());
            userShopMap.setShopName(shop.getShopName());
            userShopMap.setCreateTime(new Date());
            userShopMap.setPoint(point);
        }
        return userShopMap;
    }

}
