package com.chen.myo2o.web.shop;

import com.chen.myo2o.dto.UserShopMapExecution;
import com.chen.myo2o.entity.PersonInfo;
import com.chen.myo2o.entity.Shop;
import com.chen.myo2o.entity.UserShopMap;
import com.chen.myo2o.service.UserShopMapService;
import com.chen.myo2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yss
 * @date 2019/5/25 22:14
 * <p>
 * 包描述   com.chen.myo2o.web.shop
 * 类名称   spring-boot-myo2o
 * 类描述
 */
public class MyShopPointController {
    @Autowired
    private UserShopMapService userShopMapService;

    /**
     * 列出用户的积分信息情况
     *
     * @param request
     * @return
     */
    @RequestMapping
    @ResponseBody
    private Map<String, Object> lisUserShopMapByCustomer(HttpServletRequest request) {
        //获取分页信息
        Map<String, Object> modelMap = new HashMap<>();
        //获取分页信息
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        //从session中获取顾客信息
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        //空值判断
        if ((pageIndex > -1) && (pageSize > -1) && (user != null) && (user.getUserId() != null)) {
            UserShopMap userShopMapCntion = new UserShopMap();
            userShopMapCntion.setUser(user);
            long shopId = HttpServletRequestUtil.getLong(request, "shopId");
            //如传入的店铺id不为空 则取出该店铺该顾客的积分情况
            if (shopId > -1) {
                Shop shop = new Shop();
                shop.setShopId(shopId);
                userShopMapCntion.setShop(shop);
            }
            //根据查询条件获取顾客的个店铺积分情况
            UserShopMapExecution ue = userShopMapService.listUserShopMap(userShopMapCntion, pageIndex, pageSize);
            modelMap.put("userShpMapList", ue.getUserShopMapList());
            modelMap.put("count", ue.getCount());
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
        }
        return modelMap;
    }

}
