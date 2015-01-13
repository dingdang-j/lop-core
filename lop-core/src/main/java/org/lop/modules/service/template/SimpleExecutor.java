package org.lop.modules.service.template;

import org.lop.modules.facade.BaseResponseDTO;

/**
 * 简单的模板方法.
 * 
 * @author 潘瑞峥
 * @date 2014年8月5日
 */
public interface SimpleExecutor<POJO> {

	/**
	 * 前置处理.
	 */
	POJO before();

	/**
	 * 业务处理.
	 */
	void process( POJO pojo );

	/**
	 * 转换.
	 */
	void conver( POJO pojo, BaseResponseDTO response );

}