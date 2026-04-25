package com.example.demo.domain.food.service;

import com.example.demo.common.Role;
import com.example.demo.domain.food.dto.request.CreateFoodRequest;
import com.example.demo.domain.food.dto.response.FoodResponse;
import com.example.demo.domain.food.model.Food;
import com.example.demo.domain.food.repository.FoodRepository;
import com.example.demo.domain.user.model.User;
import com.example.demo.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;
    private final UserRepository userRepository;

    @Transactional
    public FoodResponse getById(Long id, Long userId) {
        userRepository.findById(userId).orElseThrow(()-> new IllegalArgumentException("로그인이 필요합니다.")) ;
        Food food = foodRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("없는 메뉴입니다."));
        return FoodResponse.from(food);
    }

    @Transactional
    public FoodResponse createFood(CreateFoodRequest request, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("로근인이 필요합니다."));
        if (user.getRole() != Role.ADMIN.name()) {
            throw  new IllegalArgumentException("관리자가 아닙니다.");
        }
        Food newFood = Food.builder()
                .name(request.getName())
                .price(request.getPrice())
                .createdAt(LocalDateTime.now())
                .build();
        return FoodResponse.from(newFood);
    }

}
