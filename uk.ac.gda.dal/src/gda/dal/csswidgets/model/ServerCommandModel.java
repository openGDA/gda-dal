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

public final class ServerCommandModel extends AbstractWidgetModel {
	public static final String ID = "org.csstudio.sds.components.ServerCommand";
	public static final String PROP_READBACK_VALUE = "readback";
	public static final String PROP_SERVER_COMMAND  = "server.command";
	
	@Override
	public String getTypeID() {
		return ID;
	}

	@Override
	protected void configureProperties() {
		addDoubleProperty(PROP_READBACK_VALUE, "Value", WidgetPropertyCategory.BEHAVIOR, 0, false, "");
		addStringProperty(PROP_SERVER_COMMAND, "Server Command", WidgetPropertyCategory.BEHAVIOR, "", false, "");
	}

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
	
	public String getServerCommand() {
		return getStringProperty(PROP_SERVER_COMMAND);
	}
	
}