package org.lop.modules.spring.converter;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.core.convert.converter.Converter;

import org.lop.modules.lang.money.Money;

/**
 * Money Converter.
 * 
 * <pre>
 * 	&lt;bean id="conversionService" class="org.springframework.format.support.FormattingConversionServiceFactoryBean"&gt;
 * 		&lt;property name="converters"&gt;
 * 			&lt;list&gt;
 * 				&lt;bean class="org.lop.modules.spring.converter.MoneyConverter" /&gt;
 * 			&lt;/list&gt;
 * 		&lt;/property&gt;
 * 	&lt;/bean&gt;
 * </pre>
 * 
 * @author 潘瑞峥
 * @date 2014年8月25日
 */
public class MoneyConverter implements Converter<String, Money> {

	/**
	 * @see org.springframework.core.convert.converter.Converter#convert(java.lang.Object)
	 */
	@Override
	public Money convert( String source ) {
		Money money = null;

		if ( StringUtils.isNotBlank( source ) && NumberUtils.isNumber( source ) ) {
			money = new Money( source );
		}

		return money;
	}

}