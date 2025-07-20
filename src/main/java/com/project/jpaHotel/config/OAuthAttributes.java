package com.project.jpaHotel.config;

import com.project.jpaHotel.domain.Member;
import jdk.dynalink.beans.StaticClass;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@ToString
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKet;
    private String name;
    private String email;

    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKet, String name, String email) {
        this.attributes = attributes;
        this.nameAttributeKet = nameAttributeKet;
        this.name = name;
        this.email = email;
    }
    private OAuthAttributes() {
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName,Map<String,Object> attributes){
        if (registrationId.equals("kakao")){
            return ofKakao(userNameAttributeName,attributes);
        }
        if (registrationId.equals("naver")){
            return ofNaver(userNameAttributeName,attributes);
        }
        return ofGoogle(userNameAttributeName,attributes);
    }

    private static OAuthAttributes ofKakao(String username,
                                           Map<String,Object> attributes){
        Map<String, Object> kakao_account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile =(Map<String, Object>) kakao_account.get("profile");

        return new OAuthAttributes(attributes,
                username,
                (String) profile.get("nickname"),
                (String)kakao_account.get("email"));
    }
    private static OAuthAttributes ofNaver(String username,Map<String,Object> attributes){
        Map<String,Object> response = (Map<String, Object>) attributes.get("response");

        return new OAuthAttributes(attributes,
                username,
                (String)response.get("name"),
                (String)response.get("email"));
    }

    private static OAuthAttributes ofGoogle(String username,
                                            Map<String,Object> attributes){
        return new OAuthAttributes(attributes,
                username,
                (String) attributes.get("name"),
                (String) attributes.get("email"));
    }
    public Member toEntity() {
        return new Member(name, email);
    }
}
