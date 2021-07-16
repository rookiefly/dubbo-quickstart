package com.rookiefly.quickstart.dubbo.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/")
public class UserController {

    /**
     * 测试登录，浏览器访问： http://localhost:8787/user/doLogin?username=rookiefly&password=123456
     */
    @RequestMapping("doLogin")
    public String doLogin(String username, String password) {
        // 此处仅作模拟示例，真实项目需要从数据库中查询数据进行比对 
        if ("rookiefly".equals(username) && "123456".equals(password)) {
            StpUtil.login(10001);
            SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
            return "登录成功" + ", token信息：" + tokenInfo.toString();
        }
        return "登录失败";
    }

    /**
     * 查询登录状态，浏览器访问： http://localhost:8787/user/isLogin
     */
    @RequestMapping("isLogin")
    public String isLogin(String username, String password) {
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        return "当前会话是否登录：" + StpUtil.isLogin() + ", token信息：" + tokenInfo.toString();
    }

}