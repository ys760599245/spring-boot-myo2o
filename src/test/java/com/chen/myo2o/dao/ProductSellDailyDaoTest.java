package com.chen.myo2o.dao;

import com.chen.myo2o.entity.ProductSellDaily;
import com.chen.myo2o.entity.Shop;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author yss
 * @date 2019/5/24 20:47
 * <p>
 * 包描述   com.chen.myo2o.dao
 * 类名称   spring-boot-myo2o
 * 类描述
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductSellDailyDaoTest {
    @Autowired
    private ProductSellDailyDao productSellDailyDao;

    @Test
    public void testBQueryProductSellDaily() {
        ProductSellDaily productSellDaily = new ProductSellDaily();
        //叠加店铺去查询
        Shop shop = new Shop();
        shop.setShopId(29l);
        productSellDaily.setShop(shop);
        List<ProductSellDaily> productSellDailies = productSellDailyDao.queryProductSellDailyList(null, null, null);
        System.out.println(productSellDailies.size());
    }

    @Test
    public void insertProductSellDaily() {
        //创建商品日销统计
        int i = productSellDailyDao.insertProductSellDaily();
        System.out.println(i == 1);
    }

    @Test
    public void insertDefaultProductSellDaily() {
        //创建商品日销统计
        int i = productSellDailyDao.insertDefaultProductSellDaily();
        System.out.println(i == 11);
    }
}
