package org.jeecg.modules.tiktok.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.tiktok.config.TikTokConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/tiktok")
@Slf4j
public class TikTokAuthController {

    @Autowired
    private TikTokConfig tikTokConfig;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * TikTok OAuth 回调
     * 访问地址：https://prompt-pumped-kangaroo.ngrok-free.app/jeecg-boot/api/tiktok/callback
     */
    @GetMapping("/callback")
    public Result<String> handleCallback(@RequestParam("code") String code,
                                         @RequestParam(value = "state", required = false) String state) {
        try {
            log.info("接收到 TikTok 授权回调，code: {}", code);

            // 拼接获取 access_token 请求
            String url = "https://auth.tiktok-shops.com/api/token/create";

            JSONObject body = new JSONObject();
            body.put("app_key", tikTokConfig.getAppKey());
            body.put("app_secret", tikTokConfig.getAppSecret());
            body.put("auth_code", code);
            body.put("grant_type", "authorized_code");

            HttpResponse response = HttpRequest.post(url)
                    .header("Content-Type", "application/json")
                    .body(body.toJSONString())
                    .execute();

            if (!response.isOk()) {
                log.error("请求 access_token 失败: {}", response.body());
                return Result.error("获取 access_token 失败");
            }

            JSONObject resJson = JSONObject.parseObject(response.body());
            if (resJson.getInteger("code") != 0) {
                log.error("TikTok 授权失败: {}", resJson);
                return Result.error("TikTok 授权失败: " + resJson.getString("message"));
            }

            JSONObject data = resJson.getJSONObject("data");
            String accessToken = data.getString("access_token");
            int expireIn = data.getInteger("expires_in"); // 秒
            String refreshToken = data.getString("refresh_token");

            // 存入 Redis（示例使用固定key，实际可以带上shopId）
            redisTemplate.opsForValue().set("tiktok:access_token", accessToken, expireIn - 60, TimeUnit.SECONDS);
            redisTemplate.opsForValue().set("tiktok:refresh_token", refreshToken, 30, TimeUnit.DAYS);

            log.info("TikTok 授权成功，access_token 保存成功");
            return Result.OK("授权成功！可以开始同步订单了。");

        } catch (Exception e) {
            log.error("处理 TikTok 授权回调异常", e);
            return Result.error("服务器内部错误");
        }
    }
}
