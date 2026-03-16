package com.devcrew.moodcode.domain.cart.dto;

import com.devcrew.moodcode.domain.product.ProductOption;
import java.util.List;

public record FindItemsResponse(List<ProductOption> productOptionIds) {

}
