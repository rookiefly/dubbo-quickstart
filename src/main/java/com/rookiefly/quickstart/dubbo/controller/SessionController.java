package com.rookiefly.quickstart.dubbo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/session")
public class SessionController {

    @ResponseBody
    @RequestMapping(value = "/store")
    public Map<String, Object> storeSession(HttpServletRequest request) {
        request.getSession().setAttribute("userName", "rookiefly");
        Map<String, Object> map = new HashMap<>();
        map.put("sessionId", request.getSession().getId());
        return map;
    }

    @ResponseBody
    @RequestMapping(value = "/query")
    public String querySession(HttpServletRequest request) {
        return (String) request.getSession().getAttribute("userName");
    }
}