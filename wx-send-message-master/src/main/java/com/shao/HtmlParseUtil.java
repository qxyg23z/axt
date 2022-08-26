package com.shao;

import com.alibaba.fastjson.JSONObject;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

/**
 * @Auther: shao
 * @Date: 2021/8/17 09:30
 * @Description:
 */
public class HtmlParseUtil {
    
    public static String getUrlResult(String url) throws Exception {
        
        return getUrlResult(url, null);
    }
  
    public static String getUrlResult(String url, Map<String, String> headMaps) throws Exception {
        
        return getUrlResult(url, headMaps, null, null, null);
    }
    
    /**
     * 功能描述: 获取url请求的结果内容
     *
     * @param url 地址
     * @param headMaps 请求头
     * @param hostIp 代理ip
     * @param hostType 代理的类型：http/https
     * @param hostPort 代理端口
     * @return java.lang.String
     * @author shao 2022/2/23 14:56
     */
    public static String getUrlResult(String url, Map<String, String> headMaps, String hostType, String hostIp, Integer hostPort)
            throws Exception {
        
        //1. 打开浏览器 创建httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //2. 输入网址
        HttpGet httpGet = new HttpGet(url);
        if (headMaps != null) {
            Set<String> keySet = headMaps.keySet();
            //设置请求头
            for (String s : keySet) {
                httpGet.setHeader(s, headMaps.get(s));
            }
        }
        if (hostIp != null && hostPort != null && hostType != null) {
            if ("https".equals(hostType)) {
                httpClient = (CloseableHttpClient) wrapClient();
            }
            HttpHost httpHost = new HttpHost(hostIp, hostPort);
            RequestConfig config = RequestConfig.custom().setProxy(httpHost).build();
            httpGet.setConfig(config);
        }
        
        //3. 发送请求
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
        //4. 响应结果
        HttpEntity httpEntity = httpResponse.getEntity();
        String s = EntityUtils.toString(httpEntity, "UTF-8");
        httpClient.close();
        //5. 解析结果
        return s;
    }
    
    
    //绕过证书
    private static HttpClient wrapClient() {
        
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    
                    return null;
                }
                
                @Override
                public void checkClientTrusted(X509Certificate[] arg0,
                        String arg1) throws CertificateException {
                    
                }
                
                @Override
                public void checkServerTrusted(X509Certificate[] arg0,
                        String arg1) throws CertificateException {
                    
                }
            };
            ctx.init(null, new TrustManager[]{tm}, null);
            SSLConnectionSocketFactory ssf = new SSLConnectionSocketFactory(
                    ctx, NoopHostnameVerifier.INSTANCE);
            CloseableHttpClient httpclient = HttpClients.custom()
                    .setSSLSocketFactory(ssf).build();
            return httpclient;
        } catch (Exception e) {
            return HttpClients.createDefault();
        }
    }
    
}

