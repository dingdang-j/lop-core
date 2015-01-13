package org.lop.modules.facade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * 分页响应DTO.
 * 
 * @author 潘瑞峥
 * @date 2014年8月19日
 */
public class PageResponseDTO<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 记录集. */
	private List<T> content = new ArrayList<T>();

	/** 分页请求. */
	private PageRequestDTO pageRequest;

	/** 总记录数. */
	private long total;

	public PageResponseDTO() {
	}

	public PageResponseDTO( List<T> content, PageRequestDTO pageRequest, long total ) {
		this.content = content;
		this.pageRequest = pageRequest;
		this.total = total;
	}

	public int getNumber() {
		return pageRequest == null ? 0 : pageRequest.getPage();
	}

	public int getSize() {
		return pageRequest == null ? 0 : pageRequest.getSize();
	}

	public int getNumberOfElements() {
		return content.size();
	}

	public boolean hasPrevious() {
		return getNumber() > 0;
	}

	public boolean isFirst() {
		return !hasPrevious();
	}

	public PageRequestDTO nextPageable() {
		return hasNext() ? pageRequest.next() : null;
	}

	public PageRequestDTO previousPageable() {

		if ( hasPrevious() ) {
			return pageRequest.previousOrFirst();
		}

		return null;
	}

	public boolean hasContent() {
		return !content.isEmpty();
	}

	public List<T> getContent() {
		return Collections.unmodifiableList( content );
	}

	// public Sort getSort() {
	// return pageRequest == null ? null : pageRequest.getSort();
	// }

	public Iterator<T> iterator() {
		return content.iterator();
	}

	public int getTotalPages() {
		return getSize() == 0 ? 1 : ( int ) Math.ceil( ( double ) total / ( double ) getSize() );
	}

	public long getTotalElements() {
		return total;
	}

	public boolean hasPreviousPage() {
		return hasPrevious();
	}

	public boolean isFirstPage() {
		return isFirst();
	}

	public boolean hasNext() {
		return hasNextPage();
	}

	public boolean hasNextPage() {
		return getNumber() + 1 < getTotalPages();
	}

	public boolean isLast() {
		return isLastPage();
	}

	public boolean isLastPage() {
		return !hasNextPage();
	}

	public PageRequestDTO getPageRequest() {
		return pageRequest;
	}

	public void setPageRequest( PageRequestDTO pageRequest ) {
		this.pageRequest = pageRequest;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal( long total ) {
		this.total = total;
	}

	public void setContent( List<T> content ) {
		this.content = content;
	}

	@Override
	public String toString() {
		return String.format( "Page %s of %d. totalPages: %d, totalElements: %d", getNumber(), getTotal(), getTotalPages(),
				getTotalElements() );
	}

}