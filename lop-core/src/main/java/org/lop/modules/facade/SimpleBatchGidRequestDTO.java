package org.lop.modules.facade;

import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * 简单的批量Response DTO, 必传GID.
 * 
 * @author 潘瑞峥
 * @date 2014年9月12日
 */
public class SimpleBatchGidRequestDTO extends BaseRequestDTO {

	private static final long serialVersionUID = 1L;

	/** ids. */
	@NotEmpty
	private List<Long> ids;

	public List<Long> getIds() {
		return ids;
	}

	public void setIds( List<Long> ids ) {
		this.ids = ids;
	}

	/**
	 * @see org.lop.modules.facade.BaseRequestDTO#isValidGid()
	 */
	@Override
	protected boolean isValidGid() {
		return true;
	}

	/**
	 * @see org.lop.modules.facade.BaseRequestDTO#valid()
	 */
	@Override
	public void valid() {
		super.validate( this );
	}

}