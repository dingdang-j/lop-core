package org.lop.modules.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * 为Response设置客户端缓存控制Header的Filter.
 * 
 * eg.在web.xml中设置
 * 
 * <pre>
 * 	&lt;filter&gt;
 * 		&lt;filter-name&gt;cacheControlHeaderFilter&lt;/filter-name&gt;
 * 		&lt;filter-class&gt;org.lop.modules.web.CacheControlHeaderFilter&lt;/filter-class&gt;
 * 		&lt;init-param&gt;
 * 			&lt;param-name&gt;expiresSeconds&lt;/param-name&gt;
 * 			&lt;param-value&gt;31536000&lt;/param-value&gt;
 * 		&lt;/init-param&gt;
 * 	&lt;/filter&gt;
 * 	&lt;filter-mapping&gt;
 * 		&lt;filter-name&gt;cacheControlHeaderFilter&lt;/filter-name&gt;
 * 		&lt;url-pattern&gt;/images/*&lt;/url-pattern&gt;
 * 	&lt;/filter-mapping&gt;
 * </pre>
 * 
 * @author 潘瑞峥
 * @date 2014年11月21日
 */
public class CacheControlHeaderFilter implements Filter {

	private static final String PARAM_EXPIRES_SECONDS = "expiresSeconds";
	private long expiresSeconds;

	@Override
	public void doFilter( ServletRequest req, ServletResponse res, FilterChain chain ) throws IOException, ServletException {
		Servlets.setExpiresHeader( ( HttpServletResponse ) res, expiresSeconds );
		chain.doFilter( req, res );
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	@Override
	public void init( FilterConfig filterConfig ) {
		String expiresSecondsParam = filterConfig.getInitParameter( PARAM_EXPIRES_SECONDS );
		if ( expiresSecondsParam != null ) {
			expiresSeconds = Long.valueOf( expiresSecondsParam );
		} else {
			expiresSeconds = Servlets.ONE_YEAR_SECONDS;
		}
	}

	/**
	 * @see Filter#destroy()
	 */
	@Override
	public void destroy() {
	}

}