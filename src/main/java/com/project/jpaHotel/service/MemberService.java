package com.project.jpaHotel.service;

import com.project.jpaHotel.domain.Member;
import com.project.jpaHotel.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;


    public Long join(Member member){
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMember = memberRepository.findByEmail(member.getEmail());
        if (!findMember.isEmpty()){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }
    public Member findOne(Long memberId){
      return memberRepository.findOne(memberId);
    }
    public List<Member> findAll(){
        return memberRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        List<Member> members = memberRepository.findByEmail(email);

        if (members.isEmpty()) {
            throw new UsernameNotFoundException("해당 이메일로 가입된 회원이 없습니다: " + email);
        }

        Member member = members.get(0); // ✅ 첫 번째 회원 정보 사용

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString()) // 🔐 ROLE_ 접두사 필요 시 추가
                .build();
    }
    public String loadMemberEmail(Principal principal, HttpSession httpSession){ // 외부 로그인 멤버와 회원가입 폼 Dto 로 들어온 멤버 분기
        String userEmail = "";
        Member member = (Member) httpSession.getAttribute("user");

        if (principal != null){
            if (member != null){
                String email = (String) httpSession.getAttribute("email");
                return email;
            }
            else {
                userEmail = principal.getName();
                return userEmail;
            }
        }
        return null;
    }

    public String loadMemberName(Principal principal, HttpSession httpSession){
        String userName ="";
        String memberName = (String) httpSession.getAttribute("name");

        if (principal!= null){
            if (httpSession.getAttribute("user") != null){
                System.out.println(memberName);
                return memberName;
            }
            else {
                userName= principal.getName();
                List<Member> members =memberRepository.findByEmail(userName);
                Member member = members.get(0);
                return member.getName();
            }
        }
        return null;
    }
}
