package com.chen.myo2o.dao;

import com.chen.myo2o.entity.WechatAuth;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WechatAuthDaoTest {
    @Autowired
    private WechatAuthDao wechatAuthDao;

    @Test
    public void testAInsertWechatAuth() throws Exception {
        WechatAuth wechatAuth = new WechatAuth();
        wechatAuth.setUserId(1L);
        wechatAuth.setOpenId("dafahizhfdhaih");
        wechatAuth.setCreateTime(new Date());
        int effectedNum = wechatAuthDao.insertWechatAuth(wechatAuth);
        assertEquals(1, effectedNum);
    }

    @Test
    public void testBQueryWechatAuthByOpenId() throws Exception {
        WechatAuth wechatAuth = wechatAuthDao
                .queryWechatInfoByOpenId("ovLbns4Z7ueIBJNmgVfpDTQQLCRA");
        System.out.println(wechatAuth.getPersonInfo().getName());
        //assertEquals("test", wechatAuth.getPersonInfo().getName());
    }

    @Test
    public void testDeleteWechatAuth() throws Exception {
        WechatAuth wechatAuth = wechatAuthDao
                .queryWechatInfoByOpenId("dafahizhfdhaih");
        int effectedNum = wechatAuthDao.deleteWechatAuth(wechatAuth
                .getWechatAuthId());
        assertEquals(1, effectedNum);
    }
}
