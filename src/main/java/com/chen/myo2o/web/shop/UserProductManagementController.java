package com.chen.myo2o.web.shop;

import com.chen.myo2o.dto.ShopAuthMapExecution;
import com.chen.myo2o.dto.UserProductMapExecution;
import com.chen.myo2o.entity.*;
import com.chen.myo2o.enums.UserProductMapStateEnum;
import com.chen.myo2o.service.*;
import com.chen.myo2o.util.HttpServletRequestUtil;
import com.chen.myo2o.util.weixin.message.req.WechatInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/shop")
public class UserProductManagementController {
    @Autowired
    private UserProductMapService userProductMapService;
    @Autowired
    private PersonInfoService personInfoService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ShopAuthMapService shopAuthMapService;
    @Autowired
    private ProductSellDailyService productSellDailyService;

    @RequestMapping(value = "/listuserproductmapsbyshop", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listUserProductMapsByShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        //获取分页信息
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        //获取当前店铺信息
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        //控制判断主要确保shopid不为空
        if ((pageIndex > -1) && (pageSize > -1) && (currentShop != null) && (currentShop.getShopId() != null)) {
            //添加查询条件
            UserProductMap userProductMapCondition = new UserProductMap();
            userProductMapCondition.setShopId(currentShop.getShopId());
            String productName = HttpServletRequestUtil.getString(request, "productName");
            if (productName != null) {
                //按照商品名字模糊查询 传入productName
                userProductMapCondition.setProductName(productName);
            }
            //根据传入的查询条件获取该店铺的商品销售情况
            UserProductMapExecution ue = userProductMapService.listUserProductMap(userProductMapCondition, pageIndex,
                    pageSize);
            modelMap.put("userProductMapList", ue.getUserProductMapList());
            modelMap.put("count", ue.getCount());
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
        }
        return modelMap;
    }

