package com.boyu.wang_pan.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController // 返回Json
public class TestController {

    @PostMapping("/test")
    public String testString(@RequestBody String url2) {
        return url2;
    }


}
