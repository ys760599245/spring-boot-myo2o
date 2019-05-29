package com.chen.myo2o.service;

import com.chen.myo2o.dto.ShopExecution;
import com.chen.myo2o.entity.Shop;
import com.chen.myo2o.enums.ProductStateEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopServiceTest {
    private static Logger log = LoggerFactory.getLogger(ShopServiceTest.class);
    /**
     *
     */
    @Autowired(required = false)
    private ShopService shopService;

	/*@Test
	public void testAddShop() throws Exception {
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
		ShopExecution se = shopService.addShop(shop, null);
		assertEquals("mytest1", se.getShop().getShopName());
	}*/

    @Test
    public void testGetByEmployeeId() throws Exception {
        long employeeId = 2;
        ShopExecution shopExecution = shopService.getByEmployeeId(employeeId);
        List<Shop> shopList = shopExecution.getShopList();
        for (Shop shop : shopList) {
            System.out.println(shop);
        }
    }


    @Test
    public void testGetByShopId() throws Exception {
        long shopId = 18;
        Shop shop = shopService.getByShopId(shopId);
        System.out.println(shop.toString());
    }

	/*@Test
	public void testModifyShop() throws FileNotFoundException {
		Shop shop = new Shop();
		shop.setShopId(1l);
		shop.setShopName("修改后的店铺名称");
		File shopImg = new File("Users/760599245/Pictures/nu/timg.jpg");
		InputStream is = new FileInputStream(shopImg);
		ShopExecution shopExecution = shopService.modifyShop(shop, null);
		System.out.println(shopExecution.toString());
	}
*/

    @Test
    public void test() {
        System.out.println(ProductStateEnum.SUCCESS.getState());
        log.info("" + ProductStateEnum.SUCCESS.getStateInfo());
        log.info("" + ProductStateEnum.SUCCESS.name());
        System.out.println(ProductStateEnum.SUCCESS.getStateInfo());
        System.out.println(ProductStateEnum.SUCCESS.name());
    }
}
