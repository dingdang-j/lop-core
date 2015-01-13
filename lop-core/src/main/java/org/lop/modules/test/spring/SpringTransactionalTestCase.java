package org.lop.modules.test.spring;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

/**
 * Spring的支持数据库访问, 事务控制和依赖注入的JUnit4 集成测试基类.
 * 
 * 相比Spring原基类名字更短并保存了dataSource变量.
 * 
 * 子类需要定义applicationContext文件的位置, 如:
 * 
 * @ActiveProfiles( "test" )
 * @ContextConfiguration(locations = { "/applicationContext.xml" })
 * 
 * @author 潘瑞峥
 * @date 2014年7月29日
 */
@ActiveProfiles( Profiles.UNIT_TEST )
public abstract class SpringTransactionalTestCase extends AbstractTransactionalJUnit4SpringContextTests {

	protected Logger logger = LoggerFactory.getLogger( this.getClass() );

	protected DataSource dataSource;

	@Override
	@Autowired
	public void setDataSource( DataSource dataSource ) {
		super.setDataSource( dataSource );
		this.dataSource = dataSource;
	}

}