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

import gda.jython.JythonServerFacade;

import java.util.List;

import org.csstudio.sds.model.AbstractWidgetModel;
import org.csstudio.sds.model.WidgetProperty;
import org.csstudio.sds.model.properties.actions.AbstractWidgetActionModel;
import org.csstudio.sds.model.properties.actions.CommitValueActionModel;
import org.csstudio.sds.ui.editparts.AbstractWidgetEditPart;
import org.csstudio.sds.ui.editparts.IWidgetPropertyChangeHandler;
import org.csstudio.sds.ui.widgetactionhandler.WidgetActionHandlerService;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import uk.ac.gda.dal.CameraAction;
import uk.ac.gda.dal.csswidgets.figures.ImageButtonFigure;
import uk.ac.gda.dal.csswidgets.model.ImageButtonModel;

/**
 * A button widget that includes an image icon.
 */
public class ImageButtonEditPart extends AbstractWidgetEditPart {
	public ImageButtonModel model;

	/**
	 * Returns the casted model. This is just for convenience.
	 * 
	 * @return the casted {@link ImageButtonModel}
	 */
	@Override
	public ImageButtonModel getCastedModel() {
		return (ImageButtonModel) getModel();
	}

	private void handleLeftButtonClick(MouseEvent me){
		ImageButtonModel model = (ImageButtonModel) getWidgetModel();
		String serverCommand = model.getServerCommand();
		String serverCommandVariable = model.getServerCommandVariable();
		String placeHolder = "var";
		
		if(!serverCommandVariable.equals("") && serverCommand.contains(placeHolder))
			serverCommand = serverCommand.substring(0, serverCommand.indexOf(placeHolder)) + serverCommandVariable + serverCommand.substring(serverCommand.indexOf(placeHolder) + placeHolder.length());
		
		String id = model.getViewID();
		
		if (id.length() < 2) {
			me.consume();
            final ImageButtonModel widget = (ImageButtonModel) getWidgetModel();
            
			List<AbstractWidgetActionModel> actions = widget.getActionData().getWidgetActions();
            final AbstractWidgetActionModel action = actions.get(0);
            Display.getCurrent().asyncExec(new Runnable() {
                @Override
                public void run() {
                    WidgetActionHandlerService.getInstance().performAction(widget, action);
                }
            });
		}
		
		else {
			String id1;
			String id2;
			CameraAction anv;
			if(model.getViewID().contains(":")){
				id1 = model.getViewID().substring(0,model.getViewID().indexOf(":"));
				id2 = model.getViewID().substring(model.getViewID().indexOf(":")+1);
				anv = new CameraAction(id1, id2); 
			}
			else
				anv = new CameraAction(model.getViewID(), "hello world"); 
			anv.run();  
		}
	}
	
	@Override
	protected IFigure doCreateFigure() {
		model = getCastedModel();
		final ImageButtonFigure figure = new ImageButtonFigure();
		figure.setStyle(model.getIntegerProperty(ImageButtonModel.PROP_STYLE));
		String colourHex = model.getButtonColour();
		int r = Integer.parseInt(colourHex.substring(1,3), 16);
		int g = Integer.parseInt(colourHex.substring(3,5), 16);
		int b = Integer.parseInt(colourHex.substring(5,7), 16);
		figure.setColor(new Color(null, r,g,b));
		figure.setFilePath(model.getFilename().toString());
		figure.setLabel(model.getLabel());
		if (model.isLive()) {
			figure.addMouseListener(new MouseListener.Stub() {
				@Override
				public void mousePressed(MouseEvent me) {
					if (me.button == 1) {
						handleLeftButtonClick(me);
					}
				}
			});

			figure.addMouseMotionListener(new MouseMotionListener.Stub() {
				@Override
				public void mouseEntered(MouseEvent me) {
					String colourHex = model.getOverColour();
					int r = Integer.parseInt(colourHex.substring(1,3), 16);
					int g = Integer.parseInt(colourHex.substring(3,5), 16);
					int b = Integer.parseInt(colourHex.substring(5,7), 16);
					figure.setColor(new Color(null, r,g,b));
					
				}

				@Override
				public void mouseExited(MouseEvent me) {
					String colourHex = model.getButtonColour();
					int r = Integer.parseInt(colourHex.substring(1,3), 16);
					int g = Integer.parseInt(colourHex.substring(3,5), 16);
					int b = Integer.parseInt(colourHex.substring(5,7), 16);
					figure.setColor(new Color(null, r,g,b));
				}
			});
		}
		return figure;
	}

	@Override
	protected void registerPropertyChangeHandlers() {
		// changes to the filename property
		IWidgetPropertyChangeHandler handle = new IWidgetPropertyChangeHandler() {
			@Override
			public boolean handleChange(final Object oldValue, final Object newValue, final IFigure figure) {
				ImageButtonFigure testFigure = (ImageButtonFigure) figure;
				testFigure.setFilePath(newValue.toString());
				return true;
			}
		};
		setPropertyChangeHandler(ImageButtonModel.PROP_FILENAME, handle);

		handle = new IWidgetPropertyChangeHandler() {
			@Override
			public boolean handleChange(final Object oldValue, final Object newValue, final IFigure figure) {
				ImageButtonFigure imageButtonFigure = (ImageButtonFigure) figure;
				imageButtonFigure.setLabel(newValue.toString());
				imageButtonFigure.repaint();
				return true;
			}
		};
		setPropertyChangeHandler(ImageButtonModel.PROP_LABEL, handle);
	}
	
}