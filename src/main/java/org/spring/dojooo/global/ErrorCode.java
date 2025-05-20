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

    EMAIL_BAD_REQUEST("이메일 인증이 완료되지 못했습니다", 401),

    DUPLICATE_USER("이미 가입되어 있는 사용자 입니다", 409),
    NOT_FOUND_USER("사용자를 찾을 수 없습니다", 404),
    //로그인 관련
    INVALID_LOGIN_INPUT("이메일 주소 또는 비밀번호가 일치하지 않습니다",401),
    FAILED_LOGIN("로그인에 실패하였습니다" ,401),
    ACCOUNT_LOCKED("계정이 잠겼습니다",403 ),
    ACCOUNT_DISABLE("비활성화된 계정입니다", 403),

    // Token
    INVALID_TOKEN("유효하지 않은 토큰입니다.", 401),
    TOKEN_EXPIRED("토큰이 만료되었습니다.", 401),

    //임시저장
    UPDATE_TIMEOUT("회원 정보 수정 유효 시간이 초과되었습니다",500);


    private final String message;
    private final int status;

}
