package com.chen.myo2o.dao;

import com.chen.myo2o.entity.Award;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AwardDao {
    /**
     * 根据传入进来的查询条件分页显示奖品信息列表
     *
     * @param awardCondition
     * @param rowIndex
     * @param pageSize
     * @return
     */
    List<Award> queryAwardList(@Param("awardCondition") Award awardCondition,
                               @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

    /**
     * 配合queryAwardList 返回相同查询条件下的奖牌数
     *
     * @param awardCondition
     * @return
     */
    int queryAwardCount(@Param("awardCondition") Award awardCondition);

    /**
     * 通过 awardId 查询奖品信息
     *
     * @param awardId
     * @return
     */
    Award queryAwardByAwardId(long awardId);

    /**
     * 添加奖品信息
     *
     * @param award
     * @return
     */
    int insertAward(Award award);

    /**
     * 更新奖品信息
     *
     * @param award
     * @return
     */
    int updateAward(Award award);

    /**
     * 删除奖品信息
     *
     * @param awardId
     * @return
     */
    int deleteAward(long awardId);
}
