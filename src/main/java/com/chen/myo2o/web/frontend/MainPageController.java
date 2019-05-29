package com.chen.myo2o.web.frontend;

import com.chen.myo2o.entity.HeadLine;
import com.chen.myo2o.entity.ShopCategory;
import com.chen.myo2o.enums.HeadLineStateEnum;
import com.chen.myo2o.enums.ShopCategoryStateEnum;
import com.chen.myo2o.service.HeadLineService;
import com.chen.myo2o.service.ShopCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/frontend")
public class MainPageController {
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private HeadLineService headLineService;

    /**
     * 初始化前端展示系统的主页信息 包含获取以及店铺类别列表以及头条列表
     *
     * @return
     */
    @RequestMapping(value = "/listmainpageinfo", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> list1stShopCategory() {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        List<ShopCategory> shopCategoryList = new ArrayList<ShopCategory>();
        try {
            //获取一级店铺类别列表 即可ParentId为空shopCategoryId
            shopCategoryList = shopCategoryService.getFirstLevelShopCategoryList();
            modelMap.put("shopCategoryList", shopCategoryList);
        } catch (Exception e) {
            e.printStackTrace();
            ShopCategoryStateEnum s = ShopCategoryStateEnum.INNER_ERROR;
            modelMap.put("success", false);
            modelMap.put("errMsg", s.getStateInfo());
            return modelMap;
        }
        List<HeadLine> headLineList = new ArrayList<HeadLine>();
        try {
            HeadLine headLineCondition = new HeadLine();
            //获取状态为可用(1)的头条列表
            headLineCondition.setEnableStatus(1);
            headLineList = headLineService.getHeadLineList(headLineCondition);
            modelMap.put("headLineList", headLineList);
        } catch (Exception e) {
            e.printStackTrace();
            HeadLineStateEnum s = HeadLineStateEnum.INNER_ERROR;
            modelMap.put("success", false);
            modelMap.put("errMsg", s.getStateInfo());
            return modelMap;
        }
        modelMap.put("success", true);
        return modelMap;
    }

}
