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

package gda.dal.csswidgets.editparts;

import gda.dal.csswidgets.figures.RefreshableSVGFigure;
import gda.dal.csswidgets.model.SVGModel;

import org.csstudio.sds.ui.editparts.AbstractWidgetEditPart;
import org.csstudio.sds.ui.editparts.IWidgetPropertyChangeHandler;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.draw2d.IFigure;

public final class SVGEditPart extends AbstractWidgetEditPart {
	RefreshableSVGFigure figure = new RefreshableSVGFigure();
	/**
	 * Returns the casted model. This is just for convenience.
	 * 
	 * @return the casted {@link SVGModel}
	 */
	@Override
	protected SVGModel getCastedModel() {
		return (SVGModel) getModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected IFigure doCreateFigure() {
		SVGModel model = getCastedModel();
		figure.setFilePath(model.getFilename());
		return figure;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void registerPropertyChangeHandlers() {
		// changes to the filename property
		IWidgetPropertyChangeHandler handle = new IWidgetPropertyChangeHandler() {
			@Override
			public boolean handleChange(final Object oldValue, final Object newValue, final IFigure figure) {
				RefreshableSVGFigure imageFigure = (RefreshableSVGFigure) figure;
				imageFigure.setFilePath((IPath) newValue);
				return true;
			}
		};
		setPropertyChangeHandler(SVGModel.PROP_FILENAME, handle);


		
		handle = new IWidgetPropertyChangeHandler() {
			@Override
			public boolean handleChange(final Object oldValue, final Object newValue, final IFigure figure) {
				RefreshableSVGFigure imageFigure = (RefreshableSVGFigure) figure;
				
				String path = imageFigure.getFilePath().toString();
				
				if(((String)newValue).equals("Busy")){
					
					if(!path.contains("red")){
						int beginIndex = 0;
						int endIndex = path.indexOf('.');
						path = path.substring(beginIndex, endIndex);
						path = path + "_red.png";
					}
					
					imageFigure.setFilePath(new Path(path));
					imageFigure.repaint();
				}
				
				else{
					if(path.contains("red")){
						int beginIndex = 0;
						int endIndex = path.indexOf("_red");
						path = path.substring(beginIndex, endIndex);
						path = path + ".png";
					}
					
					imageFigure.setFilePath(new Path(path));
					imageFigure.repaint();
				}
				
				imageFigure.setFilePath((IPath) newValue);
				return true;
			}
		};
		setPropertyChangeHandler(SVGModel.PROP_BUSY, handle);

		handle = new IWidgetPropertyChangeHandler() {
			@Override
			public boolean handleChange(final Object oldValue, final Object newValue, final IFigure figure) {
				RefreshableSVGFigure imageFigure = (RefreshableSVGFigure) figure;

				imageFigure.setYTranslate(Integer.parseInt(newValue.toString()));
				return true;
			}
		};
		setPropertyChangeHandler(SVGModel.PROP_Y_Translate, handle);
		
		handle = new IWidgetPropertyChangeHandler() {
			@Override
			public boolean handleChange(final Object oldValue, final Object newValue, final IFigure figure) {
				RefreshableSVGFigure imageFigure = (RefreshableSVGFigure) figure;

				imageFigure.setXTranslate(Integer.parseInt(newValue.toString()));
				return true;
			}
		};
		setPropertyChangeHandler(SVGModel.PROP_X_Translate, handle);
	}
}
