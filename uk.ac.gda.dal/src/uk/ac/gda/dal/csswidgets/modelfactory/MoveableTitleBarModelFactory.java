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

package uk.ac.gda.dal.csswidgets.modelfactory;

import org.csstudio.sds.model.AbstractWidgetModel;
import org.csstudio.sds.model.IWidgetModelFactory;

import uk.ac.gda.dal.csswidgets.model.MoveableTitleBarModel;

public final class MoveableTitleBarModelFactory implements IWidgetModelFactory {

	@Override
	public AbstractWidgetModel createWidgetModel() {
		return new MoveableTitleBarModel("",null);
	}

	@Override
	public Class<MoveableTitleBarModel> getWidgetModelType() {
		return MoveableTitleBarModel.class;
	}
}