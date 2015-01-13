package org.lop.modules.enums;

import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.lop.modules.json.jackson.databind.EnumSerializer;

/**
 * 枚举基类.
 * 
 * 所有的枚举, 必须实现该接口.
 * 
 * @author 潘瑞峥
 * @date 2014年8月1日
 */
@JsonSerialize( using = EnumSerializer.class )
public interface BaseEnum<E extends Enum<?>, T> {

	/**
	 * 值.
	 */
	T getValue();

	/**
	 * 显示值.
	 */
	String getDisplayName();

	/**
	 * 根据value获取枚举.
	 */
	E getEnum( T value );

	/**
	 * 获取枚举Map, key为value, val为enum.
	 */
	Map<T, E> getAllValueMap();

}