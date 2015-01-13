package org.lop.modules.facade;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import org.lop.commons.enums.ResponseCodeEnums;

/**
 * Response DTO基类.
 * 
 * @author 潘瑞峥
 * @date 2014年8月7日
 */
public class BaseResponseDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/** id. */
	private Long id;

	/** 请求是否接收成功. */
	private boolean success;

	/** 业务是否执行成功. */
	private boolean executed;

	/** 应答码. */
	private ResponseCodeEnums responseCode;

	/** 返回信息. */
	private String message;

	/** 扩展信息. */
	private String extend;

	public BaseResponseDTO() {
		responseCode = ResponseCodeEnums.UNKNOWN_EXCEPTION;
	}

	public Long getId() {
		return id;
	}

	public void setId( Long id ) {
		this.id = id;
	}

	/**
	 * 请求是否接收成功.
	 */
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess( boolean success ) {
		this.success = success;
	}

	/**
	 * 业务是否执行成功.
	 */
	public boolean isExecuted() {
		return executed;
	}

	public void setExecuted( boolean executed ) {
		this.executed = executed;
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

	public String getExtend() {
		return extend;
	}

	public void setExtend( String extend ) {
		this.extend = extend;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString( this, ToStringStyle.SHORT_PREFIX_STYLE );
	}

}