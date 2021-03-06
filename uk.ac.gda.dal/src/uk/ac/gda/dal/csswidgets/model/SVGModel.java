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

import org.csstudio.sds.model.AbstractWidgetModel;
import org.csstudio.sds.model.WidgetPropertyCategory;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

public final class SVGModel extends AbstractWidgetModel {
	public static final String ID = "org.csstudio.sds.components.SVG";
	public static final String PROP_FILENAME = "filename";
	public static final String[] FILE_EXTENSIONS = new String[] {"*", "jpg", "jpeg", "gif", "bmp", "png", "svg"};
	public static final String PROP_Y_Translate = "translate.y";
	public static final String PROP_X_Translate = "translate.x";
	public static final String PROP_BUSY = "Ready";

	@Override
	public String getTypeID() {
		return ID;
	}

	@Override
	protected void configureProperties() {
		addResourceProperty(PROP_FILENAME, "File", WidgetPropertyCategory.IMAGE, new Path(""), FILE_EXTENSIONS, false, "");
		addIntegerProperty(PROP_Y_Translate, "Translate Y Position", WidgetPropertyCategory.IMAGE, 0, false, "");
		addIntegerProperty(PROP_X_Translate, "Check if busy", WidgetPropertyCategory.IMAGE, 0, false, "");
		addStringProperty(PROP_BUSY, "Ready", WidgetPropertyCategory.IMAGE, "Ready", false, "");
	}

	@Override
	protected String getDefaultToolTip() {
		return null;
	}

	public int getYTranslate() {
		return getIntegerProperty(PROP_Y_Translate);
	}

	public int getXTranslate() {
		return getIntegerProperty(PROP_X_Translate);
	}

	public IPath getFilename() {
		return getResourceProperty(PROP_FILENAME);
	}
	
	public String isBusy() {
		return getStringProperty(PROP_BUSY);
	}
	
}