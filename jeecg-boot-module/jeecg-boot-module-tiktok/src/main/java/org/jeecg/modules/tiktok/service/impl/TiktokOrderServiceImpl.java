package org.jeecg.modules.tiktok.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.tiktok.service.ITiktokOrderService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TiktokOrderServiceImpl implements ITiktokOrderService {

    @Override
    public JSONObject getOrders(String accessToken, String shopId, int page, int pageSize) {
        String url = "https://api.tiktok-shops.com/order/list";

        JSONObject body = new JSONObject();
        body.put("access_token", accessToken);
        body.put("shop_id", shopId);
        body.put("page", page);
        body.put("page_size", pageSize);

        HttpResponse response = HttpRequest.post(url)
                .header("Content-Type", "application/json")
                .body(body.toJSONString())
                .execute();

        if (!response.isOk()) {
            log.error("调用 TikTok 订单接口失败，响应状态码：{}，响应内容：{}", response.getStatus(), response.body());
            return null;
        }

        JSONObject resJson = JSONObject.parseObject(response.body());
        if (resJson.getInteger("code") != 0) {
            log.error("TikTok订单接口返回错误：{}", resJson);
            return null;
        }
        return resJson;
    }

    @Override
    public void syncOrders() {
        // TODO: 实现同步逻辑，比如调用 getOrders 拉取数据后入库
        log.info("同步订单功能待实现");
    }
}
