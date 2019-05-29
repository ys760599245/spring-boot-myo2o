package com.chen.myo2o.dao;

import com.chen.myo2o.entity.Product;
import com.chen.myo2o.entity.ProductCategory;
import com.chen.myo2o.entity.ProductImg;
import com.chen.myo2o.entity.Shop;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductImgDaoTest {
    private static Logger log = LoggerFactory.getLogger(ProductImgDaoTest.class);
    @Autowired
    private ProductImgDao productImgDao;
    @Autowired
    private com.chen.myo2o.dao.ProductDao ProductDao;

    @Test
    public void testABatchInsertProductImg() throws Exception {
        ProductImg productImg1 = new ProductImg();
        productImg1.setImgAddr("图片1");
        productImg1.setImgDesc("测试图片1");
        productImg1.setPriority(1);
        productImg1.setCreateTime(new Date());
        productImg1.setProductId(1L);
        ProductImg productImg2 = new ProductImg();
        productImg2.setImgAddr("图片2");
        productImg2.setPriority(1);
        productImg2.setCreateTime(new Date());
        productImg2.setProductId(1L);
        List<ProductImg> productImgList = new ArrayList<ProductImg>();
        productImgList.add(productImg1);
        productImgList.add(productImg2);
        int effectedNum = productImgDao.batchInsertProductImg(productImgList);
        System.out.println("影响行数" + effectedNum);
        // assertEquals(2, effectedNum);
    }

    @Test
    public void testCDeleteProductImgByProductId() throws Exception {
        long productId = 10;
        int effectedNum = productImgDao.deleteProductImgByProductId(productId);
        log.info("受影响行数:" + effectedNum);
        // assertEquals(2, effectedNum);
    }

    @Test
    public void test03() {
        Product product = new Product();
        product.setProductName("test3");
        product.setProductDesc("测试Desc3");
        product.setImgAddr("test3");
        product.setPriority(3);
        product.setEnableStatus(1);
        product.setCreateTime(new Date());
        product.setLastEditTime(new Date());
        Shop shop = new Shop();
        shop.setShopId(3l);
        product.setShop(shop);
        product.setProductCategory(null);
        Integer effectedNum = ProductDao.insertProduct(product);
        System.out.println("受影响行数" + effectedNum);
    }

    @Test
    public void testBQueryProductImgList() {
        // TODO Auto-generated method stub
        //检查是否ProductId为1的商品是否有且仅有两张商品详情图片
        List<ProductImg> ProductImgList = productImgDao.queryProductImgList(1l);
        log.debug("===================" + ProductImgList);
    }

    @Test
    public void testDUpdateProduct() {
        Product product = new Product();
        ProductCategory pc = new ProductCategory();
        Shop shop = new Shop();
        shop.setShopId(1l);
        pc.setProductCategoryId(2l);
        product.setShop(shop);
        product.setProductName("第一个产品");
        //product.setProductCategory(pc);
        //修改ProductId为1的商品名称
        //以及商品类别并且校检验影响的行数是否为1
        int effectedNum = ProductDao.updateProduct(product);
        log.error("影响行数:" + effectedNum);
        System.out.println("影响行数:" + effectedNum);
        //assertEquals(1,effectedNum);
    }
}
