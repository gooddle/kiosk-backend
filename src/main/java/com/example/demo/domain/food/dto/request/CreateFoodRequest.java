package com.example.demo.domain.food.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateFoodRequest {
    private String name;
    private Integer price;
}
