package org.lop.modules.test.jetty;

import org.eclipse.jetty.server.Server;

import org.lop.modules.test.spring.Profiles;

/**
 * Jetty启动类.
 * 
 * @author 潘瑞峥
 * @date 2014年8月15日
 */
public class JettyBootstrap {

	/**
	 * 启动.
	 */
	public static void start( int port, String profile ) throws Exception {
		long beginTime = System.currentTimeMillis();

		// 设定Spring的profile.
		Profiles.setProfileAsSystemProperty( profile );
		System.setProperty( "org.apache.jasper.compiler.disablejsr199", "true" );

		Server server = JettyFactory.createServerInSource( port, "" );

		try {
			server.start();

			long endTime = System.currentTimeMillis();

			System.err.println( "[INFO] Server running at http://localhost:" + port );
			System.err.println( "[INFO] Server startup in " + ( endTime - beginTime ) + "ms" );
			System.err.println( "[HINT] 回车快速重新启动应用程序" );

			// 等待用户输入回车重载应用.
			while ( true ) {
				char c = ( char ) System.in.read();
				if ( c == '\n' ) {
					beginTime = System.currentTimeMillis();

					// 重启.
					JettyFactory.reloadContext( server );

					endTime = System.currentTimeMillis();

					System.err.println( "[INFO] Server running at http://localhost:" + port );
					System.err.println( "[INFO] Server restart in " + ( endTime - beginTime ) + "ms" );
					System.err.println( "[HINT] 回车快速重新启动应用程序" );
				}
			}
		} catch ( Exception e ) {
			e.printStackTrace();
			System.exit( -1 );
		}
	}

}