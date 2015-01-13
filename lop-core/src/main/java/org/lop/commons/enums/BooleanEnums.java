package org.lop.commons.enums;

import org.lop.modules.enums.BaseEnum;

/**
 * 是、否枚举.
 * 
 * @author 潘瑞峥
 * @date 2014年8月7日
 */
public enum BooleanEnums implements BaseEnum<BooleanEnums, String> {

	/** 是. */
	T( "T", "是" ),

	/** 否. */
	F( "F", "否" ),

	;

	private final String value;
	private final String displayName;
	private static java.util.Map<String, BooleanEnums> valueMap = new java.util.HashMap<String, BooleanEnums>();

	static {
		for ( BooleanEnums e : BooleanEnums.values() ) {
			valueMap.put( e.value, e );
		}
	}

	BooleanEnums( String value, String displayName ) {
		this.value = value;
		this.displayName = displayName;
	}

	@Override
	public BooleanEnums getEnum( String value ) {
		return valueMap.get( value );
	}

	@Override
	public java.util.Map<String, BooleanEnums> getAllValueMap() {
		return valueMap;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public String getDisplayName() {
		return displayName;
	}

}