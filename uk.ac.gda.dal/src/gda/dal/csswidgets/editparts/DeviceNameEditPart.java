/*-
 * Copyright © 2010 Diamond Light Source Ltd.
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

package gda.dal.csswidgets.editparts;

import gda.dal.csswidgets.figures.DeviceNameFigure;
import gda.dal.csswidgets.model.DeviceNameModel;

import org.csstudio.sds.ui.editparts.AbstractWidgetEditPart;
import org.eclipse.draw2d.IFigure;

public class DeviceNameEditPart extends AbstractWidgetEditPart {
	
	@Override
	protected IFigure doCreateFigure() {
		DeviceNameModel model = (DeviceNameModel) getWidgetModel();
		DeviceNameFigure figure = new DeviceNameFigure();
		String alias = model.getAllInheritedAliases().toString();
		String deviceName = alias.substring(alias.indexOf("/")+2,alias.indexOf("}"));
		if(deviceName.contains(","))
			deviceName = deviceName.substring(0, deviceName.indexOf(','));
		deviceName=deviceName.replace("dummy_", "");
		figure.setText(deviceName);
		return figure;
	}

	@Override
	protected void registerPropertyChangeHandlers() {
	}

	@Override
	protected DeviceNameModel getCastedModel() {
		return (DeviceNameModel) getModel();
	}
}
