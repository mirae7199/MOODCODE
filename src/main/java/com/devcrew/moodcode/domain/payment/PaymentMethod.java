package com.devcrew.moodcode.domain.payment;

public enum PaymentMethod {
  CARD, // 카드
  EASY_PAY, // 간편결제 (토스페이, 카카오페이 등)
  VIRTUAL_ACCOUNT, // 가상계좌
  TRANSFER; // 계좌이체
}