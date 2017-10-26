package com.bbcow.api.controller;

import com.bbcow.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by adan on 2017/10/17.
 */
@RestController
public class IndexController {

    @Autowired
    HelloService helloService;

    @RequestMapping("/v1")
    String home() {
        helloService.say();
        return "Hello World!";
    }
}
