package com.chen.myo2o.service;

import com.chen.myo2o.dto.WechatAuthExecution;
import com.chen.myo2o.entity.PersonInfo;
import com.chen.myo2o.entity.WechatAuth;
import com.chen.myo2o.enums.WechatAuthStateEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * @author yss
 * @date 2019/5/21 16:51
 * <p>
 * 包描述   com.imooc.o2o.service
 * 类名称   o2o
 * 类描述
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class WechatAuthServiceTest {
    @Autowired
    private WechatAuthService wechatAuthService;

    @Test
    public void testRegister() {
        //新增一条微信账号
        WechatAuth wechatAuth = new WechatAuth();
        PersonInfo personInfo = new PersonInfo();
        String openId = "";
        //给微信账号设置上用户信息 但是不设置上用户id
        //希望创建微信账号的时候自动创建用户信息
        personInfo.setCreateTime(new Date());
        personInfo.setName("测试一下");
        personInfo.setUserId(1l);
        wechatAuth.setPersonInfo(personInfo);
        wechatAuth.setOpenId(openId);
        wechatAuth.setCreateTime(new Date());
        wechatAuth.setUserId(1l);
        WechatAuthExecution wae = wechatAuthService.register(wechatAuth, null);
        System.out.println(wae.getState() == WechatAuthStateEnum.SUCCESS.getState());
        //通过openid找到新增的wechatAuth
        wechatAuth = wechatAuthService.getWechatAuthByOpenId(openId);
        //打印用户名字看看是否跟预期是否相符
        System.out.println(wechatAuth.getPersonInfo().getName());
    }
}
