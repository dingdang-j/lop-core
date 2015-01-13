package org.lop.modules.context;

import java.io.Serializable;
import java.util.Map;

import com.google.common.collect.Maps;

/**
 * 系统上下文定义, 存储于当前线程.
 * 
 * @author 潘瑞峥
 * @date 2014年8月4日
 */
public class ApplicationContext<REQUEST, DOMAIN, RESPONSE> implements Serializable {

	private static final long serialVersionUID = 3853675598288814918L;

	/** Request. */
	private REQUEST request;

	/** Domain. */
	private DOMAIN domain;

	/** Response */
	private RESPONSE response;

	/** 冗余字段, 用于系统流转. */
	private Map<String, Object> attribute = Maps.newHashMap();

	public ApplicationContext( REQUEST request, DOMAIN domain, RESPONSE response ) {
		this.request = request;
		this.domain = domain;
		this.response = response;
	}

	public REQUEST getRequest() {
		return request;
	}

	public void setRequest( REQUEST request ) {
		this.request = request;
	}

	public DOMAIN getDomain() {
		return domain;
	}

	public void setDomain( DOMAIN domain ) {
		this.domain = domain;
	}

	public RESPONSE getResponse() {
		return response;
	}

	public void setResponse( RESPONSE response ) {
		this.response = response;
	}

	public Map<String, Object> getAttribute() {
		return attribute;
	}

	public void setAttribute( Map<String, Object> attribute ) {
		this.attribute = attribute;
	}

	public void addAttribute( String key, Object value ) {
		attribute.put( key, value );
	}

	public void addAttributes( Map<String, Object> params ) {
		attribute.putAll( params );
	}

	public Object getAttribute( String key ) {
		Object value = null;
		if ( attribute.containsKey( key ) ) {
			value = attribute.get( key );
		}
		return value;
	}

}