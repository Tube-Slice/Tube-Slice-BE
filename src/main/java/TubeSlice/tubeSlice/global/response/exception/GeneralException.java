package TubeSlice.tubeSlice.global.response.exception;


import TubeSlice.tubeSlice.global.response.code.BaseErrorCode;
import TubeSlice.tubeSlice.global.response.code.ErrorReasonDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

    private BaseErrorCode errorCode;

    public ErrorReasonDto getErrorReason() {
        return this.errorCode.getReason();
    }

    public ErrorReasonDto getErrorReasonHttpStatus(){
        return this.errorCode.getReasonHttpStatus();
    }
}
