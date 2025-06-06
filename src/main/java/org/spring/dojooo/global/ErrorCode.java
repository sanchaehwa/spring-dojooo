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
    EMAIL_AUTH_TIMEOUT("이메일 인증 시간이 만료되었습니다. 다시 인증해주세요",401),
    EMAIL_CODE_EXPIRED("인증번호가 만료되었거나 존재하지 않습니다.", 400),
    EMAIL_CODE_NOT_MATCH("인증번호가 일치하지 않습니다.", 400),

    DUPLICATE_USER("이미 가입되어 있는 사용자 입니다", 409),
    NOT_FOUND_USER("사용자를 찾을 수 없습니다", 404),
    DUPLICATE_EMAIL("이미 사용중인 이메일 주소입니다",409),
    //로그인 관련
    INVALID_LOGIN_INPUT("이메일 주소 또는 비밀번호가 일치하지 않습니다",401),
    FAILED_LOGIN("로그인에 실패하였습니다" ,401),
    ACCOUNT_LOCKED("계정이 잠겼습니다",403 ),
    ACCOUNT_DISABLE("비활성화된 계정입니다", 403),

    // Token
    INVALID_TOKEN("유효하지 않은 토큰입니다.", 401),
    TOKEN_EXPIRED("토큰이 만료되었습니다.", 401),
    UNSUPPORTED_TOKEN("지원하지 않는 토큰입니다",400),

    //S3 Exception
    EMPTY_FILE_EXCEPTION("이미지 파일이 비어있습니다",400),
    IO_EXCEPTION_ON_IMAGE_UPLOAD("이미지 업로드 중 입출력 오류가 발생하였습니다",500),
    NO_FILE_EXTENTION("파일 확장자가 존재하지않습니다",400),
    PUT_OBJECT_EXCEPTION("S3에 파일 업로드 중 오류가 발생했습니다", 500),
    IO_EXCEPTION_ON_IMAGE_DELETE("이미지 삭제 중 입출력 오류가 발생했습니다", 500),
    NOT_USER_EQUALS_CURRENTUSER("본인 프로필만 수정 후 저장할 수 있습니다",403),

    //프로필
    WRONG_USER_EDIT("본인의 프로필만 수정할 수 있습니다.",403),
    MAX_REGISTER_TAG_EXCEPTION("최대 5개의 테그만 등록이 가능합니다",400),

    //테그 관련
    DUPLICATE_TAG_EXCEPTION("이미 등록된 테그입니다",400),
    NOTFOUND_TAG_EXCEPTION("테그를 찾을 수 없습니다",404),
    MAX_TAG_LENGTH_EXCEPTION("테그는 10자 이내로 작성할 수 있습니다",400),

    //체크리스트 관련
    NOT_FOUND_TASK_EXCEPTION("등록된 할일이 없습니다",404),
    WRONG_EDIT_CHECKLIST_EXCEPTION("본인 체크리스트만 수정 후 저장할 수 있습니다",403),

    //글쓰기 관련
    DUPLICATE_TITLE_EXCEPTION("해당 제목으로 작성할 글이 존재합니다",400),
    NOTFOUND_TECHLOG_EXCEPTION("작성한 글이 존재하지않습니다.",404),
    MAX_TITLE_LENGTH_EXCEPTION("제목은 20자 이내로 작성 할 수 있습니다",400),
    SECRET_TECHLOG_EXCEPTION("비공개 글입니다",403);

    private final String message;
    private final int status;

}
