package com.project.jpaHotel.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.project.jpaHotel.config.OAuthAttributes;
import com.project.jpaHotel.constant.Role;
import com.project.jpaHotel.dto.MemberFormDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.units.qual.C;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String name;
    private String email;
    private String password;
    private String address;
    private String provider;
    private String tel;
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "member")
    private List<Board> boards =new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Reservation> reservations = new ArrayList<>();



    public Member() {

    }

    public Member(String name, String email) {
        this.name = name;
        this.email =email;
        this.role = Role.USER;
    }

    public static Member CreateMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        Member member =new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setProvider(memberFormDto.getProvider());
        member.setRole(memberFormDto.getRole());
        member.setAddress(memberFormDto.getAddress());
        member.setPassword(password);
        return member;
    }

}
