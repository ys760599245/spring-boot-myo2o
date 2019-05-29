package com.chen.myo2o.dao;

import com.chen.myo2o.entity.Area;
import com.chen.myo2o.entity.PersonInfo;
import com.chen.myo2o.entity.Shop;
import com.chen.myo2o.entity.ShopCategory;
import com.chen.myo2o.service.ShopService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopDaoTest {
    @Autowired(required = false)
    private ShopDao shopDao;
    @Autowired
    private ShopService shopService;

    @Test
    public void testAInsertShop() throws Exception {
        Shop shop = new Shop();
        shop.setOwnerId(1L);
        Area area = new Area();
        area.setAreaId(1L);
        ShopCategory sc = new ShopCategory();
        sc.setShopCategoryId(1L);
        shop.setShopName("mytest1");
        shop.setShopDesc("mytest1");
        shop.setShopAddr("testaddr1");
        shop.setPhone("13810524526");
        shop.setShopImg("test1");
        shop.setLongitude(1D);
        shop.setLatitude(1D);
        shop.setCreateTime(new Date());
        shop.setLastEditTime(new Date());
        shop.setEnableStatus(0);
        shop.setAdvice("审核中");
        shop.setArea(area);
        shop.setShopCategory(sc);
        int effectedNum = shopDao.insertShop(shop);
        assertEquals(1, effectedNum);
    }

    @Test
    public void testBQueryByEmployeeId() throws Exception {
        long employeeId = 1;
        List<Shop> shopList = shopDao.queryByEmployeeId(employeeId);
        for (Shop shop : shopList) {
            System.out.println(shop);
        }
    }

    @Test
    public void testBQueryShopList() throws Exception {
        Shop shop = new Shop();
        List<Shop> shopList = shopDao.queryShopList(shop, 0, 2);
        assertEquals(2, shopList.size());
        int count = shopDao.queryShopCount(shop);
        assertEquals(3, count);
        shop.setShopName("花");
        shopList = shopDao.queryShopList(shop, 0, 3);
        assertEquals(2, shopList.size());
        count = shopDao.queryShopCount(shop);
        assertEquals(2, count);
        shop.setShopId(1L);
        shopList = shopDao.queryShopList(shop, 0, 3);
        assertEquals(1, shopList.size());
        count = shopDao.queryShopCount(shop);
        assertEquals(1, count);

    }

    @Test
    public void testCQueryByShopId() throws Exception {
        long shopId = 18;
        Shop shop = shopDao.queryByShopId(shopId);
        System.out.println(shop);
        System.out.println("商店的区域" + shop.getArea().getAreaName());
        System.out.println("商店的区域的id" + shop.getArea().getAreaId());
    }

    @Test
    public void testDUpdateShop() {
        long shopId = 1;
        Shop shop = shopDao.queryByShopId(shopId);
        Area area = new Area();
        area.setAreaId(1L);
        shop.setArea(area);
        ShopCategory shopCategory = new ShopCategory();
        shopCategory.setShopCategoryId(1L);
        shop.setShopCategory(shopCategory);
        shop.setShopName("四季花");
        int effectedNum = shopDao.updateShop(shop);
        assertEquals(1, effectedNum);
    }

    @Test
    public void testEDeleteShopByName() throws Exception {
        String shopName = "mytest1";
        int effectedNum = shopDao.deleteShopByName(shopName);
        assertEquals(1, effectedNum);

    }

    @Test
    public void testQueryShopList() {
        Shop shopCondition = new Shop();
        PersonInfo owner = new PersonInfo();
        owner.setUserId(1l);
        shopCondition.setOwnerId(1l);
        List<Shop> shopList = shopDao.queryShopList(shopCondition, 0, 5);
        System.out.println(shopList);
        Integer count = shopDao.queryShopCount(shopCondition);
        System.out.println("店铺列表的大小:" + shopList.size());
        System.out.println("店铺列表大小:" + count);
        ShopCategory sc = new ShopCategory();
        sc.setShopCategoryId(3l);
        shopCondition.setShopCategory(sc);
        shopList = shopDao.queryShopList(shopCondition, 0, 2);
        System.out.println("新店铺的列表大小:" + shopList.size());
        count = shopDao.queryShopCount(shopCondition);
        System.out.println("新的店铺总数:" + count);
    }

    @Test
    public void testGetShopList() {
        Shop shopCondition = new Shop();
        ShopCategory sc = new ShopCategory();
        sc.setParentId(3l);
        shopCondition.setParentCategory(sc);
        // ShopExecution se = shopService.getShopList(shopCondition, 1, 2);
        //System.out.println("店铺列表数为:" + se.getShopList().size());
        //System.out.println("店铺总数为:" + se.getCount());
    }

    @Test
    public void testQueryShopListAndCount() {
        Shop shopCondition = new Shop();
        ShopCategory childCategory = new ShopCategory();
        ShopCategory parentCategory = new ShopCategory();
        parentCategory.setShopCategoryId(12l);
        shopCondition.setShopCategory(childCategory);
        List<Shop> shopList = shopDao.queryShopList(shopCondition, 0, 5);
        Integer count = shopDao.queryShopCount(shopCondition);
        System.out.println("店铺列表的:" + shopList.size());
        System.out.println("店铺列表大小:" + count);

    }

    @Test
    public void test03() {
        Shop shopContion = new Shop();
        shopContion.setEnableStatus(1);
        List<Shop> shopList = shopDao.queryShopList(shopContion, 1, 999);
        System.out.println(shopList);
    }

}
