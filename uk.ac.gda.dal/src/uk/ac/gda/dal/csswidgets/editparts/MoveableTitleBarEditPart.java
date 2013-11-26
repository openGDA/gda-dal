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
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import uk.ac.gda.dal.csswidgets.model.MoveableTitleBarModel;

public class MoveableTitleBarEditPart extends AbstractWidgetEditPart {

	@Override
	protected IFigure doCreateFigure() {		
		Panel panel = new Panel();
		MoveableTitleBarModel model = (MoveableTitleBarModel) getWidgetModel();
		Color color = new Color(null, 143,186,210);
		Label label = new Label(model.getTitle());
		label.setSize(model.getWidth(), 24);
		label.setLocation(new Point(1,1));
		label.setForegroundColor(new Color(null, 0,0,0));
		label.setBackgroundColor(color);
		label.setOpaque(true);
		panel.setBackgroundColor(color);		
		panel.add(label);
		Image image = new Image(null,model.getIcon());
		ImageFigure img = new ImageFigure(image);
		img.setLocation(new Point(model.getWidth()-20, 3));
		img.setSize(19, 19);
		panel.add(img);
		RectangleFigure rect = new RectangleFigure();
		rect.setSize(model.getWidth(), 24);
		rect.setLocation(new Point(0,0));
		rect.setFill(false);
		rect.setVisible(true);
		rect.setForegroundColor(new Color(null, 0,0,0));
		rect.setBackgroundColor(new Color(null, 0,0,0));
		panel.add(rect);
		return panel;
	}

	@Override
	protected void registerPropertyChangeHandlers() {
	}
	
}