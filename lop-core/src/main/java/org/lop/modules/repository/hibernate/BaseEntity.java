package org.lop.modules.repository.hibernate;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.GenericGenerator;

/**
 * 实体基类.
 * 
 * @author 潘瑞峥
 * @date 2013-5-31
 */
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue( generator = "system-uuid" )
	@GenericGenerator( name = "system-uuid", strategy = "uuid" )
	protected String id;

	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString( this, ToStringStyle.MULTI_LINE_STYLE );
	}

}