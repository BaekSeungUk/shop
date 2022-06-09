package com.bitc.shop.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
//        현재 로그인한 사용자의 정보를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String userId = "";

        if (authentication != null) {
            userId = authentication.getName();
        }
        return Optional.of(userId);
    }
}
