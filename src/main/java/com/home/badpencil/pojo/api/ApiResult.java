package com.home.badpencil.pojo.api;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;

@Data
public class ApiResult<T> {
    private Integer status;
    @ApiModelProperty(value = "口信", example = "没有所查询的资源!")
    private String msg;
    private T data;

    public enum ResultCode {
        SUCCESS(200, "成功"),
        FORBIDDEN(403, "无权限访问"),
        SYSTEM_ERROR(500, "系统错误"),
        UN_AUTHORIZED(401, "用户未登录"),
        HIVE_TABLE_NOT_EXIST(5001, "hive表未创建"),
        VERIFY_NOT_EXPECT(5005, "未进行埋点验证或失败"),
        PARAM_IS_INVALID(1000, "参数错误"),
        USER_IS_EXISTED(1001, "用户已存在");
        private Integer code;
        private String message;

        ResultCode(Integer code, String message) {
            this.code = code;
            this.message = message;
        }

        public Integer code() {
            return this.code;
        }

        public String message() {
            return this.message;
        }
    }
    public static <T> ApiResult<T> opResultAndMsg(boolean success,String msg) {
        ApiResult<T> apiResult = new ApiResult<>();
        apiResult.setResultCode(success ? ResultCode.SUCCESS : ResultCode.SYSTEM_ERROR);
        apiResult.setMsg(msg);
        return apiResult;
    }
    public static <T> ApiResult<T> success() {
        ApiResult<T> apiResult = new ApiResult<>();
        apiResult.setResultCode(ResultCode.SUCCESS);
        return apiResult;
    }
    public static <T> ApiResult<T> success(T data) {
        return success(ResultCode.SUCCESS.code,data);
    }
    public static <T> ApiResult<T> success(int code ,T data) {
        ApiResult<T> apiResult = new ApiResult<>();
        apiResult.setResultCode(ResultCode.SUCCESS);
        apiResult.setData(data);
        return apiResult;
    }
    public static <T> ApiResult<T> okWithMsg(String msg) {
        ApiResult<T> apiResult = new ApiResult<>();
        apiResult.setResultCode(ResultCode.SUCCESS);
        apiResult.setMsg(msg);
        return apiResult;
    }

    public static <T> ApiResult<T> forbidden(String msg) {
        ApiResult<T> apiResult = new ApiResult<>();
        apiResult.setStatus(ResultCode.FORBIDDEN.code);
        apiResult.setMsg(StringUtils.isBlank(msg) ? ResultCode.FORBIDDEN.message : msg);
        return apiResult;
    }

    public static <T> ApiResult<T> fail(String msg) {
        ApiResult<T> apiResult = new ApiResult<>();
        apiResult.setStatus(ResultCode.SYSTEM_ERROR.code);
        apiResult.setMsg(msg);
        return apiResult;
    }
    public static <T> ApiResult<T> fail(ResultCode code, String msg) {
       return fail(code,msg,null);
    }
    public static <T> ApiResult<T> fail(ResultCode code, String msg,T data) {
        ApiResult<T> apiResult = new ApiResult<>();
        apiResult.setStatus(code.code);
        apiResult.setMsg(msg);
        apiResult.setData(data);
        return apiResult;
    }

    public static <T> ApiResult<T> fail(ResultCode resultCode) {
        ApiResult<T> apiResult = new ApiResult<>();
        apiResult.setResultCode(resultCode);
        return apiResult;
    }

    public static Boolean checkSuccess(ApiResult result) {
        if (result != null && ResultCode.SUCCESS.code.equals(result.getStatus())) {
            return true;
        }
        return false;
    }

    public static interface ServiceCall<T> {
        ApiResult<T> call() throws Exception;
    }

    public static <T> ApiResult<T> call(ServiceCall<T> serviceCall, Logger logger) {
        try {
            return serviceCall.call();
        } catch (Exception ex) {
            HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
            logger.error("Query api occur some exception, request={}, params={}.",request.getRequestURI() , JSONObject.toJSONString(request.getParameterMap()), ex);
            return ApiResult.fail(ApiResult.printStackTrace(ex));
        }
    }

    private void setResultCode(ResultCode resultCode) {
        this.status = resultCode.code();
        this.msg = resultCode.message();
    }
    public static String printStackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        try {
            e.printStackTrace(pw);
            String  fullStack =  sw.toString();
            String[] array =   fullStack.split("\n");
            StringBuilder resultBuilder = new StringBuilder();
            for (int lineIndex = 0; lineIndex < Math.min(array.length,5) ; lineIndex++) {
                resultBuilder.append(array[lineIndex]).append("\n");
            }
            return resultBuilder.toString();
        } catch (Exception e1) {
            return "";
        }
    }
}
