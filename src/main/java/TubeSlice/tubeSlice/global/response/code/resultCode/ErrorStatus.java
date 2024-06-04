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
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER401", "해당 유저가 존재하지 않습니다." ),
    USER_TYPE_NOT_VALID(HttpStatus.BAD_REQUEST, "USER402", "검색 타입을 잘못 입력하셨습니다."),

    // Post
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "POST401", "게시글이 존재하지 않습니다."),
    POST_SEARCH_BAD_REQUEST(HttpStatus.BAD_REQUEST, "POST402", "검색 타입을 잘못 입력하셨습니다."),

    // PostLike
    POST_LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "POSTLIKE401", "해당하는 좋아요가 존재하지 않습니다."),

    // Comment
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMENT401", "해당하는 댓글이 존재하지 않습니다."),
    COMMENT_FORBIDDEN(HttpStatus.FORBIDDEN, "COMMENT402", "댓글 수정 혹은 삭제에 관한 권한이 존재하지 않습니다."),

    // Keyword
    KEYWORD_NOT_FOUND(HttpStatus.NOT_FOUND, "KEYWORD401","해당하는 키워드가 존재하지 않습니다."),

    //Translation
    TRANSLATION_BAD_REQUEST(HttpStatus.BAD_REQUEST,"TRANSLATION400","변환 오류. 다시 변환해주세요."),

    //User Script
    USER_SCRIPT_NOT_FOUND(HttpStatus.NOT_FOUND, "USERSCRIPT401", "저장되지 않은 스크립트입니다."),
    USER_SCRIPT_NOT_FOUND2(HttpStatus.NOT_FOUND, "USERSCRIPT401", "저장한 스크립트가 없습니다.");

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
