package com.bitc.shop.controller;

import com.bitc.shop.dto.CartDetailDto;
import com.bitc.shop.dto.CartItemDto;
import com.bitc.shop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @ResponseBody
    @RequestMapping(value = "/cart", method = RequestMethod.POST)
    public ResponseEntity order(@RequestBody @Valid CartItemDto cartItemDto, BindingResult bindingResult, Principal principal) {

        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }
//        현재 로그인된 사용자 정보 가져오기
        String email = principal.getName();
        Long cartItemId;

        try {
            cartItemId = cartService.addCart(cartItemDto, email);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Long>(cartItemId,HttpStatus.OK);
    }

    @RequestMapping(value = "/cart", method = RequestMethod.GET)
    public String orderHist(Principal principal, Model model) {
        List<CartDetailDto> cartDetailList = cartService.getCartList(principal.getName());
        model.addAttribute("cartItems", cartDetailList);

        return "cart/cartList";
    }

    @ResponseBody
//    현재 화면의 일부분만 수정을 진행하므로 PATCH 타입으로 데이터를 전송
    @RequestMapping(value = "/cartItem/{cartItemId}", method = RequestMethod.PATCH)
    public ResponseEntity updateCartItem(@PathVariable("cartItemId") Long cartItemId, int count, Principal principal) {

//        변경할 수량을 확인
        if (count <= 0) {
            return new ResponseEntity<String>("최소 1개 이상 담아주세요", HttpStatus.BAD_REQUEST);
        }
//        현재 사용자의 수정 권한이 있는지 확인
        else if (!cartService.validateCartItem(cartItemId, principal.getName())) {
            return new ResponseEntity("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

//        선택한 상품의 수량 변경
        cartService.updateCartItemCount(cartItemId, count);

        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }
}
