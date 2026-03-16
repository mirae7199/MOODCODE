package com.devcrew.moodcode.domain.payment;

public enum PaymentMethod {
  카드, // 카드
  간편결제, // 간편결제 (토스페이, 카카오페이 등)
  가상결제, // 가상계좌
  계좌이체; // 계좌이체
}