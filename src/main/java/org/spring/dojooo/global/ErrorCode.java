package org.spring.dojooo.global;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE) //외부에서 생성자 호출을 막고, enum 내부에서만 생성자 호출 가능
public enum ErrorCode {

    INTERNAL_SERVER_ERROR("정의되지 않은 에러가 발생했습니다.", 500),
    INVALID_INPUT("올바른 입력 형식이 아닙니다.", 400),
    NOT_FOUND_RESOURCE("존재하지 않는 리소스입니다.", 404),
    CONFLICT_ERROR("중복된 값입니다.", 409),
    EMAIL_BAD_REQUEST("이메일 인증이 완료되지 못했습니다", 401);

    private final String message;
    private final int status;

}
