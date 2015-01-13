package org.lop.modules.exception;

import org.lop.commons.enums.ResponseCodeEnums;

/**
 * 异常基类.
 * 
 * @author 潘瑞峥
 * @date 2014年8月7日
 */
public class ApplicationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/** 响应码. */
	private ResponseCodeEnums responseCode;

	/** 消息. */
	private String message;

	public ApplicationException( ResponseCodeEnums responseCode ) {
		this.responseCode = responseCode;

		if ( null != responseCode ) {
			message = responseCode.getDisplayName();
		}
	}

	public ApplicationException( ResponseCodeEnums responseCode, String message ) {
		this.responseCode = responseCode;
		this.message = message;
	}

	public ResponseCodeEnums getResponseCode() {
		return responseCode;
	}

	public void setResponseCode( ResponseCodeEnums responseCode ) {
		this.responseCode = responseCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage( String message ) {
		this.message = message;
	}

}