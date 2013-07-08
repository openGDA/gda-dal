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

import java.io.InputStream;

import org.csstudio.sds.model.AbstractWidgetModel;

public class MoveableTitleBarModel extends AbstractWidgetModel{


		/**
		 * The ID of this widget model.
		 */
		public static final String ID = "org.csstudio.sds.components.MoveableTitleBarModel"; //$NON-NLS-1$

	
		public String theTitle = "";
		public InputStream icon;
		
		/**
		 * Standard constructor.
		 */
		public MoveableTitleBarModel(String title, InputStream newIcon) {
			theTitle = title;
			icon = newIcon ;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getTypeID() {
			return ID;
		}
		


		@Override
		protected void configureProperties() {
			
		}
	}
