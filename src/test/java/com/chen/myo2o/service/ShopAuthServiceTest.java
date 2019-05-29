package com.chen.myo2o.service;

import com.chen.myo2o.dto.ShopAuthMapExecution;
import com.chen.myo2o.entity.ShopAuthMap;
import com.chen.myo2o.enums.ShopAuthMapStateEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopAuthServiceTest {
    @Autowired
    private ShopAuthMapService shopAuthService;

    @Test
    public void testUpdateShopAuthMap() {
        Long shopAuthId = 1L;
        String title = "CEO";
        Integer titleFlag = 2;
        Integer enableStatus = null;
        ShopAuthMap shopAuthMap = new ShopAuthMap();
        shopAuthMap.setShopAuthId(shopAuthId);
        shopAuthMap.setTitle(title);
        shopAuthMap.setTitleFlag(titleFlag);
        shopAuthMap.setEnableStatus(enableStatus);
        ShopAuthMapExecution same = shopAuthService
                .modifyShopAuthMap(shopAuthMap);
        assertEquals(ShopAuthMapStateEnum.SUCCESS.getState(), same.getState());
    }

    @Test
    public void testListShopAuthMapByShopId() {
        ShopAuthMapExecution shopAuthMapExecution = shopAuthService
                .listShopAuthMapByShopId(1L, 2, 1);
        List<ShopAuthMap> shopAuthMapList = shopAuthMapExecution
                .getShopAuthMapList();
        assertEquals(1, shopAuthMapList.size());
        shopAuthMapExecution = shopAuthService
                .listShopAuthMapByShopId(1L, 1, 2);
        shopAuthMapList = shopAuthMapExecution.getShopAuthMapList();
        assertEquals(2, shopAuthMapList.size());
    }
}
