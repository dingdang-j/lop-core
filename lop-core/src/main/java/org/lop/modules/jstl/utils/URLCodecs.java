package org.lop.modules.jstl.utils;

import org.apache.commons.codec.net.URLCodec;

/**
 * 编码、解码工具.
 * 
 * @author 潘瑞峥
 * @date 2014年11月12日
 */
public class URLCodecs {

	private static URLCodec urlCodec = new URLCodec();

	/**
	 * url编码.
	 */
	public static String encode( final String source ) throws Exception {

		return urlCodec.encode( source );
	}

	/**
	 * url编码两次.
	 */
	public static String encode2( final String source ) throws Exception {

		return encode( encode( source ) );
	}

	/**
	 * url解码.
	 */
	public static String decode( final String source ) throws Exception {

		return urlCodec.decode( source );
	}

	/**
	 * url解码两次.
	 */
	public static String decode2( final String source ) throws Exception {

		return decode( decode( source ) );
	}

}