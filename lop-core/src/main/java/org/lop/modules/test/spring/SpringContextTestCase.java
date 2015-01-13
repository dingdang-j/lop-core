package org.lop.modules.test.spring;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import org.lop.modules.facade.BaseResponseDTO;

/**
 * Spring的支持依赖注入的JUnit4集成测试基类, 相比Spring原基类名字更短.
 * 
 * 子类需要定义applicationContext文件的位置, 如:
 * 
 * @ActiveProfiles( "test" )
 * @ContextConfiguration(locations = { "/applicationContext-test.xml" })
 * 
 * @author 潘瑞峥
 * @date 2014年7月29日
 */
public abstract class SpringContextTestCase extends AbstractJUnit4SpringContextTests {

	protected Logger logger = LoggerFactory.getLogger( this.getClass() );

	public static final String PREFIX = ">>>>>>>>>>";

	protected BaseResponseDTO response;

	/**
	 * 生成gid.
	 */
	protected String getGid() {
		String gid = RandomStringUtils.randomNumeric( 36 );

		logger.info( "{}gid: {}", PREFIX, gid );

		return gid;
	}

	protected void printResponse() {

		logger.info( "{}response: {}", PREFIX, ToStringBuilder.reflectionToString( response, ToStringStyle.MULTI_LINE_STYLE ) );
	}

}