package com.devcrew.moodcode.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductOptionDetailResponse {

    private Long optionId;
    private String optionName;
    private int stock;
    private boolean soldOut;
}
