package com.chen.myo2o.dao;

import com.chen.myo2o.entity.ShopCategory;
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
public class ShopCategoryDaoTest {
    @Autowired
    private ShopCategoryDao shopCategoryDao;

    @Test
    public void testAInsertShopCategory() throws Exception {
        ShopCategory shopCategory = new ShopCategory();
        shopCategory.setShopCategoryName("店铺类别1");
        shopCategory.setShopCategoryDesc("测试商品类别");
        shopCategory.setPriority(1);
        shopCategory.setCreateTime(new Date());
        shopCategory.setLastEditTime(new Date());
        shopCategory.setParentId(1L);
        int effectedNum = shopCategoryDao.insertShopCategory(shopCategory);
        assertEquals(1, effectedNum);
    }

    @Test
    public void testBQueryShopCategory() throws Exception {
        ShopCategory sc = new ShopCategory();
        List<ShopCategory> shopCategoryList = shopCategoryDao
                .queryShopCategory(sc);
        assertEquals(3, shopCategoryList.size());
        sc.setParentId(1L);
        shopCategoryList = shopCategoryDao.queryShopCategory(sc);
        assertEquals(1, shopCategoryList.size());
        sc.setParentId(null);
        sc.setShopCategoryId(0L);
        shopCategoryList = shopCategoryDao.queryShopCategory(sc);
        assertEquals(2, shopCategoryList.size());

    }

    @Test
    public void testCUpdateShopCategory() throws Exception {
        ShopCategory shopCategory = new ShopCategory();
        shopCategory.setShopCategoryId(1L);
        shopCategory.setShopCategoryName("把妹");
        shopCategory.setShopCategoryDesc("把妹");
        shopCategory.setLastEditTime(new Date());
        int effectedNum = shopCategoryDao.updateShopCategory(shopCategory);
        assertEquals(1, effectedNum);
    }

    @Test
    public void testDDeleteShopCategory() throws Exception {
        ShopCategory sc = new ShopCategory();
        sc.setParentId(1L);
        List<ShopCategory> shopCategoryList = shopCategoryDao
                .queryShopCategory(sc);
        long shopCategoryId = shopCategoryList.get(0).getShopCategoryId();
        int effectedNum = shopCategoryDao.deleteShopCategory(shopCategoryId);
        assertEquals(1, effectedNum);
    }

    @Test
    public void testQueryShopCategory() {
        List<ShopCategory> ShopCategoryList = shopCategoryDao.queryShopCategoryByIds(null);
        System.out.println(ShopCategoryList);
    }
}
