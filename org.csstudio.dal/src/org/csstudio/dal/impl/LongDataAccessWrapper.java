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

package org.csstudio.dal.impl;

import org.csstudio.dal.DataAccess;
import org.csstudio.dal.LongAccess;


public class LongDataAccessWrapper extends AbstractDataAccessWrapper<Long>implements LongAccess{
	private static final int DOUBLE_TO_LONG = 1;
	private static final int NUMBER_TO_LONG = 2;

	public LongDataAccessWrapper(DataAccess sourceDA){
		super(Long.class, sourceDA);
	}

	@Override
	protected int getConversion(){
		if (valClass.equals(Long.class)) {
			if (sourceDA.getDataType().equals(Double.class))
				return DOUBLE_TO_LONG;
			else if (sourceDA.getDataType().equals(Number.class))
				return NUMBER_TO_LONG;
		}
		return UNKNOWN;
	}

	@Override
	protected Object convertToOriginal(Long value, DataAccess dataAccess){
		if (value == null)
			return null;
		switch (conversion) {
		case DOUBLE_TO_LONG: {
			Double doubleVal = value.doubleValue();
			return doubleVal;
		}
		case NUMBER_TO_LONG: {
			return value;
		}
		}
		return null;
	}

	@Override
	protected Long convertFromOriginal(Object value, DataAccess dataAccess){
		if (value == null)
			return null;
		switch (conversion) {
		case DOUBLE_TO_LONG: {
			Long longVal = new Long(((Double)value).longValue());
			return longVal;
		}
		case NUMBER_TO_LONG: {
			Long longVal = new Long(((Number)value).longValue());
			return longVal;
		}
		}
		return null;
	}
	
}