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

package gda.dal.csswidgets.figures;

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
import org.eclipse.swt.graphics.Color;

public final class TempCircleFigure extends Shape implements IAdaptable {
	private Rectangle bound = getBounds().getCopy();
	private Color liveColor;
	private Color historyColorA;
	private Color historyColorB;
	private Color historyColorC;
	private IBorderEquippedWidget _borderAdapter;
    private CrossedOutAdapter _crossedOutAdapter;
    private RhombusAdapter _rhombusAdapter;
	
	public TempCircleFigure(Color newColor){
		liveColor = newColor;
	}
	
	@Override
	protected boolean useLocalCoordinates() {
		return true;
	}

	@Override
	protected void fillShape(final Graphics gfx) {
	}

	@Override
	protected void outlineShape(final Graphics gfx) {
	}

	@Override
	public void paintFigure(final Graphics gfx) {
		bound = getBounds().getCopy();
		gfx.setForegroundColor(new Color(null, 0,0,0));
		gfx.drawArc(bound.x, bound.y, 26, 26, 90, 180);
		gfx.drawLine(bound.x+13, bound.y, bound.x+53, bound.y);
		gfx.drawLine(bound.x+13, bound.y + 26, bound.x+53, bound.y+26);
		gfx.drawLine(bound.x+53, bound.y, bound.x+53, bound.y+26);
		gfx.setBackgroundColor(historyColorA);
		gfx.fillRectangle(bound.x + 44-3-6-3-6, bound.y + 3, 6, 20);
		gfx.setBackgroundColor(historyColorB);
		gfx.fillRectangle(bound.x + 44-3-6, bound.y + 3, 6, 20);
		gfx.setBackgroundColor(historyColorC);
		gfx.fillRectangle(bound.x + 44, bound.y + 3, 6, 20);
		gfx.setBackgroundColor(liveColor);
		gfx.fillOval(bound.x+3, bound.y+3, 20, 20);
		_crossedOutAdapter.paint(gfx);
	    _rhombusAdapter.paint(gfx);
	}

	@Override
	@SuppressWarnings({ "rawtypes" })
	public Object getAdapter(final Class adapter) {
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
	
	public void setLiveColor(Color newColor){
		liveColor = newColor;
	}
	
	public void setHistoryColorA(Color newColor){
		historyColorA = newColor;
	}
	
	public void setHistoryColorB(Color newColor){
		historyColorB = newColor;
	}
	
	public void setHistoryColorC(Color newColor){
		historyColorC = newColor;
	}
	
	public Color getHistoryColorA(){
		return historyColorA;
	}
	
	public Color getHistoryColorB(){
		return historyColorB;
	}
	
	public Color getHistoryColorC(){
		return historyColorC;
	}
	
}