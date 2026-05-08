package com.sky.utils;

import com.alibaba.fastjson.JSONObject;
import com.sky.properties.BaiduMapProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class BaiduMapUtil {
    private final BaiduMapProperties baiduMapProperties;

    /**
     * 计算店铺到用户地址的驾车距离（米）
     */
    public int post(String userAddress) throws Exception {
        String ak = baiduMapProperties.getAk();
        // 获取商家经纬度
        Map<String, String> shoppingParam = new LinkedHashMap<>();
        shoppingParam.put("address", baiduMapProperties.getAddress());
        shoppingParam.put("output", "json");
        shoppingParam.put("ak", ak);
        String origin = getLocation(shoppingParam);
        // 获取用户经纬度
        Map<String, String> userParam = new LinkedHashMap<>();
        userParam.put("address", userAddress);
        userParam.put("output", "json");
        userParam.put("ak", ak);
        String destination = getLocation(userParam);
        // 调用驾车路线API计算距离
        Map<String, String> params = new LinkedHashMap<>();
        params.put("origin", origin);
        params.put("destination", destination);
        params.put("ak", ak);
        params.put("tactics", "2");
        String result = HttpClientUtil.doGet(baiduMapProperties.getDirectionUrl(), params);
        JSONObject json = JSONObject.parseObject(result);
        if (json.getInteger("status") != 0) {
            throw new RuntimeException("查询距离失败：" + json.getString("message"));
        }
        return json.getJSONObject("result")
                .getJSONArray("routes")
                .getJSONObject(0)
                .getInteger("distance");
    }

    /**
     * 地址 → "lat,lng" 字符串
     */
    public String getLocation(Map<String, String> param) {
        String result = HttpClientUtil.doGet(baiduMapProperties.getGeocodingUrl(), param);
        JSONObject json = JSONObject.parseObject(result);
        if (json.getInteger("status") != 0) {
            throw new RuntimeException("地理编码失败：" + json.getString("message"));
        }
        JSONObject location = json.getJSONObject("result").getJSONObject("location");
        float lng = location.getFloat("lng");
        float lat = location.getFloat("lat");
        return lat + "," + lng;
    }
}