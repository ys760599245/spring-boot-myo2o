package com.chen.myo2o.web.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/frontend")
public class FrontendController {
    @RequestMapping(value = "/mainpage", method = RequestMethod.GET)
    private String showMainPage() {
        return "frontend/mainpage";
    }

    /**
     * 商品详情路由
     *
     * @return
     */
    @RequestMapping(value = "/productdetail", method = RequestMethod.GET)
    private String showProductDetail() {
        return "frontend/productdetail";
    }

    /**
     * 店铺详情路由
     *
     * @return
     */
    @RequestMapping(value = "/shopdetail", method = RequestMethod.GET)
    private String showShopDetail() {
        return "frontend/shopdetail";
    }

    @RequestMapping(value = "/shoplist", method = RequestMethod.GET)
    private String showShopList() {
        return "frontend/shoplist";
    }

    @RequestMapping(value = "/index")

    private String index() {
        return "frontend/index";
    }

    /**
     * 用户各个店铺积分信息页路由
     *
     * @return
     */
    @RequestMapping(value = "/mypoint", method = RequestMethod.GET)
    private String myPoint() {
        return "frontend/mypoint";
    }

    //消费记录列表路由
    @RequestMapping(value = "/myrecord", method = RequestMethod.GET)
    private String myRecord() {
        return "frontend/myrecord";
    }

    @RequestMapping(value = "/pointrecord", method = RequestMethod.GET)
    private String pointRecord() {
        return "frontend/pointrecord";
    }

    //奖品详情图页面路由
    @RequestMapping(value = "/awarddetail", method = RequestMethod.GET)
    private String awardDetail() {
        return "frontend/awarddetail";
    }

    @RequestMapping(value = "/customerbind", method = RequestMethod.GET)
    private String customerBind() {
        return "frontend/customerbind";
    }


    //奖品详情图页面路由
    @RequestMapping(value = "/myawarddetail", method = RequestMethod.GET)
    private String ShopMyAwardDetail() {
        return "frontend/myawarddetail";
    }

}
