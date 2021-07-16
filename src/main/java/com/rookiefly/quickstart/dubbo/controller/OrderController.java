package com.rookiefly.quickstart.dubbo.controller;

import com.rookiefly.quickstart.dubbo.monitor.PrometheusCustomMonitor;
import com.rookiefly.quickstart.dubbo.service.OrderIdGeneratorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.security.SecureRandom;
import java.util.Random;

@RestController
public class OrderController {

    @Resource
    private PrometheusCustomMonitor monitor;

    @Resource
    private OrderIdGeneratorService orderIdGeneratorService;

    @GetMapping("/test/order")
    public String order(@RequestParam(defaultValue = "0") String flag) throws Exception {
        // 统计下单次数
        monitor.getOrderCount().increment();
        if ("1".equals(flag)) {
            throw new Exception("出错啦");
        }
        String orderId = orderIdGeneratorService.generateOrderId();
        Random rand = SecureRandom.getInstanceStrong();
        int amount = rand.nextInt(100);
        // 统计金额
        monitor.getAmountSum().record(amount);
        return String.format("下单成功，订单ID：%s； 金额: %s元", orderId, amount);
    }
}