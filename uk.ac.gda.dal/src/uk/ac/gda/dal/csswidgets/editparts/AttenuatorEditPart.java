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

package uk.ac.gda.dal.csswidgets.editparts;


import org.csstudio.sds.ui.editparts.AbstractWidgetEditPart;
import org.csstudio.sds.ui.editparts.IWidgetPropertyChangeHandler;
import org.eclipse.core.runtime.Path;
import org.eclipse.draw2d.IFigure;

import uk.ac.gda.dal.csswidgets.figures.AttenuatorFigure;
import uk.ac.gda.dal.csswidgets.model.AttenuatorModel;

public class AttenuatorEditPart extends AbstractWidgetEditPart {
	AttenuatorFigure refreshableAttenuatorFigure;
	
	/**
	 * Returns the casted model. This is just for convenience.
	 * 
	 * @return the casted {@link AttenuatorModel}
	 */
	@Override
	protected AttenuatorModel getCastedModel() {
		return (AttenuatorModel) getModel();
	}

	@Override
	protected IFigure doCreateFigure() {
		AttenuatorModel model = getCastedModel();
		refreshableAttenuatorFigure = new AttenuatorFigure();
		refreshableAttenuatorFigure.setNoBlocks(model.getNoBlocks());
		refreshableAttenuatorFigure.setEmptyBlockPos(model.getEmptyBlockPos());
		return refreshableAttenuatorFigure;
	}

	@Override
	protected void registerPropertyChangeHandlers() {
		
		IWidgetPropertyChangeHandler handle = new IWidgetPropertyChangeHandler() {
			@Override
			public boolean handleChange(final Object oldValue, final Object newValue, final IFigure figure) {
				AttenuatorFigure imageFigure = (AttenuatorFigure) figure;

				imageFigure.setYTranslate(Integer.parseInt(newValue.toString()));
				return true;
			}
		};
		setPropertyChangeHandler(AttenuatorModel.PROP_Y_Translate, handle);
		
		handle = new IWidgetPropertyChangeHandler() {
			@Override
			public boolean handleChange(final Object oldValue, final Object newValue, final IFigure figure) {
				AttenuatorFigure imageFigure = (AttenuatorFigure) figure;

				imageFigure.setNoBlocks(Integer.parseInt(newValue.toString()));
				return true;
			}
		};
		setPropertyChangeHandler(AttenuatorModel.PROP_NO_BLOCKS, handle);
		
		handle = new IWidgetPropertyChangeHandler() {
			@Override
			public boolean handleChange(final Object oldValue, final Object newValue, final IFigure figure) {
				AttenuatorFigure imageFigure = (AttenuatorFigure) figure;

				imageFigure.setEmptyBlockPos(Integer.parseInt(newValue.toString()));
				return true;
			}
		};
		setPropertyChangeHandler(AttenuatorModel.PROP_EMPTY_POS, handle);
		
		handle = new IWidgetPropertyChangeHandler() {
			@Override
			public boolean handleChange(final Object oldValue, final Object newValue, final IFigure figure) {
				AttenuatorFigure imageFigure = (AttenuatorFigure) figure;
				String upPath = imageFigure.getArrowPath().toString();
				if (((String) newValue).equals("Busy")) {
					if (!upPath.contains("red")) {
						int beginIndex = 0;
						int endIndex = upPath.indexOf('.');
						upPath = upPath.substring(beginIndex, endIndex);
						upPath = upPath + "_red.png";
					}
					imageFigure.setArrowPath(new Path(upPath));
					imageFigure.repaint();
				}
				else {
					if (upPath.contains("red")) {
						int beginIndex = 0;
						int endIndex = upPath.indexOf("_red");
						upPath = upPath.substring(beginIndex, endIndex);
						upPath = upPath + ".png";
					}
					imageFigure.setArrowPath(new Path(upPath));
					imageFigure.repaint();
				}
				return true;
			}
		};
		setPropertyChangeHandler(AttenuatorModel.PROP_BUSY, handle);
	}
	
}