package com.chen.myo2o.dao;

import com.chen.myo2o.entity.PersonInfo;
import com.chen.myo2o.entity.Shop;
import com.chen.myo2o.entity.ShopAuthMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

/**
 * @author yss
 * @date 2019/5/24 22:41
 * <p>
 * 包描述   com.chen.myo2o.dao
 * 类名称   spring-boot-myo2o
 * 类描述
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopAuthMapDaoTest {

    @Autowired
    private ShopAuthMapDao shopAuthMapDao;

    @Test
    public void testCUpdateShopAuthMap() {
        List<ShopAuthMap> shopAuthMapList = shopAuthMapDao.queryShopAuthMapListByShopId(15, 0, 3);
        System.out.println("==========" + shopAuthMapList.toString());
        System.out.println(shopAuthMapList.size());
        shopAuthMapList.get(0).setTitle("CCO");
        shopAuthMapList.get(0).setTitleFlag(2);
        int i = shopAuthMapDao.updateShopAuthMap(shopAuthMapList.get(0));
        System.out.println(i);
    }

    @Test
    public void testBQueryShopAuth() {
        List<ShopAuthMap> shopAuthMapList = shopAuthMapDao.queryShopAuthMapListByShopId(21, 0, 3);
        System.out.println(shopAuthMapList.size() == 1);
        ShopAuthMap shopAuthMap = shopAuthMapDao.queryShopAuthMapById(21l);
        System.out.println(shopAuthMap.getTitle());
        System.out.println("雇员名字:" + shopAuthMap.getEmployee().getName());
        System.out.println("商店名字:" + shopAuthMap.getShop().getShopName());
        int i = shopAuthMapDao.queryShopAuthCountByShopId(21);
        System.out.println(i == 1);
    }

    @Test
    public void testInsertShopAuthMap() {
        //创建店铺授权信息1
        ShopAuthMap shopAuthMap1 = new ShopAuthMap();
        PersonInfo employee = new PersonInfo();
        employee.setUserId(1l);

        shopAuthMap1.setEmployee(employee);
        Shop shop = new Shop();
        shop.setShopId(1l);
        shopAuthMap1.setShop(shop);
        shopAuthMap1.setTitle("CEO");
        shopAuthMap1.setTitleFlag(1);
        shopAuthMap1.setCreateTime(new Date());
        shopAuthMap1.setLastEditTime(new Date());
        shopAuthMap1.setEnableStatus(1);
        int i = shopAuthMapDao.insertShopAuthMap(shopAuthMap1);
        System.out.println(i == 1);
        //创建店铺授权信息2
        ShopAuthMap shopAuthMap2 = new ShopAuthMap();
        shopAuthMap2.setEmployee(employee);
        Shop shop2 = new Shop();
        shop2.setShopId(2l);
        shopAuthMap2.setShop(shop2);
        shopAuthMap2.setTitle("打工仔");
        shopAuthMap2.setTitleFlag(2);
        shopAuthMap2.setCreateTime(new Date());
        shopAuthMap2.setLastEditTime(new Date());
        shopAuthMap2.setEnableStatus(0);
        int i1 = shopAuthMapDao.insertShopAuthMap(shopAuthMap2);
        System.out.println(i1 == 1);
    }

    /**
     * 测试删除
     */
    @Test
    public void testDeleteShopAuthMap() {
        List<ShopAuthMap> shopAuthMaps1 = shopAuthMapDao.queryShopAuthMapListByShopId(14, 0, 1);
        List<ShopAuthMap> shopAuthMaps2 = shopAuthMapDao.queryShopAuthMapListByShopId(25, 0, 1);
        System.out.println("1:" + shopAuthMaps1.size());
        System.out.println("2:" + shopAuthMaps2.toString());
        int i = shopAuthMapDao.deleteShopAuthMap(shopAuthMaps1.get(0).getEmployeeId(), shopAuthMaps1.get(0).getShopId());
        int i1 = shopAuthMapDao.deleteShopAuthMap(shopAuthMaps2.get(0).getEmployeeId(), shopAuthMaps2.get(0).getShopId());
        System.out.println(i == i1);
    }
}
