package org.lop.modules.utils;

import java.util.Date;

import org.apache.commons.lang3.Validate;

/**
 * 时间工具.
 * 
 * @author 潘瑞峥
 * @date 2013-6-2
 */
public class DateUtils {

	/**
	 * 返回参数日期的当天最后一秒钟.
	 * 
	 * 2013-06-01 12:27:43 -> 2013-06-01 23:59:59.
	 */
	public static Date getEndDatetime( Date date ) {
		Validate.notNull( date, "date不能为空" );

		date = org.apache.commons.lang3.time.DateUtils.setHours( date, 0 );
		date = org.apache.commons.lang3.time.DateUtils.setMinutes( date, 0 );
		date = org.apache.commons.lang3.time.DateUtils.setSeconds( date, 0 );
		date = org.apache.commons.lang3.time.DateUtils.setMilliseconds( date, 0 );

		date = org.apache.commons.lang3.time.DateUtils.addDays( date, 1 );
		date = org.apache.commons.lang3.time.DateUtils.addSeconds( date, -1 );

		return date;
	}

}