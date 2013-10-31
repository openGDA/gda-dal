/*-
 * Copyright © 2011 Diamond Light Source Ltd.
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

import gda.dal.csswidgets.model.MoveableTitleBarModel;

import java.util.List;

import org.csstudio.sds.model.AbstractWidgetModel;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Dimension;

public class MoveableWindow {
	private String name;
	private List<AbstractWidgetModel> box;
	private RectangleFigure rectangle;
	private RectangleFigure button;
	private MoveableTitleBarModel titleBarModel;
	private MoveableTitleBarModel overTitleBarModel;
	private MouseListener mouseListener;
	private MouseMotionListener mouseMotionListener;
	private MouseListener closeListener;
	private Dimension boxSize;
	private MouseMotionListener closeButtonHoverListener;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public List<AbstractWidgetModel> getBox() {
		return box;
	}
	
	public void setBox(List<AbstractWidgetModel> box) {
		this.box = box;
	}
	
	public RectangleFigure getRectangle() {
		return rectangle;
	}
	
	public void setRectangle(RectangleFigure rectangle) {
		this.rectangle = rectangle;
	}
	
	public RectangleFigure getButton() {
		return button;
	}
	
	public void setButton(RectangleFigure button) {
		this.button = button;
	}
	
	public MoveableTitleBarModel getTitleBarModel() {
		return titleBarModel;
	}
	
	public void setTitleBarModel(MoveableTitleBarModel titleBarModel) {
		this.titleBarModel = titleBarModel;
	}
	
	public MoveableTitleBarModel getOverTitleBarModel() {
		return overTitleBarModel;
	}
	
	public void setOverTitleBarModel(MoveableTitleBarModel overTitleBarModel) {
		this.overTitleBarModel = overTitleBarModel;
	}
	
	public MouseListener getMouseListener() {
		return mouseListener;
	}
	
	public void setMouseListener(MouseListener mouseListener) {
		this.mouseListener = mouseListener;
	}
	public MouseMotionListener getMouseMotionListener() {
		return mouseMotionListener;
	}
	
	public void setMouseMotionListener(MouseMotionListener mouseMotionListener) {
		this.mouseMotionListener = mouseMotionListener;
	}
	
	public MouseListener getCloseListener() {
		return closeListener;
	}
	
	public void setCloseListener(MouseListener closeListener) {
		this.closeListener = closeListener;
	}
	
	public Dimension getBoxSize() {
		return boxSize;
	}
	
	public void setBoxSize(Dimension boxSize) {
		this.boxSize = boxSize;
	}
	
	public MouseMotionListener getCloseButtonHoverListener() {
		return closeButtonHoverListener;
	}
	
	public void setCloseButtonHoverListener(MouseMotionListener closeButtonHoverListener) {
		this.closeButtonHoverListener = closeButtonHoverListener;
	}
	
}