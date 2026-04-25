package com.example.demo.domain.food.dto.response;

import com.example.demo.domain.food.model.Food;

public record FoodResponse (
        Long id,
        String name,
        Integer price
){
    public static FoodResponse from(Food food) {
        return new FoodResponse(food.getId(),food.getName(), food.getPrice());
    }
}
