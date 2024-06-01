package TubeSlice.tubeSlice.global.response.exception.handler;

import TubeSlice.tubeSlice.global.response.code.BaseErrorCode;
import TubeSlice.tubeSlice.global.response.exception.GeneralException;

public class TransHandler extends GeneralException {

    public TransHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
