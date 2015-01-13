package org.lop.modules.facade;

/**
 * 空参的Response DTO.
 * 
 * @author 潘瑞峥
 * @date 2014年10月14日
 */
public class NullRequestDTO extends BaseRequestDTO {

	private static final long serialVersionUID = 1L;

	@Override
	protected boolean isValidGid() {
		return false;
	}

	@Override
	public void valid() {
	}

}