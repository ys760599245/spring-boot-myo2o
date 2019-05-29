package com.chen.myo2o.util.weixin;

import com.chen.myo2o.entity.PersonInfo;
import com.chen.myo2o.util.DESUtils;
import com.chen.myo2o.util.weixin.message.pojo.UserAccessToken;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.ConnectException;
import java.net.URL;
import java.util.Properties;

public class WeiXinUserUtil {

    private static Logger log = LoggerFactory.getLogger(MenuManager.class);


    public static UserAccessToken getUserAccessToken(String code) throws IOException {
        Properties pro = new Properties();
        pro.load(WeiXinUserUtil.class.getClassLoader().getResourceAsStream("weixin.properties"));
        //测试号信息的APPID
        String appId = DESUtils.getDecryptString(pro.getProperty("weixinappid"));
        log.debug("appId:" + appId);
        //测试号信息的appsecret
        String appsecret = DESUtils.getDecryptString(pro.getProperty("weixinappsecret"));
        log.debug("secret:" + appsecret);
        //根据传入的Code  拼接出来 访问微信定义好的接口的URL
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appId + "&secret=" + appsecret
                + "&code=" + code + "&grant_type=authorization_code";
        //向相应的URL 发送请求 获取token json字符串
        JSONObject jsonObject = WeixinUtil.httpsRequest(url, "GET", null);
        log.debug("userAccessToken:" + jsonObject.toString());
        String accessToken = jsonObject.getString("access_token");
        if (null == accessToken) {
            log.debug("获取用户accessToken失败。");
            return null;
        }
        UserAccessToken token = new UserAccessToken();
        token.setAccessToken(accessToken);
        token.setExpiresIn(jsonObject.getString("expires_in"));
        token.setOpenId(jsonObject.getString("openid"));
        token.setRefreshToken(jsonObject.getString("refresh_token"));
        token.setScope(jsonObject.getString("scope"));
        return token;
    }

    public static WeiXinUser getUserInfo(String accessToken, String openId) {
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openId
                + "&lang=zh_CN";
        JSONObject jsonObject = WeixinUtil.httpsRequest(url, "GET", null);
        WeiXinUser user = new WeiXinUser();
        String openid = jsonObject.getString("openid");
        if (openid == null) {
            log.debug("获取用户信息失败。");
            return null;
        }
        user.setOpenId(openid);
        user.setNickName(jsonObject.getString("nickname"));
        user.setSex(jsonObject.getInt("sex"));
        user.setProvince(jsonObject.getString("province"));
        user.setCity(jsonObject.getString("city"));
        user.setCountry(jsonObject.getString("country"));
        user.setHeadimgurl(jsonObject.getString("headimgurl"));
        user.setPrivilege(null);
        // user.setUnionid(jsonObject.getString("unionid"));
        return user;
    }

    public static boolean validAccessToken(String accessToken, String openId) {
        String url = "https://api.weixin.qq.com/sns/auth?access_token=" + accessToken + "&openid=" + openId;
        JSONObject jsonObject = WeixinUtil.httpsRequest(url, "GET", null);
        int errcode = jsonObject.getInt("errcode");
        if (errcode == 0) {
            return true;
        } else {
            return false;
        }
    }

    //将WeiXinUser 里面的信息转换成PersonInfo的信息并且返回PersonInfo实体类
    public static PersonInfo getPersonInfoFromRequest(WeiXinUser user) {
        PersonInfo personInfo = new PersonInfo();
        personInfo.setName(user.getNickName());
        personInfo.setGender(user.getSex() + "");
        personInfo.setProfileImg(user.getHeadimgurl());
        personInfo.setEnableStatus(1);
        return personInfo;
    }

    /**
     * 发起https请求并获取结果
     *
     * @param requestUrl    请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr     提交的数据
     * @return json字符串
     */
    public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
        StringBuffer buffer = new StringBuffer();
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = {new MyX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);

            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod(requestMethod);

            if ("GET".equalsIgnoreCase(requestMethod)) {
                httpUrlConn.connect();
            }

            // 当有数据需要提交时
            if (null != outputStr) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();
            log.debug("https buffer:" + buffer.toString());
        } catch (ConnectException ce) {
            log.error("Weixin server connection timed out.");
        } catch (Exception e) {
            log.error("https request error:{}", e);
        }
        return buffer.toString();
    }


}
