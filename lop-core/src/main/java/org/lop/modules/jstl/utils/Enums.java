package org.lop.modules.jstl.utils;

import java.util.List;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 枚举工具类.
 * 
 * @author 潘瑞峥
 * @date 2014年11月12日
 */
public class Enums {

	private static final Logger LOG = LoggerFactory.getLogger( Enums.class );

	/**
	 * 根据枚举名称获取集合.
	 */
	@SuppressWarnings( { "rawtypes", "unchecked" } )
	public static List getEnumList( String enumClassName ) {
		if ( StringUtils.isBlank( enumClassName ) ) {

			return null;
		}

		List list = null;

		try {
			Class<Enum> enumClass = ( Class<Enum> ) Class.forName( enumClassName );

			list = EnumUtils.getEnumList( enumClass );

		} catch ( Exception e ) {

			LOG.warn( e.getMessage(), e );
		}

		return list;
	}

}