package org.lop.modules.facade;

import javax.validation.constraints.NotNull;

/**
 * Request page DTO基类.
 * 
 * @author 潘瑞峥
 * @date 2014年8月18日
 */
public class BasePageRequestDTO extends BaseRequestDTO {

	private static final long serialVersionUID = 1L;

	/** 分页. */
	@NotNull
	private PageRequestDTO pageable;

	public BasePageRequestDTO() {
		super();
	}

	public BasePageRequestDTO( PageRequestDTO pageable ) {
		super();
		this.pageable = pageable;
	}

	public PageRequestDTO getPageable() {
		return pageable;
	}

	public void setPageable( PageRequestDTO pageable ) {
		this.pageable = pageable;
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