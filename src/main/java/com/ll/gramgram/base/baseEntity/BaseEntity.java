package com.ll.gramgram.base.baseEntity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;


@MappedSuperclass
//공통 매핑 정보가 필요할 때, 부모 클래스에 선언하고
//속성만 상속 받아서 사용하고 싶을 때 @MappedSuperclass를 사용
@Getter
@SuperBuilder
//빌더 패턴(Builder Pattern)으로 간편하게 구현
//@Builder 어노테이션으로는 상속받은 필드를 빌더에서 사용하지 못함
@NoArgsConstructor
//파라미터가 없는 기본 생성자를 생성
//@AllArgsConstructor = 생성자가 이미 정의 = 기본 생성자를 자동으로 생성 X.

//@Builder와 @NoArgsConstructor 같이 사용이유
//빌더 패턴을 사용하려면 객체를 생성하기 위한 파라미터가 없는 기본 생성자가 필요
@EntityListeners(AuditingEntityListener.class)
// JPA제공, JPA가 Entity에서 이벤트가 발생할 때마다 특정 로직을 실행
// @CreatedDate, @LastModifiedDate 작동하게 허용
@ToString
//클래스나 객체의 정보를 문자열 형태로 출력
//객체를 디버깅할 때 객체의 내부 상태를 확인
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    //기본 키(PRIMERY) 매핑
    private Long id;
    @CreatedDate //아래 칼럼에는 값이 자동으로 들어간다(insert 할때)
    private LocalDateTime createDate;
    @LastModifiedDate //아래 칼럼에는 값이 자동으로 들어간다(update 할때)
    private LocalDateTime modifyDate;
}
