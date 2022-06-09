package com.bitc.shop.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;
// @EntityListeners(value = {AuditingEntityListener.class}) : JPA Auditing 기능 사용
// @MappedSuperclass : 해당 클래스를 부모 클래스로써 자식 클래스에 매핑 정보만 제공
@EntityListeners(value = {AuditingEntityListener.class})
@MappedSuperclass
@Data
public abstract class BaseTimeEntity {

//    @CreatedDate : 해당 컬럼을 등록 시간으로 사용함
//    @Column(updatable = false) : 해당 컬럼의 수정을 하지 않음
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime regTime;

//    @LastModifiedDate : 해당 컬럼을 수정 시간을 사용함
    @LastModifiedDate
    private LocalDateTime updateTime;
}
