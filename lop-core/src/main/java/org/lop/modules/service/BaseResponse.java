package org.lop.modules.service;

import java.io.Serializable;

/**
 * Request基类.
 * 
 * @author 潘瑞峥
 * @date 2014年8月4日
 */
public abstract class BaseResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 请求是否接收成功.
	 */
	private boolean success;

	/**
	 * 业务是否执行成功.
	 */
	private boolean executed;

	/**
	 * 返回信息.
	 */
	private String message;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess( boolean success ) {
		this.success = success;
	}

	public boolean isExecuted() {
		return executed;
	}

	public void setExecuted( boolean executed ) {
		this.executed = executed;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage( String message ) {
		this.message = message;
	}

}