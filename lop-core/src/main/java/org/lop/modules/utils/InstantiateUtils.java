package org.lop.modules.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类实例化工具.
 * 
 * 需实例化的类必须有默认空参构造方法, 否则不能生成实例.
 * 
 * @author 潘瑞峥
 * @date 2014年8月1日
 */
public class InstantiateUtils {

	private static final Logger LOG = LoggerFactory.getLogger( InstantiateUtils.class );

	/**
	 * 创建实例.
	 * 
	 * 只能根据无参的构造器创建.
	 */
	@SuppressWarnings( "unchecked" )
	public static <R> R instantiate( Class<R> clazz ) {

		Validate.notNull( clazz, "Class is null" );

		Constructor<?> constructor = null;

		try {
			// 获取空参构造器.
			constructor = ConstructorUtils.getAccessibleConstructor( clazz );

		} catch ( Exception e ) {
			LOG.error( "no default (no-argument) constructor for class: " + clazz.getName()
					+ " (class must be instantiated by Interceptor)", e );
		}

		// 是否接口或抽象类.
		int modifier = clazz.getModifiers();
		if ( Modifier.isAbstract( modifier ) || Modifier.isInterface( modifier ) ) {

			throw new RuntimeException( "Cannot instantiate abstract class or interface: " + clazz.getName() );
		}
		// 不存在空参构造器.
		else if ( null == constructor ) {

			throw new RuntimeException( "No default constructor for result: " + clazz.getName() );
		} else {
			try {

				return ( R ) constructor.newInstance( ( Object[] ) null );

			} catch ( Exception e ) {

				throw new RuntimeException( "Could not instantiate result: " + clazz.getName() );
			}
		}
	}

}