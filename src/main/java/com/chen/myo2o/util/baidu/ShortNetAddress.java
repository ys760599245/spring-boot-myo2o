package com.chen.myo2o.util.baidu;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ShortNetAddress {
    public static int TIMEOUT = 30 * 1000;
    public static String ENCODING = "UTF-8";
    private static Logger log = LoggerFactory.getLogger(ShortNetAddress.class);

    /**
     * JSON get value by key
     * json 依据传入的key获取value
     *
     * @param replyText
     * @param key
     * @return
     */
    private static String getValueByKey_JSON(String replyText, String key) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node;
        String tinyUrl = null;
        try {
            node = mapper.readTree(replyText);
            tinyUrl = node.get(key).asText();
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            log.error("getValueByKey_JSON error:" + e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            log.error("getValueByKey_JSON error:" + e.toString());
        }

        return tinyUrl;
    }

    /**
     * 通过HttpConnection 获取返回的字符串
     *
     * @param connection
     * @return
     * @throws IOException
     */
    private static String getResponseStr(HttpURLConnection connection) throws IOException {
        StringBuffer result = new StringBuffer();
        //从连接中获取http状态码
        int responseCode = connection.getResponseCode();
//如果返回的状态码是OK的，那么取出连接的输出流
        if (responseCode == HttpURLConnection.HTTP_OK) {
            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, ENCODING));
            String inputLine = "";
            while ((inputLine = reader.readLine()) != null) {
                //将结果转换成String并且返回
                result.append(inputLine);
            }
        }
        return String.valueOf(result);
    }

    /**
     * 根据传入的url 通过访问百度短视频的接口 将其转换为短的url
     *
     * @param originURL
     * @return
     */
    public static String getShortURL(String originURL) {
        String tinyUrl = null;
        try {
            //指定百度短视频的接口
            URL url = new URL("http://dwz.cn/create.php");
            //建立连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // POST Request Define:
            //设置连接的参数
            connection.setDoOutput(true);
            //设置连接进行输出
            connection.setDoInput(true);
            //不用缓存
            connection.setUseCaches(false);
            //设置连接超时时间为30秒
            connection.setConnectTimeout(TIMEOUT);
            //设置请求模式为POST
            connection.setRequestMethod("POST");
            //设置POST信息 这里为传入的原始的URL
            String postData = URLEncoder.encode(originURL.toString(), "utf-8");
            //输出原始的url
            connection.getOutputStream().write(("url=" + postData).getBytes());
            //连接百度短视频接口
            connection.connect();
            //获取返回的字符串
            String responseStr = getResponseStr(connection);
            log.info("response string: " + responseStr);
            //在字符串里面获取tinyUrl 即短连接
            tinyUrl = getValueByKey_JSON(responseStr, "tinyurl");
            log.info("tinyurl: " + tinyUrl);
            //关闭连接
            connection.disconnect();
        } catch (IOException e) {
            log.error("getshortURL error:" + e.toString());
        }
        return tinyUrl;

    }

    /**
     * ‘ 百度短链接接口 无法处理不知名网站，会安全识别报错
     *
     * @param args
     */
    public static void main(String[] args) {
        String shortURL = getShortURL(
                "https://mp.weixin.qq.com/debug/cgi-bin/sandbox?t=sandbox/login");
        System.out.println(shortURL);
    }
}
