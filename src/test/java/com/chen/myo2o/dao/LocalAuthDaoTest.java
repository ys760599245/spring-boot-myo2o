package com.chen.myo2o.dao;

import com.chen.myo2o.entity.LocalAuth;
import com.chen.myo2o.entity.PersonInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * @author yss
 * @date 2019/5/21 18:26
 * <p>
 * 包描述   com.chen.o2o.dao
 * 类名称   o2o
 * 类描述
 */
// 按case名称字母顺序排序
@RunWith(SpringRunner.class)
@SpringBootTest
public class LocalAuthDaoTest {
    private static final String username = "testusername";
    private static final String password = "testpassword";
    @Autowired
    private LocalAuthDao localAuthDao;

    @Test
    public void testAInsertLocalAuth() {
        //新增一条平台账号信息
        LocalAuth localAuth = new LocalAuth();
        PersonInfo personInfo = new PersonInfo();
        personInfo.setUserId(1l);
        //给平台账号绑定上用户信息
        localAuth.setPersonInfo(personInfo);
        //设置上用户名和密码
        localAuth.setUserName(username);
        localAuth.setPassword(password);
        localAuth.setCreateTime(new Date());
        Integer effectedNum = localAuthDao.insertLocalAuth(localAuth);
        System.out.println(1 == effectedNum);
    }


    @Test
    public void testBQueryLocalAuthByUserNameAndPwd() {
        //按照账号和密码查询用户信息
        LocalAuth localAuth = localAuthDao.queryLocalByUserNameAndPwd(username, password);
        System.out.println(localAuth.toString());
    }

    @Test
    public void testCQueryLocalByUserId() {
        //按照用户Id 查询平台账号 进而获取用户信息
        LocalAuth localAuth = localAuthDao.queryLocalByUserId(8l);
        System.out.println("测试".equals(localAuth.getPersonInfo().getName()));
        System.out.println(localAuth.getPersonInfo().getName());

    }

    @Test
    public void testDUpdateLocalAuth() {
        //依据用户id 平台账号 以及旧密码修改平台账号密码
        Date now = new Date();
        Integer effectedNum = localAuthDao.updateLocalAuth(8l, username, password, password + "new", now);
        //查询出该条平台账号的最新信息
        LocalAuth localAuth = localAuthDao.queryLocalByUserId(8l);
        //输出新密码
        System.out.println("新密码:" + localAuth.getPassword());
        System.out.println(effectedNum);
        System.out.println(effectedNum == 1);

    }
}
