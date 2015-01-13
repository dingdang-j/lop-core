package org.lop.modules.facade;

import java.io.Serializable;

import javax.validation.constraints.Size;
import javax.validation.groups.Default;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotBlank;

import org.lop.modules.constants.FacadeConstants;
import org.lop.modules.validate.BeanValidators;
import org.lop.modules.validate.groups.GidGroup;

/**
 * Request DTO基类.
 * 
 * @author 潘瑞峥
 * @date 2014年8月1日
 */
public abstract class BaseRequestDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 全局流水号. */
	@NotBlank( groups = GidGroup.class )
	@Size( min = FacadeConstants.GID_LENGTH, max = FacadeConstants.GID_LENGTH, groups = GidGroup.class )
	private String gid;

	public String getGid() {
		return gid;
	}

	public void setGid( String gid ) {
		this.gid = gid;
	}

	/**
	 * 是否校验gid, 业务场景决定.
	 */
	protected abstract boolean isValidGid();

	/**
	 * 校验RequestDTO.
	 * 
	 * 提供给外部调用.
	 */
	public abstract void valid();

	/**
	 * JSR303校验.
	 */
	protected final void validate( Object object, Class<?>... groups ) {

		if ( 0 == groups.length ) {
			groups = ArrayUtils.add( groups, Default.class );
		}

		// gid.
		if ( isValidGid() ) {
			groups = ArrayUtils.add( groups, GidGroup.class );
		}

		// 校验.
		String errorMessages = BeanValidators.validateWithString( object, groups );

		// 有错误信息则抛出IllegalArgumentException异常.
		if ( StringUtils.isNotBlank( errorMessages ) ) {
			throw new IllegalArgumentException( errorMessages );
		}
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString( this, ToStringStyle.SHORT_PREFIX_STYLE );
	}

}