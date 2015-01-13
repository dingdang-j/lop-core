package org.lop.modules.service;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.lop.commons.enums.ResponseCodeEnums;
import org.lop.modules.context.ApplicationContext;
import org.lop.modules.context.ApplicationContextHolder;
import org.lop.modules.enums.BaseEnum;
import org.lop.modules.exception.ApplicationException;
import org.lop.modules.facade.BaseRequestDTO;
import org.lop.modules.facade.BaseResponseDTO;
import org.lop.modules.repository.mybatis.BaseModel;
import org.lop.modules.service.template.SimpleExecutor;
import org.lop.modules.utils.InstantiateUtils;

/**
 * 简单Service基类.
 * 
 * @author 潘瑞峥
 * @date 2014年12月9日
 */
public class SimpleService {

	protected final Logger logger = LoggerFactory.getLogger( this.getClass() );

	protected final Logger loggerDigest = LoggerFactory.getLogger( "digest" );

	/**
	 * 业务简单处理模版方法骨架.
	 * 
	 * 不开启事务.
	 */
	@SuppressWarnings( { "rawtypes", "unchecked" } )
	protected final <R extends BaseResponseDTO> R serviceNoTransaction( BaseEnum action, BaseRequestDTO request, Class<R> responseClass,
			SimpleExecutor simpleExecutor ) {

		logger.debug( "请求业务: {}[{}]", action.getDisplayName(), action.getValue() );
		// loggerDigest.info( "请求业务: {}[{}]", action.getDisplayName(), action.getValue() );

		// 1.实例化Result.
		BaseResponseDTO response = InstantiateUtils.instantiate( responseClass );

		// 接收请求成功.
		response.setSuccess( Boolean.TRUE );

		try {

			// 2.校验Order.
			Validate.notNull( request, "request不能为空" );
			request.valid();

			// 3.创建上下文对象, 并使线程持有.
			ApplicationContextHolder.set( new ApplicationContext<BaseRequestDTO, BaseModel, BaseResponseDTO>( request, null, response ) );

			// 4.设置成功应答.
			setSuccess( response );
			// 5.前置处理.
			Object pojo = simpleExecutor.before();
			// 6.业务处理.
			simpleExecutor.process( pojo );
			// 7.转换.
			simpleExecutor.conver( pojo, response );

		} catch ( IllegalArgumentException e ) {
			ResponseCodeEnums responseCode = ResponseCodeEnums.ILLEGAL_ARGUMENT_EXCEPTION;

			response.setExecuted( Boolean.FALSE );
			response.setResponseCode( responseCode );
			response.setMessage( e.getMessage() );

			logger.error( "request: {}, message: {}:{}", request, responseCode.getDisplayName(), e.getMessage() );

		} catch ( ApplicationException e ) {
			ResponseCodeEnums responseCode = e.getResponseCode();

			response.setExecuted( Boolean.FALSE );
			response.setResponseCode( responseCode );
			response.setMessage( e.getMessage() );

			// logger.error( "request: {}, message: {}:{}", request, responseCode.getDisplayName(),
			// e.getMessage() );

		} catch ( Exception e ) {
			ResponseCodeEnums responseCode = ResponseCodeEnums.UNKNOWN_EXCEPTION;

			response.setExecuted( Boolean.FALSE );
			response.setResponseCode( responseCode );
			response.setMessage( responseCode.getDisplayName() );

			logger.error( "request: {}, message: {}:{}", request, responseCode.getDisplayName(), e.getMessage(), e );

		} finally {

			// 5.清空独立线程持有.
			ApplicationContextHolder.clear();
		}

		logger.debug( "响应业务: {}[{}]", action.getDisplayName(), action.getValue() );
		// loggerDigest.info( "响应业务: {}[{}]", action.getDisplayName(), action.getValue() );

		return ( R ) response;
	}

	/**
	 * 设置成功.
	 */
	public static void setSuccess( BaseResponseDTO response ) {
		ResponseCodeEnums responseCode = ResponseCodeEnums.EXECUTE_SUCCESS;

		response.setSuccess( Boolean.TRUE );
		response.setExecuted( Boolean.TRUE );
		response.setResponseCode( responseCode );
		response.setMessage( responseCode.getDisplayName() );
	}

}