package org.lop.modules.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 反射工具类.
 * 
 * 扩展apache-commons功能.
 * 
 * @author 潘瑞峥
 * @date 2013-5-31
 */
public class ReflectionUtils {

	private static Logger logger = LoggerFactory.getLogger( ReflectionUtils.class );

	/**
	 * 通过反射, 获得Class定义中声明的父类的泛型参数的类型. 如无法找到, 返回Object.class.
	 * 
	 * eg.public UserDao extends HibernateDao<User>
	 * 
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings( "unchecked" )
	public static <T> Class<T> getSuperClassGenricType( final Class<?> clazz ) {
		return getSuperClassGenricType( clazz, 0 );
	}

	/**
	 * 通过反射, 获得Class定义中声明的父类的泛型参数的类型. 如无法找到, 返回Object.class.
	 * 
	 * eg.public UserDao extends HibernateDao<User,Long>
	 * 
	 * @param clazz
	 * @param index
	 * @return
	 */
	@SuppressWarnings( "rawtypes" )
	public static Class getSuperClassGenricType( final Class<?> clazz, final int index ) {

		Type genType = clazz.getGenericSuperclass();

		if ( !( genType instanceof ParameterizedType ) ) {
			logger.warn( clazz.getSimpleName() + "'s superclass not ParameterizedType" );
			return Object.class;
		}

		Type[] types = ( ( ParameterizedType ) genType ).getActualTypeArguments();

		if ( index >= types.length || index < 0 ) {
			logger.warn( "Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: " + types.length );
			return Object.class;
		}
		if ( !( types[ index ] instanceof Class ) ) {
			logger.warn( clazz.getSimpleName() + " not set the actual class on superclass generic parameter" );
			return Object.class;
		}

		return ( Class ) types[ index ];
	}

	/**
	 * 将反射时的checked exception转换为unchecked exception.
	 */
	public static RuntimeException convertReflectionExceptionToUnchecked( Exception e ) {
		if ( e instanceof IllegalAccessException || e instanceof IllegalArgumentException || e instanceof NoSuchMethodException ) {
			return new IllegalArgumentException( "Reflection Exception.", e );
		} else if ( e instanceof InvocationTargetException ) {
			return new RuntimeException( "Reflection Exception.", ( ( InvocationTargetException ) e ).getTargetException() );
		} else if ( e instanceof RuntimeException ) {
			return ( RuntimeException ) e;
		}
		return new RuntimeException( "Unexpected Checked Exception.", e );
	}

}