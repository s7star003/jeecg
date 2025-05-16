package org.jeecg.modules.tiktok.controller;

@RestController
public class helloController {
    @GetMapping("/hello")
    public String hello() {
        System.out.println("hello接口被调用了");
        return "hello";
    }
}
