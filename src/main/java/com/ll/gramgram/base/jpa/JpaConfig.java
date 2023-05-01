package com.ll.gramgram.base.jpa;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//quaryDSL 사용을 위해 필요한 클래스

@Configuration
//스프링의 자바 기반 '설정' 파일
public class JpaConfig {

    @PersistenceContext
    //EntityManager를 빈으로 주입할 때 사용하는 어노테이션입니다.
    //@Autowired가 대신 @PersistenceContext라는 어노테이션으로 주입
    private EntityManager entityManager;
    //영속성 관리를 위한것

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        //데이터베이스에서 쿼리를 실행하기 위한 도구
        return new JPAQueryFactory(entityManager);
    }
}
