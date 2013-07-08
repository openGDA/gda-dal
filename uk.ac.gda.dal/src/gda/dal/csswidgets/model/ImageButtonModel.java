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
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

/**
 *
 */
public final class ImageButtonModel extends AbstractWidgetModel {

	public static final String ID = "org.csstudio.sds.components.ImageButton";
	
	public static final String PROP_FILENAME = "filename";
	public static final String PROP_STYLE = "style";
	public static final String PROP_LABEL = "label";
	public static final String PROP_OVER_COLOUR = "colour.over";
	public static final String PROP_BUTTON_COLOUR = "colour.button";
	public static final String PROP_FOREGROUND_COLOUR = "colour.foreground";
	public static final String PROP_VIEW_ID  = "viewid";
	public static final String PROP_SERVER_COMMAND  = "server.command";
	public static final String PROP_SERVER_COMMAND_VARIABLE  = "server.command.variable";
	
	/**
	 * The default value for the file extensions.
	 */
	private static final String[] FILE_EXTENSIONS = new String[] {"*", "jpg", "jpeg", "gif", "bmp", "png", "svg"};
	
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
		this.addResourceProperty(PROP_FILENAME, "File", WidgetPropertyCategory.IMAGE, new Path(""), FILE_EXTENSIONS, false, "");
		this.addIntegerProperty(PROP_STYLE, "Style", WidgetPropertyCategory.DISPLAY, 0, false, "");
		this.addStringProperty(PROP_LABEL, "Label Value", WidgetPropertyCategory.DISPLAY, "label", false, "");
		this.addColorProperty(PROP_OVER_COLOUR, "Mouse Over Color", WidgetPropertyCategory.FORMAT, "#ADD8E6", false);
		this.addColorProperty(PROP_BUTTON_COLOUR, "Button Color", WidgetPropertyCategory.FORMAT, "#8FBAD2", false);
		this.addColorProperty(PROP_FOREGROUND_COLOUR, "Foreground Color", WidgetPropertyCategory.FORMAT, "#000000", false);
		this.addStringProperty(PROP_VIEW_ID, "View id", WidgetPropertyCategory.BEHAVIOR, "", false, "");
		this.addStringProperty(PROP_SERVER_COMMAND, "Server Command", WidgetPropertyCategory.BEHAVIOR, "", false, "");
		this.addStringProperty(PROP_SERVER_COMMAND_VARIABLE, "Server Command Variable", WidgetPropertyCategory.BEHAVIOR, "", false, "");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getDefaultToolTip() {
		return "";
	}

	/**
	 * Returns the path to the specified file.
	 * @return The path to the specified file
	 */
	public IPath getFilename() {
		return getResourceProperty(PROP_FILENAME);
	}
	
	/**
	 * @return getProperty(PROP_LABEL).getPropertyValue()
	 */
	public String getLabel() {
		return getStringProperty(PROP_LABEL);
	}
	
	public String getButtonColour() {
		return getColor(PROP_BUTTON_COLOUR);
	}

	public String getOverColour() {
		return getColor(PROP_OVER_COLOUR);
	}

	public String getViewID() {
		return getStringProperty(PROP_VIEW_ID);
	}

	public int getStyle() {
		return getIntegerProperty(PROP_STYLE);
	}

	public String getServerCommand() {
		return getStringProperty(PROP_SERVER_COMMAND);
	}
	
	public String getServerCommandVariable() {
		return getStringProperty(PROP_SERVER_COMMAND_VARIABLE);
	}
	
}
