package com.devcrew.moodcode.domain.user.exception;

import com.devcrew.moodcode.global.error.ErrorCode;
import com.devcrew.moodcode.global.error.exception.BusinessException;

public class DuplicateEmailException extends BusinessException {

    public DuplicateEmailException() {
        super(ErrorCode.EMAIL_DUPLICATION);
    }
}
