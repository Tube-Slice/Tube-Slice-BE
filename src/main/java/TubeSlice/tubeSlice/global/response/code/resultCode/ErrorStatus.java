package TubeSlice.tubeSlice.global.response.code.resultCode;


import TubeSlice.tubeSlice.global.response.code.BaseErrorCode;
import TubeSlice.tubeSlice.global.response.code.ErrorReasonDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {
    // Global
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "GLOBAL401", "서버 오류"),
    KAKAO_TOKEN_ERROR(HttpStatus.BAD_REQUEST, "GLOBAL402", "토큰관련 서버 에러"),

    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER401", "해당 유저가 존재하지 않습니다." );

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDto getReason() {
        return ErrorReasonDto.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDto getReasonHttpStatus() {
        return ErrorReasonDto.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}