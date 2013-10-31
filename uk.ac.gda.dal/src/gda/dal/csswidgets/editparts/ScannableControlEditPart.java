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

package gda.dal.csswidgets.editparts;

import gda.dal.csswidgets.figures.ScannableControlFigure;
import gda.dal.csswidgets.model.ScannableControlModel;
import gda.jython.InterfaceProvider;
import gda.jython.batoncontrol.BatonChanged;
import gda.observable.IObserver;
import java.util.List;
import org.csstudio.sds.model.AbstractWidgetModel;
import org.csstudio.sds.model.WidgetProperty;
import org.csstudio.sds.model.properties.actions.AbstractWidgetActionModel;
import org.csstudio.sds.ui.editparts.AbstractWidgetEditPart;
import org.csstudio.sds.ui.editparts.IWidgetPropertyChangeHandler;
import org.csstudio.sds.ui.widgetactionhandler.WidgetActionHandlerService;
import org.eclipse.draw2d.ActionEvent;
import org.eclipse.draw2d.ActionListener;
import org.eclipse.draw2d.ArrowButton;
import org.eclipse.draw2d.Button;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.viewers.ICellEditorListener;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

public class ScannableControlEditPart extends AbstractWidgetEditPart {
	private ScannableControlFigure scannableControlFigure;
	private double incrementVal = 1.0;
	private double offset = 0.0;
	private ArrowButton increment = new ArrowButton(PositionConstants.EAST);
	private ArrowButton decrement = new ArrowButton(PositionConstants.WEST);
	private Button goReal = new Button("Go Real");
	private Button goSim = new Button("Go Sim");
	private Boolean amIHolder = InterfaceProvider.getBatonStateProvider().amIBatonHolder();
	private Label label = new Label();
	private RectangleFigure border = new RectangleFigure();
	private String tooltipText;
	private ScannableControlModel model;
	private int wholeDigits=1;
	private int dp=4;
	private boolean showButtons=false;

	/**
	 * Returns the casted model. This is just for convenience.
	 * 
	 * @return the casted {@link ScannableControlModel}
	 */
	@Override
	protected ScannableControlModel getCastedModel() {
		return (ScannableControlModel) getModel();
	}

