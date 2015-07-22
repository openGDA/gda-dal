/*
 * Copyright (c) 2006 Stiftung Deutsches Elektronen-Synchroton,
 * Member of the Helmholtz Association, (DESY), HAMBURG, GERMANY.
 *
 * THIS SOFTWARE IS PROVIDED UNDER THIS LICENSE ON AN "../AS IS" BASIS.
 * WITHOUT WARRANTY OF ANY KIND, EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR PARTICULAR PURPOSE AND
 * NON-INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR
 * THE USE OR OTHER DEALINGS IN THE SOFTWARE. SHOULD THE SOFTWARE PROVE DEFECTIVE
 * IN ANY RESPECT, THE USER ASSUMES THE COST OF ANY NECESSARY SERVICING, REPAIR OR
 * CORRECTION. THIS DISCLAIMER OF WARRANTY CONSTITUTES AN ESSENTIAL PART OF THIS LICENSE.
 * NO USE OF ANY SOFTWARE IS AUTHORIZED HEREUNDER EXCEPT UNDER THIS DISCLAIMER.
 * DESY HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS,
 * OR MODIFICATIONS.
 * THE FULL LICENSE SPECIFYING FOR THE SOFTWARE THE REDISTRIBUTION, MODIFICATION,
 * USAGE AND OTHER RIGHTS AND OBLIGATIONS IS INCLUDED WITH THE DISTRIBUTION OF THIS
 * PROJECT IN THE FILE LICENSE.HTML. IF THE LICENSE IS NOT INCLUDED YOU MAY FIND A COPY
 * AT HTTP://WWW.DESY.DE/LEGAL/LICENSE.HTM
 */

/**
 *
 */
package org.epics.css.dal.directory;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.OperationNotSupportedException;
import javax.naming.directory.Attribute;
import javax.naming.directory.DirContext;


/**
 * Simple implementation of Naming Attribute with single value.
 *
 * @author ikriznar
 */
public class SingleValueAttribute implements Attribute
{
	private static final long serialVersionUID = -4819918493510359133L;
	private String id;
	private Object value;

	/**
	 * Creates a SingleValueAttribute object.

	 * @param id    Attribute name
	 * @param value    Attribute value
	 */
	public SingleValueAttribute(String id, Object value)
	{
		super();
		this.id = id;
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see javax.naming.directory.Attribute#size()
	 */
	@Override
	public int size()
	{
		return 1;
	}

	/* (non-Javadoc)
	 * @see javax.naming.directory.Attribute#clear()
	 */
	@Override
	public void clear()
	{
		value = null;
	}

	/* (non-Javadoc)
	 * @see javax.naming.directory.Attribute#isOrdered()
	 */
	@Override
	public boolean isOrdered()
	{
		return true;
	}

	/* (non-Javadoc)
	 * @see javax.naming.directory.Attribute#get()
	 */
	@Override
	public Object get() throws NamingException
	{
		return value;
	}

	/* (non-Javadoc)
	 * @see javax.naming.directory.Attribute#get(int)
	 */
	@Override
	public Object get(int ix) throws NamingException
	{
		if (ix > 0) {
			throw new IllegalArgumentException();
		}

		return value;
	}

	/* (non-Javadoc)
	 * @see javax.naming.directory.Attribute#remove(int)
	 */
	@Override
	public Object remove(int ix)
	{
		if (ix > 0) {
			throw new IllegalArgumentException();
		}

		Object o = value;
		value = null;

		return o;
	}

	/* (non-Javadoc)
	 * @see javax.naming.directory.Attribute#add(int, java.lang.Object)
	 */
	@Override
	public void add(int ix, Object attrVal)
	{
		if (ix > 0) {
			throw new IllegalArgumentException();
		}

		value = attrVal;
	}

	/* (non-Javadoc)
	 * @see javax.naming.directory.Attribute#add(java.lang.Object)
	 */
	@Override
	public boolean add(Object attrVal)
	{
		if (value == null) {
			value = attrVal;

			return true;
		}

		return false;
	}

	/* (non-Javadoc)
	 * @see javax.naming.directory.Attribute#contains(java.lang.Object)
	 */
	@Override
	public boolean contains(Object attrVal)
	{
		return attrVal == value;
	}

	/* (non-Javadoc)
	 * @see javax.naming.directory.Attribute#remove(java.lang.Object)
	 */
	@Override
	public boolean remove(Object attrval)
	{
		if (attrval == value) {
			value = null;

			return true;
		}

		return false;
	}

	/* (non-Javadoc)
	 * @see javax.naming.directory.Attribute#getID()
	 */
	@Override
	public String getID()
	{
		return id;
	}

	/* (non-Javadoc)
	 * @see javax.naming.directory.Attribute#getAll()
	 */
	@Override
	public NamingEnumeration<?> getAll() throws NamingException
	{
		return new NamingEnumeration() {
				Object val = value;

				@Override
				public Object nextElement()
				{
					Object o = val;
					val = null;

					return o;
				}

				@Override
				public boolean hasMoreElements()
				{
					return val != null;
				}

				@Override
				public void close() throws NamingException
				{
					val = null;
				}

				@Override
				public boolean hasMore() throws NamingException
				{
					return val != null;
				}

				@Override
				public Object next() throws NamingException
				{
					Object o = val;
					val = null;

					return o;
				}
			};
	}

	/* (non-Javadoc)
	 * @see javax.naming.directory.Attribute#getAttributeDefinition()
	 */
	@Override
	public DirContext getAttributeDefinition() throws NamingException
	{
		throw new OperationNotSupportedException();
	}

	/* (non-Javadoc)
	 * @see javax.naming.directory.Attribute#getAttributeSyntaxDefinition()
	 */
	@Override
	public DirContext getAttributeSyntaxDefinition() throws NamingException
	{
		throw new OperationNotSupportedException();
	}

	/* (non-Javadoc)
	 * @see javax.naming.directory.Attribute#set(int, java.lang.Object)
	 */
	@Override
	public Object set(int ix, Object attrVal)
	{
		if (ix > 0) {
			throw new IllegalArgumentException();
		}

		Object o = value;
		value = attrVal;

		return o;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone()
	{
		SingleValueAttribute attr;

		try {
			attr = (SingleValueAttribute)super.clone();
		} catch (CloneNotSupportedException e) {
			attr = new SingleValueAttribute(id, value);
		}

		return attr;
	}
}

/* __oOo__ */
