package org.lop.modules.facade;

import java.io.Serializable;

import org.lop.commons.constants.LopConstants;

/**
 * 分页请求DTO.
 * 
 * @author 潘瑞峥
 * @date 2014年8月19日
 */
public class PageRequestDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 起始页. */
	private int page;

	/** 每页记录数. */
	private int size;

	public PageRequestDTO() {
		this( LopConstants.PAGE_DEFAULT_BEGIN, LopConstants.PAGE_DEFAULT_SIZE );
	}

	public PageRequestDTO( int page, int size ) {

		if ( 0 > page ) {
			page = LopConstants.PAGE_DEFAULT_BEGIN;
		}

		if ( 1 > size ) {
			size = LopConstants.PAGE_DEFAULT_SIZE;
		}

		this.page = page;
		this.size = size;
	}

	public int getPageSize() {
		return size;
	}

	public int getPageNumber() {
		return page;
	}

	public int getOffset() {
		return page * size;
	}

	public boolean hasPrevious() {
		return page > 0;
	}

	public PageRequestDTO previousOrFirst() {
		return hasPrevious() ? previous() : first();
	}

	public PageRequestDTO next() {
		return new PageRequestDTO( getPageNumber() + 1, getPageSize() );
	}

	public PageRequestDTO previous() {
		return getPageNumber() == 0 ? this : new PageRequestDTO( getPageNumber() - 1, getPageSize() );
	}

	public PageRequestDTO first() {
		return new PageRequestDTO( 0, getPageSize() );
	}

	public int getPage() {
		if ( 0 > page ) {
			page = LopConstants.PAGE_DEFAULT_BEGIN;
		}
		return page;
	}

	public void setPage( int page ) {
		if ( 0 > page ) {
			page = LopConstants.PAGE_DEFAULT_BEGIN;
		}
		this.page = page;
	}

	public int getSize() {
		if ( 1 > size ) {
			size = LopConstants.PAGE_DEFAULT_SIZE;
		}
		return size;
	}

	public void setSize( int size ) {
		if ( 1 > size ) {
			size = LopConstants.PAGE_DEFAULT_SIZE;
		}
		this.size = size;
	}

	@Override
	public String toString() {
		return String.format( "Page request [number: %d, size: %d]", getPageNumber(), getPageSize() );
	}

}