package com.chen.myo2o.web.frontend;

import com.chen.myo2o.dto.UserAwardMapExecution;
import com.chen.myo2o.entity.Award;
import com.chen.myo2o.entity.PersonInfo;
import com.chen.myo2o.entity.UserAwardMap;
import com.chen.myo2o.enums.UserAwardMapStateEnum;
import com.chen.myo2o.service.AwardService;
import com.chen.myo2o.service.PersonInfoService;
import com.chen.myo2o.service.ShopAuthMapService;
import com.chen.myo2o.service.UserAwardMapService;
import com.chen.myo2o.util.HttpServletRequestUtil;
import com.chen.myo2o.util.QRCodeUtil;
import com.chen.myo2o.util.baidu.ShortNetAddress;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/frontend")
public class MyAwardController {
    /*private static String URLPREFIX = "https://open.weixin.qq.com/connect/oauth2/authorize?"
            + "appid=wxd7f6c5b8899fba83&" + "redirect_uri=115.28.159.6/myo2o/shop/exchangeaward&"
            + "response_type=code&scope=snsapi_userinfo&state=";*/
    //微信获取用户信息的API前缀
    private static String urlPrefix;
    //微信获取用户信息的API中间部分
    private static String urlMiddle;
    //微信获取用户信息的API后缀
    private static String urlSuffix;
    //微信回传给响应添加授权信息的url
    private static String authUrl;
    @Autowired
    private UserAwardMapService userAwardMapService;
    @Autowired
    private AwardService awardService;
    @Autowired
    private PersonInfoService personInfoService;
    @Autowired
    private ShopAuthMapService shopAuthMapService;

    @Value("wechat.prefix")
    public static void setUrlPrefix(String urlPrefix) {
        MyAwardController.urlPrefix = urlPrefix;
    }

    @Value("wechat.middle")
    public static void setUrlMiddle(String urlMiddle) {
        MyAwardController.urlMiddle = urlMiddle;
    }

    @Value("wechat.suffix")
    public static void setUrlSuffix(String urlSuffix) {
        MyAwardController.urlSuffix = urlSuffix;
    }

    @Value("wechat.auth.url")
    public static void setAuthUrl(String authUrl) {
        MyAwardController.authUrl = authUrl;
    }

    /**
     * 根据顾客奖品映射id获取单条顾客奖品的映射信息
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/listuserawardmapsbycustomer", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listUserAwardMapsByCustomer(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        Long userId = 1L;
        //空值判断确定用户id不为空
        if ((pageIndex > -1) && (pageSize > -1) && (userId != null)) {
            UserAwardMap userAwardMapCondition = new UserAwardMap();
            userAwardMapCondition.setUserId(userId);
            long shopId = HttpServletRequestUtil.getLong(request, "shopId");
            if (shopId > -1) {
                //若店铺id为非空 则将其添加进去 查询 条件 查询该用户在某个店铺的兑换信息
                userAwardMapCondition.setShopId(shopId);
            }
            //瑞奖品名字为非空 则将其添加查询条件里面进行模糊查询
            String awardName = HttpServletRequestUtil.getString(request, "awardName");
            if (awardName != null) {
                userAwardMapCondition.setAwardName(awardName);
            }
            //用户奖品映射
            UserAwardMapExecution ue = userAwardMapService.listUserAwardMap(userAwardMapCondition, pageIndex, pageSize);
            modelMap.put("userAwardMapList", ue.getUserAwardMapList());
            modelMap.put("count", ue.getCount());
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty pageSize or pageIndex or userId");
        }
        return modelMap;
    }

    /**
     * 在线兑换礼品
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/adduserawardmap", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> addUserAwardMap(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        //从Session中获取用户信息
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        //从前端请求中获取奖品id
        Long awardId = HttpServletRequestUtil.getLong(request, "awardId");
        //封装成用户奖品映射对象
        UserAwardMap userAwardMap = compactUserAwardMap4Add(user, awardId);
        if (userAwardMap != null) {
            try {
                //添加兑换信息
                UserAwardMapExecution se = userAwardMapService.addUserAwardMap(userAwardMap);
                if (se.getState() == UserAwardMapStateEnum.SUCCESS.getState()) {
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
            modelMap.put("errMsg", "请选择领取的奖品");
        }
        return modelMap;
    }

    /**
     * 生成带有URL的二维码  微信扫一扫就能链接到对应的url
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/generateqrcode4award", method = RequestMethod.GET)
    @ResponseBody
    private void generateQRCode4Product(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        //从Session里面获取当前的Shop的信息
        long userAwardId = HttpServletRequestUtil.getLong(request, "userAwardId");
        UserAwardMap userAwardMap = userAwardMapService.getUserAwardMapById(userAwardId);
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        if (userAwardMap != null && user != null && user.getUserId() != null
                && userAwardMap.getUserId().equals(user.getUserId()) && userAwardMap.getUsedStatus() == 0) {
            //获取当前的时间戳 用来保证二维码的时间的有效性 确认到毫秒
            long timpStamp = System.currentTimeMillis();
            //将店铺id和timpStamp 传入content 赋值到state 这样微信获取这些信息后回回传到授权欣欣的添加
            String content = "{\"userAwardId\":" + userAwardId + ",\"customerId\":" + user.getUserId()
                    + ",\"createTime\":" + timpStamp + "}";

            String longUrl = urlPrefix + content + urlSuffix;
            //将content的信息先进行base64编码用来避免特殊字符造成的告饶 之后拼接目标url
            //String longUrl = urlPrefix + authUrl + urlMiddle + URLEncoder.encode(content, "UTF-8") + urlSuffix;

            //将目标url转化成短的url
            String shortUrl = ShortNetAddress.getShortURL(longUrl);
            //调用二维码生成的工具类方法 传入短的url 生成二维码
            BitMatrix qRcodeImg = QRCodeUtil.generateQRCodeStream(shortUrl, response);
            try {
                //将二维码用图片流的形式输出到前端
                MatrixToImageWriter.writeToStream(qRcodeImg, "png", response.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private UserAwardMap compactUserAwardMap4Add(PersonInfo user, Long awardId) {
        UserAwardMap userAwardMap = null;
        if (user != null && user.getUserId() != null && awardId != -1) {
            userAwardMap = new UserAwardMap();
            PersonInfo personInfo = personInfoService.getPersonInfoById(user.getUserId());
            Award award = awardService.getAwardById(awardId);
            userAwardMap.setUserId(user.getUserId());
            userAwardMap.setAwardId(awardId);
            userAwardMap.setShopId(award.getShopId());
            userAwardMap.setUserName(personInfo.getName());
            userAwardMap.setAwardName(award.getAwardName());
            userAwardMap.setPoint(award.getPoint());
            userAwardMap.setCreateTime(new Date());
            userAwardMap.setUsedStatus(1);
        }
        return userAwardMap;
    }

    @RequestMapping(value = "getawardbuserawardyid", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getAwardbyId(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();

        long userAwardId = HttpServletRequestUtil.getLong(request, "userAwardId");
        if (userAwardId > -1) {
            //根据id获取顾客奖品的映射信息 进而获取奖品id
            UserAwardMap userAwardMap = userAwardMapService.getUserAwardMapById(userAwardId);
            //根据奖品id获取奖品信息
            Award award = awardService.getAwardById(userAwardMap.getAward().getAwardId());
            //将奖品信息和状态返回给前端
            modelMap.put("award", award);
            modelMap.put("usedStatus", userAwardMap.getUsedStatus());
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty awardId");
        }

        return modelMap;
    }
}
