package com.bitc.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/thymeleaf")
public class ThymeleafController {

    @RequestMapping("/ex07")
    public String thymeleafEx07() throws Exception {
        return "thEx/thEx07";
    }
}
