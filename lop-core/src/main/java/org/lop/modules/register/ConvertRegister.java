package org.lop.modules.register;

import java.util.Date;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;

/**
 * Convert注册器类.
 * 
 * <pre>
 * 	&lt;bean class="org.lop.modules.register.ConvertRegister" lazy-init="false" /&gt;
 * </pre>
 * 
 * @author 潘瑞峥
 * @date 2014年8月13日
 */
public class ConvertRegister {

	/** 时间格式. */
	public static final String[] DATE_PATTERNS = new String[] { "yyyyMMdd", "yyyy-MM-dd", "yyyy/MM/dd", "yyyy.MM.dd", "yyyyMMddHHmm",
			"yyyyMMddHHmmss", "yyyy-MM-dd HH:mm", "yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM/dd HH:mm:ss", "yyyy.MM.dd HH:mm",
			"yyyy.MM.dd HH:mm:ss" };

	static {
		registerDateConverter();
	}

	/**
	 * 注册自定义日期Converter的格式.
	 */
	private static void registerDateConverter() {
		DateConverter dateConverter = new DateConverter();
		dateConverter.setUseLocaleFormat( true );
		dateConverter.setPatterns( DATE_PATTERNS );

		ConvertUtils.register( dateConverter, Date.class );
	}

}