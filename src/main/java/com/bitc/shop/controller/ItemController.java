package com.bitc.shop.controller;

import antlr.StringUtils;
import com.bitc.shop.dto.ItemFormDto;
import com.bitc.shop.dto.ItemSearchDto;
import com.bitc.shop.entity.Item;
import com.bitc.shop.entity.ItemImg;
import com.bitc.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

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

//    @Valid : 정상적인 데이터 들어왔는지 확인을 하는 어노테이션
    @RequestMapping(value = "/admin/item/new", method = RequestMethod.POST)
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, Model model, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {

//        필드에 비정상적인 데이터가 있을 경우 이전의 화면으로 되돌아감
        if (bindingResult.hasErrors()) {
            return "item/itemForm";
        }

//        첫번째 이미지 등록 필드에 데이터가 없을 경우 에러메세지를 출력하고 이전의 화면으로 되돌아감
        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값입니다.");
        }

//        서비스를 이용하여 데이터베이스에 저장
        try {
            itemService.saveItem(itemFormDto, itemImgFileList);
        } catch (Exception e) {
//            오류 발생 시 에러 메세지를 모델에 저장하고 이전 화면으로 되돌아감
            model.addAttribute("errorMessage","상품 등록 중 에러가 발생했습니다.");
            return "item/itemForm";
        }
        return "redirect:/";
    }

    @RequestMapping(value = "/admin/item/{itemId}", method = RequestMethod.GET)
    public String itemDtl(@PathVariable("itemId") Long itemId, Model model) {
        try {
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
            model.addAttribute("itemFormDto", itemFormDto);
        }
        catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "존재하지 않는 상품입니다.");
            model.addAttribute("itemFormDto", new ItemFormDto());
            return "item/itemForm";
        }
        return "item/itemForm";
    }

    @RequestMapping(value = "/admin/item/{itemId}", method = RequestMethod.POST)
    public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, Model model) {
        if (bindingResult.hasErrors()) {
            return "item/itemForm";
        }
        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage","첫번째 상품 이미지는 필수 입력값 입니다.");
            return "item/itemForm";
        }
        try {
            itemService.updateItem(itemFormDto, itemImgFileList);
        }
        catch (Exception e) {
            model.addAttribute("errorMessage","상품 수정 중 에러가 발생했습니다");
            return "item/itemForm";
        }
        return "redirect:/";
    }

    @RequestMapping(value = {"/admin/items", "/admin/items/{page}"}, method = RequestMethod.GET)
    public String itemManage(ItemSearchDto itemSearchDto, @PathVariable("page")Optional<Integer> page, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);
        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);
        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);

        return "item/itemMng";
    }

    @RequestMapping(value = "/item/{itemId}", method = RequestMethod.GET)
    public String itemDtl(Model model, @PathVariable("itemId") Long itemId) {
        ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
        model.addAttribute("item",itemFormDto);
        return "item/itemDtl";
    }
}
