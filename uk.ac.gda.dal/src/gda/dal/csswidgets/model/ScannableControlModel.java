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
import org.eclipse.core.runtime.Path;

/**
 *
 */
public final class ScannableControlModel extends AbstractWidgetModel {

	/**
	 * Unique identifier.
	 */
	public static final String ID = "org.csstudio.sds.components.ScannableControl";
	public static final String PROP_READBACK_VALUE = "readback";
	public static final String PROP_HIGH_LIMIT = "high";
	public static final String PROP_LOW_LIMIT = "low";
	public static final String PROP_UNITS = "units";
	public static final String PROP_OFFSET = "offset";
	public static final String PROP_DP = "decimal places";
	public static final String PROP_SHOW_BUTTONS = "action buttons";
	public static final String PROP_SIM_READBACK_VALUE = "simulation readback";
	public static final String PROP_DEF_INCREMENT = "default increment";
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getTypeID() {
		return ID;
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void configureProperties() {
		this.addDoubleProperty(PROP_READBACK_VALUE, "Value", WidgetPropertyCategory.BEHAVIOR, 0, false, "");
		this.addDoubleProperty(PROP_HIGH_LIMIT, "High Limit", WidgetPropertyCategory.BEHAVIOR, 10.0, false, "");
		this.addDoubleProperty(PROP_LOW_LIMIT, "Low Limit", WidgetPropertyCategory.BEHAVIOR, -10.0, false, "");
		this.addStringProperty(PROP_UNITS, "Units", WidgetPropertyCategory.BEHAVIOR, "deg", false, "");
		this.addStringProperty(PROP_OFFSET, "offset", WidgetPropertyCategory.BEHAVIOR, "deg", false, "");
		this.addIntegerProperty(PROP_DP, "decimal places", WidgetPropertyCategory.BEHAVIOR, 4, false, "");
		this.addBooleanProperty(PROP_SHOW_BUTTONS, "action buttons", WidgetPropertyCategory.BEHAVIOR, false, false, "");
		this.addDoubleProperty(PROP_SIM_READBACK_VALUE, "Simulation Value", WidgetPropertyCategory.BEHAVIOR, 0, false, "");
		this.addDoubleProperty(PROP_DEF_INCREMENT, "default increment", WidgetPropertyCategory.BEHAVIOR, 1, false, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getDefaultToolTip() {
		StringBuffer buffer = new StringBuffer();
		return buffer.toString();
	}

	public double getReadbackValue() {
		return getDoubleProperty(PROP_READBACK_VALUE);
	}

	public void setReadbackValue(double val) {
		getPropertyInternal(PROP_READBACK_VALUE).setManualValue(val);
	}
	
	public double getHighLimit() {
		return getPropertyInternal(PROP_HIGH_LIMIT).getPropertyValue();
	}

	public double getLowLimit() {
		return getPropertyInternal(PROP_LOW_LIMIT).getPropertyValue();
	}

	public String getPropUnits() {
		return getPropertyInternal(PROP_UNITS).getPropertyValue();
	}
	
	public String getOffset() {
		return getPropertyInternal(PROP_OFFSET).getPropertyValue();
	}
	
	public void setOffset(double val){
		getPropertyInternal(PROP_OFFSET).setManualValue(val);
	}

	public int getDP() {
		return getPropertyInternal(PROP_DP).getPropertyValue();
	}
	
	public void setDP(int val){
		getPropertyInternal(PROP_DP).setManualValue(val);
	}	
	
	public boolean isShowButtons() {
		return getPropertyInternal(PROP_SHOW_BUTTONS).getPropertyValue();
	}
	
	public void setShowButtons(boolean val){
		getPropertyInternal(PROP_SHOW_BUTTONS).setManualValue(val);
	}
	
	public double getSimReadbackValue() {
		return getDoubleProperty(PROP_SIM_READBACK_VALUE);
	}

	public void setSimReadbackValue(double val) {
		getPropertyInternal(PROP_SIM_READBACK_VALUE).setManualValue(val);
	}
	
	public double getDefaultIncrement() {
		return getDoubleProperty(PROP_DEF_INCREMENT);
	}

	public void setDefaultIncrement(double val) {
		getPropertyInternal(PROP_DEF_INCREMENT).setManualValue(val);
	}
}
