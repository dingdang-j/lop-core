package org.lop.modules.service.template;

import org.lop.modules.repository.mybatis.BaseModel;
import org.lop.modules.service.BaseResponse;

/**
 * Response -> Model.
 * 
 * @author 潘瑞峥
 * @date 2014年8月5日
 */
public interface ConvertInvokeService {

	public void convert( BaseResponse response, BaseModel model );

}