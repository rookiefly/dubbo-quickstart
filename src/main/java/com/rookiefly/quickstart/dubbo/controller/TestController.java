package com.rookiefly.quickstart.dubbo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("test")
public class TestController {

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();

    @GetMapping("call/{name}")
    public DeferredResult<String> testSync(@PathVariable String name) {
        final DeferredResult<String> deferredResult = new DeferredResult<>(5000L);
        EXECUTOR_SERVICE.execute(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            deferredResult.setResult(String.format("Hello %s", name));
        });
        // deferredResult.setResult(String.format("Hello %s", name));
        return deferredResult;
    }
}
