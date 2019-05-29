package com.chen.myo2o.service;

import com.chen.myo2o.dto.ProductCategoryExecution;
import com.chen.myo2o.dto.ProductExecution;
import com.chen.myo2o.entity.Product;
import com.chen.myo2o.entity.ProductCategory;
import com.chen.myo2o.enums.ProductCategoryStateEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductCategoryServiceTest {
    @Autowired
    private ProductCategoryService productCategoryService;
    @Autowired
    private ProductService productService;

    @Test
    public void testAAddProductCategory() throws Exception {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setProductCategoryName("商品类别1");
        productCategory.setProductCategoryDesc("测试商品类别");
        productCategory.setPriority(1);
        productCategory.setCreateTime(new Date());
        productCategory.setLastEditTime(new Date());
        productCategory.setShopId(1L);
        ProductCategory productCategory2 = new ProductCategory();
        productCategory2.setProductCategoryName("商品类别2");
        productCategory2.setProductCategoryDesc("测试商品类别2");
        productCategory2.setPriority(2);
        productCategory2.setCreateTime(new Date());
        productCategory2.setLastEditTime(new Date());
        productCategory2.setShopId(1L);
        List<ProductCategory> productCategoryList = new ArrayList<ProductCategory>();
        productCategoryList.add(productCategory);
        productCategoryList.add(productCategory2);
        ProductCategoryExecution productCategoryExecution = productCategoryService
                .batchAddProductCategory(productCategoryList);
        assertEquals(ProductCategoryStateEnum.SUCCESS.getStateInfo(),
                productCategoryExecution.getStateInfo());
    }

    @Test
    public void testBGetByShopId() throws Exception {
        long shopId = 1;
        List<ProductCategory> productCategoryList = productCategoryService
                .getByShopId(shopId);
        assertEquals(2, productCategoryList.size());
        System.out.println(productCategoryList.get(0).toString());

    }

    @Test
    public void testCDeleteProductCategory() throws Exception {
        long shopId = 1;
        List<ProductCategory> productCategoryList = productCategoryService
                .getByShopId(shopId);
        ProductCategoryExecution productCategoryExecution = productCategoryService
                .deleteProductCategory(productCategoryList.get(0)
                        .getProductCategoryId(), shopId);
        assertEquals(ProductCategoryStateEnum.SUCCESS.getStateInfo(),
                productCategoryExecution.getStateInfo());
        productCategoryExecution = productCategoryService
                .deleteProductCategory(productCategoryList.get(1)
                        .getProductCategoryId(), shopId);
        assertEquals(ProductCategoryStateEnum.SUCCESS.getStateInfo(),
                productCategoryExecution.getStateInfo());
    }

    @Test
    public void test01() {
        Product productCondition = new Product();
        productCondition.setEnableStatus(1);
        ProductExecution productList = productService.getProductList(productCondition, 0, 3);
        System.out.println(productList.toString());


    }

}
