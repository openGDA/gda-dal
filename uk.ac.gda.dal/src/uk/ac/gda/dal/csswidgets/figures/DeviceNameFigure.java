/*-
 * Copyright © 2010 Diamond Light Source Ltd.
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

package uk.ac.gda.dal.csswidgets.figures;

import org.csstudio.sds.ui.figures.BorderAdapter;
import org.csstudio.sds.ui.figures.CrossedOutAdapter;
import org.csstudio.sds.ui.figures.IBorderEquippedWidget;
import org.csstudio.sds.ui.figures.ICrossedFigure;
import org.csstudio.sds.ui.figures.IRhombusEquippedWidget;
import org.csstudio.sds.ui.figures.RhombusAdapter;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Rectangle;

public class DeviceNameFigure extends Shape implements IAdaptable {
	private Rectangle bound = getBounds().getCopy();
	private String text = "alias not set";
	private IBorderEquippedWidget _borderAdapter;
    private CrossedOutAdapter _crossedOutAdapter;
    private RhombusAdapter _rhombusAdapter;
    
	@Override
	protected boolean useLocalCoordinates() {
		return true;
	}
	
	@Override
	protected void fillShape(Graphics graphics) {	
	}

	@Override
	protected void outlineShape(Graphics graphics) {
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		if (adapter == IBorderEquippedWidget.class) {
			if(_borderAdapter==null)
				_borderAdapter = new BorderAdapter(this);
			return _borderAdapter;
		} else if(adapter == ICrossedFigure.class) {
            if(_crossedOutAdapter==null)
                _crossedOutAdapter = new CrossedOutAdapter(this);
            return _crossedOutAdapter;
        } else if(adapter == IRhombusEquippedWidget.class) {
            if(_rhombusAdapter==null)
                _rhombusAdapter = new RhombusAdapter(this);
            return _rhombusAdapter;
        }
		return null;
	}
	
	@Override
	public void paintFigure(final Graphics gfx) {
		bound = getBounds().getCopy();
		gfx.drawText(text, bound.x, bound.y);
	}
	
	public void setText(String text){
		this.text=text;
	}
}