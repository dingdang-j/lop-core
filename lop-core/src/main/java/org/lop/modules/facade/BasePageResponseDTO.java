package org.lop.modules.facade;

/**
 * Response page DTO基类.
 * 
 * 需要分页的继承该类.
 * 
 * @author 潘瑞峥
 * @date 2014年8月18日
 */
public class BasePageResponseDTO<DTO> extends BaseResponseDTO {

	private static final long serialVersionUID = 1L;

	/** 分页对象. */
	private PageResponseDTO<DTO> page;

	public BasePageResponseDTO() {
		super();
	}

	public BasePageResponseDTO( PageResponseDTO<DTO> page ) {
		super();
		this.page = page;
	}

	public PageResponseDTO<DTO> getPage() {
		return page;
	}

	public void setPage( PageResponseDTO<DTO> page ) {
		this.page = page;
	}

}