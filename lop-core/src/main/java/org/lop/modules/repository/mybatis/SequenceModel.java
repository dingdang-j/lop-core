package org.lop.modules.repository.mybatis;

import java.io.Serializable;

/**
 * 用于MyBatis在selectKey时, 必须将返回值复制给对象的属性.
 * 
 * @author 潘瑞峥
 * @date 2014年8月12日
 */
public class SequenceModel implements Serializable {

	private static final long serialVersionUID = 2091055563188534576L;

	/** 序列名称. */
	private String name;

	/** 起始值. */
	private Long start;

	/** 序列值. */
	private long sequence;

	public String getName() {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}

	public Long getStart() {
		return start;
	}

	public void setStart( Long start ) {
		this.start = start;
	}

	public long getSequence() {
		return sequence;
	}

	public void setSequence( long sequence ) {
		this.sequence = sequence;
	}

	@Override
	public String toString() {
		return "SequenceModel [name=" + name + ", start=" + start + ", sequence=" + sequence + "]";
	}

}