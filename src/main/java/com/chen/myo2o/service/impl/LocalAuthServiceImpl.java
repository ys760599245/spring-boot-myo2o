package com.chen.myo2o.service.impl;

import com.chen.myo2o.dao.LocalAuthDao;
import com.chen.myo2o.dao.PersonInfoDao;
import com.chen.myo2o.dto.LocalAuthExecution;
import com.chen.myo2o.entity.LocalAuth;
import com.chen.myo2o.entity.PersonInfo;
import com.chen.myo2o.enums.LocalAuthStateEnum;
import com.chen.myo2o.service.LocalAuthService;
import com.chen.myo2o.util.FileUtil;
import com.chen.myo2o.util.ImageUtil;
import com.chen.myo2o.util.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.Date;

@Service
public class LocalAuthServiceImpl implements LocalAuthService {

    @Autowired(required = false)
    private LocalAuthDao localAuthDao;
    @Autowired(required = false)
    private PersonInfoDao personInfoDao;

    @Override
    public LocalAuth getLocalAuthByUserNameAndPwd(String userName,
                                                  String password) {
        return localAuthDao.queryLocalByUserNameAndPwd(userName, password);
    }

    @Override
    public LocalAuth getLocalAuthByUserId(long userId) {
        return localAuthDao.queryLocalByUserId(userId);
    }

    @Override
    @Transactional
    public LocalAuthExecution register(LocalAuth localAuth,
                                       CommonsMultipartFile profileImg) throws RuntimeException {
        //空值判断 传入的localAuth 账号和密码 用户信息特别userid不能为空 否则直接返回错误
        if (localAuth == null || localAuth.getPassword() == null
                || localAuth.getUserName() == null) {
            return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
        }
        //查询此用户是否已经绑定平台用户
        try {
            localAuth.setCreateTime(new Date());
            localAuth.setLastEditTime(new Date());
            localAuth.setPassword(MD5.getMd5(localAuth.getPassword()));
            if (localAuth.getPersonInfo() != null
                    && localAuth.getPersonInfo().getUserId() == null) {
                if (profileImg != null) {
                    localAuth.getPersonInfo().setCreateTime(new Date());
                    localAuth.getPersonInfo().setLastEditTime(new Date());
                    localAuth.getPersonInfo().setEnableStatus(1);
                    try {
                        addProfileImg(localAuth, profileImg);
                    } catch (Exception e) {
                        throw new RuntimeException("addUserProfileImg error: "
                                + e.getMessage());
                    }
                }
                try {
                    PersonInfo personInfo = localAuth.getPersonInfo();
                    int effectedNum = personInfoDao
                            .insertPersonInfo(personInfo);
                    localAuth.setUserId(personInfo.getUserId());
                    if (effectedNum <= 0) {
                        throw new RuntimeException("添加用户信息失败");
                    }
                } catch (Exception e) {
                    throw new RuntimeException("insertPersonInfo error: "
                            + e.getMessage());
                }
            }
            int effectedNum = localAuthDao.insertLocalAuth(localAuth);
            if (effectedNum <= 0) {
                throw new RuntimeException("帐号创建失败");
            } else {
                return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS,
                        localAuth);
            }
        } catch (Exception e) {
            throw new RuntimeException("insertLocalAuth error: "
                    + e.getMessage());
        }
    }

    @Override
    @Transactional
    public LocalAuthExecution bindLocalAuth(LocalAuth localAuth)
            throws RuntimeException {
        //空值判断 传入的localAuth 账号和密码 用户信息特别userid不能为空 否则直接返回错误
        if (localAuth == null || localAuth.getPassword() == null
                || localAuth.getUserName() == null
                || localAuth.getUserId() == null) {
            return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
        }
        //查询此用户是否已经绑定平台用户
        LocalAuth tempAuth = localAuthDao.queryLocalByUserId(localAuth
                .getUserId());
        if (tempAuth != null) {
            //如果绑定则直接退出 用来保证平台账号的唯一性
            return new LocalAuthExecution(LocalAuthStateEnum.ONLY_ONE_ACCOUNT);
        }
        try {
            localAuth.setCreateTime(new Date());
            localAuth.setLastEditTime(new Date());
            localAuth.setPassword(MD5.getMd5(localAuth.getPassword()));
            //判断创建是否成功
            int effectedNum = localAuthDao.insertLocalAuth(localAuth);
            if (effectedNum <= 0) {
                throw new RuntimeException("帐号绑定失败");
            } else {
                return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS,
                        localAuth);
            }
        } catch (Exception e) {
            throw new RuntimeException("insertLocalAuth error: "
                    + e.getMessage());
        }
    }

    @Override
    @Transactional
    public LocalAuthExecution modifyLocalAuth(Long userId, String userName,
                                              String password, String newPassword) {
        //非空判断 判断传入的用户id 账号新旧密码是否为空   是否相同 若不满足条件则返回错误信息
        if (userId != null && userName != null && password != null
                && newPassword != null && !password.equals(newPassword)) {
            try {
                //更新密码并且对密码进行md5加密
                int effectedNum = localAuthDao.updateLocalAuth(userId,
                        userName, MD5.getMd5(password),
                        MD5.getMd5(newPassword), new Date());
                //判断是否更新成功
                if (effectedNum <= 0) {
                    throw new RuntimeException("更新密码失败");
                }
                return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS);
            } catch (Exception e) {
                throw new RuntimeException("更新密码失败:" + e.toString());
            }
        } else {
            return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
        }
    }

    private void addProfileImg(LocalAuth localAuth,
                               CommonsMultipartFile profileImg) {
        String dest = FileUtil.getPersonInfoImagePath();
        String profileImgAddr = ImageUtil.generateThumbnail(profileImg, dest);
        localAuth.getPersonInfo().setProfileImg(profileImgAddr);
    }

}
