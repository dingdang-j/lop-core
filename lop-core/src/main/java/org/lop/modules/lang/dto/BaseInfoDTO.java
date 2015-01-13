package org.lop.modules.lang.dto;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.lop.commons.constants.LopConstants;
import org.lop.commons.enums.BooleanEnums;
import org.lop.modules.validate.groups.IdentityGroup;

/**
 * DTO基类.
 * 
 * 考虑到DTO兼容, 统一设置serialVersionUID = 1L.
 * 
 * @author 潘瑞峥
 * @date 2014年8月7日
 */
public abstract class BaseInfoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 主键. */
	@NotNull( groups = IdentityGroup.class )
	private Long id;

	/** 是否删除. */
	private BooleanEnums isDeleted;

	/** 数据新增时间. */
	@JsonFormat( pattern = LopConstants.JSON_DATETIME_FORMAT, timezone = LopConstants.JSON_TIME_TIMEZONE )
	private Date gmtCreate;

	/** 数据修改时间. */
	@JsonFormat( pattern = LopConstants.JSON_DATETIME_FORMAT, timezone = LopConstants.JSON_TIME_TIMEZONE )
	private Date gmtModify;

	public Long getId() {
		return id;
	}

	public void setId( Long id ) {
		this.id = id;
	}

	public BooleanEnums getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted( BooleanEnums isDeleted ) {
		this.isDeleted = isDeleted;
	}

	public Date getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate( Date gmtCreate ) {
		this.gmtCreate = gmtCreate;
	}

	public Date getGmtModify() {
		return gmtModify;
	}

	public void setGmtModify( Date gmtModify ) {
		this.gmtModify = gmtModify;
	}

}