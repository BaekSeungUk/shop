package com.bitc.shop.entity;

import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

@EntityListeners(value = {AuditingEntityListener.class})
@MappedSuperclass
@Getter
public abstract class BaseEntity extends BaseTimeEntity{

//    @CreatedBy : 해당 컬럼을 등록자로 사용
    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

//    @LastModifiedBy : 해당 컬럼을 수정자로 사용
    @LastModifiedBy
    private String modifiedBy;
}
