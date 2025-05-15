package org.jeecg.modules.tiktok.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.tiktok.service.ITiktokOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tiktok/order")
@Slf4j
public class TiktokOrderController {

    @Autowired
    private ITiktokOrderService tiktokOrderService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 获取订单列表接口
     */
    @GetMapping("/list")
    public Result<JSONObject> listOrders(@RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "10") int pageSize) {
    String accessToken = redisTemplate.opsForValue().get("tiktok:access_token");
    if (accessToken == null) {
        return Result.error("access_token 不存在，请先授权");
    }

    // 写死 shopId
    String shopId = "7496011340563450827";

    JSONObject ordersJson = tiktokOrderService.getOrders(accessToken, shopId, page, pageSize);
    if (ordersJson == null) {
        return Result.error("获取订单失败");
    }
    return Result.OK(ordersJson);
}
}
