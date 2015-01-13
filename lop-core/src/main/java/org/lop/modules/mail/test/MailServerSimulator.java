package org.lop.modules.mail.test;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;

/**
 * 基于GreenMail的MailServer模拟器, 用于开发和测试环境.
 * 
 * 默认在localhost的3025端口启动SMTP服务, greenmail@localhost.com:greemail.
 * 
 * @author 潘瑞峥
 * @date 2014年11月28日
 */
public class MailServerSimulator implements InitializingBean, DisposableBean, FactoryBean<GreenMail> {

	public static final int DEFAULT_PORT = 3025;

	public static final String DEFAULT_USERNAME = "greenmail@localhost.com";

	public static final String DEFAULT_PASSWORD = "greenmail";

	private GreenMail greenMail;

	private int port = DEFAULT_PORT;

	private String username = DEFAULT_USERNAME;

	private String password = DEFAULT_PASSWORD;

	@Override
	public void afterPropertiesSet() throws Exception {
		greenMail = new GreenMail( new ServerSetup( port, null, ServerSetup.PROTOCOL_SMTP ) );
		greenMail.setUser( username, password );
		greenMail.start();
	}

	@Override
	public void destroy() throws Exception {
		if ( greenMail != null ) {
			greenMail.stop();
		}
	}

	@Override
	public GreenMail getObject() throws Exception {
		return greenMail;
	}

	@Override
	public Class<?> getObjectType() {
		return GreenMail.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public void setPort( int port ) {
		this.port = port;
	}

	public void setUsername( String username ) {
		this.username = username;
	}

	public void setPassword( String password ) {
		this.password = password;
	}

}