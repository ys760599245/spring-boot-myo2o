package com.chen.myo2o.service;

import com.chen.myo2o.dto.LocalAuthExecution;
import com.chen.myo2o.entity.LocalAuth;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

public interface LocalAuthService {
    /**
     * @param userName
     * @return
     */
    LocalAuth getLocalAuthByUserNameAndPwd(String userName, String password);

    /**
     * @param userId
     * @return
     */
    LocalAuth getLocalAuthByUserId(long userId);

    /**
     * @param localAuth
     * @param profileImg
     * @return
     * @throws RuntimeException
     */
    LocalAuthExecution register(LocalAuth localAuth, CommonsMultipartFile profileImg) throws RuntimeException;

    /**
     * 绑定微信 生成平台专属的账号
     *
     * @param localAuth
     * @return
     * @throws RuntimeException
     */
    LocalAuthExecution bindLocalAuth(LocalAuth localAuth) throws RuntimeException;

    /**
     * 修改平台账号的登录密码
     *
     * @param localAuthId
     * @param userName
     * @param password
     * @param newPassword
     * @param lastEditTime
     * @return
     */
    LocalAuthExecution modifyLocalAuth(Long userId, String userName, String password, String newPassword);
}
