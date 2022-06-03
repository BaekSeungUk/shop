package com.bitc.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ItemController {

    @RequestMapping(value = "/admin/item/new", method = RequestMethod.GET)
    public String itemForm() {
        return "/item/itemForm";
    }
}
