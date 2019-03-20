package com.exrates.inout.controller.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/")
    public String  test(){
        return "Success test. Microservice is alive";
    }


}
