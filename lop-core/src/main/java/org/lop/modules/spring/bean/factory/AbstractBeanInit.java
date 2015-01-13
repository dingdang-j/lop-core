package org.lop.modules.spring.bean.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContextAware;

/**
 * SpringBean加载前处理基类.
 * 
 * @author 潘瑞峥
 * @date 2014年9月17日
 */
public abstract class AbstractBeanInit implements InitializingBean, ApplicationContextAware {

	protected final Logger logger = LoggerFactory.getLogger( this.getClass() );

}