package com.chen.myo2o.dao;

import com.chen.myo2o.entity.PersonInfo;
import com.chen.myo2o.entity.Shop;
import com.chen.myo2o.entity.UserShopMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

/**
 * @author yss
 * @date 2019/5/24 22:08
 * <p>
 * 包描述   com.chen.myo2o.dao
 * 类名称   spring-boot-myo2o
 * 类描述
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserShopMapDaoTest {
    @Autowired
    private UserShopMapDao userShopMapDao;

    /**
     * 测试更新功能
     */
    @Test
    public void testCUpdateShopMap() {
        UserShopMap userShopMap = new UserShopMap();
        userShopMap = userShopMapDao.queryUserShopMap(1, 29);
        if (userShopMap.getPoint() == 1) {
            System.out.println("积分不一致");
        }
        userShopMap.setPoint(2);
        int i = userShopMapDao.updateUserShopMapPoint(userShopMap);
        System.out.println(1 == i);
    }

    /**
     * 查询
     */
    @Test
    public void testBQueryUserShopMap() {
        UserShopMap userShopMap = new UserShopMap();
        //查看全部
        List<UserShopMap> userShopMapList = (List<UserShopMap>) userShopMapDao.queryUserShopMapList(null, 0, 1);
        System.out.println(userShopMapList.size());
        int count = userShopMapDao.queryUserShopMapCount(userShopMap);
        System.out.println(count);
        //按照店铺去查询
        Shop shop = new Shop();
        shop.setShopId(29l);
        userShopMap.setShop(shop);
        userShopMapList = userShopMapDao.queryUserShopMapList(userShopMap, 0, 1);
        System.out.println(userShopMapList.toString());
        count = userShopMapDao.queryUserShopMapCount(userShopMap);
        System.out.println(count);
        //按照用户id和店铺查询
        userShopMap = userShopMapDao.queryUserShopMap(1, 29);
        System.out.println(userShopMap);
        System.out.println("测试".equals(userShopMap.getUser().getName()));

    }

    /**
     * 测试添加功能
     */
    @Test
    public void testInsertUserShopMap() {
        //创建用户店铺积分统计信息1
        UserShopMap userShopMap = new UserShopMap();
        userShopMap.setShopName("小明");
        PersonInfo customer = new PersonInfo();
        customer.setUserId(1l);
        userShopMap.setUser(customer);
        Shop shop = new Shop();
        shop.setShopId(29l);
        shop.setShopName("小明");
        userShopMap.setShop(shop);
        userShopMap.setCreateTime(new Date());
        userShopMap.setPoint(1);
        int i = userShopMapDao.insertUserShopMap(userShopMap);


        //创建用户店铺积分统计信息2
        UserShopMap userShopMap2 = new UserShopMap();
        userShopMap2.setShopName("小明2");
        PersonInfo customer2 = new PersonInfo();
        customer2.setUserId(8l);
        userShopMap2.setUser(customer2);
        Shop shop2 = new Shop();
        shop2.setShopName("小明2");
        shop2.setShopId(28l);
        userShopMap2.setShop(shop2);
        userShopMap2.setCreateTime(new Date());
        userShopMap2.setPoint(1);
        int i2 = userShopMapDao.insertUserShopMap(userShopMap);
        System.out.println(i);
        System.out.println(i2);


    }
}
