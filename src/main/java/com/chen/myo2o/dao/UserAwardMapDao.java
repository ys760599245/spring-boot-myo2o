package com.chen.myo2o.dao;

import com.chen.myo2o.entity.UserAwardMap;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserAwardMapDao {
    /**
     * @param userAwardCondition
     * @param rowIndex
     * @param pageSize           根据传入过来的查询条件 分页返回用户兑换奖品记录的列表信息
     * @return
     */
    List<UserAwardMap> queryUserAwardMapList(
            @Param("userAwardCondition") UserAwardMap userAwardCondition,
            @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

    /**
     * @param userAwardCondition
     * @return 配合queryUserAwardMapList 返回相同查询条件下的兑换奖品记录数
     */
    int queryUserAwardMapCount(
            @Param("userAwardCondition") UserAwardMap userAwardCondition);

    /**
     * @param userAwardId
     * @return 根据userAwardId  返回某条奖品兑换信息
     */
    UserAwardMap queryUserAwardMapById(long userAwardId);

    /**
     * @param userAwardMap
     * @return 添加一条奖品兑换信息
     */
    int insertUserAwardMap(UserAwardMap userAwardMap);

    /**
     * @param userAwardMap
     * @return 更新奖品兑换信息 主要是更新奖品领取状态
     */
    int updateUserAwardMap(UserAwardMap userAwardMap);
}
