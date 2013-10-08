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

/**
 * An image figure that supports SVG.
 * 
 */
public final class RefreshableSVGFigure extends Shape implements IAdaptable {

	private IBorderEquippedWidget _borderAdapter;
    private CrossedOutAdapter _crossedOutAdapter;
    private RhombusAdapter _rhombusAdapter;
	private IPath _path = new Path("");
	private Image _image = null;
	private int _imgWidth = 0;
	private int _imgHeight = 0;
	private int _yTranslate = 0;
	private int _xTranslateX = 0;
	private int _xTranslateY = 0;
	private Rectangle bound = getBounds().getCopy();
	/**
	 * We want to have local coordinates here.
	 * 
	 * @return True if here should used local coordinates
	 */
	@Override
	protected boolean useLocalCoordinates() {
		return true;
	}

	/**
	 * Fills the image. Nothing to do here.
	 * 
	 * @param gfx
	 *            The {@link Graphics} to use
	 */
	@Override
	protected void fillShape(final Graphics gfx) {
	}

	/**
	 * Draws the outline of the image. Nothing to do here.
	 * 
	 * @param gfx
	 *            The {@link Graphics} to use
	 */
	@Override
	protected void outlineShape(final Graphics gfx) {
	}

	/**
	 * The main drawing routine.
	 * 
	 * @param gfx
	 *            The {@link Graphics} to use
	 */
	@Override
	public void paintFigure(final Graphics gfx) {

		bound = getBounds().getCopy();
		
		bound.crop(this.getInsets());

		try {
			if (_image == null && !_path.isEmpty()) {

				final InputStream imageStream = getClass().getResourceAsStream(_path.toString());
				_image = new Image(Display.getDefault(), imageStream);

				_imgWidth = _image.getBounds().width;
				_imgHeight = _image.getBounds().height;
			}
		} catch (Exception e) {
			if (_image != null) {
				_image.dispose();
			}
		}
		if (_image != null) {
			
			gfx.drawImage(_image, 0, 0, _imgWidth, _imgHeight, bound.x+_xTranslateX,
					bound.y+_yTranslate+_xTranslateY, _imgWidth, _imgHeight);
		}
	}

	/**
	 * Sets the path to the image.
	 * 
	 * @param newval
	 *            The path to the image
	 */
	public void setFilePath(final IPath newval) {
		_path = newval;
		if (_image != null) {
			_image.dispose();
		}
		_image = null;
	}

	/**
	 * Returns the path to the image.
	 * 
	 * @return The path to the image
	 */
	public IPath getFilePath() {
		return _path;
	}
	
	public void setYTranslate(int newval){
		_yTranslate=newval;
	}
	
	public int getYTranslate(){
		return _yTranslate;
	}
	
	public void setXTranslate(int newval){
		_xTranslateX=newval;
		_xTranslateY=(int)(newval*0.53846);
	}
	
	public Point getXTranslate(){
		return new Point(_xTranslateX, _xTranslateY);
	}


	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(final Class adapter) {
		if (adapter == IBorderEquippedWidget.class) {
			if(_borderAdapter==null) {
				_borderAdapter = new BorderAdapter(this);
			}
			return _borderAdapter;
		} else if(adapter == ICrossedFigure.class) {
            if(_crossedOutAdapter==null) {
                _crossedOutAdapter = new CrossedOutAdapter(this);
            }
            return _crossedOutAdapter;
        } else if(adapter == IRhombusEquippedWidget.class) {
            if(_rhombusAdapter==null) {
                _rhombusAdapter = new RhombusAdapter(this);
            }
            return _rhombusAdapter;
        }

		return null;
	}
}
