package com.chen.myo2o.dao;

import com.chen.myo2o.entity.UserShopMap;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserShopMapDao {
    /**
     * 根据查询条件分页返回用户店铺积分列表
     *
     * @param userShopCondition
     * @param rowIndex
     * @param pageSize
     * @return
     */
    List<UserShopMap> queryUserShopMapList(@Param("userShopCondition") UserShopMap userShopCondition,
                                           @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

    /**
     * 根据传入的用户id和shopid查询该用户在某个店铺的积分信息
     *
     * @param userId
     * @param shopId
     * @return
     */
    UserShopMap queryUserShopMap(@Param("userId") long userId, @Param("shopId") long shopId);

    /**
     * 配合queryUserShopMapList根据相同的查询条件
     * 返回用户店铺基恩记录总数
     *
     * @param userShopCondition
     * @return
     */
    int queryUserShopMapCount(@Param("userShopCondition") UserShopMap userShopCondition);

    /**
     * 添加一条用户店铺的积分信息
     *
     * @param userShopMap
     * @return
     */
    int insertUserShopMap(UserShopMap userShopMap);

    /**
     * 更新用户在店铺的积分
     *
     * @param userShopMap
     * @return
     */
    int updateUserShopMapPoint(UserShopMap userShopMap);

}