    @RequestMapping(value = "/adduserproductmap", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> addUserProductMap(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        String qrCodeinfo = HttpServletRequestUtil.getString(request, "state");
        ObjectMapper mapper = new ObjectMapper();
        WechatInfo wechatInfo = null;
        try {
            wechatInfo = mapper.readValue(qrCodeinfo, WechatInfo.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }
        if (!checkQRCodeInfo(wechatInfo)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "二维码信息非法！");
            return modelMap;
        }
        //获取添加消费巨鹿所需要的参数并且组建成userproductmap实例
        Long productId = wechatInfo.getProductId();
        Long customerId = wechatInfo.getCustomerId();
        UserProductMap userProductMap = compactUserProductMap4Add(customerId, productId);
        if (userProductMap != null && customerId != -1) {
            try {
                if (!checkShopAuth(user.getUserId(), userProductMap)) {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", "无操作权限");
                    return modelMap;
                }
                UserProductMapExecution se = userProductMapService.addUserProductMap(userProductMap);
                //添加消费记录
                if (se.getState() == UserProductMapStateEnum.SUCCESS.getState()) {
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
    }

    private boolean checkQRCodeInfo(WechatInfo wechatInfo) {
        if (wechatInfo != null && wechatInfo.getProductId() != null && wechatInfo.getCustomerId() != null
                && wechatInfo.getCreateTime() != null) {
            long nowTime = System.currentTimeMillis();
            if ((nowTime - wechatInfo.getCreateTime()) <= 5000) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private UserProductMap compactUserProductMap4Add(Long customerId, Long productId) {
        UserProductMap userProductMap = null;
        if (customerId != null && productId != null) {
            userProductMap = new UserProductMap();
            PersonInfo personInfo = personInfoService.getPersonInfoById(customerId);
            //获取商品信息
            Product product = productService.getProductById(productId);
            userProductMap.setProductId(productId);
            userProductMap.setShopId(product.getShop().getShopId());
            userProductMap.setProductName(product.getProductName());
            userProductMap.setUserName(personInfo.getName());
            userProductMap.setPoint(product.getPoint());
            userProductMap.setCreateTime(new Date());
        }
        return userProductMap;
    }

    /**
     * * @param userId
     * 检查扫码的人员是否有操作的权限
     *
     * @param userProductMap
     * @return
     */
    private boolean checkShopAuth(long userId, UserProductMap userProductMap) {
        //获取该店铺所有的授权信息
        ShopAuthMapExecution shopAuthMapExecution = shopAuthMapService
                .listShopAuthMapByShopId(userProductMap.getShopId(), 1, 1000);
        for (ShopAuthMap shopAuthMap : shopAuthMapExecution.getShopAuthMapList()) {
            //看看是否给该人员进行授权
            if (shopAuthMap.getEmployeeId() == userId) {
                return true;
            }
        }
        return false;
    }

    @RequestMapping(value = "/listproductselldailyinfobyshop", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listProductSellDailyinfobyshop(HttpServletRequest request, ModelMap modelMap) {
        //获取当前的店铺信息
        Shop currntShop = (Shop) request.getSession().getAttribute("currntShop");
        //空值判断 主要确保shopid不为空
        if ((currntShop != null) && (currntShop.getShopId() != null)) {
            //添加查询条件
            ProductSellDaily productSellDailyCondition = new ProductSellDaily();
            productSellDailyCondition.setShop(currntShop);
            Calendar calendar = Calendar.getInstance();
            //获取昨天的日期
            calendar.add(Calendar.DATE, -1);
            Date endTime = calendar.getTime();
            //获取7天前的日期
            calendar.add(Calendar.DATE, -6);
            Date beginTime = calendar.getTime();
            //根据传入的查询条件获取该店铺的商品销售情况
            List<ProductSellDaily> ProductSellDailyList = productSellDailyService.queryProductSellDailyList(productSellDailyCondition, beginTime, endTime);
            //指定日期格式
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            //商品名列表 保证唯一性
            HashSet<String> legendData = new HashSet<>();
            //X数据
            HashSet<String> xData = new HashSet<>();
            //定义Series
            List<EchartSeries> series = new ArrayList<>();
            //日销售量列表
            List<Integer> totalList = new ArrayList<>();
            //当前商品名 默认为空
            String currentProductName = "";
            for (int i = 0; i < ProductSellDailyList.size(); i++) {
                ProductSellDaily productSellDaily = ProductSellDailyList.get(i);
                //自动去重
                legendData.add(productSellDaily.getProduct().getProductName());
                xData.add(sdf.format(productSellDaily.getCreateTime()));
                if (!currentProductName.equals(productSellDaily.getProduct().getProductName()) && !currentProductName.isEmpty()) {
                    //如果当前currentProductName 不等于获取的商品名 或者已经遍历到列表的尾部 而且 currentname
                    //则是遍历下一个商品的日销量信息 将前一轮遍历的信息存在在series中
                    //包含 商品名以及商品对应的统计日期以及日销量
                    EchartSeries es = new EchartSeries();
                    es.setName(currentProductName);
                    es.setData(totalList.subList(0, totalList.size()));
                    series.add(es);
                    //重置totallist
                    totalList = new ArrayList<Integer>();
                    //变换下currentproductid为当前的productid
                    currentProductName = productSellDaily.getProduct().getProductName();
                    //继续添加新的值
                    totalList.add(productSellDaily.getTotal());
                } else {
                    //如果还是当前的prodoctid则继续添加新值
                    totalList.add(productSellDaily.getTotal());
                    currentProductName = productSellDaily.getProduct().getProductName();
                }
                //队列之尾 需要将最后一个商品销售信息也添加上
                if (i == ProductSellDailyList.size() - 1) {
                    EchartSeries es = new EchartSeries();
                    es.setName(currentProductName);
                    es.setData(totalList.subList(0, totalList.size()));
                    series.add(es);
                }
            }
            modelMap.put("series", series);
            modelMap.put("legendData", legendData);
            //拼接处XAxis
            List<EchartXAxis> xAxis = new ArrayList<>();
            EchartXAxis exa = new EchartXAxis();
            exa.setGetData(xData);
            xAxis.add(exa);
            modelMap.put("xAxis", xAxis);
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty shopId");
        }
        return modelMap;
    }
}
