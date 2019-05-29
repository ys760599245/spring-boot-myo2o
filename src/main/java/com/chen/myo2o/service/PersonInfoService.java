package com.chen.myo2o.service;

import com.chen.myo2o.dto.PersonInfoExecution;
import com.chen.myo2o.entity.PersonInfo;

public interface PersonInfoService {

    /**
     * 根据用户id(userId)获取PersonInfo信息
     *
     * @param userId
     * @return
     */
    PersonInfo getPersonInfoById(Long userId);

    /**
     * @param personInfoCondition
     * @param pageIndex
     * @param pageSize
     * @return
     */
    PersonInfoExecution getPersonInfoList(PersonInfo personInfoCondition, int pageIndex, int pageSize);

    /**
     * @param personInfo
     * @return
     */
    PersonInfoExecution addPersonInfo(PersonInfo personInfo);

    /**
     * @param personInfo
     * @return
     */
    PersonInfoExecution modifyPersonInfo(PersonInfo personInfo);

}
