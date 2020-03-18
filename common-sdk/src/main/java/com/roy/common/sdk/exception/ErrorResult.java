package com.roy.common.sdk.exception;

import com.roy.common.sdk.enums.ResultCode;

/**
 * @author chenlin
 */
public class ErrorResult extends RuntimeException {

    private ResultCode resultCode;

    public ErrorResult(ResultCode resultCode) {
        this.resultCode = resultCode;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }

    public Integer getCode(){
        return resultCode.getCode();
    }

    @Override
    public String getMessage(){

        return resultCode.getMessage();
    }


}
