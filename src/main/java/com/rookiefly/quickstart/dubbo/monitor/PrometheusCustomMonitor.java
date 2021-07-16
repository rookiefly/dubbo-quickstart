package com.rookiefly.quickstart.dubbo.monitor;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class PrometheusCustomMonitor {

    /**
     * 记录请求出错次数
     */
    private Counter requestErrorCount;

    /**
     * 字段查询次数
     */
    private Counter dictQueryCount;

    private final MeterRegistry registry;

    /**
     * 金额统计
     */
    private DistributionSummary amountSum;

    /**
     * 订单发起次数
     */
    private Counter orderCount;

    @Autowired
    public PrometheusCustomMonitor(MeterRegistry registry) {
        this.registry = registry;
    }

    @PostConstruct
    private void init() {
        requestErrorCount = registry.counter("requests_error_total", "status", "error");
        orderCount = registry.counter("order_request_count", "order", "order-test");
        amountSum = registry.summary("order_amount_sum", "orderAmount", "order-test");
        dictQueryCount = registry.counter("dictQuery_request_count", "dictQuery", "dictQuery_request");
    }

    public Counter getRequestErrorCount() {
        return requestErrorCount;
    }

    public Counter getDictQueryCount() {
        return dictQueryCount;
    }

    public Counter getOrderCount() {
        return orderCount;
    }

    public DistributionSummary getAmountSum() {
        return amountSum;
    }
}