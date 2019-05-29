package com.chen.myo2o.web.frontend;

import com.chen.myo2o.entity.PersonInfo;
import com.chen.myo2o.entity.Product;
import com.chen.myo2o.service.ProductService;
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
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/frontend")
public class ProductDetailController {
    private static String URLPREFIX = "https://open.weixin.qq.com/connect/oauth2/authorize?"
            + "appid=wxd7f6c5b8899fba83&" + "redirect_uri=115.28.159.6/myo2o/shop/adduserproductmap&"
            + "response_type=code&scope=snsapi_userinfo&state=";
    private static String URLSUFFIX = "#wechat_redirect";
    //微信获取用户信息的API前缀
    private static String urlPrefix;
    //微信获取用户信息的API中间部分
    private static String urlMiddle;
    //微信获取用户信息的API后缀
    private static String urlSuffix;
    //微信回传给响应添加授权信息的url
    private static String authUrl;
    private static String ProductMapUrl;
    @Autowired
    private ProductService productService;

    @Value("wechat.productmap.url")
    public static void setProductMapUrl(String productMapUrl) {
        ProductDetailController.ProductMapUrl = productMapUrl;
    }

    @Value("wechat.prefix")
    public static void setUrlPrefix(String urlPrefix) {
        ProductDetailController.urlPrefix = urlPrefix;
    }

    @Value("wechat.middle")
    public static void setUrlMiddle(String urlMiddle) {
        ProductDetailController.urlMiddle = urlMiddle;
    }

    @Value("wechat.suffix")
    public static void setUrlSuffix(String urlSuffix) {
        ProductDetailController.urlSuffix = urlSuffix;
    }

    @Value("wechat.auth.url")
    public static void setAuthUrl(String authUrl) {
        ProductDetailController.authUrl = authUrl;
    }

    @RequestMapping(value = "/listproductdetailpageinfo", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listProductDetailPageInfo(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();

        long productId = HttpServletRequestUtil.getLong(request, "productId");
        Product product = null;
        if (productId != -1) {
            //根据productid火球商品信息 包含商品详情图列表
            product = productService.getProductById(productId);
            PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
            if (user == null) {
                modelMap.put("needQRCode", false);
            } else {
                modelMap.put("needQRCode", true);
            }
            modelMap.put("product", product);
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty productId");
        }
        return modelMap;
    }

    @RequestMapping(value = "/generateqrcode4product", method = RequestMethod.GET)
    @ResponseBody
    private void generateQRCode4Product(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        long productId = HttpServletRequestUtil.getLong(request, "productId");
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        if (productId != -1 && user != null && user.getUserId() != null) {
            long timpStamp = System.currentTimeMillis();
            //将hsangpinid 顾客id 和timestrap传入 content赋值到state中 这样微信
            //加上aa是为了 一会的在添加信息的方法里面替换这些信息使用
            String content = "{\"productId\":" + productId + ",\"customerId\":" + user.getUserId() + ",\"createTime\":"
                    + timpStamp + "}";
            // String content = "{aaaproductIdaaa:" + productId + "+ ,aaacustomerIdaaa:" + user.getUserId() + ",aaacreateTimeaaa:" + timpStamp + "} ";
            //String longUrl = URLPREFIX + content + URLSUFFIX;
            //将content的信息先进行 base64加密 用来避免特殊字符造成的干扰 之后拼接成目标额url
            String longUrl = URLPREFIX + ProductMapUrl + urlMiddle + URLEncoder.encode(content, "UTF-8") + URLSUFFIX;
            //将目标url成短的url
            String shortUrl = ShortNetAddress.getShortURL(longUrl);
            //调用二维码生成的工具类方法 生成短的url 生成二维码
            BitMatrix qRcodeImg = QRCodeUtil.generateQRCodeStream(shortUrl, response);
            try {
                //二维码以图片流的心事输出到前端
                MatrixToImageWriter.writeToStream(qRcodeImg, "png", response.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
