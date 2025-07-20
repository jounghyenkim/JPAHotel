package com.project.jpaHotel.domain;


import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(value = {AuditingEntityListener.class})
@MappedSuperclass
@Getter
@Setter
public class BaseTimeEntity {

    @CreatedDate // 생성시 자동 저장
    @Column(updatable = false)
    private LocalDateTime regTime;

    @LastModifiedDate // 변경시 자동 저장
    private LocalDateTime updateTime;
}
