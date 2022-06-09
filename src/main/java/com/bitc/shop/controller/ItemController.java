package com.bitc.shop.controller;

import com.bitc.shop.dto.ItemFormDto;
import com.bitc.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @RequestMapping(value = "/admin/item/new", method = RequestMethod.GET)
    public String itemForm(Model model) {
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "item/itemForm";
    }
    /*public ModelAndView itemForm() {
        ModelAndView mv = new ModelAndView("/item/itemForm");
        mv.addObject("itemFormDto", new ItemFormDto());
        return mv;
    }*/

    @RequestMapping(value = "/admin/item/new", method = RequestMethod.POST)
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, Model model, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {

        if (bindingResult.hasErrors()) {
            return "item/itemForm";
        }

        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값입니다.");
        }

        try {
            itemService.saveItem(itemFormDto, itemImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage","상품 등록 중 에러가 발생했습니다.");
            return "item/itemForm";
        }
        return "item/itemForm";
    }
}
