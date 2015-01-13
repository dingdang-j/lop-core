package org.lop.modules.repository.mapper;

import java.util.Date;

import org.apache.commons.lang3.Validate;

/**
 * 属性过滤查询对象.
 * 
 * @author 潘瑞峥
 * @date 2013-6-1
 */
public class QueryFilter {

	/** 查询类型(eq, like, in等). */
	public RestrictionType restrictionType;

	/** 属性名称, 可能多个(eg.NAME_OR_REMARK). */
	public String[] propertyNames;

	/** 属性值. */
	public Object value;

	/** 属性值的Class类型. */
	public Class<?> valueClass;

	public QueryFilter( RestrictionType restrictionType, String propertyName, Object value ) {
		this.restrictionType = restrictionType;
		this.propertyNames = new String[] { propertyName };
		this.value = value;
	}

	public QueryFilter( RestrictionType restrictionType, String propertyName, Object value, Class<?> valueClass ) {
		this.restrictionType = restrictionType;
		this.propertyNames = new String[] { propertyName };
		this.value = value;
		this.valueClass = valueClass;
	}

	public QueryFilter( RestrictionType restrictionType, String[] propertyNames, Object value, Class<?> valueClass ) {
		this.restrictionType = restrictionType;
		this.propertyNames = propertyNames;
		this.value = value;
		this.valueClass = valueClass;
	}

	public RestrictionType getRestrictionType() {
		return restrictionType;
	}

	public String[] getPropertyNames() {
		return propertyNames;
	}

	public Object getValue() {
		return value;
	}

	public Class<?> getValueClass() {
		return valueClass;
	}

	/**
	 * 获取唯一属性名称.
	 */
	public String getPropertyName() {
		Validate.isTrue( 1 == propertyNames.length, "There are not only one property in this filter." );

		return propertyNames[ 0 ];
	}

	/**
	 * 是否比较多个属性.
	 */
	public boolean hasMultiProperties() {
		return ( 1 < propertyNames.length );
	}

	/**
	 * 属性数据类型.
	 */
	public enum PropertyType {
		S( String.class ), I( Integer.class ), L( Long.class ),

		D( Double.class ), B( Boolean.class ), DATE( Date.class ),

		E( Enum.class );

		private Class<?> clazz;

		private PropertyType( Class<?> clazz ) {
			this.clazz = clazz;
		}

		public Class<?> getValue() {
			return clazz;
		}
	}

	/**
	 * 条件类型.
	 */
	public enum RestrictionType {
		LT, LE, GT, GE, EQ, NE, IN,

		// '%value%'
		LIKE,

		// 'value%'
		LIKESTART,

		// '%value'
		LIKEEND;
	}

}