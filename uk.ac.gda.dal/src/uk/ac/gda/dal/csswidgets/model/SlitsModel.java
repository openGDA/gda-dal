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

public final class SlitsModel extends AbstractWidgetModel {

	public static final String ID = "org.csstudio.sds.components.Slits";
	public static final String PROP_X_Translate = "translate.x";
	public static final String PROP_Y_Translate = "translate.y";
	public static final String PROP_X_BUSY = "ready.x";
	public static final String PROP_Y_BUSY = "ready.y";
	public static final String PROP_HGAP_BUSY = "ready.hgap";
	public static final String PROP_VGAP_BUSY = "ready.vgap";
	public static final String PROP_VGAP = "v gap";
	public static final String PROP_HGAP = "h gap";
	public static final String PROP_SLIT_TYPE = "type";
	public static final String PROP_BEAM_OUT = "out";

	@Override
	public String getTypeID() {
		return ID;
	}

	@Override
	protected void configureProperties() {
		this.addIntegerProperty(PROP_X_Translate, "Translate X Position", WidgetPropertyCategory.IMAGE, 0, false, "");
		this.addIntegerProperty(PROP_Y_Translate, "Translate Y Position", WidgetPropertyCategory.IMAGE, 0, false, "");
		this.addIntegerProperty(PROP_VGAP, "V Gap", WidgetPropertyCategory.IMAGE, 0, false, "");
		this.addIntegerProperty(PROP_HGAP, "H Gap", WidgetPropertyCategory.IMAGE, 0, false, "");
		this.addStringProperty(PROP_X_BUSY, "Check if x is busy", WidgetPropertyCategory.BEHAVIOR, "Ready", false, "");
		this.addStringProperty(PROP_Y_BUSY, "Check if y is busy", WidgetPropertyCategory.BEHAVIOR, "Ready", false, "");
		this.addStringProperty(PROP_HGAP_BUSY, "Check if hgap is busy", WidgetPropertyCategory.BEHAVIOR, "Ready", false, "");
		this.addStringProperty(PROP_VGAP_BUSY, "Check if vgap is busy", WidgetPropertyCategory.BEHAVIOR, "Ready", false, "");
		String[] options = {"vertical", "slits"};
		this.addArrayOptionProperty(PROP_SLIT_TYPE, "Slit type", WidgetPropertyCategory.IMAGE, options, 0, false, "");
		this.addStringProperty(PROP_BEAM_OUT, "Beam out", WidgetPropertyCategory.BEHAVIOR, "yes", false, "");
	}
	
	@Override
	protected String getDefaultToolTip() {
		return null;
	}
	
	public int getSlitType() {
		return this.getPropertyInternal(PROP_SLIT_TYPE).getPropertyValue();
	}
	
	public double getVGap() {
		return getDoubleProperty(PROP_VGAP);
	}
	
	public double getHGap() {
		return getDoubleProperty(PROP_HGAP);
	}

	public double getXTranslate() {
		return getDoubleProperty(PROP_X_Translate);
	}
	
	public double getYTranslate() {
		return getDoubleProperty(PROP_Y_Translate);
	}
	
	public String isXBusy() {
		return getStringProperty(PROP_X_BUSY);
	}
	
	public String isYBusy() {
		return getStringProperty(PROP_Y_BUSY);
	}
	
	public String isHGAPBusy() {
		return getStringProperty(PROP_HGAP_BUSY);
	}
	
	public String isVGAPBusy() {
		return getStringProperty(PROP_VGAP_BUSY);
	}

	public String isBeamOut() {
		return getStringProperty(PROP_BEAM_OUT);
	}
	
}