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
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureListener;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.FreeformViewport;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayeredPane;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.ScalableFreeformLayeredPane;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.editparts.ZoomManager;

/**
 * A widget that can contain multiple movable linking containers.
 */
public final class MoveableWindowFigure extends Panel implements IAdaptable {
	private IBorderEquippedWidget _borderAdapter;
    private CrossedOutAdapter _crossedOutAdapter;
    private RhombusAdapter _rhombusAdapter;
	public ScalableFreeformLayeredPane _pane;
	private ZoomManager _zoomManager;
	public FreeformViewport _freeformViewport;
	private boolean autoFit;
	private ScrollPane scrollpane;

	@SuppressWarnings("deprecation")
	public MoveableWindowFigure() {
		XYLayout layout = new XYLayout();
		setLayoutManager(layout);
		scrollpane = new ScrollPane();
		scrollpane.setScrollBarVisibility(ScrollPane.NEVER);
		scrollpane.setFocusTraversable(false);
		_freeformViewport = new FreeformViewport();
		_freeformViewport.setSize(40, 40);
		scrollpane.setViewport(_freeformViewport);
		add(scrollpane);
		_pane = new ScalableFreeformLayeredPane();
		_pane.setLayoutManager(new FreeformLayout());
		scrollpane.setContents(_pane);
		_zoomManager = new ZoomManager(_pane, _freeformViewport);
		setForegroundColor(ColorConstants.blue);
		setOpaque(true);
		addFigureListener(new FigureListener() {
			@Override
			public void figureMoved(final IFigure source) {
				updateChildConstraints();
				updateZoom();
			}
		});
		updateChildConstraints();
		updateZoom();
	}

	private void updateChildConstraints() {
		final Rectangle figureBounds = getBounds();
		Rectangle r = new Rectangle(0, 0, figureBounds.width, figureBounds.height);
		_freeformViewport.setSize(r.width, r.height);
		_pane.setSize(new Dimension(r.width, r.height));
		setConstraint(scrollpane, r);
	}
	
	/**
	 * Returns the content pane.
	 * 
	 * @return IFigure The content pane.
	 */
	public LayeredPane getContentsPane() {
		return _pane;
	}

	/**
	 * Refreshes the zoom.
	 */
	public void updateZoom() {
		_zoomManager.setZoom(1.0);
		if (autoFit)
			_zoomManager.setZoomAsText(ZoomManager.FIT_ALL);
	}

	@Override
	protected boolean useLocalCoordinates() {
		return true;
	}

	/**
	 * @param autoFit
	 */
	public void setAutoFit(boolean autoFit) {
		this.autoFit = autoFit;
		updateZoom();
	}

	@SuppressWarnings("rawtypes")
	@Override
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