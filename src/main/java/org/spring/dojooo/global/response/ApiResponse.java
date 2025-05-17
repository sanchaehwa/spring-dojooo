package org.spring.dojooo.global.response;

import lombok.Getter;

@Getter
public class ApiResponse<T> {

    private final T data; //API 응답의 실제 데이터를 저장할 필드 (변경 불가능하게 불변 객체로 설정)

    public ApiResponse(T data) {
        this.data = data;
    }

    public static <T> ApiResponse<T> of(T data) {
        return new ApiResponse<>(data);
    }

}
