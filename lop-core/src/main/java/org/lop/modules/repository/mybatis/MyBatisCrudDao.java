package org.lop.modules.repository.mybatis;

/**
 * MyBatis CRUD DAO.
 * 
 * @author 潘瑞峥
 * @date 2014年8月7日
 */
public interface MyBatisCrudDao<MODEL extends BaseModel> {

	/**
	 * 根据id查询.
	 */
	MODEL find( Long id );

	/**
	 * 新增.
	 */
	int create( MODEL model );

	/**
	 * 修改.
	 */
	int update( MODEL model );

	/**
	 * 局部修改属性.
	 * 
	 * 若Model属性不为null, 则修改该属性; null则反之.
	 */
	int updateProperties( MODEL model );

	/**
	 * 删除.
	 */
	int delete( Long id );

	/**
	 * 批量修改.
	 * 
	 * 只能将满足model.ids的记录全部修改成model里的值.
	 */
	int batchUpdateProperties( MODEL model );

}