package com.chen.myo2o.dao;


import com.chen.myo2o.entity.Area;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AreaDaoTest {
    @Autowired
    private AreaDao areaDao;
    @Autowired
    private com.chen.myo2o.cache.JedisUtil JedisUtil;

    @Test
    public void testAInsertArea() throws Exception {
        Area area = new Area();
        area.setAreaName("区域1");
        area.setAreaDesc("区域1");
        area.setPriority(1);
        area.setCreateTime(new Date());
        area.setLastEditTime(new Date());
        int effectedNum = areaDao.insertArea(area);
        assertEquals(1, effectedNum);
    }

    @Test
    public void testBQueryArea() throws Exception {
        List<Area> areaList = areaDao.queryArea();
        // assertEquals(2, areaList.size());
        System.out.println(areaList);
    }

    @Test
    public void testCUpdateArea() throws Exception {
        Area area = new Area();
        area.setAreaId(1L);
        area.setAreaName("南苑");
        area.setLastEditTime(new Date());
        int effectedNum = areaDao.updateArea(area);
        assertEquals(1, effectedNum);
    }

    @Test
    public void testDDeleteArea() throws Exception {
        long areaId = -1;
        List<Area> areaList = areaDao.queryArea();
        for (Area myArea : areaList) {
            if ("区域1".equals(myArea.getAreaName())) {
                areaId = myArea.getAreaId();
            }
        }
        List<Long> areaIdList = new ArrayList<Long>();
        areaIdList.add(areaId);
        int effectedNum = areaDao.batchDeleteArea(areaIdList);
        assertEquals(1, effectedNum);
    }

    @Test
    public void test01() {
        JedisUtil.STRINGS.set("Banner", "小明");
        System.out.println(JedisUtil.STRINGS.get("Banner"));
    }

    @Test
    public void test02() throws UnsupportedEncodingException {
        String name = java.net.URLEncoder.encode("测试", "UTF-8");
        System.out.println(name);
        name = java.net.URLEncoder.encode(name, "UTF-8");
        System.out.println(name);
        name = java.net.URLDecoder.decode(name, "UTF-8");
        System.out.println(name);
        System.out.println(java.net.URLDecoder.decode(name, "UTF-8"));
        long timpStamp = System.currentTimeMillis();
        long userAwardId = 2l;
        long UserId = 2l;
        String content = "{\"userAwardId\":" + userAwardId + ",\"customerId\":" + UserId
                + ",\"createTime\":" + timpStamp + "}";
        String urlPrefix = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=${weixinappid}&redirect_uri=";
        String authUrl = "http://o2o.yitiaojieinfo.com/o2o/shop/addshopauthmap";
        String urlMiddle = "&response_type=code&scope=snsapi_userinfo&state=";
        String urlSuffix = "#wechat_redirect";
        String longUrl2 = urlPrefix + content + urlSuffix;
        //String longUrl = urlPrefix + authUrl + urlMiddle + URLEncoder.encode(content, "UTF-8") + urlSuffix;
        //System.out.println("longUrl:"+longUrl);
        System.out.println("longUrl2:" + longUrl2);

    }

}
