package org.lop.modules.service;

import java.util.Date;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import org.lop.commons.enums.ResponseCodeEnums;
import org.lop.modules.context.ApplicationContext;
import org.lop.modules.context.ApplicationContextHolder;
import org.lop.modules.enums.BaseEnum;
import org.lop.modules.exception.ApplicationException;
import org.lop.modules.facade.BaseRequestDTO;
import org.lop.modules.facade.BaseResponseDTO;
import org.lop.modules.repository.CommonDao;
import org.lop.modules.repository.mybatis.BaseModel;
import org.lop.modules.service.template.SimpleExecutor;
import org.lop.modules.utils.InstantiateUtils;

/**
 * 带事务Service基类.
 * 
 * @author 潘瑞峥
 * @date 2014年12月9日
 */
public class TransactionService extends SimpleService {

	/** 使用事务模版, 不使用注解方式. */
	@Autowired
	protected TransactionTemplate transactionTemplate;

	@Autowired
	protected CommonDao commonDao;

	/**
	 * 业务简单处理模版方法骨架.
	 * 
	 * 开启事务.
	 */
	@SuppressWarnings( { "rawtypes", "unchecked" } )
	protected final <R extends BaseResponseDTO> R serviceTransaction( BaseEnum action, BaseRequestDTO request, Class<R> responseClass,
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

			// 4.创建简单的事务模板, 进行原子事务处理.
			SimpleTransactionCallback transactionCallback = new SimpleTransactionCallback( response, simpleExecutor );
			this.transactionTemplate.execute( transactionCallback );

		} catch ( IllegalArgumentException e ) {
			ResponseCodeEnums responseCode = ResponseCodeEnums.ILLEGAL_ARGUMENT_EXCEPTION;

			response.setExecuted( Boolean.FALSE );
			response.setResponseCode( responseCode );
			response.setMessage( e.getMessage() );

			logger.error( "request: {}, message: {}:{}", request, responseCode.getDisplayName(), e.getMessage() );

		} catch ( DuplicateKeyException e ) {
			ResponseCodeEnums responseCode = ResponseCodeEnums.DATA_EXIST_ALREADY;

			response.setExecuted( Boolean.FALSE );
			response.setResponseCode( responseCode );
			response.setMessage( responseCode.getDisplayName() );

			logger.error( "request: {}, message: {}:{}", request, responseCode.getDisplayName(), e.getMessage(), e );

		} catch ( DataAccessException e ) {
			ResponseCodeEnums responseCode = ResponseCodeEnums.DATABASE_EXCEPTION;

			response.setExecuted( Boolean.FALSE );
			response.setResponseCode( responseCode );
			response.setMessage( responseCode.getDisplayName() );

			logger.error( "request: {}, message: {}:{}", request, responseCode.getDisplayName(), e.getMessage(), e );

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
	 * 获取数据库日期.
	 */
	protected final Date getCurrentDate() {
		Date currentDate = this.commonDao.getCurrentDate();

		logger.debug( "数据库日期: {}", currentDate );

		return currentDate;
	}

	/**
	 * 创建事务模板, 进行原子事务处理.
	 * 
	 * 简单的事务模版.
	 * 
	 * @author 潘瑞峥
	 * @date 2014年8月4日
	 */
	private class SimpleTransactionCallback implements TransactionCallback<Object> {

		private BaseResponseDTO response;

		private SimpleExecutor<Object> invoker;

		private SimpleTransactionCallback( BaseResponseDTO response, SimpleExecutor<Object> invoker ) {
			this.response = response;
			this.invoker = invoker;
		}

		@Override
		public Object doInTransaction( TransactionStatus status ) {

			Object pojo = null;

			try {
				// 设置成功应答.
				setSuccess( response );
				// 前置处理.
				pojo = invoker.before();
				// 业务处理.
				invoker.process( pojo );
				// 转换.
				invoker.conver( pojo, response );

			} catch ( RuntimeException e ) {
				// 回滚.
				status.setRollbackOnly();

				throw e;
			}

			return pojo;
		}

	}

}