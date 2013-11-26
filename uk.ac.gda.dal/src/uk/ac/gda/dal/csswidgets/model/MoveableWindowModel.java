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

package uk.ac.gda.dal.csswidgets.model;

import org.csstudio.sds.model.ContainerModel;
import org.csstudio.sds.model.WidgetPropertyCategory;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

/**
 * A widget that can contain multiple movable linking containers.
 */
public final class MoveableWindowModel extends ContainerModel {

	public static final String PROP_RESOURCE = "resource";
	private static final String PROP_AUTOZOOM = "autozoom";
	public static final String ID = "org.csstudio.sds.components.MoveableWindow";
	private static final int DEFAULT_HEIGHT = 100;
	private static final int DEFAULT_WIDTH = 100;

	public MoveableWindowModel() {
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}

	@Override
	public String getTypeID() {
		return ID;
	}

	@Override
	protected void configureProperties() {
		addResourceProperty(PROP_RESOURCE, "Resource", WidgetPropertyCategory.BEHAVIOR, new Path(""), new String[] { "css-sds" }, false, "");
		addBooleanProperty(PROP_AUTOZOOM, "Automatic Zoom", WidgetPropertyCategory.DISPLAY, true, false, "");
	}
	
	@Override
	protected String getDefaultToolTip() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(createTooltipParameter(PROP_ALIASES)+"\n");
		buffer.append("Resource:\t");
		buffer.append(createTooltipParameter(PROP_RESOURCE));
		return buffer.toString();
	}

	public IPath getResource() {
		return getResourceProperty(PROP_RESOURCE);
	}

	public boolean isAutoZoom() {
		return getBooleanProperty(PROP_AUTOZOOM);
	}
	
}