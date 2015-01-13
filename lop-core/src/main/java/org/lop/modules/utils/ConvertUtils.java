package org.lop.modules.utils;

import java.util.Date;

import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.reflect.MethodUtils;

import org.lop.modules.repository.mapper.QueryFilter.RestrictionType;

/**
 * 转换工具.
 * 
 * @author 潘瑞峥
 * @date 2013-6-2
 */
public class ConvertUtils {

	/** 注册时间转换器的格式. */
	private static final String[] CONVERT_DATE_FORMAT = new String[] { "yyyyMMdd", "yyyyMMddHHmmss", "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss",
			"yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss" };

	/** 根据值获得枚举的方法名称 */
	private static final String RETURN_ENUM_METHOD_NAME = "returnEnum";

	static {
		registerDateConverter();
	}

	/**
	 * 将值的类型转换为指定类型.
	 */
	public static Object conver( String value, Class<?> clazz ) {
		Validate.notNull( value, "value不能为空" );
		Validate.notNull( clazz, "clazz不能为空" );

		try {
			return org.apache.commons.beanutils.ConvertUtils.convert( value, clazz );
		} catch ( Exception e ) {
			throw ReflectionUtils.convertReflectionExceptionToUnchecked( e );
		}
	}

	/**
	 * 将值的类型转换为指定类型.
	 */
	public static Object convert( RestrictionType restrictionType, String value, Class<?> clazz, String className ) {
		Validate.notNull( value, "value不能为空" );
		Validate.notNull( clazz, "clazz不能为空" );

		Object result;

		/* Enum. */
		if ( Enum.class == clazz ) {
			Validate.notBlank( className, "className不能为空" );

			try {
				return MethodUtils.invokeStaticMethod( Class.forName( className.trim() ), RETURN_ENUM_METHOD_NAME, value );
			} catch ( Exception e ) {
				throw ReflectionUtils.convertReflectionExceptionToUnchecked( e );
			}
		}

		try {
			result = org.apache.commons.beanutils.ConvertUtils.convert( value, clazz );
		} catch ( Exception e ) {
			throw ReflectionUtils.convertReflectionExceptionToUnchecked( e );
		}

		/* 小于或小于等于, 需要将Date类型的值时分秒毫秒设置为0, 再增加一天减少一秒(2013-06-02 12:27:43 -> 2013-06-02 23:59:59). */
		if ( ( RestrictionType.LT.equals( restrictionType ) || RestrictionType.LE.equals( restrictionType ) ) && Date.class == clazz ) {
			return DateUtils.getEndDatetime( ( Date ) result );
		}

		return result;
	}

	/**
	 * 注册时间转换器.
	 */
	private static void registerDateConverter() {
		DateConverter dateConverter = new DateConverter();
		dateConverter.setUseLocaleFormat( true );
		dateConverter.setPatterns( CONVERT_DATE_FORMAT );
		org.apache.commons.beanutils.ConvertUtils.register( dateConverter, Date.class );
	}

}