	@Override
	protected IFigure doCreateFigure() {
		model = getCastedModel();
		incrementVal = model.getDefaultIncrement();
		scannableControlFigure = new ScannableControlFigure(amIHolder);
		dp = model.getDP();
		scannableControlFigure.setDP(dp);
		scannableControlFigure.setBoxWidth(15+(dp*10)+((wholeDigits-1)*10));
		if (model.isLive()) {
			scannableControlFigure.addMouseListener(new MouseListener.Stub() {
				@Override
				public void mousePressed(MouseEvent me) {
					me.consume();
					if (me.button == 1 && amIHolder) {
						Rectangle mouseBound = new Rectangle(me.x, me.y, 1, 1);
						if (scannableControlFigure.getDemandBox().intersects(mouseBound)) {
							Rectangle rect = ScannableControlEditPart.this.scannableControlFigure.getBounds().getCopy();
							rect.x = rect.x + 1;
							rect.y = rect.y + 1;
							rect.height = rect.height - 1;
							rect.width = rect.width - 1;
							getFigure().translateToAbsolute(rect);
							scannableControlFigure.showDemand(rect.x, rect.y);
						}
					}
				}
			});
		}

		increment.setLocation(new Point(35+(dp*10)+((wholeDigits-1)*10), 19));
		increment.setVisible(true);
		increment.setSize(20, 20);
		increment.setEnabled(amIHolder);
		increment.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (amIHolder) {
					double upperBound = model.getHighLimit();
					if (model.getReadbackValue() + incrementVal <= upperBound) {
						double incVal = incrementVal;
						model.setReadbackValue(scannableControlFigure.getDemand() + incVal);
						scannableControlFigure.setDemand(scannableControlFigure.getDemand() + incVal);
					}
				}
			}
		});

		scannableControlFigure.setIncrementCellEditor(new TextCellEditor((Composite) getViewer().getControl()));

		if (model.isLive()) {

			increment.addMouseListener(new MouseListener.Stub() {
				@Override
				public void mousePressed(MouseEvent me) {
					me.consume();
					if (me.button == 2) {
						Rectangle rect = ScannableControlEditPart.this.scannableControlFigure.getBounds().getCopy();
						rect.x = rect.x + 1;
						rect.y = rect.y + 1;
						rect.height = rect.height - 1;
						rect.width = rect.width - 1;
						getFigure().translateToAbsolute(rect);
						border.setLocation(new Point(0, 0));
						border.setSize(rect.width, rect.height);
						border.setForegroundColor(new Color(null, 0, 0, 0));
						label.setText("Increment=" + incrementVal);
						label.setLocation(new Point(0, 0));
						label.setSize(rect.width, 20);
						label.setForegroundColor(new Color(null, 0, 0, 0));
						scannableControlFigure.add(border);
						scannableControlFigure.add(label);
						scannableControlFigure.showIncrement(rect.x + 19, rect.y + 19);
					}

					if (me.button == 3) {
						Rectangle rect = ScannableControlEditPart.this.scannableControlFigure.getBounds().getCopy();
						rect.x = rect.x + 1;
						rect.y = rect.y + 1;
						rect.height = rect.height - 1;
						rect.width = rect.width - 1;
						getFigure().translateToAbsolute(rect);
						border.setLocation(new Point(0, 0));
						border.setSize(rect.width, rect.height);
						border.setForegroundColor(new Color(null, 0, 0, 0));
						label.setText("Offset=" + offset);
						label.setLocation(new Point(0, 0));
						label.setSize(rect.width, 20);
						label.setForegroundColor(new Color(null, 0, 0, 0));
						scannableControlFigure.add(border);
						scannableControlFigure.add(label);
						scannableControlFigure.showOffset(rect.x + 19, rect.y + 19);
					}
				}
			});

			decrement.addMouseListener(new MouseListener.Stub() {
				@Override
				public void mousePressed(MouseEvent me) {
					me.consume();
					if (me.button == 2) {
						Rectangle rect = ScannableControlEditPart.this.scannableControlFigure.getBounds().getCopy();
						rect.x = rect.x + 1;
						rect.y = rect.y + 1;
						rect.height = rect.height - 1;
						rect.width = rect.width - 1;
						getFigure().translateToAbsolute(rect);
						border.setLocation(new Point(0, 0));
						border.setSize(rect.width, rect.height);
						border.setForegroundColor(new Color(null, 0, 0, 0));
						label.setText("Increment=" + incrementVal);
						label.setLocation(new Point(0, 0));
						label.setSize(rect.width, 20);
						label.setForegroundColor(new Color(null, 0, 0, 0));
						scannableControlFigure.add(border);
						scannableControlFigure.add(label);
						scannableControlFigure.showIncrement(rect.x + 19, rect.y + 19);
					}
					if (me.button == 3) {
						Rectangle rect = ScannableControlEditPart.this.scannableControlFigure.getBounds().getCopy();
						rect.x = rect.x + 1;
						rect.y = rect.y + 1;
						rect.height = rect.height - 1;
						rect.width = rect.width - 1;
						getFigure().translateToAbsolute(rect);
						border.setLocation(new Point(0, 0));
						border.setSize(rect.width, rect.height);
						border.setForegroundColor(new Color(null, 0, 0, 0));
						label.setText("Offset=" + offset);
						label.setLocation(new Point(0, 0));
						label.setSize(rect.width, 20);
						label.setForegroundColor(new Color(null, 0, 0, 0));
						scannableControlFigure.add(border);
						scannableControlFigure.add(label);
						scannableControlFigure.showOffset(rect.x + 19, rect.y + 19);
					}
				}
			});
		}

		scannableControlFigure.getIncrementCellEditor().addListener(new ICellEditorListener() {

			@Override
			public void applyEditorValue() {
				scannableControlFigure.remove(label);
				scannableControlFigure.remove(border);
				try {
					incrementVal = Double.parseDouble(scannableControlFigure.getIncrementCellEditor().getValue().toString());
					tooltipText = "Limits:Low=" + model.getLowLimit() + ",High=" + model.getHighLimit() + "\rUnits="
							+ model.getPropUnits() + "\rIncrement=" + incrementVal;
					model.setPropertyValue(AbstractWidgetModel.PROP_TOOLTIP, tooltipText);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void cancelEditor() {
			}

			@Override
			public void editorValueChanged(boolean oldValidState, boolean newValidState) {
			}
		});

		scannableControlFigure.setOffsetCellEditor(new TextCellEditor((Composite) getViewer().getControl()));
		scannableControlFigure.getOffsetCellEditor().addListener(new ICellEditorListener() {
			@Override
			public void applyEditorValue() {
				scannableControlFigure.remove(label);
				scannableControlFigure.remove(border);
				try {
					offset = Double.parseDouble(scannableControlFigure.getOffsetCellEditor().getValue().toString());
					model.setOffset(offset);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void cancelEditor() {
			}

			@Override
			public void editorValueChanged(boolean oldValidState, boolean newValidState) {
			}
		});

		scannableControlFigure.add(increment);

		decrement.setLocation(new Point(0, 19));
		decrement.setVisible(true);
		decrement.setSize(20, 20);
		decrement.setEnabled(amIHolder);
		decrement.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (amIHolder) {
					double lowerBound = model.getLowLimit();
					if (model.getReadbackValue() + incrementVal >= lowerBound) {
						double incVal = incrementVal;
						model.setReadbackValue(scannableControlFigure.getDemand() - incVal);
						scannableControlFigure.setDemand(scannableControlFigure.getDemand() - incVal);
					}
				}
			}
		});
		scannableControlFigure.add(decrement);

		InterfaceProvider.getBatonStateProvider().addBatonChangedObserver(new IObserver() {

			@Override
			public void update(Object source, Object arg) {
				if (arg instanceof BatonChanged) {
					amIHolder = InterfaceProvider.getBatonStateProvider().amIBatonHolder();
					Display display = PlatformUI.getWorkbench().getDisplay();
					if(!display.isDisposed()){
						display.asyncExec(new Runnable() {
							@Override
							public void run() {
								scannableControlFigure.setBatton(amIHolder);
								increment.setEnabled(amIHolder);
								decrement.setEnabled(amIHolder);
								scannableControlFigure.repaint();
							}
						});
					}
				}
			}
		});
		scannableControlFigure.setDemandCellEditor(new TextCellEditor((Composite) getViewer().getControl()));	
		
		final Text text = (Text) scannableControlFigure.getDemandCellEditor().getControl();
		text.addKeyListener(new KeyAdapter(){
			@Override
			public void keyPressed(KeyEvent e) {
				showButtons=model.isShowButtons();
				if (((e.keyCode == SWT.CR) || (e.keyCode == SWT.KEYPAD_CR)) && !showButtons) {
					String demandVal = scannableControlFigure.getDemandCellEditor().getValue().toString();
					double demand = Double.parseDouble(demandVal);
					if((demand>=model.getLowLimit())&&(demand<=model.getHighLimit())){
						scannableControlFigure.setDemand(demand);
						model.setReadbackValue(Double.parseDouble(scannableControlFigure.getDemandCellEditor().getValue().toString()));
					}
				}
			}
		});
		
		goReal.setLocation(new Point(55+(dp*10)+((wholeDigits-1)*10), 19));
		goReal.setVisible(model.isShowButtons());
		goReal.setSize(60, 20);
		goReal.setEnabled(amIHolder);
		goReal.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String demandVal = scannableControlFigure.getDemandCellEditor().getValue().toString();
				double demand = Double.parseDouble(demandVal);
				if((demand>=model.getLowLimit())&&(demand<=model.getHighLimit()) && amIHolder){
					scannableControlFigure.setDemand(demand);
					model.setReadbackValue(Double.parseDouble(scannableControlFigure.getDemandCellEditor().getValue().toString()));
				}
			}
		});
		
		scannableControlFigure.add(goReal);
		
		goSim.setLocation(new Point(55+(dp*10)+((wholeDigits-1)*10), 39));
		goSim.setVisible(model.isShowButtons());
		goSim.setSize(60, 20);
		goSim.setEnabled(amIHolder);
		goSim.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String demandVal = scannableControlFigure.getDemandCellEditor().getValue().toString();
				double demand = Double.parseDouble(demandVal);
				if((demand>=model.getLowLimit())&&(demand<=model.getHighLimit()) && amIHolder){
					scannableControlFigure.setDemand(demand);
					model.setSimReadbackValue(Double.parseDouble(scannableControlFigure.getDemandCellEditor().getValue().toString()));
					List<AbstractWidgetActionModel> actions = model.getActionData().getWidgetActions();
					WidgetProperty property = model.getPropertyInternal(AbstractWidgetModel.PROP_ACTIONDATA);
					property.setManualValue("dummy");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					WidgetActionHandlerService.getInstance().performAction(model, actions.get(0));
				}
			}
		});
		
		scannableControlFigure.add(goSim);
		tooltipText = "Limits:Low=" + model.getLowLimit() + ",High=" + model.getHighLimit() + "\rUnits="
				+ model.getPropUnits() + "\rIncrement=" + incrementVal;
		model.setPropertyValue(AbstractWidgetModel.PROP_TOOLTIP, tooltipText);
		return scannableControlFigure;
	}
		
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void registerPropertyChangeHandlers() {

		IWidgetPropertyChangeHandler handler = new IWidgetPropertyChangeHandler() {
			@Override
			public boolean handleChange(final Object oldValue, final Object newValue, final IFigure refreshableFigure) {
				ScannableControlFigure figure = (ScannableControlFigure) refreshableFigure;
				wholeDigits=1;
				figure.setReadback((Double)newValue);
				figure.repaint();
				return true;
			}
		};
		setPropertyChangeHandler(ScannableControlModel.PROP_READBACK_VALUE, handler);
		
		handler = new IWidgetPropertyChangeHandler() {
			@Override
			public boolean handleChange(final Object oldValue, final Object newValue, final IFigure refreshableFigure) {
				tooltipText = "Limits:Low=" + model.getLowLimit() + ",High=" + newValue + "\rUnits="
						+ model.getPropUnits() + "\rIncrement=" + incrementVal;
				model.setPropertyValue(AbstractWidgetModel.PROP_TOOLTIP, tooltipText);
				return true;
			}
		};
		setPropertyChangeHandler(ScannableControlModel.PROP_HIGH_LIMIT, handler);

		handler = new IWidgetPropertyChangeHandler() {
			@Override
			public boolean handleChange(final Object oldValue, final Object newValue, final IFigure refreshableFigure) {
				tooltipText = "Limits:Low=" + newValue + ",High=" + model.getHighLimit() + "\rUnits=" + model.getPropUnits() + "\rIncrement=" + incrementVal;
				model.setPropertyValue(AbstractWidgetModel.PROP_TOOLTIP, tooltipText);
				return true;
			}
		};
		setPropertyChangeHandler(ScannableControlModel.PROP_LOW_LIMIT, handler);

		handler = new IWidgetPropertyChangeHandler() {
			@Override
			public boolean handleChange(final Object oldValue, final Object newValue, final IFigure refreshableFigure) {
				tooltipText = "Limits:Low=" + model.getLowLimit() + ",High=" + model.getHighLimit() + "\rUnits="+ newValue + "\rIncrement=" + incrementVal;
				model.setPropertyValue(AbstractWidgetModel.PROP_TOOLTIP, tooltipText);
				return true;
			}
		};
		setPropertyChangeHandler(ScannableControlModel.PROP_UNITS, handler);

		handler = new IWidgetPropertyChangeHandler() {
			@Override
			public boolean handleChange(final Object oldValue, final Object newValue, final IFigure refreshableFigure) {
				offset = (Double) newValue;
				return true;
			}
		};
		setPropertyChangeHandler(ScannableControlModel.PROP_OFFSET, handler);
		
		handler = new IWidgetPropertyChangeHandler() {
			@Override
			public boolean handleChange(final Object oldValue, final Object newValue, final IFigure refreshableFigure) {
				dp = (int) Double.parseDouble(newValue.toString());
				increment.setLocation(new Point(35+(dp*10)+((wholeDigits-1)*10), 19));
				scannableControlFigure.setBoxWidth(15+(dp*10)+((wholeDigits-1)*10));
				return true;
			}
		};
		setPropertyChangeHandler(ScannableControlModel.PROP_DP, handler);
		
		handler = new IWidgetPropertyChangeHandler() {
			@Override
			public boolean handleChange(final Object oldValue, final Object newValue, final IFigure refreshableFigure) {
				showButtons = (Boolean) newValue;
				goReal.setVisible(showButtons);
				goSim.setVisible(showButtons);
				return true;
			}
		};
		setPropertyChangeHandler(ScannableControlModel.PROP_SHOW_BUTTONS, handler);
		
		handler = new IWidgetPropertyChangeHandler() {
			@Override
			public boolean handleChange(final Object oldValue, final Object newValue, final IFigure refreshableFigure) {
				incrementVal=(Double) newValue;
				return true;
			}
		};
		setPropertyChangeHandler(ScannableControlModel.PROP_DEF_INCREMENT, handler);
	}
	
}