package com.chen.myo2o.web.frontend;

import com.chen.myo2o.dto.AwardExecution;
import com.chen.myo2o.entity.Award;
import com.chen.myo2o.entity.PersonInfo;
import com.chen.myo2o.entity.UserShopMap;
import com.chen.myo2o.service.AwardService;
import com.chen.myo2o.service.UserShopMapService;
import com.chen.myo2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/frontend")
public class ShopAwardController {
    @Autowired
    private AwardService awardService;
    @Autowired
    private UserShopMapService userShopMapService;

    @RequestMapping(value = "/getawardbyid", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getAwardbyId(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        long awardId = HttpServletRequestUtil.getLong(request, "awardId");
        if (awardId > -1) {
            Award award = awardService.getAwardById(awardId);
            modelMap.put("award", award);
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty awardId");
        }
        return modelMap;
    }

    @RequestMapping(value = "/listawardsbyshop", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listAwardsByShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        if ((pageIndex > -1) && (pageSize > -1) && (shopId > -1)) {
            //根据输入的奖品名称模糊查询
            String awardName = HttpServletRequestUtil.getString(request, "awardName");
            Award awardCondition = compactAwardCondition4Search(shopId, awardName);
            //传入查询天剑分页获取奖品信息
            AwardExecution ae = awardService.getAwardList(awardCondition, pageIndex, pageSize);
            modelMap.put("awardList", ae.getAwardList());
            modelMap.put("count", ae.getCount());
            modelMap.put("success", true);
            //从session中获取用户信息 主要是为了 显示该用户在本店铺的积分
            PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
            if (user != null && user.getUserId() != null) {
                //获取该用户在本店铺的积分信息  有点问题
                UserShopMap userShopMap = userShopMapService.getUserShopMap(user.getUserId(), awardCondition.getShopId());
                if (userShopMap == null) {
                    modelMap.put("totalPoint", 0);
                } else {

                    modelMap.put("totalPoint", userShopMap.getPoint());
                }
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
        }
        return modelMap;
    }

    private Award compactAwardCondition4Search(long shopId, String awardName) {
        Award awardCondition = new Award();
        awardCondition.setShopId(shopId);
        if (awardName != null) {
            awardCondition.setAwardName(awardName);
        }
        return awardCondition;
    }
}
