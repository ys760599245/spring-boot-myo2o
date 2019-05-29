package com.chen.myo2o.web.shop;

import com.chen.myo2o.dto.ShopAuthMapExecution;
import com.chen.myo2o.entity.PersonInfo;
import com.chen.myo2o.entity.Shop;
import com.chen.myo2o.entity.ShopAuthMap;
import com.chen.myo2o.entity.WechatAuth;
import com.chen.myo2o.enums.ShopAuthMapStateEnum;
import com.chen.myo2o.service.PersonInfoService;
import com.chen.myo2o.service.ShopAuthMapService;
import com.chen.myo2o.service.WechatAuthService;
import com.chen.myo2o.util.CodeUtil;
import com.chen.myo2o.util.HttpServletRequestUtil;
import com.chen.myo2o.util.weixin.WeiXinUserUtil;
import com.chen.myo2o.util.weixin.message.pojo.UserAccessToken;
import com.chen.myo2o.util.weixin.message.req.WechatInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/shop")
public class ShopAuthManagementController {
    //微信获取用户信息的API前缀
    private static String urlPrefix;
    //微信获取用户信息的API中间部分
    private static String urlMiddle;
    //微信获取用户信息的API后缀
    private static String urlSuffix;
    //微信回传给响应添加授权信息的url
    private static String authUrl;
    @Autowired
    private ShopAuthMapService shopAuthMapService;
    @Autowired
    private PersonInfoService personInfoService;
    @Autowired
    private WechatAuthService wechatAuthService;

    @Value("wechat.prefix")
    public static void setUrlPrefix(String urlPrefix) {
        ShopAuthManagementController.urlPrefix = urlPrefix;
    }

    @Value("wechat.middle")
    public static void setUrlMiddle(String urlMiddle) {
        ShopAuthManagementController.urlMiddle = urlMiddle;
    }

    @Value("wechat.suffix")
    public static void setUrlSuffix(String urlSuffix) {
        ShopAuthManagementController.urlSuffix = urlSuffix;
    }

    @Value("wechat.auth.url")
    public static void setAuthUrl(String authUrl) {
        ShopAuthManagementController.authUrl = authUrl;
    }

    @RequestMapping(value = "/listshopauthmapsbyshop", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listShopAuthMapsByShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        //取出分页信息
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        //从Session中获取店铺信息
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        //空值判断
        if ((pageIndex > -1) && (pageSize > -1) && (currentShop != null) && (currentShop.getShopId() != null)) {
            //分页取出该店铺下面的授权信息
            ShopAuthMapExecution se = shopAuthMapService.listShopAuthMapByShopId(currentShop.getShopId(), pageIndex,
                    pageSize);
            modelMap.put("shopAuthMapList", se.getShopAuthMapList());
            modelMap.put("count", se.getCount());
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
        }
        return modelMap;
    }

    @RequestMapping(value = "/getshopauthmapbyid", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getShopAuthMapById(@RequestParam Long shopAuthId) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if (shopAuthId != null && shopAuthId > -1) {
            //前台传入的shopAuthid查找对应的授权信息
            ShopAuthMap shopAuthMap = shopAuthMapService.getShopAuthMapById(shopAuthId);
            modelMap.put("shopAuthMap", shopAuthMap);
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty shopAuthId");
        }
        return modelMap;
    }

  /*  @RequestMapping(value = "/addshopauthmap", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> addShopAuthMap(String shopAuthMapStr, HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        ObjectMapper mapper = new ObjectMapper();
        ShopAuthMap shopAuthMap = null;
        try {
            shopAuthMap = mapper.readValue(shopAuthMapStr, ShopAuthMap.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }
        if (shopAuthMap != null) {
            try {
                Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
                PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
                if (!currentShop.getOwnerId().equals(user.getUserId())) {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", "无操作权限");
                    return modelMap;
                }
                shopAuthMap.setShopId(currentShop.getShopId());
                shopAuthMap.setEmployeeId(user.getUserId());
                ShopAuthMapExecution se = shopAuthMapService.addShopAuthMap(shopAuthMap);
                if (se.getState() == ShopAuthMapStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", se.getStateInfo());
                }
            } catch (RuntimeException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }

        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入授权信息");
        }
        return modelMap;
    }*/

    @RequestMapping(value = "/modifyshopauthmap", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> modifyShopAuthMap(String shopAuthMapStr, HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        //是授权编辑时候调用还是删除或者回复授权操作的时候调用
        //若为前者 就进行验证码判断 否则则跳过验证码判断
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "输入了错误的验证码");
            return modelMap;
        }
        ObjectMapper mapper = new ObjectMapper();
        ShopAuthMap shopAuthMap = null;
        try {
            //将前台传入的字符串json 转换为shopAuthmap
            shopAuthMap = mapper.readValue(shopAuthMapStr, ShopAuthMap.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }
        if (shopAuthMap != null && shopAuthMap.getShopAuthId() != null) {
            try {
                //看看被操作的对象是否为店家本身 店家本身不支持修改
                if (!checkPermission(shopAuthMap.getShopAuthId())) {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", "无法对店家本身权限做操作(已经是店铺的最高权限)");
                    return modelMap;
                }
                Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
                PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
                shopAuthMap.setShopId(currentShop.getShopId());
                shopAuthMap.setEmployeeId(user.getUserId());
                ShopAuthMapExecution se = shopAuthMapService.modifyShopAuthMap(shopAuthMap);
                if (se.getState() == ShopAuthMapStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", se.getStateInfo());
                }
            } catch (RuntimeException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }

        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入要修改的授权信息");
        }
        return modelMap;
    }

