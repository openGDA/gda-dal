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

package gda.dal.dataprovider;

public class DoubleProvideDataRunnable extends ProvideDataRunnable<Double> {

	public DoubleProvideDataRunnable(String scannableName) {
		super(scannableName);
	}

	@Override
	Double valueToType(Object o) {
		if (o instanceof Double)
			return (Double) o;
		else if (o instanceof Number)
			return (((Number) o).doubleValue());
		else if (o instanceof String && isDouble((String)o))
			return Double.parseDouble((String) o);
		else
			return null;
	}

	public boolean isDouble(String string) {
		try {
			Double.valueOf(string);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	@Override
	public void setTargetValue(Double targetValue) {
		setTargetValueT(valueToType(targetValue));
	}

}