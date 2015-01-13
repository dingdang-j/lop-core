package org.lop.modules.exception;

import java.util.HashMap;
import java.util.Map;

/**
 * Validate Exception.
 * 
 * @author 潘瑞峥
 * @date 2014年9月6日
 */
public class ValidateException extends IllegalArgumentException {

	private static final long serialVersionUID = 1L;

	/** 默认propertyPath与message分隔符. */
	public static final String DEFAULT_SEPARATOR = ": ";

	/** 默认错误消息间分隔符. */
	public static final String ERRORMESSAGES_SEPARATOR = ", ";

	private String message;

	private Map<String, String> messageMap = new HashMap<String, String>();

	public ValidateException() {
		super();
	}

	public String getMessage() {
		if ( null != message ) {
			return message;
		}

		if ( messageMap.isEmpty() ) {
			message = "";
			return message;
		}

		int index = 0;
		StringBuilder buf = new StringBuilder();
		for ( Map.Entry<String, String> entry : messageMap.entrySet() ) {
			if ( 0 != index ) {
				buf.append( ERRORMESSAGES_SEPARATOR );
			}

			buf.append( entry.getKey() ).append( DEFAULT_SEPARATOR ).append( entry.getValue() );

			index++;
		}
		message = buf.toString();

		return message;
	}

	public Map<String, String> getMessageMap() {
		return messageMap;
	}

	public void addMessage( String propertyPath, String message ) {
		this.messageMap.put( propertyPath, message );
		this.message = null;
	}

}