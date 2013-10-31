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

public final class AttenuatorModel extends AbstractWidgetModel {
	public static final String ID = "org.csstudio.sds.components.Attenuator";
	public static final String PROP_Y_Translate = "translate.y";
	public static final String PROP_BUSY = "Ready";
	public static final String PROP_NO_BLOCKS = "blocks";
	public static final String PROP_EMPTY_POS = "empty pos";
	
	@Override
	public String getTypeID() {
		return ID;
	}

	@Override
	protected void configureProperties() {
		addIntegerProperty(PROP_Y_Translate, "Translate Y Position", WidgetPropertyCategory.IMAGE, 0, false, "");
		addStringProperty(PROP_BUSY, "Check if busy", WidgetPropertyCategory.BEHAVIOR, "Ready", false, "");
		addIntegerProperty(PROP_NO_BLOCKS, "Number of blocks", WidgetPropertyCategory.IMAGE, 6, false, "");
		addIntegerProperty(PROP_EMPTY_POS, "Empty position", WidgetPropertyCategory.IMAGE, 6, false, "");
	}
	
	@Override
	protected String getDefaultToolTip() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(createTooltipParameter(PROP_ALIASES)+"\n");
		return buffer.toString();
	}
	
	public int getYTranslate() {
		return getIntegerProperty(PROP_Y_Translate);
	}
	
	public String isBusy() {
		return getStringProperty(PROP_BUSY);
	}
	
	public int getNoBlocks() {
		return getIntegerProperty(PROP_NO_BLOCKS);
	}
	
	public int getEmptyBlockPos() {
		return getIntegerProperty(PROP_EMPTY_POS);
	}
	
}