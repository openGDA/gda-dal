/**
 * 
 */
package org.csstudio.dal.impl;

/**
 * Defautl implementation of DAL enum object.
 * 
 * @author ikriznar
 *
 */
public final class EnumImpl implements org.csstudio.dal.Enum {
	
	private int index;
	private Object value;
	private String description;
	
	public EnumImpl(int index, Object value, String description) {
		this.index=index;
		this.value=value;
		this.description=description;
	}
	
	@Override
	public String description() {
		return description;
	}
	@Override
	public int index() {
		return index;
	}
	@Override
	public Object value() {
		return value;
	}
	
}