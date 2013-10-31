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

import gda.dal.csswidgets.figures.TempCircleFigure;
import gda.dal.csswidgets.model.TempCircleModel;

import org.csstudio.sds.ui.editparts.AbstractWidgetEditPart;
import org.csstudio.sds.ui.editparts.IWidgetPropertyChangeHandler;
import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.graphics.Color;

public class TempCircleEditPart extends AbstractWidgetEditPart {
	private TempCircleFigure tempCircleFigure;
	private Color previousColor;

	@Override
	protected IFigure doCreateFigure() {
		TempCircleModel model = (TempCircleModel) getWidgetModel();
		double temp = model.getTemp();
		double min = model.getTempMin();
		double max = model.getTempMax();
		tempCircleFigure = new TempCircleFigure(heatMap(temp, min, max));
		Color currentColor = heatMap(temp, min, max);
		previousColor = currentColor;
		tempCircleFigure.setLiveColor(currentColor);
		tempCircleFigure.setHistoryColorA(new Color(null, 255, 255, 255));
		tempCircleFigure.setHistoryColorB(new Color(null, 255, 255, 255));
		tempCircleFigure.setHistoryColorC(new Color(null, 255, 255, 255));
		return tempCircleFigure;
	}

	@Override
	protected void registerPropertyChangeHandlers() {
		IWidgetPropertyChangeHandler handle = new IWidgetPropertyChangeHandler() {
			@Override
			public boolean handleChange(final Object oldValue, final Object newValue, final IFigure figure) {
				TempCircleFigure tempCircleFigure = (TempCircleFigure) figure;
				TempCircleModel model = (TempCircleModel) getWidgetModel();
				double temp = model.getTemp();
				double min = model.getTempMin();
				double max = model.getTempMax();
				Color currentColor = heatMap(temp, min, max);
				if (!compareColors(currentColor, previousColor)) {
					tempCircleFigure.setHistoryColorC(tempCircleFigure.getHistoryColorB());
					tempCircleFigure.setHistoryColorB(tempCircleFigure.getHistoryColorA());
					tempCircleFigure.setHistoryColorA(previousColor);
				}
				tempCircleFigure.setLiveColor(currentColor);
				previousColor = heatMap(temp, min, max);
				return true;
			}
		};
		setPropertyChangeHandler(TempCircleModel.PROP_TEMP, handle);
	}

	public boolean compareColors(Color col1, Color col2) {
		boolean same = true;
		if (col1.getRed() != col2.getRed() || col1.getGreen() != col2.getGreen() || col1.getBlue() != col2.getBlue())
			same = false;
		return same;
	}

	public Color heatMap(double temp, double min, double max) {
		Color heatMap[] = new Color[10];
		heatMap[0] = new Color(null, 0, 0, 255);
		heatMap[1] = new Color(null, 0, 128, 255);
		heatMap[2] = new Color(null, 0, 255, 255);
		heatMap[3] = new Color(null, 0, 255, 128);
		heatMap[4] = new Color(null, 0, 255, 0);
		heatMap[5] = new Color(null, 128, 255, 0);
		heatMap[6] = new Color(null, 255, 255, 0);
		heatMap[7] = new Color(null, 255, 128, 0);
		heatMap[8] = new Color(null, 255, 0, 0);
		double factor = temp / (max - min);
		Color col = new Color(null, 0, 0, 0);
		if (factor < 0.11)
			col = heatMap[0];
		else if (factor < 0.22)
			col = heatMap[1];
		else if (factor < 0.33)
			col = heatMap[2];
		else if (factor < 0.44)
			col = heatMap[3];
		else if (factor < 0.55)
			col = heatMap[4];
		else if (factor < 0.66)
			col = heatMap[5];
		else if (factor < 0.77)
			col = heatMap[6];
		else if (factor < 0.88)
			col = heatMap[7];
		else if (factor <= 1)
			col = heatMap[8];
		else if (factor > 1)
			col = heatMap[8];
		return col;
	}
	
}