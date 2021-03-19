package com.north.base.api;


import java.io.Serializable;
import java.util.Optional;

public class R<T> implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 业务错误码
	 */
	private long code;
	/**
	 * 结果集
	 */
	private T data;
	/**
	 * 描述
	 */
	private String msg;

	public R() {
		// to do nothing
	}

	public R(IErrorCode errorCode) {
		errorCode = Optional.ofNullable(errorCode).orElse(ApiErrorCode.FAILED);
		this.code = errorCode.getCode();
		this.msg = errorCode.getMsg();
	}

	public static R ok() {
		ApiErrorCode aec = ApiErrorCode.SUCCESS;
		return restResult("", aec);
	}

	public static <T> R ok(T data) {
		ApiErrorCode aec = ApiErrorCode.SUCCESS;
		if (data instanceof Boolean && Boolean.FALSE.equals(data)) {
			aec = ApiErrorCode.FAILED;
		}
		return restResult(data, aec);
	}

	public static <T> R failed(String msg) {
		return restResult(null, ApiErrorCode.FAILED.getCode(), msg);
	}

	public static <T> R failed(IErrorCode errorCode) {
		return restResult(null, errorCode);
	}

	public static <T> R failed(IErrorCode errorCode,String msg) {
		return restResult(null, errorCode.getCode(),msg);
	}

	public static <T> R restResult(T data, IErrorCode errorCode) {
		return restResult(data, errorCode.getCode(), errorCode.getMsg());
	}

	private static <T> R restResult(T data, long code, String msg) {
		R apiResult = new R();
		apiResult.setCode(code);
		apiResult.setData(data);
		apiResult.setMsg(msg);
		return apiResult;
	}

	public boolean innerOk() {
		return ApiErrorCode.SUCCESS.getCode() == code;
	}

	/**
	 * 服务间调用非业务正常，异常直接释放
	 */
	public T serviceData() {
		if (!innerOk()) {
			throw new ApiException(this.msg);
		}
		return data;
	}

	public long getCode() {
		return code;
	}

	public R setCode(long code) {
		this.code = code;
		return this;
	}

	public T getData() {
		return data;
	}

	public R setData(T data) {
		this.data = data;
		return this;
	}

	public String getMsg() {
		return msg;
	}

	public R setMsg(String msg) {
		this.msg = msg;
		return this;
	}
}
