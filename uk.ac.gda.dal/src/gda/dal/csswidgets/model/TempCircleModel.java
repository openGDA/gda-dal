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

package gda.dal.csswidgets.model;

import org.csstudio.sds.model.AbstractWidgetModel;
import org.csstudio.sds.model.WidgetPropertyCategory;

public final class TempCircleModel extends AbstractWidgetModel {
	public static final String ID = "org.csstudio.sds.components.TempCircle";
	public static final String PROP_TEMP = "temp";
	public static final String PROP_TEMP_MIN = "min";
	public static final String PROP_TEMP_MAX = "max";
	private int DEFAULT_WIDTH = 20;
	private int DEFAULT_HEIGHT = 10;

	public TempCircleModel() {
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}
	
	@Override
	public String getTypeID() {
		return ID;
	}

	@Override
	protected void configureProperties() {
		addDoubleProperty(PROP_TEMP, "Temperature", WidgetPropertyCategory.BEHAVIOR, 0.0, false, "");
		addDoubleProperty(PROP_TEMP_MIN, "Minimum Temperature", WidgetPropertyCategory.BEHAVIOR, 0.0, false, "");
		addDoubleProperty(PROP_TEMP_MAX, "Maximum Temperature", WidgetPropertyCategory.BEHAVIOR, 0.0, false, "");
	}

	@Override
	protected String getDefaultToolTip() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(createTooltipParameter(PROP_ALIASES) + "\n");
		buffer.append(createTooltipParameter(PROP_TEMP));
		return buffer.toString();
	}

	public double getTemp() {
		return getDoubleProperty(PROP_TEMP);
	}

	public double getTempMin() {
		return getDoubleProperty(PROP_TEMP_MIN);
	}

	public double getTempMax() {
		return getDoubleProperty(PROP_TEMP_MAX);
	}
	
}