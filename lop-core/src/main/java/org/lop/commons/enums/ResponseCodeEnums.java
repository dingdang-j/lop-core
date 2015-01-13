package org.lop.commons.enums;

import org.lop.modules.enums.BaseEnum;

/**
 * 应答码枚举.
 * 
 * @author 潘瑞峥
 * @date 2014年8月7日
 */
public enum ResponseCodeEnums implements BaseEnum<ResponseCodeEnums, String> {

	/** 未知异常. */
	UNKNOWN_EXCEPTION( "UNKNOWN_EXCEPTION", "未知异常" ),

	/** 不合法或不正确的参数. */
	ILLEGAL_ARGUMENT_EXCEPTION( "ILLEGAL_ARGUMENT_EXCEPTION", "不合法或不正确的参数" ),

	/** 没有查询到满足条件的数据. */
	NOT_MEET_CONDITIONS_DATA( "NOT_MEET_CONDITIONS_DATA", "没有查询到满足条件的数据" ),

	/** 业务异常. */
	BIZ_EXCEPTION( "BIZ_EXCEPTION", "业务异常" ),

	/** 数据库异常. */
	DATABASE_EXCEPTION( "DATABASE_EXCEPTION", "数据库异常" ),

	/** 幂等性校验不通过. */
	IDEMPOTENCE_EXCEPTION( "IDEMPOTENCE_EXCEPTION", "幂等性校验不通过" ),

	/** 数据已经存在. */
	DATA_EXIST_ALREADY( "DATA_EXIST_ALREADY", "数据已经存在" ),

	/** 执行成功. */
	EXECUTE_SUCCESS( "EXECUTE_SUCCESS", "执行成功" ),

	;

	private final String value;
	private final String displayName;
	private static java.util.Map<String, ResponseCodeEnums> valueMap = new java.util.HashMap<String, ResponseCodeEnums>();

	static {
		for ( ResponseCodeEnums e : ResponseCodeEnums.values() ) {
			valueMap.put( e.value, e );
		}
	}

	ResponseCodeEnums( String value, String displayName ) {
		this.value = value;
		this.displayName = displayName;
	}

	@Override
	public ResponseCodeEnums getEnum( String value ) {
		return valueMap.get( value );
	}

	@Override
	public java.util.Map<String, ResponseCodeEnums> getAllValueMap() {
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