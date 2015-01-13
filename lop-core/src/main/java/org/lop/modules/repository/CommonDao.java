package org.lop.modules.repository;

import java.util.Date;

/**
 * 公共DAO.
 * 
 * @author 潘瑞峥
 * @date 2014年8月12日
 */
public interface CommonDao {

	/**
	 * 获取数据库当前时间.
	 */
	Date getCurrentDate();

	/**
	 * 获取序列下一个值.
	 */
	long nextval( String seqName );

	/**
	 * 获取序列下一个值, 并给定起始值.
	 */
	long nextval( String seqName, Long start );

}