package com.devcrew.moodcode.domain.order;

public enum OrderedProductStatus {
  PREPARING,  // 상품 준비 중
  SHIPPING,   // 배송 중
  DELIVERED,  // 배송 완료
  CANCELLED   // 취소
}
