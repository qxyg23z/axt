package com.shao.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shao.HtmlParseUtil;
import com.shao.Utils;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Describe：发送消息的具体逻辑
 *
 * @author Administrator
 * @createTime 2022/08/22 21:31
 */
@Component
public class SendMessage {
    
    @Value("${wx.appID}")
    private String appId;
    
    @Value("${wx.appsecret}")
    private String appsecret;
    @Value("${wx.touser}")
    private String touser;
    @Value("${wx.template_id}")
    private String template_id;
    private int sendTimeNum = 0;
    @Resource
    private SendMessage sendMessage;
    public static List<String> parm = new ArrayList<>();
    
    SendMessage() {
        
        System.out.println("===========加载配置文件============");
        try {
            FileInputStream inputStream = new FileInputStream(
                    "/root/like.txt");//留言文件的存放地址
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                parm.add(str);
            }
            
            inputStream.close();
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 功能描述: 获取token
     *
     * @param
     * @author Administrator 2022/8/22 21:39
     */
    private String getAccessToken() {
        
        try {
            String bodyStr = HtmlParseUtil.getUrlResult(
                    "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appId + "&secret="
                            + appsecret);
            JSONObject bodyJson = JSON.parseObject(bodyStr);
            return bodyJson.getString("access_token");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    
    public void sendMessageByTemplate() {
        
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            JSONObject requestData = new JSONObject();
            requestData.put("touser", touser);
            requestData.put("template_id", template_id);
            requestData.put("url", "");
            requestData.put("topcolor", "");
            JSONObject data = new JSONObject();//模板文件中的各个参数
            JSONObject date = new JSONObject();//日期
            date.put("value", Utils.getNowDate() + " " + Utils.getWeekOfDate(new Date()));
            date.put("color", "#005faf");
            JSONObject dateNumbers = new JSONObject();//天数
            String s = Utils.toNowDatNum();
            dateNumbers.put("value", s);
            dateNumbers.put("color", "#005faf");
            JSONObject text = new JSONObject();//文本
            text.put("value", "♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥\n" + parm.get(sendMessage.sendTimeNum));
            text.put("color", "#f73131");
            
            JSONObject weather = Utils.getWeather();
            String tianqiStr = "";
            if (weather != null) {
                String high = weather.getString("high");
                String low = weather.getString("low");
                String fx = weather.getString("fx");
                String fl = weather.getString("fl");
                String type = weather.getString("type");
                tianqiStr = type + ",最高温度：" + high + ",最低温度：" + low + "," + fx + fl;
            }
            
            JSONObject tianqi = new JSONObject();//天气
            tianqi.put("value", tianqiStr);
            tianqi.put("color", "#ff4d00");
            data.put("date", date);
            data.put("dateNumbers", dateNumbers);//天数
            data.put("text", text);
            data.put("tianqi", tianqi);
            requestData.put("data", data);
            RequestBody body = RequestBody.create(mediaType, requestData.toJSONString());
            Request request = new Request.Builder()
                    .url("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + sendMessage.getAccessToken())
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            sendMessage.sendTimeNum++;//每天都发的不一样的留言
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
