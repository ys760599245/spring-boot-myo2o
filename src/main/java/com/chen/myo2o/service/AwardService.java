package com.chen.myo2o.service;

import com.chen.myo2o.dto.AwardExecution;
import com.chen.myo2o.entity.Award;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

public interface AwardService {

    /**
     * 根据传入的条件获取奖品列表 并且查询条件下的总数
     *
     * @param awardCondition
     * @param pageIndex
     * @param pageSize
     * @return
     */
    AwardExecution getAwardList(Award awardCondition, int pageIndex, int pageSize);

    /**
     * 根据Awardid查询奖品信息
     *
     * @param awardId
     * @return
     */
    Award getAwardById(long awardId);

    /**
     * 添加奖品信息 并且添加奖品图片
     *
     * @param award
     * @param thumbnail
     * @return
     */
    AwardExecution addAward(Award award, CommonsMultipartFile thumbnail);

    /**
     * 根据传入的奖品实例 修改对应的奖品信息 若传入的图片则替换原先的图片
     *
     * @param award
     * @param thumbnail
     * @param awardImgs
     * @return
     */
    AwardExecution modifyAward(Award award, CommonsMultipartFile thumbnail);

}
