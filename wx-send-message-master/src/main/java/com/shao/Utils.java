package com.shao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Describe：
 *
 * @author Administrator
 * @createTime 2022/08/22 22:21
 */
public class Utils {
    
    public static String getNowDate() {
        
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        return simpleDateFormat.format(date);
    }
    
    public static String toNowDatNum() {
        
        long currentTime = System.currentTimeMillis();
        String targetTimeStr = "2022-03-03";//在一起的时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        try {
            Date targetDate = simpleDateFormat.parse(targetTimeStr);
            long targetTime = targetDate.getTime();
            long res = (currentTime - targetTime) / 1000 / 3600 / 24;
            return String.valueOf(res + 1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
    
    public static JSONObject getWeather() {
        
        try {
            String weather = "";
            URLConnection connectionData;
            BufferedReader br;// 读取data数据流
            StringBuilder sb = null;
            
            // 连接中央气象台的API
            URL url = new URL("http://t.weather.sojson.com/api/weather/city/101010300");
            connectionData = url.openConnection();
            connectionData.setConnectTimeout(1000);
            try {
                br = new BufferedReader(new InputStreamReader(connectionData.getInputStream(), "UTF-8"));
                sb = new StringBuilder();
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            assert sb != null;
            weather = sb.toString();
            JSONObject jsonData = JSONObject.parseObject(weather);
            JSONObject data = jsonData.getJSONObject("data");
            JSONObject forecast = (JSONObject) data.getJSONArray("forecast").get(0);
            return forecast;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getWeekOfDate(Date date) {
        String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }
}
