package com.project.jpaHotel.service;

import com.project.jpaHotel.domain.CartRoom;
import com.project.jpaHotel.repository.CartRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CartRoomService {
    private final CartRoomRepository cartRoomRepository;

    public List<CartRoom> findByMemberId(Long memberId){
        return cartRoomRepository.findByMemberId(memberId);
    }
}
