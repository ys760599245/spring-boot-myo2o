package com.chen.myo2o.dao;

import com.chen.myo2o.entity.WechatAuth;

public interface WechatAuthDao {
    /**
     * 通过eopenid 查询对应本平台的微信账号
     *
     * @param openId
     * @return
     */
    WechatAuth queryWechatInfoByOpenId(String openId);

    /**
     * 添加对应本平台的微信账号
     *
     * @param wechatAuth
     * @return
     */
    int insertWechatAuth(WechatAuth wechatAuth);

    /**
     * @param wechatAuthId
     * @return
     */
    int deleteWechatAuth(Long wechatAuthId);
}
