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
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.draw2d.IFigure;

import uk.ac.gda.dal.csswidgets.figures.ArrowFigure;
import uk.ac.gda.dal.csswidgets.model.ArrowModel;

public class ArrowEditPart extends AbstractWidgetEditPart {
	private ArrowFigure arrowFigure = new ArrowFigure();

	@Override
	protected ArrowModel getCastedModel() {
		return (ArrowModel) getModel();
	}

	@Override
	protected IFigure doCreateFigure() {
		ArrowModel model = getCastedModel();
		arrowFigure.setFilePath(model.getFilename());
		return arrowFigure;
	}

	@Override
	protected void registerPropertyChangeHandlers() {
		IWidgetPropertyChangeHandler handle = new IWidgetPropertyChangeHandler() {
			@Override
			public boolean handleChange(final Object oldValue, final Object newValue, final IFigure figure) {
				ArrowFigure imageFigure = (ArrowFigure) figure;
				imageFigure.setFilePath((IPath) newValue);
				return true;
			}
		};
		setPropertyChangeHandler(ArrowModel.PROP_FILENAME, handle);

		handle = new IWidgetPropertyChangeHandler() {
			@Override
			public boolean handleChange(final Object oldValue, final Object newValue, final IFigure figure) {
				ArrowFigure imageFigure = (ArrowFigure) figure;
				String path = imageFigure.getFilePath().toString();
				if (((String) newValue).equals("Busy")) {
					if (!path.contains("red")) {
						int beginIndex = 0;
						int endIndex = path.indexOf('.');
						path = path.substring(beginIndex, endIndex);
						path = path + "_red.png";
					}
					imageFigure.setFilePath(new Path(path));
					imageFigure.repaint();
				}
				else {
					if (path.contains("red")) {
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
		setPropertyChangeHandler(ArrowModel.PROP_BUSY, handle);

		handle = new IWidgetPropertyChangeHandler() {
			@Override
			public boolean handleChange(final Object oldValue, final Object newValue, final IFigure figure) {
				ArrowFigure imageFigure = (ArrowFigure) figure;
				Path path = new Path("");
				if(Integer.parseInt(newValue.toString())==0)
					path = new Path("/arrow images/x.png");
				else if(Integer.parseInt(newValue.toString())==1)
					path = new Path("/arrow images/y.png");
				else if(Integer.parseInt(newValue.toString())==2)
					path = new Path("/arrow images/y2.png");
				else if(Integer.parseInt(newValue.toString())==3)
					path = new Path("/arrow images/z.png");
				else if(Integer.parseInt(newValue.toString())==4)
					path = new Path("/arrow images/yaw.png");
				else if(Integer.parseInt(newValue.toString())==5)
					path = new Path("/arrow images/pitch.png");
				else if(Integer.parseInt(newValue.toString())==6)
					path = new Path("/arrow images/roll.png");
				else if(Integer.parseInt(newValue.toString())==7)
					path = new Path("/arrow images/vgap.png");
				else if(Integer.parseInt(newValue.toString())==8)
					path = new Path("/arrow images/hgap.png");
				else if(Integer.parseInt(newValue.toString())==9)
					path = new Path("/arrow images/bend_up.png");
				else if(Integer.parseInt(newValue.toString())==10)
					path = new Path("/arrow images/bend_down.png");
				else if(Integer.parseInt(newValue.toString())==11)
					path = new Path("/arrow images/yaw2.png");
				else if(Integer.parseInt(newValue.toString())==12)
					path = new Path("/arrow images/x2.png");
				imageFigure.setFilePath(path);
				return true;
			}
		};
		setPropertyChangeHandler(ArrowModel.PROP_ARROW, handle);
	}
	
}