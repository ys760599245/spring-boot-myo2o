package com.chen.myo2o.dao;

import com.chen.myo2o.entity.PersonInfo;
import com.chen.myo2o.entity.Shop;
import com.chen.myo2o.entity.UserProductMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author yss
 * @date 2019/5/24 19:13
 * <p>
 * 包描述   com.chen.myo2o.dao
 * 类名称   spring-boot-myo2o
 * 类描述
 */
@RunWith(SpringRunner.class)
@SpringBootTest

public class UserProductMapDaoTest {
    @Autowired
    private UserProductMapDao userProductMapDao;

    @Test
    public void testBQueryUserProductMap() {
        UserProductMap userProductMap = new UserProductMap();
        PersonInfo customer = new PersonInfo();
        //按照顾客名称模糊查询
        customer.setName("xiaoming");
        userProductMap.setUser(customer);
        List<UserProductMap> userProductMapList = userProductMapDao.queryUserProductMapList(userProductMap, 0, 1);
        System.out.println(userProductMapList.toString());
        System.out.println(userProductMapList.size() == 1);
        int count = userProductMapDao.queryUserProductMapCount(userProductMap);
        System.out.println(1 == count);
        //叠加店铺查询
        Shop shop = new Shop();
        shop.setShopId(18l);
        userProductMap.setShop(shop);
        userProductMapList = userProductMapList = userProductMapDao.queryUserProductMapList(userProductMap, 0, 3);
        System.out.println(userProductMapList.size());
        count = userProductMapDao.queryUserProductMapCount(userProductMap);
        System.out.println(count);
    }
}