    @RequestMapping(value = "/removeshopauthmap", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> removeShopAuthMap(Long shopAuthId) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if (shopAuthId != null && shopAuthId > 0) {
            try {
                ShopAuthMapExecution se = shopAuthMapService.removeShopAuthMap(shopAuthId);
                if (se.getState() == ShopAuthMapStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", se.getStateInfo());
                }
            } catch (RuntimeException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }

        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请至少选择一个授权进行删除");
        }
        return modelMap;
    }

    /**
     * 检查被操作的对象是否可以被修改
     *
     * @param shopAuthId
     * @return
     */
    private boolean checkPermission(long shopAuthId) {

        ShopAuthMap grantedPerson = shopAuthMapService.getShopAuthMapById(shopAuthId);
        if (grantedPerson.getTitleFlag() == 0) {
            //若是店家本身 不能操作
            return false;
        } else {
            return true;
        }
    }

    /**
     * 根据微信回传回来的参数添加店铺授权信息
     * * @param request
     *
     * @param response
     * @return
     */
    @RequestMapping(value = "/addshopauthmap", method = RequestMethod.GET)
    private String addshopauthmap(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        //从request里面获取微信用户信息
        WechatAuth auth = getEmployeeInfo(request);
        if (auth != null) {
            //根据userid获取用户信息
            PersonInfo user = personInfoService.getPersonInfoById(auth.getPersonInfo().getUserId());
            //将用户信息添加进去user里面
            request.getSession().setAttribute("user", user);
            //解析微信回传过来的自定义的参数的state 由于之前进行编码 现在需要解码
            String qrCodeinfo = new String(URLDecoder.decode(HttpServletRequestUtil.getString(request, "state"), "UTF-8"));
            ObjectMapper mapper = new ObjectMapper();
            WechatInfo wechatInfo = null;
            //将解码后的内容用aaa去替换掉之前生成二维码的时候添加aaa前缀 转化成wechatInfo实体类
            try {
                //wechatInfo = mapper.readValue(qrCodeinfo.replace("aaa", "\""), wechatInfo.getClass());
                wechatInfo = mapper.readValue(qrCodeinfo, wechatInfo.getClass());
            } catch (IOException e) {
                return "shop/operationfail";
            }
            //检查二维码是否已经过期
            if (!checkQRCodeInfo(wechatInfo)) {
                return "shop/operationfail";
            }
            //去重校检
            //获取该店铺下所有的授权信息
            ShopAuthMapExecution allAuthList = shopAuthMapService.listShopAuthMapByShopId(wechatInfo.getCustomerId(), 1, 999);
            List<ShopAuthMap> shopAuthMapList = allAuthList.getShopAuthMapList();
            for (ShopAuthMap sm : shopAuthMapList) {
                if (sm.getEmployee().getUserId().equals(user.getUserId())) {
                    return "shop/operationfail";
                }
            }

            try {
                //根据获取到的内容 添加微信授权对象
                ShopAuthMap shopAuthMap = new ShopAuthMap();
                Shop shop = new Shop();
                shop.setShopId(wechatInfo.getCustomerId());
                PersonInfo employee = new PersonInfo();
                shopAuthMap.setEmployee(employee);
                shopAuthMap.setTitle("员工");
                shopAuthMap.setTitleFlag(1);
                ShopAuthMapExecution se = shopAuthMapService.addShopAuthMap(shopAuthMap);
                if (se.getState() == ShopAuthMapStateEnum.SUCCESS.getState()) {
                    return "shop/operationSuccess";

                } else {
                    return "shop/operationfail";

                }
            } catch (RuntimeException e) {
                return "shop/operationfail";
            }
        }
        return "shop/operationfail";
    }

    /**
     * 根据二维码携带的createTime 判断其是否超过10分钟 超过10分钟认为过期
     *
     * @param wechatInfo
     * @return
     */

    private boolean checkQRCodeInfo(WechatInfo wechatInfo) {

        if (wechatInfo != null && wechatInfo.getCreateTime() != null) {
            long nowTime = System.currentTimeMillis();
            if (nowTime - wechatInfo.getCreateTime() <= 600000) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 根据微信回传的code获取用户信息
     *
     * @param request
     * @return
     */
    private WechatAuth getEmployeeInfo(HttpServletRequest request) {

        String code = request.getParameter("code");
        WechatAuth auth = null;
        if (null != code) {
            UserAccessToken token;
            try {
                token = WeiXinUserUtil.getUserAccessToken(code);
                String openId = token.getOpenId();
                request.getSession().setAttribute("openId", openId);
                auth = wechatAuthService.getWechatAuthByOpenId(openId);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return auth;
    }

    /**
     * 根据code获取UserAccessToken 进而通过token里面openid获取微信用户信息
     *
     * @return
     */
    private WechatAuth getOperatorInfo(HttpServletRequest request) {
        String code = request.getParameter("code");
        WechatAuth auth = null;
        if (null != code) {
            UserAccessToken token;
            try {
                token = WeiXinUserUtil.getUserAccessToken(code);
                String openId = token.getOpenId();
                request.getSession().setAttribute("openId", openId);
                auth = wechatAuthService.getWechatAuthByOpenId(openId);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return auth;
    }


}
