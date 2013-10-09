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
	RefreshableSVGFigure svgFigure;
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected IFigure doCreateFigure() {
		SVGModel model = (SVGModel) getModel();
		svgFigure = new RefreshableSVGFigure();
		svgFigure.setFilePath(model.getFilename());
		return svgFigure;
	}

	@Override
	public void deactivate() {
		super.deactivate();
		svgFigure.dispose();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void registerPropertyChangeHandlers() {
		IWidgetPropertyChangeHandler handle = new IWidgetPropertyChangeHandler() {
			@Override
			public boolean handleChange(final Object oldValue, final Object newValue, final IFigure figure) {
				((RefreshableSVGFigure) figure).setFilePath((IPath) newValue);
				return true;
			}
		};
		setPropertyChangeHandler(SVGModel.PROP_FILENAME, handle);

		handle = new IWidgetPropertyChangeHandler() {
			@Override
			public boolean handleChange(final Object oldValue, final Object newValue, final IFigure figure) {
				
				String path = ((RefreshableSVGFigure) figure).getFilePath().toString();
				
				if(((String)newValue).equals("Busy")){
					
					if(!path.contains("red")){
						int beginIndex = 0;
						int endIndex = path.indexOf('.');
						path = path.substring(beginIndex, endIndex);
						path = path + "_red.png";
					}
					
					((RefreshableSVGFigure) figure).setFilePath(new Path(path));
					figure.repaint();
				}
				
				else{
					if(path.contains("red")){
						int beginIndex = 0;
						int endIndex = path.indexOf("_red");
						path = path.substring(beginIndex, endIndex);
						path = path + ".png";
					}
					
					((RefreshableSVGFigure) figure).setFilePath(new Path(path));
					figure.repaint();
				}
				
				return true;
			}
		};
		setPropertyChangeHandler(SVGModel.PROP_BUSY, handle);

		handle = new IWidgetPropertyChangeHandler() {
			@Override
			public boolean handleChange(final Object oldValue, final Object newValue, final IFigure figure) {
				
				((RefreshableSVGFigure) figure).setYTranslate((Integer)newValue);
				return true;
			}
		};
		setPropertyChangeHandler(SVGModel.PROP_Y_Translate, handle);
		
		handle = new IWidgetPropertyChangeHandler() {
			@Override
			public boolean handleChange(final Object oldValue, final Object newValue, final IFigure figure) {
				((RefreshableSVGFigure) figure).setXTranslate((Integer)newValue);
				return true;
			}
		};
		setPropertyChangeHandler(SVGModel.PROP_X_Translate, handle);
	}
}
