package com.chen.myo2o.web.frontend;

import com.chen.myo2o.service.AreaService;
import com.chen.myo2o.service.CacheService;
import com.chen.myo2o.service.HeadLineService;
import com.chen.myo2o.service.ShopCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author yss
 * @date 2019/5/25 22:42
 * <p>
 * 包描述   com.chen.myo2o.web.frontend
 * 类名称   spring-boot-myo2o
 * 类描述
 * 超级管理员 清楚头条 区域 店铺 的缓存
 */
@Controller
@RequestMapping(value = "superadmins")
public class CacheController {
    @Value("AREALISTKEY")
    private String AREALISTKEY;

    @Value("HLLISTKEY")
    private String HLLISTKEY;
    @Value("SCLISTKEY")
    private String SCLISTKEY;

    @Autowired
    private CacheService cacheService;
    @Autowired
    private AreaService areaService;
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private HeadLineService headLineService;

    /**
     * 清楚区域信息相关的所有的Redis缓存
     *
     * @return
     */
    @RequestMapping(value = "/clearcache4Area", method = RequestMethod.GET)
    public String Clearcache4Area() {
        cacheService.removeFromCache(AREALISTKEY);
        return "shop/operationsuccess";
    }

    /**
     * 清楚头条信息相关的所有的Redis缓存
     *
     * @return
     */
    @RequestMapping(value = "/clearcache4headline", method = RequestMethod.GET)
    public String Clearcache4Headline() {
        cacheService.removeFromCache(HLLISTKEY);
        return "shop/operationsuccess";
    }

    /**
     * 清楚店铺信息相关的所有的Redis缓存
     *
     * @return
     */
    @RequestMapping(value = "/clearcache4shopcategory", method = RequestMethod.GET)
    public String Clearcache4Shopcategory() {
        cacheService.removeFromCache(SCLISTKEY);
        return "shop/operationsuccess";
    }
}
