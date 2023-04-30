package com.ll.gramgram.base.event;

import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
//이벤트 객체모델.extends ApplicationEven로 사용가능
//이벤트 구독자들이 필요로 하는 정보를 담는 DTO 클래스
public class EventAfterFromInstaMemberChangeGender extends ApplicationEvent {
    private final InstaMember instaMember;
    private final String oldGender;

    public EventAfterFromInstaMemberChangeGender(Object source, InstaMember instaMember, String oldGender) {
        super(source);
        this.instaMember = instaMember;
        this.oldGender = oldGender;
    }
}
