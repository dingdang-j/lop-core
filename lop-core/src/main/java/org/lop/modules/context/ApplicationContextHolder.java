package org.lop.modules.context;

import java.io.Serializable;

/**
 * 应用上下文持有.
 * 
 * @author 潘瑞峥
 * @date 2014年8月4日
 */
public class ApplicationContextHolder implements Serializable {

	private static final long serialVersionUID = -8072866163589806432L;

	/** 应用上下文, 所在ThreadLocal. */
	private static InheritableThreadLocal<ApplicationContext<?, ?, ?>> contextLocal = new InheritableThreadLocal<ApplicationContext<?, ?, ?>>();

	/**
	 * 获取上下文.
	 */
	@SuppressWarnings( "unchecked" )
	public static <REQUEST, DOMAIN, RESPONSE> ApplicationContext<REQUEST, DOMAIN, RESPONSE> get() {
		return ( ApplicationContext<REQUEST, DOMAIN, RESPONSE> ) contextLocal.get();
	}

	/**
	 * 赋予上下文.
	 */
	public static <REQUEST, DOMAIN, RESPONSE> void set( ApplicationContext<REQUEST, DOMAIN, RESPONSE> context ) {
		contextLocal.set( context );
	}

	/**
	 * 清理充值上下文.
	 */
	public static void clear() {
		contextLocal.set( null );
	}

}