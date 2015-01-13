package org.lop.modules.spring.converter;

import java.util.Date;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;

/**
 * Date Converter.
 * 
 * <pre>
 * 	&lt;bean id="conversionService" class="org.springframework.format.support.FormattingConversionServiceFactoryBean"&gt;
 * 		&lt;property name="converters"&gt;
 * 			&lt;list&gt;
 * 				&lt;bean class="org.lop.modules.spring.converter.DateConverter" /&gt;
 * 			&lt;/list&gt;
 * 		&lt;/property&gt;
 * 	&lt;/bean&gt;
 * </pre>
 * 
 * @author 潘瑞峥
 * @date 2014年8月13日
 */
public class DateConverter implements Converter<String, Date> {

	/**
	 * @see org.springframework.core.convert.converter.Converter#convert(java.lang.Object)
	 */
	@Override
	public Date convert( String source ) {
		Date date = null;

		if ( StringUtils.isNotBlank( source ) ) {
			date = ( Date ) ConvertUtils.convert( source, Date.class );
		}

		return date;
	}

}