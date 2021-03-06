/*-
 * Copyright © 2009 Diamond Light Source Ltd.
 *
 * This file is part of GDA.
 *
 * GDA is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License version 3 as published by the Free
 * Software Foundation.
 *
 * GDA is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along
 * with GDA. If not, see <http://www.gnu.org/licenses/>.
 */

package uk.ac.gda.dal.dataprovider;

public class DoubleIncrementProvideDataRunnable extends ProvideDataRunnable<Object> {

	public DoubleIncrementProvideDataRunnable(String scannableName) {
		super(scannableName);
	}

	@Override
	Double valueToType(Object o) {
		if (o instanceof Double) 
			return (Double)o;
		else if (o instanceof Number) 
			return ( ((Number)o).doubleValue());
		else if (o instanceof String) 
			return Double.parseDouble((String) o);
		else 
			throw new RuntimeException("Unknown type: " + o.getClass().getName());
	}

	@Override
	public void setTargetValue(Object targetValue) {
		setTargetValueT( valueToType(targetValue));
	}

}