package org.lop.modules.facade;

import javax.validation.constraints.NotNull;

/**
 * 简单的Response DTO, 不必传GID.
 * 
 * @author 潘瑞峥
 * @date 2014年8月9日
 */
public class SimpleNoGidRequestDTO extends BaseRequestDTO {

	private static final long serialVersionUID = 1L;

	/** id. */
	@NotNull
	private Long id;

	public SimpleNoGidRequestDTO() {
		super();
	}

	public SimpleNoGidRequestDTO( Long id ) {
		super();
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId( Long id ) {
		this.id = id;
	}

	/**
	 * @see org.lop.modules.facade.BaseRequestDTO#isValidGid()
	 */
	@Override
	protected boolean isValidGid() {
		return false;
	}

	/**
	 * @see org.lop.modules.facade.BaseRequestDTO#valid()
	 */
	@Override
	public void valid() {
		super.validate( this );
	}

}