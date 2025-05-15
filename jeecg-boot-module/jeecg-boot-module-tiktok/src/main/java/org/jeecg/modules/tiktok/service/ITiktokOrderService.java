package org.jeecg.modules.tiktok.service;

import com.alibaba.fastjson.JSONObject;

public interface ITiktokOrderService {
    /**
     * 调用 TikTok 订单接口，获取订单列表
     *
     * @param accessToken 授权 token
     * @param shopId 店铺ID
     * @param page 页码
     * @param pageSize 每页数量
     * @return TikTok订单接口返回的JSON数据
     */
    JSONObject getOrders(String accessToken, String shopId, int page, int pageSize);

    /**
     * 同步订单（调用 getOrders，存库等）
     */
    void syncOrders();
}
