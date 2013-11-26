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

import java.io.InputStream;

import org.csstudio.sds.model.AbstractWidgetModel;

public class MoveableTitleBarModel extends AbstractWidgetModel{
	public static final String ID = "org.csstudio.sds.components.MoveableTitleBarModel";
	private String title = "";
	private InputStream icon;
	
	public MoveableTitleBarModel(String title, InputStream icon) {
		this.title = title;
		this.icon = icon ;
	}

	@Override
	public String getTypeID() {
		return ID;
	}
	
	@Override
	protected void configureProperties() {
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String theTitle) {
		this.title = theTitle;
	}

	public InputStream getIcon() {
		return icon;
	}

	public void setIcon(InputStream icon) {
		this.icon = icon;
	}
	
}