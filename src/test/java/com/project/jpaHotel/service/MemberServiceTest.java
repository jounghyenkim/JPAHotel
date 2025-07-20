package com.project.jpaHotel.service;

import com.project.jpaHotel.domain.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {
    @Autowired MemberService memberService;

    @Test
    @Rollback(value = false)
    public void 회원가입() throws Exception{
        // given
        Member member =new Member();
        member.setEmail("test1234");

        // when
        Long memberId = memberService.join(member);
        Member findMember= memberService.findOne(memberId);

        // then
        assertEquals(findMember,member);

    }

    @Test
    public void 중복_회원_예외() throws Exception{
        // given
        Member member1 =new Member();
        Member member2 =new Member();
        // when
        member1.setEmail("test");
        member2.setEmail("test");

        //then
        memberService.join(member1);
        memberService.join(member2);

    }
}