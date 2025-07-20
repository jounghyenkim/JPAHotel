package com.project.jpaHotel.config;

import com.project.jpaHotel.constant.Role;
import com.project.jpaHotel.domain.Member;
import com.project.jpaHotel.domain.QMember;
import com.project.jpaHotel.dto.SessionUser;
import com.project.jpaHotel.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final HttpSession httpSession;
    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName,
                oAuth2User.getAttributes());
        Member member = saveOrUpdate(attributes);
        httpSession.setAttribute("name", attributes.getName());
        httpSession.setAttribute("email", attributes.getEmail());
        if (registrationId.equals("kakao")) {
            httpSession.setAttribute("Kakao_User", attributes.toString());
        } else if (registrationId.equals("naver")) {
            httpSession.setAttribute("Naver_User", attributes.toString());
        } else {
            httpSession.setAttribute("Google_User", attributes.toString());
        }
        String memberRole = null;
        memberRole = "ROLE_" + member.getRole().toString();


        System.out.println("memberRole = " + memberRole);

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(memberRole)),
                attributes.getAttributes()
                , attributes.getNameAttributeKet()
        );
    }

    private Member saveOrUpdate(OAuthAttributes attributes) {
        List<Member> members = memberRepository.findByEmail(attributes.getEmail());
        Member member;

        if (members.isEmpty()) {
            member = attributes.toEntity();
            memberRepository.save(member);
        } else {
            member = members.get(0);  // 첫 번째 회원 선택
            member.setName(attributes.getName());  // 기존 유저 정보 업데이트
        }

        httpSession.setAttribute("user", member);
        System.out.println("member = " + member);
        return member;
    }
}



