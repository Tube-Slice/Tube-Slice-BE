package TubeSlice.tubeSlice.global.response.exception.handler;

import TubeSlice.tubeSlice.global.response.code.BaseErrorCode;
import TubeSlice.tubeSlice.global.response.exception.GeneralException;

public class PostLikeHandler extends GeneralException {
    public PostLikeHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
