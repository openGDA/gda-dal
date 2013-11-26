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

import java.text.DecimalFormat;

import org.csstudio.sds.ui.figures.BorderAdapter;
import org.csstudio.sds.ui.figures.CrossedOutAdapter;
import org.csstudio.sds.ui.figures.IBorderEquippedWidget;
import org.csstudio.sds.ui.figures.ICrossedFigure;
import org.csstudio.sds.ui.figures.IRhombusEquippedWidget;
import org.csstudio.sds.ui.figures.RhombusAdapter;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

/**
 * An image figure that supports SVG.
 */
public final class ScannableControlFigure extends Shape implements IAdaptable {
	private Rectangle demandBox;
	private Rectangle readbackBox;
	private double demand;
	private double readback;
    private CrossedOutAdapter _crossedOutAdapter;
    private RhombusAdapter _rhombusAdapter;
    private boolean amIHolder;
    private boolean demandSet = false;
    private CellEditor demandCellEditor;
    private CellEditor incrementCellEditor;
    private CellEditor offsetCellEditor;
	private int boxWidth = 60;
	private int dp=5;
	private IBorderEquippedWidget _borderAdapter;
	private Rectangle bound = getBounds().getCopy();

	public ScannableControlFigure(boolean baton) {
		amIHolder = baton;
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
		if(!demandSet)
			demand = readback;
		bound = getBounds().getCopy();
		gfx.setForegroundColor(new Color(null, 0, 0, 0));
		readbackBox = new Rectangle(bound.x + 19, bound.y, boxWidth, 19);
		gfx.drawRectangle(readbackBox); // readback
		gfx.drawText(String.valueOf(readback), bound.x + 23, bound.y + 2);
		demandBox = new Rectangle(bound.x + 19, bound.y + 19, boxWidth, 19);
		if (amIHolder) {
			gfx.setBackgroundColor(new Color(null, 255, 255, 255));
			gfx.drawRectangle(demandBox); // demand
			gfx.drawText(String.valueOf(demand), bound.x + 23, bound.y + 21);
		} else {
			gfx.setBackgroundColor(new Color(null, 200, 200, 200));
			gfx.fillRectangle(demandBox); // demand
			gfx.drawText(String.valueOf(demand), bound.x + 23, bound.y + 21);
		}
	}

	public Rectangle getDemandBox() {
		return demandBox;
	}

	public void showDemand(int x, int y) {
		int width = boxWidth-1;
		int height = 18;
		x=x+19;
		y=y+19;
		demandCellEditor.getControl().setBounds(x, y, width,height);
		demandCellEditor.getControl().setLayoutData(new GridData(SWT.CENTER));
		demandCellEditor.getControl().setVisible(true);
		demandCellEditor.getControl().setLocation(x, y);
		demandCellEditor.getControl().setBackground(new Color(null, 200, 200, 200));
		demandCellEditor.activate();
		demandCellEditor.setFocus();
	}
	
	public void showIncrement(int x, int y){
		int cellWidth = 59;
		int cellHeight = 18;
		incrementCellEditor.getControl().setBounds(x, y, cellWidth,cellHeight);
		incrementCellEditor.getControl().setLayoutData(new GridData(SWT.CENTER));
		incrementCellEditor.getControl().setVisible(true);
		incrementCellEditor.getControl().setLocation(x, y);
		incrementCellEditor.getControl().setBackground(new Color(null, 200, 200, 200));
		incrementCellEditor.activate();
		incrementCellEditor.setFocus();
	}
	
	public void showOffset(int x, int y){
		int cellWidth = 59;
		int cellHeight = 18;
		
		offsetCellEditor.getControl().setBounds(x, y, cellWidth,cellHeight);
		offsetCellEditor.getControl().setLayoutData(new GridData(SWT.CENTER));
		offsetCellEditor.getControl().setVisible(true);
		offsetCellEditor.getControl().setLocation(x, y);
		offsetCellEditor.getControl().setBackground(new Color(null, 200, 200, 200));
		offsetCellEditor.activate();
		offsetCellEditor.setFocus();
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

	public void setDemand(Double newDemand) {
		demandSet = true;
		demand = newDemand;
		DecimalFormat df = new DecimalFormat("#.#####");
		demand = Double.parseDouble(df.format(demand));
		repaint();
	}

	public double getDemand() {
		return demand;
	}

	public double getReadback() {
		return readback;
	}

	public void setReadback(double newReadback) {
		DecimalFormat df = new DecimalFormat("#.#####");
		df.setMaximumFractionDigits(dp);
		readback = Double.parseDouble(df.format(newReadback));
	}

	public void setBatton(boolean newVal) {
		amIHolder = newVal;
	}

	public void setBoxWidth(int boxWidth) {
		this.boxWidth = boxWidth;
		repaint();
	}
	
	public void setDP(int val){
		dp=val;
	}

	public CellEditor getDemandCellEditor() {
		return demandCellEditor;
	}

	public CellEditor getIncrementCellEditor() {
		return incrementCellEditor;
	}

	public void setDemandCellEditor(CellEditor demandCellEditor) {
		this.demandCellEditor = demandCellEditor;
	}

	public void setIncrementCellEditor(CellEditor incrementCellEditor) {
		this.incrementCellEditor = incrementCellEditor;
	}

	public CellEditor getOffsetCellEditor() {
		return offsetCellEditor;
	}

	public void setOffsetCellEditor(CellEditor offsetCellEditor) {
		this.offsetCellEditor = offsetCellEditor;
	}
	
}