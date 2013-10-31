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
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * An image figure that supports SVG.
 */
public final class AttenuatorFigure extends Shape implements IAdaptable {
	private IBorderEquippedWidget _borderAdapter;
	private CrossedOutAdapter _crossedOutAdapter;
	private RhombusAdapter _rhombusAdapter;
	private IPath _path = new Path("/oe images/attenuator_single_block.png");
	private IPath empty_block_path = new Path("/oe images/attenuator_single_block_empty.png");
	private IPath arrowPath = new Path("/arrow images/y.png");
	private Image _image = null;
	private Image emptyBlockImage = null;
	private Image _arrowImage = null;
	private int _yTranslate = 0;
	private int _noBlocks = 6;
	private int _emptyBlockPos = 5;
	private int _yOffset = 0;
	private Rectangle bound;

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
		InputStream arrowImageStream = getClass().getResourceAsStream(arrowPath.toString());
		_arrowImage = new Image(Display.getDefault(), arrowImageStream);
		
		try {
			if (_image == null && !_path.isEmpty()) {
				InputStream imageStream = getClass().getResourceAsStream(_path.toString());
				_image = new Image(Display.getDefault(), imageStream);
				InputStream emptyBlockStream = getClass().getResourceAsStream(empty_block_path.toString());
				emptyBlockImage = new Image(Display.getDefault(), emptyBlockStream);
			}
		} catch (Exception e) {
			if (_image != null)
				_image.dispose();
		}
		if (_image != null) {
			int blockHeight = 30;
			if(_emptyBlockPos!=0){
				_yOffset=(_noBlocks-1)*blockHeight;
				_yTranslate = -Math.abs(_yTranslate);
			}
			else
				_yOffset=0;
			for (int i = 0; i < _noBlocks; i++) {
				if(i==_noBlocks-_emptyBlockPos-1)
					gfx.drawImage(emptyBlockImage, bound.x, (_noBlocks*blockHeight) + bound.y - (i*blockHeight) - blockHeight + _yTranslate + _yOffset);
				else
					gfx.drawImage(_image, bound.x, (_noBlocks*blockHeight) + bound.y - (i*blockHeight) - blockHeight + _yTranslate + _yOffset);
			}
			gfx.drawImage(_arrowImage, bound.x+40, bound.y + 16 + _yTranslate + _yOffset);
		}
	}

	public void setYTranslate(int newval) {
		_yTranslate = newval * 30;
	}

	public int getYTranslate() {
		return _yTranslate;
	}

	public void setNoBlocks(int newval) {
		_noBlocks = newval;
	}
	
	public void setEmptyBlockPos(int newval) {
		_emptyBlockPos = newval;
	}

	public IPath getArrowPath() {
		return arrowPath;
	}

	public void setArrowPath(IPath arrowPath) {
		this.arrowPath = arrowPath;
	}

	@Override
	public Object getAdapter(final Class adapter) {
		if (adapter == IBorderEquippedWidget.class) {
			if(_borderAdapter==null)
				_borderAdapter = new BorderAdapter(this);
			return _borderAdapter;
		} 
		else if(adapter == ICrossedFigure.class) {
            if(_crossedOutAdapter==null)
                _crossedOutAdapter = new CrossedOutAdapter(this);
            return _crossedOutAdapter;
        } 
		else if(adapter == IRhombusEquippedWidget.class) {
            if(_rhombusAdapter==null)
                _rhombusAdapter = new RhombusAdapter(this);
            return _rhombusAdapter;
        }
		return null;
	}
	
}