package org.lop.modules.repository.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.web.util.WebUtils;

import org.lop.modules.repository.mapper.QueryFilter.PropertyType;
import org.lop.modules.repository.mapper.QueryFilter.RestrictionType;
import org.lop.modules.utils.ConvertUtils;

/**
 * 条件过滤查询模式处理器.
 * 
 * @author 潘瑞峥
 * @date 2013-6-2
 */
public class QueryFilterHandler {

	/** 分隔符. */
	private static final String SEPARATOR = "_";

	/** 参数的前缀key. */
	private static final String PARAMS_PREFIX_KEY = "query";

	/** 多属性间OR的分隔符key. */
	private static final String OR_SEPARATOR_KEY = "OR";

	/** 参数的前缀. */
	public static final String PARAMS_PREFIX = String.format( "%s%s", PARAMS_PREFIX_KEY, SEPARATOR );

	/** 多属性间OR的分隔符. */
	public static final String OR_SEPARATOR = String.format( "%s%s%s", SEPARATOR, OR_SEPARATOR_KEY, SEPARATOR );

	/** 参数的个数. */
	private static final int MAX_AMOUNT = 3;

	/**
	 * 从HttpRequest中创建QueryFilter列表, 默认Filter属性名前缀为"query_".
	 * 
	 * @see #createFilter(HttpServletRequest, String)
	 */
	public static List<QueryFilter> createFilter( ServletRequest request ) {

		return createFilter( request, PARAMS_PREFIX );
	}

	/**
	 * 从HttpRequest中创建QueryFilter列表.<br>
	 * 命名规则为Filter属性前缀_比较类型_属性类型[枚举]_属性名(或_OR_另属性名).
	 * 
	 * <pre>
	 * eg. query_EQ_S_name, query_LIKE_S_name_OR_username, query_LIKESTART_S_id.no<br>
	 *     query_GE_DATE_createTime, query_EQ_E(org.lop.CustomEnum)_type.
	 * </pre>
	 */
	public static List<QueryFilter> createFilter( Map<String, Object> filterParamMap ) {

		List<QueryFilter> filterList = new ArrayList<QueryFilter>();

		if ( MapUtils.isEmpty( filterParamMap ) ) {

			return filterList;
		}

		// 分析参数Map, 构造PropertyFilter列表
		for ( Map.Entry<String, Object> entry : filterParamMap.entrySet() ) {
			String filterName = entry.getKey();

			// String.
			if ( entry.getValue() instanceof String ) {
				String value = ( String ) entry.getValue();
				// 如果value值为空或空字符串或空格, 则忽略此filter.
				if ( StringUtils.isNotBlank( value ) ) {
					QueryFilter filter = createFilter( filterName, value );
					filterList.add( filter );
				}
			}
			// String[] 暂不支持.
			else {
			}
		}

		return filterList;
	}

	/**
	 * 从HttpRequest中创建QueryFilter列表.<br>
	 * 命名规则为Filter属性前缀_比较类型_属性类型[枚举]_属性名(或_OR_另属性名).
	 * 
	 * <pre>
	 * eg. query_EQ_S_name, query_LIKE_S_name_OR_username, query_LIKESTART_S_id.no<br>
	 *     query_GE_DATE_createTime, query_EQ_E(org.lop.CustomEnum)_type.
	 * </pre>
	 */
	public static List<QueryFilter> createFilter( ServletRequest request, String prefix ) {
		Validate.notNull( request, "request不能为空" );
		Validate.notNull( prefix, "prefix不能为空" );

		// 从request中获取含属性前缀名的参数, 构造去除前缀名后的参数Map.
		Map<String, Object> filterParamMap = WebUtils.getParametersStartingWith( request, prefix );

		return createFilter( filterParamMap );
	}

	/**
	 * 根据参数创建QueryFilter.
	 */
	public static QueryFilter createFilter( final String filterName, final String value ) {
		RestrictionType restrictionType = null;
		String[] propertyNames = null;
		Object propertyValue = null;
		Class<?> valueClass = null;
		String className = null;

		String errMsg = String.format(
				"filter名称%s没有按照规则编写, 无法得到属性比较类型(eg.EQ_D_money, LIKE_S_name_OR_username, EQ_E(org.lop.CustomEnum)_type).", filterName );

		String[] params = StringUtils.split( filterName, SEPARATOR, MAX_AMOUNT );
		Validate.isTrue( MAX_AMOUNT == params.length, errMsg );

		String restrictionCode = params[ 0 ];
		String propertyTypeCode = params[ 1 ];
		String propertyFieldName = params[ 2 ];

		/* Enum. */
		if ( StringUtils.startsWithIgnoreCase( propertyTypeCode, PropertyType.E.name() ) ) {
			Validate.isTrue( -1 != StringUtils.indexOf( propertyTypeCode, "(" ), "若为Enum类型, 格式为:EQ_E(org.lop.CustomEnum)_type" );

			className = StringUtils.trim( StringUtils.substringBetween( propertyTypeCode, "(", ")" ) );
			propertyTypeCode = PropertyType.E.name();
		}

		try {
			restrictionType = Enum.valueOf( RestrictionType.class, restrictionCode );
		} catch ( RuntimeException e ) {
			throw new IllegalArgumentException( errMsg, e );
		}

		try {
			valueClass = Enum.valueOf( PropertyType.class, propertyTypeCode ).getValue();
		} catch ( RuntimeException e ) {
			throw new IllegalArgumentException( errMsg, e );
		}

		Validate.isTrue( StringUtils.isNotBlank( propertyFieldName ), errMsg );
		propertyNames = StringUtils.splitByWholeSeparator( propertyFieldName, OR_SEPARATOR );

		propertyValue = ConvertUtils.convert( restrictionType, value, valueClass, className );

		QueryFilter queryFilter = new QueryFilter( restrictionType, propertyNames, propertyValue, valueClass );
		return queryFilter;
	}

}