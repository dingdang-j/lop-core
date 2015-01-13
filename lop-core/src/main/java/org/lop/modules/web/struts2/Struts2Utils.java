package org.lop.modules.web.struts2;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;

import org.lop.modules.mapper.JsonMapper;
import org.lop.modules.web.Servlets;

/**
 * Struts2工具.
 * 
 * @author 潘瑞峥
 * @date 2013-6-3
 */
public class Struts2Utils {

	private static final String HEADER_ENCODING = "encoding";
	private static final String HEADER_NOCACHE = "no-cache";
	private static final String DEFAULT_ENCODING = "UTF-8";
	private static final boolean DEFAULT_NOCACHE = true;

	private static JsonMapper mapper = JsonMapper.nonEmptyMapper();

	public static HttpSession getSession() {
		return ServletActionContext.getRequest().getSession();
	}

	public static HttpSession getSession( boolean isNew ) {
		return ServletActionContext.getRequest().getSession( isNew );
	}

	public static Object getSessionAttribute( String name ) {
		HttpSession session = getSession( false );
		return ( null != session ? session.getAttribute( name ) : null );
	}

	public static HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}

	public static HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}

	public static String getParameter( String name ) {
		return getRequest().getParameter( name );
	}

	/**
	 * 直接输出内容的简便函数.
	 * 
	 * eg. render("text/plain", "hello", "encoding:GBK");<br>
	 * render("text/plain", "hello", "no-cache:false"); <br>
	 * render("text/plain", "hello", "encoding:GBK", "no-cache:false").
	 * 
	 * @param headers
	 *            可变的header数组, 目前接受的值为"encoding:"或"no-cache:", 默认值分别为UTF-8和true.
	 */
	public static void render( final String contentType, final String content, final String... headers ) {
		HttpServletResponse response = initResponseHeader( contentType, headers );
		try {
			response.getWriter().write( content );
			response.getWriter().flush();
		} catch ( IOException e ) {
			throw new RuntimeException( e.getMessage(), e );
		}
	}

	/**
	 * 直接输出文本.
	 * 
	 * @see #render(String, String, String...)
	 */
	public static void renderText( final String text, final String... headers ) {
		render( Servlets.TEXT_TYPE, text, headers );
	}

	/**
	 * 直接输出HTML.
	 * 
	 * @see #render(String, String, String...)
	 */
	public static void renderHtml( final String html, final String... headers ) {
		render( Servlets.HTML_TYPE, html, headers );
	}

	/**
	 * 直接输出XML.
	 * 
	 * @see #render(String, String, String...)
	 */
	public static void renderXml( final String xml, final String... headers ) {
		render( Servlets.XML_TYPE, xml, headers );
	}

	/**
	 * 直接输出JSON.
	 * 
	 * @param jsonString
	 *            json字符串.
	 * @see #render(String, String, String...)
	 */
	public static void renderJson( final String jsonString, final String... headers ) {
		render( Servlets.JSON_TYPE, jsonString, headers );
	}

	/**
	 * 直接输出JSON, 使用Jackson转换Java对象.
	 * 
	 * @param data
	 *            可以是List<POJO>, POJO[], POJO, 也可以Map名值对.
	 * @see #render(String, String, String...)
	 */
	public static void renderJson( final Object data, final String... headers ) {
		HttpServletResponse response = initResponseHeader( Servlets.JSON_TYPE, headers );
		try {
			mapper.getMapper().writeValue( response.getWriter(), data );
		} catch ( IOException e ) {
			throw new IllegalArgumentException( e );
		}
	}

	/**
	 * 直接输出支持跨域Mashup的JSONP.
	 * 
	 * @param callbackName
	 *            callback函数名.
	 * @param object
	 *            Java对象, 可以是List<POJO>, POJO[], POJO, 也可以Map名值对, 将被转化为json字符串.
	 */
	public static void renderJsonp( final String callbackName, final Object object, final String... headers ) {
		String jsonString = mapper.toJson( object );

		String result = new StringBuilder().append( callbackName ).append( "(" ).append( jsonString ).append( ");" ).toString();

		// 渲染Content-Type为javascript的返回内容,输出结果为javascript语句,
		// 如callback197("{html:'Hello World!!!'}");
		render( Servlets.JS_TYPE, result, headers );
	}

	/**
	 * 分析并设置contentType与headers.
	 */
	private static HttpServletResponse initResponseHeader( final String contentType, final String... headers ) {
		// 分析headers参数.
		String encoding = DEFAULT_ENCODING;
		boolean noCache = DEFAULT_NOCACHE;
		for ( String header : headers ) {
			String headerName = StringUtils.substringBefore( header, ":" );
			String headerValue = StringUtils.substringAfter( header, ":" );

			if ( StringUtils.equalsIgnoreCase( headerName, HEADER_ENCODING ) ) {
				encoding = headerValue;
			} else if ( StringUtils.equalsIgnoreCase( headerName, HEADER_NOCACHE ) ) {
				noCache = Boolean.parseBoolean( headerValue );
			} else {
				throw new IllegalArgumentException( headerName + "不是一个合法的header类型" );
			}
		}

		HttpServletResponse response = getResponse();

		// 设置headers参数.
		String fullContentType = contentType + ";charset=" + encoding;
		response.setContentType( fullContentType );
		if ( noCache ) {
			Servlets.setNoCacheHeader( response );
		}

		return response;
	}

}