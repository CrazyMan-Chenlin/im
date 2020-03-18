package com.roy.common.sdk.model;

import com.roy.common.sdk.enums.ResultCode;

import java.io.Serializable;

/**
 * @author chenlin
 */
public class Result implements Serializable {

     public Integer getCode() {
          return code;
     }

     public void setCode(Integer code) {
          this.code = code;
     }

     public String getMessage() {
          return message;
     }

     public void setMessage(String message) {
          this.message = message;
     }

     public Result(){

     }

     public Object getData() {
          return data;
     }

     public void setData(Object data) {
          this.data = data;
     }

     private Integer code;
     private String message;
     private Object data;

     public Result(ResultCode resultCode,Object data){
          this.code = resultCode.getCode();
          this.message = resultCode.getMessage();
          this.data = data;
     }

     public void setResultCode(ResultCode resultCode){
          this.code = resultCode.getCode();
          this.message = resultCode.getMessage();
     }

     public static Result success(){
          Result result = new Result();
          result.setResultCode(ResultCode.SUCCESS);
          return result;
     }

     public static Result success(Object data){
          Result result = new Result();
          result.setResultCode(ResultCode.SUCCESS);
          result.setData(data);
          return result;
     }

     public static Result failure(Integer code,String message){
          Result result = new Result();
          result.setMessage(message);
          result.setCode(code);
          return result;
     }

     public static Result failure(ResultCode resultCode){
          Result result = new Result();
          result.setResultCode(resultCode);
          return result;
     }

     public static Result failure(ResultCode resultCode,String message){
          Result result = new Result();
          result.setMessage(message);
          result.setCode(resultCode.getCode());
          return result;
     }

     public static Result failure(ResultCode resultCode,Object data){
          Result result = new Result();
          result.setResultCode(resultCode);
          result.setData(data);
          return result;
     }
}
