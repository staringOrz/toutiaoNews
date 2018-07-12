package com.xijian.toutiao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexContrller {

    @RequestMapping({"profile"})
    @ResponseBody
    public String news(){
        return "index";
    }
}
