package com.bitc.shop.service;

import com.bitc.shop.dto.CartDetailDto;
import com.bitc.shop.dto.CartItemDto;
import com.bitc.shop.entity.Cart;
import com.bitc.shop.entity.CartItem;
import com.bitc.shop.entity.Item;
import com.bitc.shop.entity.Member;
import com.bitc.shop.repository.CartItemRepository;
import com.bitc.shop.repository.CartRepository;
import com.bitc.shop.repository.ItemRepository;
import com.bitc.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    
//    장바구니에 상품 추가
    public Long addCart(CartItemDto cartItemDto, String email) {
//        상품 정보를 데이터베이스에서 조회
        Item item = itemRepository.findById(cartItemDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);
//        현재 로그인된 사용자 정보 조회
        Member member = memberRepository.findByEmail(email);

//        현재 사용자의 장바구니 정보를 데이터베이스에서 조회
        Cart cart = cartRepository.findByMemberId(member.getId());
        
//        현재 사용자의 장바구니가 존재하는지 확인
        if (cart == null) {
//            장바구니 생성
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }

//        장바구니에 저장된 상품이 내가 현재 저장하려는 상품인지 확인
        CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());

//        장바구니에 현재 상품이 이미 저장되어있는지 확인
        if (savedCartItem != null) {
//            장바구니에 현재 상품 개수 추가
            savedCartItem.addCount(cartItemDto.getCount());
            return savedCartItem.getId();
        } else {
//            장바구니에 현재 상품이 없을 경우 새로 생성
            CartItem cartItem = CartItem.createCartItem(cart, item, cartItemDto.getCount());
            cartItemRepository.save(cartItem);
            return cartItem.getId();
        }
    }

//    현재 장바구니 목록 가져오기
    @Transactional(readOnly = true)
    public List<CartDetailDto> getCartList(String email) {

        List<CartDetailDto> cartDetailDtoList = new ArrayList<>();

//        현재 로그인된 사용자 정보 가져오기
        Member member = memberRepository.findByEmail(email);
//        현재 로그인된 사용자의 장바구니 정보를 가져옴
        Cart cart = cartRepository.findByMemberId(member.getId());
//        사용자의 장바구니 정보가 없으면 빈 리스트 반환
        if (cart == null) {
            return cartDetailDtoList;
        }

//        장바구니 ID를 기준으로 장바구니에 들어있는 상품 정보를 모두 가져옴
        cartDetailDtoList = cartItemRepository.findCartDetailDtoList(cart.getId());

        return cartDetailDtoList;
    }

    @Transactional(readOnly = true)
    public boolean validateCartItem(Long cartItemId, String email) {
        Member curMember = memberRepository.findByEmail(email);
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);
        Member savedMember = cartItem.getCart().getMember();

        if (!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())) {
            return false;
        }

        return true;
    }

    public void updateCartItemCount(Long cartItemId, int count) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);

        cartItem.updateCount(count);
    }
}
