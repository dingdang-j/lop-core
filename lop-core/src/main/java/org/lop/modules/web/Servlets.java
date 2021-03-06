package org.lop.modules.web;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Charsets;
import com.google.common.net.HttpHeaders;
import org.lop.modules.utils.Collections3;
import org.lop.modules.utils.Encodes;

/**
 * Http与Servlet工具类.
 * 
 * @author 潘瑞峥
 * @date 2014年7月29日
 */
public class Servlets {

	/** Content Type. */
	public static final String TEXT_TYPE = "text/plain";
	public static final String JSON_TYPE = "application/json";
	public static final String XML_TYPE = "text/xml";
	public static final String HTML_TYPE = "text/html";
	public static final String JS_TYPE = "text/javascript";
	public static final String EXCEL_TYPE = "application/vnd.ms-excel";

	public static final long ONE_YEAR_SECONDS = 60 * 60 * 24 * 365;

	/**
	 * 设置客户端缓存过期时间 的Header.
	 */
	public static void setExpiresHeader( HttpServletResponse response, long expiresSeconds ) {
		// Http 1.0 header, set a fix expires date.
		response.setDateHeader( HttpHeaders.EXPIRES, System.currentTimeMillis() + ( expiresSeconds * 1000 ) );
		// Http 1.1 header, set a time after now.
		response.setHeader( HttpHeaders.CACHE_CONTROL, "private, max-age=" + expiresSeconds );
	}

	/**
	 * 设置禁止客户端缓存的Header.
	 */
	public static void setNoCacheHeader( HttpServletResponse response ) {
		// Http 1.0 header
		response.setDateHeader( HttpHeaders.EXPIRES, 1L );
		response.addHeader( HttpHeaders.PRAGMA, "no-cache" );
		// Http 1.1 header
		response.setHeader( HttpHeaders.CACHE_CONTROL, "no-cache, no-store, max-age=0" );
	}

	/**
	 * 设置LastModified Header.
	 */
	public static void setLastModifiedHeader( HttpServletResponse response, long lastModifiedDate ) {
		response.setDateHeader( HttpHeaders.LAST_MODIFIED, lastModifiedDate );
	}

	/**
	 * 设置Etag Header.
	 */
	public static void setEtag( HttpServletResponse response, String etag ) {
		response.setHeader( HttpHeaders.ETAG, etag );
	}

	/**
	 * 根据浏览器If-Modified-Since Header, 计算文件是否已被修改.
	 * 
	 * 如果无修改, checkIfModify返回false, 设置304 not modify status.
	 * 
	 * @param lastModified
	 *            内容的最后修改时间.
	 */
	public static boolean checkIfModifiedSince( HttpServletRequest request, HttpServletResponse response, long lastModified ) {
		long ifModifiedSince = request.getDateHeader( HttpHeaders.IF_MODIFIED_SINCE );
		if ( ( ifModifiedSince != -1 ) && ( lastModified < ( ifModifiedSince + 1000 ) ) ) {
			response.setStatus( HttpServletResponse.SC_NOT_MODIFIED );
			return false;
		}
		return true;
	}

	/**
	 * 根据浏览器 If-None-Match Header, 计算Etag是否已无效.
	 * 
	 * 如果Etag有效, checkIfNoneMatch返回false, 设置304 not modify status.
	 * 
	 * @param etag
	 *            内容的ETag.
	 */
	public static boolean checkIfNoneMatchEtag( HttpServletRequest request, HttpServletResponse response, String etag ) {
		String headerValue = request.getHeader( HttpHeaders.IF_NONE_MATCH );
		if ( headerValue != null ) {
			boolean conditionSatisfied = false;
			if ( !"*".equals( headerValue ) ) {
				StringTokenizer commaTokenizer = new StringTokenizer( headerValue, "," );

				while ( !conditionSatisfied && commaTokenizer.hasMoreTokens() ) {
					String currentToken = commaTokenizer.nextToken();
					if ( currentToken.trim().equals( etag ) ) {
						conditionSatisfied = true;
					}
				}
			} else {
				conditionSatisfied = true;
			}

			if ( conditionSatisfied ) {
				response.setStatus( HttpServletResponse.SC_NOT_MODIFIED );
				response.setHeader( HttpHeaders.ETAG, etag );
				return false;
			}
		}
		return true;
	}

	/**
	 * 设置让浏览器弹出下载对话框的Header.
	 * 
	 * @param fileName
	 *            下载后的文件名.
	 */
	public static void setFileDownloadHeader( HttpServletResponse response, String fileName ) {
		// 中文文件名支持
		String encodedfileName = new String( fileName.getBytes(), Charsets.ISO_8859_1 );
		response.setHeader( HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedfileName + "\"" );

	}

	/**
	 * 组合Parameters生成Query String的Parameter部分, 并在paramter name上加上prefix.
	 * 
	 * @see #getParametersStartingWith
	 */
	public static String encodeParameterStringWithPrefix( Map<String, Object> params, String prefix ) {
		if ( Collections3.isEmpty( params ) ) {
			return "";
		}

		if ( prefix == null ) {
			prefix = "";
		}

		StringBuilder queryStringBuilder = new StringBuilder();
		Iterator<Entry<String, Object>> it = params.entrySet().iterator();
		while ( it.hasNext() ) {
			Entry<String, Object> entry = it.next();

			if ( entry.getValue() instanceof String ) {
				String value = ( String ) entry.getValue();
				// 如果value值为空或空字符串或空格, 则忽略.
				if ( StringUtils.isNotBlank( value ) ) {

					queryStringBuilder.append( prefix ).append( entry.getKey() ).append( '=' ).append( entry.getValue() );
					if ( it.hasNext() ) {
						queryStringBuilder.append( '&' );
					}
				}
			}
		}

		return queryStringBuilder.toString();
	}

	/**
	 * 客户端对Http Basic验证的 Header进行编码.
	 */
	public static String encodeHttpBasic( String userName, String password ) {
		String encode = userName + ":" + password;
		return "Basic " + Encodes.encodeBase64( encode.getBytes() );
	}

}