package com.bbcow.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by adan on 2017/10/17.
 */
@RestController
public class IndexController {

    @RequestMapping("/v1")
    String home() {

        return "Hello World!";
    }
}
