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

package uk.ac.gda.dal.csswidgets.figures;

import java.io.InputStream;

import org.csstudio.sds.ui.figures.BorderAdapter;
import org.csstudio.sds.ui.figures.CrossedOutAdapter;
import org.csstudio.sds.ui.figures.IBorderEquippedWidget;
import org.csstudio.sds.ui.figures.ICrossedFigure;
import org.csstudio.sds.ui.figures.IRhombusEquippedWidget;
import org.csstudio.sds.ui.figures.RhombusAdapter;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public final class SlitsFigure extends Shape implements IAdaptable {
	private IBorderEquippedWidget _borderAdapter;
    private CrossedOutAdapter _crossedOutAdapter;
    private RhombusAdapter _rhombusAdapter;
	private IPath _slit_path = new Path("/oe images/blade.png");
	private IPath _upArrowPath = new Path("/arrow images/up.png");
	private IPath _downArrowPath = new Path("/arrow images/down.png");
	private IPath _xPlusArrowPath = new Path("/arrow images/x_plus.png");
	private IPath _xMinusArrowPath = new Path("/arrow images/x_minus.png");
	private Image _slitImage = null;
	private Image _upArrowImage = null;
	private Image _downArrowImage = null;
	private Image _xPlusArrowImage = null;
	private Image _xMinusArrowImage = null;
	private int _imgWidth = 0;
	private int _imgHeight = 0;
	private int _xTranslateX = 0;
	private int _xTranslateY = 0;
	private int _yTranslate = 0;
	private int _Vgap = 0;
	private int _Hgap = 0;
	private int _slitType = 0;
	private Rectangle bound = getBounds().getCopy();

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

		InputStream slitImageStream = getClass().getResourceAsStream(_slit_path.toString());
		_slitImage = new Image(Display.getDefault(), slitImageStream);

		InputStream upArrowImageStream = getClass().getResourceAsStream(_upArrowPath.toString());
		_upArrowImage = new Image(Display.getDefault(), upArrowImageStream);

		InputStream downArrowImageStream = getClass().getResourceAsStream(_downArrowPath.toString());
		_downArrowImage = new Image(Display.getDefault(), downArrowImageStream);

		InputStream xPlusArrowImageStream = getClass().getResourceAsStream(_xPlusArrowPath.toString());
		_xPlusArrowImage = new Image(Display.getDefault(), xPlusArrowImageStream);
		
		InputStream xMinusArrowImageStream = getClass().getResourceAsStream(_xMinusArrowPath.toString());
		_xMinusArrowImage = new Image(Display.getDefault(), xMinusArrowImageStream);
		
		_imgWidth = _slitImage.getBounds().width;
		_imgHeight = _slitImage.getBounds().height;

		if (getSlitType() == 0) {

			gfx.drawImage(_slitImage, 0, 0, _imgWidth, _imgHeight, bound.x + _xTranslateX + 4 + 80, bound.y
					+ _xTranslateY - 5 - _Vgap / 2 + 80 + _yTranslate, _imgWidth, _imgHeight);

			gfx.drawImage(_slitImage, 0, 0, _imgWidth, _imgHeight, bound.x + _xTranslateX + 78, bound.y + _xTranslateY
					+ _Vgap / 2 + 160 + _yTranslate, _imgWidth, _imgHeight);

			gfx.drawImage(_upArrowImage, bound.x + _xTranslateX + 103, bound.y + _xTranslateY + _Vgap / 2 + 162
					+ _yTranslate + 18);

			gfx.drawImage(_downArrowImage, bound.x + _xTranslateX + 4 + 80 + 21, bound.y + _xTranslateY - 3 - _Vgap / 2
					+ 80 + _yTranslate + 40);
		}

		if (getSlitType() == 1) {

			gfx.drawImage(_slitImage, 0, 0, _imgWidth, _imgHeight, bound.x + _xTranslateX + 84, bound.y
					+ _xTranslateY - 3 - _Vgap / 2 + 80 + _yTranslate, _imgWidth, _imgHeight);

			gfx.drawImage(_slitImage, 0, 0, _imgWidth, _imgHeight, bound.x + _xTranslateX + 78, bound.y + _xTranslateY
					+ _Vgap / 2 + 162 + _yTranslate, _imgWidth, _imgHeight);

			gfx.drawImage(_slitImage, 0, 0, _imgWidth, _imgHeight, bound.x + _xTranslateX + (_Hgap / 2) + 110,
					bound.y + _xTranslateY + 135 + (int) ((_Hgap / 2) * 0.53846) + _yTranslate, _imgWidth, _imgHeight);

			gfx.drawImage(_slitImage, 0, 0, _imgWidth, _imgHeight, bound.x + _xTranslateX - (_Hgap / 2) + 45,
					bound.y + _xTranslateY + 107 - (int) ((_Hgap / 2) * 0.53846) + _yTranslate, _imgWidth, _imgHeight);

			
			gfx.drawImage(_upArrowImage, bound.x + _xTranslateX + 78 + 20 + 5, bound.y + _xTranslateY + _Vgap / 2 + 162
					+ _yTranslate + 15 - 2);

			gfx.drawImage(_downArrowImage, bound.x + _xTranslateX + 4 + 80 + 21, bound.y + _xTranslateY - 3 - _Vgap / 2
					+ 80 + _yTranslate + 40 - 2);
			
			
			gfx.drawImage(_xMinusArrowImage, 
					bound.x + _xTranslateX + (_Hgap / 2) + 111,
					bound.y + _xTranslateY + 135 + (int) ((_Hgap / 2) * 0.53846) + _yTranslate + 37 - 2);

			gfx.drawImage(_xPlusArrowImage, 
					bound.x + _xTranslateX - (_Hgap / 2) +57,
					bound.y + _xTranslateY + 107 - (int) ((_Hgap / 2) * 0.53846) + _yTranslate + 45 - 2);
		}
	}

	/**
	 * Returns the path to the image.
	 * 
	 * @return The path to the image
	 */
	public IPath getVGapUpPath() {
		return _upArrowPath;
	}
	
	public IPath getVGapDownPath() {
		return _downArrowPath;
	}
	
	public IPath getHGapPlusPath() {
		return _xPlusArrowPath;
	}
	
	public IPath getHGapMinusPath() {
		return _xMinusArrowPath;
	}

	public void setXTranslate(int newval) {
		_xTranslateX = newval;
		_xTranslateY = (int) (newval * 0.53846);
	}

	public Point getXTranslate() {
		return new Point(_xTranslateX, _xTranslateY);
	}

	public void setYTranslate(int newval) {
		_yTranslate = -(int) (newval * 1.3571428571428572);
	}

	public int getYTranslate() {
		return _yTranslate;
	}

	public void setVGap(int newval) {
		_Vgap = newval;
	}

	public int getVGap() {
		return _Vgap;
	}

	public void setHGap(int newval) {
		_Hgap = newval;
	}

	public int getHGap() {
		return _Hgap;
	}

	public void setSlitType(int newval) {
		_slitType = newval;
	}

	public int getSlitType() {
		return _slitType;
	}

	public void setVGapUpPath(IPath path) {
		_upArrowPath = path;
	}
	
	public void setVGapDownPath(IPath path) {
		_downArrowPath = path;
	}
	
	public void setHGapMinusPath(IPath path) {
		_xMinusArrowPath = path;
	}
	
	public void setHGapPlusPath(IPath path) {
		_xPlusArrowPath = path;
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
}