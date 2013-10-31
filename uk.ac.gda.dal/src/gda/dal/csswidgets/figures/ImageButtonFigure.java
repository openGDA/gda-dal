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
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public final class ImageButtonFigure extends Shape implements IAdaptable {
	private IBorderEquippedWidget _borderAdapter;
	private CrossedOutAdapter _crossedOutAdapter;
	private RhombusAdapter _rhombusAdapter;
	private int style = 0;
	private String _path = "";
	private String label = "";
	private Image _image = null;
	private Color color = new Color(null, 143, 186, 210);

	@Override
	protected boolean useLocalCoordinates() {
		return true;
	}

	public void setColor(Color newColor) {
		color = newColor;
		repaint();
	}

	@Override
	protected void fillShape(final Graphics gfx) {
	}

	@Override
	protected void outlineShape(final Graphics gfx) {
	}

	@Override
	public void paintFigure(final Graphics gfx) {
		int width = this.getSize().width;
		int height = this.getSize().height;
		int x = this.getLocation().x;
		int y = this.getLocation().y;
		gfx.setBackgroundColor(color);
		Font defaultFont = gfx.getFont();
		if (style == 0) {
			gfx.fillRoundRectangle(new Rectangle(x, y, width - 1, height - 1), 22, 22);
			gfx.setFont(defaultFont);
		}
		if (style == 1) {
			gfx.fillRectangle(new Rectangle(x, y, width - 1, height - 1));
			gfx.setFont(new Font(null, "courier", 11, 0));
		}
		if (!_path.isEmpty()) {
			InputStream imageStream = getClass().getResourceAsStream(_path.toString());
			_image = new Image(Display.getDefault(), imageStream);
			gfx.drawImage(_image, x + ((width / 2) - (_image.getImageData().width / 2)), y
					+ ((height / 2) - (_image.getImageData().height / 2)));
		}
		gfx.drawString(label, x - 6, y + 1);
	}

	public void setFilePath(String newval) {
		_path = newval;
	}

	public void setLabel(String newval) {
		label = newval;
	}

	public String getFilePath() {
		return _path;
	}

	public void setStyle(int style) {
		this.style = style;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(final Class adapter) {
		if (adapter == IBorderEquippedWidget.class) {
			if (_borderAdapter == null)
				_borderAdapter = new BorderAdapter(this);
			return _borderAdapter;
		} 
		else if (adapter == ICrossedFigure.class) {
			if (_crossedOutAdapter == null)
				_crossedOutAdapter = new CrossedOutAdapter(this);
			return _crossedOutAdapter;
		} 
		else if (adapter == IRhombusEquippedWidget.class) {
			if (_rhombusAdapter == null)
				_rhombusAdapter = new RhombusAdapter(this);
			return _rhombusAdapter;
		}
		return null;
	}
	
}