package com.chen.myo2o.dao;

import com.chen.myo2o.entity.Award;
import com.chen.myo2o.entity.PersonInfo;
import com.chen.myo2o.entity.Shop;
import com.chen.myo2o.entity.UserAwardMap;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

/**
 * @author yss
 * @date 2019/5/24 17:53
 * <p>
 * 包描述   com.chen.myo2o.dao
 * 类名称   spring-boot-myo2o
 * 类描述
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserAwardMapDaoTest {

    @Autowired
    private UserAwardMapDao userAwardMapDao;

    /**
     * 测试更新功能
     */
    @Test
    public void testCUpdateUserAwardMap() {
        UserAwardMap userAwardMap = new UserAwardMap();
        PersonInfo customer = new PersonInfo();
        //按照用户名模糊查询
        userAwardMap.setUser(customer);
        List<UserAwardMap> userAwardMapList = userAwardMapDao.queryUserAwardMapList(userAwardMap, 0, 1);
        if (1 == userAwardMapList.get(0).getUsedStatus()) {
            System.out.println("积分不一致");
        }
        int i = userAwardMapDao.updateUserAwardMap(userAwardMap);
        System.out.println(1 == i);
    }

    @Test
    public void testBQueryUserAwardMapList() {

        UserAwardMap userAwardMap = new UserAwardMap();
        //测试QueryUserAwardMapList
        List<UserAwardMap> userAwardMapList = userAwardMapDao.queryUserAwardMapList(userAwardMap, 0, 1);
        System.out.println(userAwardMapList.toString());
        int count = userAwardMapDao.queryUserAwardMapCount(userAwardMap);
        //按照用户名字模糊查询
        PersonInfo customer = new PersonInfo();
        customer.setName("小明");
        userAwardMap.setUser(customer);
        List<UserAwardMap> list = userAwardMapDao.queryUserAwardMapList(userAwardMap, 0, 3);
        System.out.println(list.size() == 1);
        count = userAwardMapDao.queryUserAwardMapCount(userAwardMap);
        System.out.println(count == 1);
        //测试queryUserAwardMapById 预期按照优先级排列返回的第二个奖品的信息
        userAwardMap = userAwardMapDao.queryUserAwardMapById(userAwardMapList.get(0).getAwardId());
        System.out.println("小明".equals(userAwardMap.getAward().getAwardName()));
    }

    /**
     * 测试添加功能
     */
    @Test
    public void testAInsertAwardMap() {
        //穿点用户奖品映射信息1
        UserAwardMap userAwardMap = new UserAwardMap();
        PersonInfo customer = new PersonInfo();
        customer.setUserId(1l);
        userAwardMap.setUser(customer);

        Award award = new Award();
        award.setAwardId(1l);
        userAwardMap.setAward(award);
        Shop shop = new Shop();
        shop.setShopId(1l);
        userAwardMap.setShop(shop);
        userAwardMap.setCreateTime(new Date());
        userAwardMap.setUsedStatus(1);
        userAwardMap.setPoint(1);
        System.out.println("小明:" + userAwardMap.toString());
        int i = userAwardMapDao.insertUserAwardMap(userAwardMap);
        System.out.println(i == 1);
    }


}
