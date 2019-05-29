package com.chen.myo2o.service;

import com.chen.myo2o.dto.LocalAuthExecution;
import com.chen.myo2o.entity.LocalAuth;
import com.chen.myo2o.entity.PersonInfo;
import com.chen.myo2o.enums.WechatAuthStateEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author yss
 * @date 2019/5/22 10:13
 * <p>
 * 包描述   com.imooc.o2o.service
 * 类名称   o2o
 * 类描述
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class LocalAuthServiceTest {
    private final static String username = "xiangze";
    private final static String password = "123456";
    private final static String newPassword = "testnewpassword";
    @Autowired
    private LocalAuthService localAuthService;

    @Test
    public void testBindLocalAuth() {
        //新增一个平台账号
        LocalAuth localAuth = new LocalAuth();
        PersonInfo personInfo = new PersonInfo();
        //给平台账户设置上用户信息
        //给用户设置上用户Id 标明是某个用户创建的账号
        personInfo.setUserId(7l);
        //给平台账户设置上用户信息 标明是哪个用户绑定的
        localAuth.setPersonInfo(personInfo);
        //设置账户
        localAuth.setUserName(username);
        //设置密码
        localAuth.setPassword(password);
        //绑定账户
        LocalAuthExecution lae = localAuthService.bindLocalAuth(localAuth);
        System.out.println(lae.getState() == WechatAuthStateEnum.SUCCESS.getState());
        //通过userid找到新增的localAuth
        localAuth = localAuthService.getLocalAuthByUserId(personInfo.getUserId());
        //打印用户名和账户密码看看是否与预期相符合
        System.out.println("用户昵称:" + localAuth.getPersonInfo().getName());
        System.out.println("用户密码:" + localAuth.getPassword());

    }

    @Test
    public void testBModifyLocalAuth() {
//设置账户信息
        Long userid = 8l;
        //修改账号对应的密码
        LocalAuthExecution localAuthExecution = localAuthService.modifyLocalAuth(userid, username, password, password);
        System.out.println(WechatAuthStateEnum.SUCCESS.getState() == localAuthExecution.getState());
        //通过账号密码找到修改后的LocalAuth
        LocalAuth LocalAuth = localAuthService.getLocalAuthByUserNameAndPwd(username, password);
        //打印用户名查看跟预期是否一样
        System.out.println(LocalAuth.getPersonInfo().getName());

    }
}
