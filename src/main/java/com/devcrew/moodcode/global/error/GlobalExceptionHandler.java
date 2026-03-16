package com.devcrew.moodcode.global.error;

import com.devcrew.moodcode.global.error.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * 1. 비즈니스 예외 처리
   * 개발자가 의도적으로 발생시킨 예외 (예: 중복 이메일, 잔액 부족 등)
   */
  @ExceptionHandler(BusinessException.class)
  protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
    log.error("BusinessException: code={} message={}", e.getErrorCode().getCode(), e.getMessage());
    return ErrorResponse.toResponseEntity(e.getErrorCode());
  }

  /**
   * 2. 유효성 검사 예외 처리 (@Valid 실패)
   * DTO의 @NotBlank, @Email 등의 검증 실패 시 발생
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    BindingResult bindingResult = e.getBindingResult();
    StringBuilder builder = new StringBuilder();

    // 에러 메시지를 "필드명: 메시지" 형태로 조합
    for (FieldError fieldError : bindingResult.getFieldErrors()) {
      builder.append("[");
      builder.append(fieldError.getField());
      builder.append("](은)는 ");
      builder.append(fieldError.getDefaultMessage());
      builder.append(". "); // 여러 개일 경우 구분
    }

    // 마지막 공백 제거 및 로그 출력
    String message = builder.toString();
    log.error("Validation Fail: {}", message);

    // INVALID_INPUT_VALUE 코드와 함께 상세 메시지 전달
    return ErrorResponse.toResponseEntity(ErrorCode.INVALID_INPUT_VALUE, message);
  }

  /**
   * 3. 그 외 나머지 예외 처리 (최후의 보루)
   * 예상치 못한 NullPointerException 등이 발생하면 500 에러로 처리
   */
  @ExceptionHandler(Exception.class)
  protected ResponseEntity<ErrorResponse> handleException(Exception e) {
    log.error("Internal Server Error", e);
    return ErrorResponse.toResponseEntity(ErrorCode.INTERNAL_SERVER_ERROR);
  }
}