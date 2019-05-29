package com.chen.myo2o.service;

import com.chen.myo2o.dto.WechatAuthExecution;
import com.chen.myo2o.entity.WechatAuth;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

public interface WechatAuthService {

    /**
     * 通过openid查找平台对应的微信账号
     *
     * @param openId
     * @return
     */
    WechatAuth getWechatAuthByOpenId(String openId);

    /**
     * 注册本平台的微信账号
     *
     * @param wechatAuth
     * @param profileImg
     * @return
     * @throws RuntimeException
     */
    WechatAuthExecution register(WechatAuth wechatAuth, CommonsMultipartFile profileImg) throws RuntimeException;

}
