package com.boyu.wang_pan;

import com.boyu.wang_pan.service.impl.OssDemoService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class WangPanApplicationTests {

    @Resource
    OssDemoService ossDemoService;

    @Test
    void contextLoads() {
    }

    @Test
    void testOSSDemo(){
        ossDemoService.uploadWithClient();
    }

}
