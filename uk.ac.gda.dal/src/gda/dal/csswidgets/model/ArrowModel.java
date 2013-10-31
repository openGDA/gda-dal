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

public final class ArrowModel extends AbstractWidgetModel {
	public static final String ID = "org.csstudio.sds.components.Arrow";
	public static final String PROP_FILENAME = "filename";
	public static final String PROP_BUSY = "Ready";
	public static final String PROP_ARROW = "x";
	private String[] FILE_EXTENSIONS = new String[] {"*", "jpg", "jpeg", "gif", "bmp", "png", "svg"};
	
	@Override
	public String getTypeID() {
		return ID;
	}

	@Override
	protected void configureProperties() {
		addResourceProperty(PROP_FILENAME, "File", WidgetPropertyCategory.IMAGE, new Path(""), FILE_EXTENSIONS, false, "");
		addStringProperty(PROP_BUSY, "Check if busy", WidgetPropertyCategory.BEHAVIOR, "Ready", false, "");
		String[] options = {"x","y","y2","z","yaw","pitch","roll","vgap","hgap","bend up","bend down", "yaw2", "x2"};
		addArrayOptionProperty(PROP_ARROW, "Arrow type", WidgetPropertyCategory.IMAGE, options, 0, false, "");
	}
	
	@Override
	protected String getDefaultToolTip() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(createTooltipParameter(PROP_ALIASES)+"\n");
		buffer.append("Image:\t");
		buffer.append(createTooltipParameter(PROP_ARROW));
		return buffer.toString();
	}
	
	public IPath getFilename() {
		int newValue = getArrow();
		Path path = new Path("");
		if(newValue==0)
			path = new Path("/arrow images/x.png");
		else if(newValue==1)
			path = new Path("/arrow images/y.png");
		else if(newValue==2)
			path = new Path("/arrow images/y2.png");
		else if(newValue==3)
			path = new Path("/arrow images/z.png");
		else if(newValue==4)
			path = new Path("/arrow images/yaw.png");
		else if(newValue==5)
			path = new Path("/arrow images/pitch.png");
		else if(newValue==6)
			path = new Path("/arrow images/roll.png");
		else if(newValue==7)
			path = new Path("/arrow images/vgap.png");
		else if(newValue==8)
			path = new Path("/arrow images/hgap.png");
		else if(newValue==9)
			path = new Path("/arrow images/bend_up.png");
		else if(newValue==10)
			path = new Path("/arrow images/bend_down.png");
		else if(newValue==11)
			path = new Path("/arrow images/yaw2.png");
		else if(newValue==12)
			path = new Path("/arrow images/x2.png");
		return path;
	}
	
	public int getArrow() {
		return getArrayOptionProperty(PROP_ARROW);
	}
	
	public String isBusy() {
		return getStringProperty(PROP_BUSY);
	}
	
}