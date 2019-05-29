/**
 *
 */
package com.chen.myo2o.service;

import com.chen.myo2o.entity.Product;
import com.chen.myo2o.entity.ProductCategory;
import com.chen.myo2o.entity.Shop;
import com.chen.myo2o.enums.ProductStateEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

/**
 * @author 760599245
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceTest {
    @Autowired
    private ProductService productService;

    @Test
    private void testAddProduct() throws FileNotFoundException {
        // TODO Auto-generated method stub
        // 创建Shopid为1而且productCategoryid为1的商品实例并且给其成员变量赋值
        Product product = new Product();
        Shop shop = new Shop();
        shop.setShopId(1l);
        ProductCategory pc = new ProductCategory();
        pc.setProductCategoryId(1l);
        product.setShop(shop);
        product.setProductCategory(pc);
        product.setProductName("测试商品1");
        product.setProductDesc("测试商品1");
        product.setPriority(20);
        product.setCreateTime(new Date());
        product.setEnableStatus(ProductStateEnum.SUCCESS.getState());
        //创建文件流
        File thumbnailFile = new File("C:\\Users\\760599245\\Pictures\\Nuts\\psb.jpg");
        InputStream is = new FileInputStream(thumbnailFile);


    }
}

