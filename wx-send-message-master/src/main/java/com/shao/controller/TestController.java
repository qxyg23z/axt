package com.shao.controller;

import com.shao.service.SendMessage;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Describe：
 *
 * @author Administrator
 * @createTime 2022/08/23 16:30
 */
@RestController
@RequestMapping("test")
public class TestController {
    
    @Resource
    private SendMessage sendMessage;
    @RequestMapping("test")
    public void test(){
        sendMessage.sendMessageByTemplate();
    }
}
