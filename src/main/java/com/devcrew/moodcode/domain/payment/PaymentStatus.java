package com.devcrew.moodcode.domain.payment;

public enum PaymentStatus {
  READY, // 결제 준비 (결제창 열림)
  IN_PROGRESS, // 결제 진행 중
  WAITING_FOR_DEPOSIT, // 가상계좌 입금 대기
  DONE, // 결제 완료
  CANCELED, // 결제 취소
  PARTIAL_CANCELED, // 부분 취소
  ABORTED, // 결제 중단 (카드 한도 초과 등)
  EXPIRED  // 결제 만료 (10분 초과)
}