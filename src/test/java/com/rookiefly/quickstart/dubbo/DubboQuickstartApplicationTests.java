package com.rookiefly.quickstart.dubbo;

import com.rookiefly.quickstart.dubbo.service.OrderIdGeneratorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class DubboQuickstartApplicationTests {

    @Resource
    private OrderIdGeneratorService orderIdGeneratorService;

    @Test
    void testOrderIdGenerator() {
        String orderId = orderIdGeneratorService.generateOrderId();
        System.out.println(orderId);
        Assertions.assertNotNull(orderId);
    }

}
