package TubeSlice.tubeSlice.global.response.exception.handler;

import TubeSlice.tubeSlice.global.response.code.BaseErrorCode;
import TubeSlice.tubeSlice.global.response.exception.GeneralException;

public class PostHandler extends GeneralException {
    public PostHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
