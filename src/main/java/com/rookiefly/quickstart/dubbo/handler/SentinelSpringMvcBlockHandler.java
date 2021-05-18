package com.rookiefly.quickstart.dubbo.handler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.rookiefly.quickstart.dubbo.vo.CommonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Spring configuration for global exception handler.
 * This will be activated when the {@code BlockExceptionHandler}
 * throws {@link BlockException directly}.
 */
@ControllerAdvice
@Order(0)
public class SentinelSpringMvcBlockHandler {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(BlockException.class)
    @ResponseBody
    public CommonResponse sentinelBlockHandler(BlockException e) {
        logger.warn("Blocked by Sentinel: {}", e.getRule());
        // Return the customized result.
        return CommonResponse.blocked();
    }
}