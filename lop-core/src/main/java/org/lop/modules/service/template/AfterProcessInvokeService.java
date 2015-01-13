package org.lop.modules.service.template;

import org.lop.modules.repository.mybatis.BaseModel;

/**
 * 后处理.
 * 
 * @author 潘瑞峥
 * @date 2014年8月5日
 */
public interface AfterProcessInvokeService {

	public BaseModel after( BaseModel model );

}