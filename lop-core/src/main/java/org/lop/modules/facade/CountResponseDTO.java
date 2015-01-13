package org.lop.modules.facade;

/**
 * 统计总记录数响应DTO.
 * 
 * @author 潘瑞峥
 * @date 2014年9月6日
 */
public class CountResponseDTO extends BaseResponseDTO {

	private static final long serialVersionUID = 1L;

	/** 记录总数. */
	private long total;

	public long getTotal() {
		return total;
	}

	public void setTotal( long total ) {
		this.total = total;
	}

}