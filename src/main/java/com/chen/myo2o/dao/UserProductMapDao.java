package com.chen.myo2o.dao;

import com.chen.myo2o.entity.UserProductMap;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserProductMapDao {
    /**
     * @param userProductCondition
     * @param rowIndex
     * @param pageSize
     * @return
     */
    List<UserProductMap> queryUserProductMapList(
            @Param("userProductCondition") UserProductMap userProductCondition,
            @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

    /**
     * 根据相同的查询条件返回用户购买商品的记录数
     *
     * @param userProductCondition
     * @return
     */
    int queryUserProductMapCount(
            @Param("userProductCondition") UserProductMap userProductCondition);

    /**
     * 添加一条用户购买商品的记录
     *
     * @param userProductMap
     * @return
     */
    int insertUserProductMap(UserProductMap userProductMap);
}
