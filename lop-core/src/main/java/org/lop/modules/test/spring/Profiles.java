package org.lop.modules.test.spring;

import org.springframework.core.env.AbstractEnvironment;

/**
 * Spring profile常用方法与profile名称.
 * 
 * @author 潘瑞峥
 * @date 2014年7月29日
 */
public class Profiles {

	public static final String ACTIVE_PROFILE = AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME;
	public static final String DEFAULT_PROFILE = AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME;

	public static final String PRODUCTION = "production";
	public static final String DEVELOPMENT = "development";
	public static final String UNIT_TEST = "test";
	public static final String FUNCTIONAL_TEST = "functional";

	/**
	 * 在Spring启动前, 设置profile的环境变量.
	 */
	public static void setProfileAsSystemProperty( String profile ) {
		System.setProperty( ACTIVE_PROFILE, profile );
	}

}