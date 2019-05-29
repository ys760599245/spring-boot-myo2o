/**
 *
 */
package com.chen.myo2o.service;

import com.chen.myo2o.entity.Area;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

/**
 * @author 760599245
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AreaServiceTest {
    @Autowired(required = false)
    private AreaService areaService;
    @Autowired
    private CacheService cacheService;

    @Test
    public void testGetAreaList() throws JsonParseException, JsonMappingException, IOException {
        List<Area> areaList = areaService.getAreaList();
        cacheService.removeFromCache(areaService.AREALISTKEY);
        //assertEquals("西苑", areaList.get(0).getAreaName());
        for (Area area : areaList) {
            System.out.println(area.toString());
        }

    }

}
