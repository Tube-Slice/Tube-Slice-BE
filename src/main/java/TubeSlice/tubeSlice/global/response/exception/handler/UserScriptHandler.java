package TubeSlice.tubeSlice.global.response.exception.handler;

import TubeSlice.tubeSlice.global.response.code.BaseErrorCode;
import TubeSlice.tubeSlice.global.response.exception.GeneralException;

public class UserScriptHandler extends GeneralException {

    public UserScriptHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
