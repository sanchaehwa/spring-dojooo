package org.spring.dojooo.global.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApiResponse<T> {

    private final int status;
    private final String message;
    private final T data; //API 응답의 실제 데이터를 저장할 필드 (변경 불가능하게 불변 객체로 설정)


    public static <T> ApiResponse<T> of(int status, String message, T data) {
        return new ApiResponse<>(status, message, data);
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<T>(200, "요청이 성공적으로 처리되었습니다" ,data);
    }

}
