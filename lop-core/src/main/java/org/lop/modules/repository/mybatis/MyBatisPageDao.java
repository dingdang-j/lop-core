package org.lop.modules.repository.mybatis;

import java.util.List;

/**
 * MyBatis Page DAO.
 * 
 * @author 潘瑞峥
 * @date 2014年8月20日
 */
public interface MyBatisPageDao<MODEL extends BaseModel> extends MyBatisCrudDao<MODEL> {

	/**
	 * 统计记录总数.
	 */
	long count( MODEL model );

	/**
	 * 分页查询.
	 */
	List<MODEL> findPage( MODEL model );

}