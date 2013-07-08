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

import gda.dal.csswidgets.figures.MoveableWindowFigure;
import gda.dal.csswidgets.model.MoveableTitleBarModel;
import gda.dal.csswidgets.model.MoveableWindowModel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.csstudio.sds.model.AbstractWidgetModel;
import org.csstudio.sds.model.ContainerModel;
import org.csstudio.sds.model.DisplayModel;
import org.csstudio.sds.model.LabelModel;
import org.csstudio.sds.internal.persistence.PersistenceUtil;
import org.csstudio.sds.ui.CheckedUiRunnable;
import org.csstudio.sds.ui.editparts.AbstractContainerEditPart;
import org.csstudio.sds.ui.editparts.IWidgetPropertyChangeHandler;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.progress.IJobRunnable;

/**
 * A widget that can contain multiple movable linking containers.
 */
public final class MoveableWindowEditPart extends AbstractContainerEditPart {

	private IProgressMonitor _runningMonitor;

	int delta_x = 0;
	int delta_y = 0;
//	int boxWidth = 0;
//	int boxHeight = 0;
	String title;
	ContainerLoadJob job;
	Point clickedAt;
	MoveableTitleBarModel rect;
	MoveableTitleBarModel overRect;
	MoveableWindowFigure moveableWindowFigure = new MoveableWindowFigure();
	MoveableWindow win;
	
	/**
	 * Constructor.
	 */
	public MoveableWindowEditPart() {
		setChildrenSelectable(true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IFigure getContentPane() {
		return ((MoveableWindowFigure) getFigure()).getContentsPane();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected IFigure doCreateFigure() {

		MoveableWindowModel widget = (MoveableWindowModel) getContainerModel();
		MoveableWindowFigure moveableWindowFigure = new MoveableWindowFigure();
		moveableWindowFigure.setAutoFit(widget.isAutoZoom());

		if (widget.isLive()) {
			moveableWindowFigure.addMouseListener(new MouseListener.Stub() {
				@Override
				public void mousePressed(MouseEvent me) {
					me.consume();
				}
			});
		}

		return moveableWindowFigure;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void registerPropertyChangeHandlers() {

		final HashMap<String, MoveableWindow> moveableWindows = new HashMap<String, MoveableWindow>();

		IWidgetPropertyChangeHandler handler = new IWidgetPropertyChangeHandler() {

			@Override
			public boolean handleChange(final Object oldValue, final Object newValue, final IFigure figure) {

				if (!moveableWindows.containsKey(newValue.toString())) {

					loadResource((IPath) newValue, (MoveableWindowFigure) figure);

					title = newValue.toString();
					System.out.println(title);
					int beginIndex = title.lastIndexOf('/') + 1;
					int endIndex = title.indexOf('.');
					title = title.substring(beginIndex, endIndex).toUpperCase();

					final RectangleFigure invisibleRectangle = new RectangleFigure();

					invisibleRectangle.setSize(0, 24);
					invisibleRectangle.setLocation(new Point(0, 0));
					invisibleRectangle.setForegroundColor(new Color(null, 0, 0, 0));
					invisibleRectangle.setFill(false);
					invisibleRectangle.setOutline(false);

					win = new MoveableWindow();
					win.setName(newValue.toString());
					win.setRectangle(invisibleRectangle);

					final RectangleFigure invisibleButton = new RectangleFigure();

					invisibleButton.setSize(19, 19);
					invisibleButton.setFill(false);
					invisibleButton.setOutline(false);
					final InputStream icon = getClass().getResourceAsStream("/icons/close.png");
					rect = new MoveableTitleBarModel(title, icon);
					rect.setLocation(0, 0);
					rect.setSize(0, 24);
					rect.setVisible(true);
					win.setTitleBarModel(rect);

					final InputStream overIcon = getClass().getResourceAsStream("/icons/close_over.png");
					overRect = new MoveableTitleBarModel(title, overIcon);
					overRect.setLocation(0, 0);
					overRect.setSize(0, 24);
					overRect.setVisible(false);
					win.setOverTitleBarModel(overRect);

					job.widgets.add(rect);
					job.widgets.add(overRect);
					win.setBox(job.widgets);

					final List<AbstractWidgetModel> widgets = win.getBox();

					final int[] xPositions = new int[widgets.size()];
					final int[] yPositions = new int[widgets.size()];

					int boxWidth = 0;
					int boxHeight = 0;

					for (int i = 0; i < widgets.size(); i++) {

						if (widgets.get(i).getWidth() > boxWidth)
							boxWidth = (widgets.get(i).getWidth());
						if (widgets.get(i).getHeight() > boxHeight)
							boxHeight = (widgets.get(i).getHeight());

						xPositions[i] = widgets.get(i).getX();

						if (!widgets.get(i).getClass().isInstance(rect))
							yPositions[i] = widgets.get(i).getY() + 23;
						else
							yPositions[i] = widgets.get(i).getY();
					}

					Dimension boxSize = new Dimension(boxWidth, boxHeight);
					win.setBoxSize(boxSize);

					invisibleRectangle.setSize(boxSize.width, 24);
					invisibleButton.setLocation(new Point(boxSize.width - 21, 3));
					rect.setWidth(boxSize.width);
					overRect.setWidth(boxSize.width);
					win.setButton(invisibleButton);

					AbstractWidgetModel widget;

					for (int i = 0; i < widgets.size(); i++) {
						widget = widgets.get(i);
						widget.setX(xPositions[i]);
						widget.setY(yPositions[i]);
					}

					overRect.setVisible(false);
					rect.setVisible(true);

					MouseMotionListener mml = new MouseMotionListener.Stub() {

						@Override
						public void mouseDragged(MouseEvent arg0) {

							AbstractWidgetModel widget;
							int newX = arg0.x - delta_x;
							int newY = arg0.y - delta_y;

							for (int i = 0; i < widgets.size(); i++) {
								widget = widgets.get(i);

								if (newX >= 0)
									widget.setX(newX + xPositions[i]);
								else
									widget.setX(xPositions[i]);

								if (newY >= 0)
									widget.setY(newY + yPositions[i]);
								else
									widget.setY(yPositions[i]);
							}

							int titleBarX = invisibleRectangle.getLocation().x;
							int titleBarY = invisibleRectangle.getLocation().y - rect.getHeight();
							int buttonX = invisibleButton.getLocation().x;
							int buttonY = invisibleButton.getLocation().y;

							int boxWidth = 0;
							int boxHeight = 0;

							for (int i = 0; i < widgets.size(); i++) {
								if (widgets.get(i).getWidth() > boxWidth)
									boxWidth = (widgets.get(i).getWidth());
								if (widgets.get(i).getHeight() > boxHeight)
									boxHeight = (widgets.get(i).getHeight());
							}
							
							if (newX >= 0) {
								titleBarX = newX;
								buttonX = titleBarX + boxWidth - 21;
							} else {
								titleBarX = 0;
								buttonX = titleBarX + boxWidth - 21;
							}

							if (newY >= 0) {
								titleBarY = newY;
								buttonY = titleBarY + 3;
							} else {
								titleBarY = 0;
								buttonY = titleBarY + 3;
							}

							invisibleRectangle.setLocation(new Point(titleBarX, titleBarY));
							invisibleButton.setLocation(new Point(buttonX, buttonY));
						}
					};

					invisibleRectangle.addMouseMotionListener(mml);
					win.setMouseMotionListener(mml);

					MouseListener ml = new MouseListener.Stub() {

						@Override
						public void mousePressed(MouseEvent arg0) {
							arg0.consume();// required for mouseDragged
							// to work
							clickedAt = new Point(arg0.x, arg0.y);
							delta_x = clickedAt.x - moveableWindows.get(newValue.toString()).getRectangle().getLocation().x;
							delta_y = clickedAt.y - moveableWindows.get(newValue.toString()).getRectangle().getLocation().y;
							List<AbstractWidgetModel> newWidgets = widgets;
							job._container.removeWidgets(moveableWindows.get(newValue.toString()).getBox());
							job._container.addWidgets(newWidgets);
						}
					};

					MouseMotionListener cbhl = new MouseMotionListener.Stub() {

						@Override
						public void mouseEntered(MouseEvent arg0) {
							moveableWindows.get(newValue.toString()).getTitleBarModel().setVisible(false);
							moveableWindows.get(newValue.toString()).getOverTitleBarModel().setVisible(true);
						}

						@Override
						public void mouseExited(MouseEvent arg0) {
							moveableWindows.get(newValue.toString()).getOverTitleBarModel().setVisible(false);
							moveableWindows.get(newValue.toString()).getTitleBarModel().setVisible(true);
						}
					};
					invisibleButton.addMouseMotionListener(cbhl);
					win.setCloseButtonHoverListener(cbhl);

					invisibleRectangle.addMouseListener(ml);

					win.setMouseListener(ml);

					MouseListener cl = new MouseListener.Stub() {

						@Override
						public void mousePressed(MouseEvent arg0) {
							job._container.removeWidgets(moveableWindows.get(newValue.toString()).getBox());
							job._figure.getContentsPane().remove(moveableWindows.get(newValue.toString()).getRectangle());
							job._figure.getContentsPane().remove(moveableWindows.get(newValue.toString()).getButton());
							moveableWindows.remove(newValue.toString());
						}
					};
					invisibleButton.addMouseListener(cl);

					win.setCloseListener(cl);

					job._figure.getContentsPane().add(invisibleRectangle);
					job._figure.getContentsPane().add(invisibleButton);
					job._container.addWidgets(widgets);

					moveableWindows.put(newValue.toString(), win);
				}

				return true;
			}

		};

		setPropertyChangeHandler(MoveableWindowModel.PROP_RESOURCE, handler);

		MoveableWindowModel m = (MoveableWindowModel) getContainerModel();
		loadResource(m.getResource(), moveableWindowFigure);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void refreshChildren() {
		super.refreshChildren();

		// we need to ensure the correct zoom level, when figures are added or
		// removed
		((MoveableWindowFigure) getFigure()).updateZoom();
	}

	private void loadResource(final IPath resource, MoveableWindowFigure figure) {
		if (_runningMonitor != null) {
			_runningMonitor.setCanceled(true);
		}

		_runningMonitor = new NullProgressMonitor();

		job = new ContainerLoadJob((MoveableWindowModel) getContainerModel(), resource, figure);

		job.run(_runningMonitor);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(EditPolicy.CONTAINER_ROLE, null);
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new XYLayoutEditPolicy() {

			@Override
			protected Command createChangeConstraintCommand(final EditPart child, final Object constraint) {
				return null;
			}

			@Override
			protected Command getCreateCommand(final CreateRequest request) {
				return null;
			}

			@Override
			protected void showSizeOnDropFeedback(final CreateRequest request) {

			}

		});

		installEditPolicy(EditPolicy.LAYOUT_ROLE, null);

	}

	private static class ContainerLoadJob implements IJobRunnable {
		MoveableWindowModel _container;
		IPath _path;
		MoveableWindowFigure _figure;
		DisplayModel tempModel;
		List<AbstractWidgetModel> widgets;

		public ContainerLoadJob(MoveableWindowModel container, IPath path, MoveableWindowFigure figure) {
			assert container != null;
			assert path != null;
			assert figure != null;
			_container = container;
			_path = path;
			_figure = figure;
		}

		@Override
		public IStatus run(IProgressMonitor progressMonitor) {
			IStatus status = Status.OK_STATUS;

			if (_path != null && !_path.isEmpty()) {
				// display a temporary message + cancel button while the display
				// is loading

				if (!progressMonitor.isCanceled()) {
					// showMessage(progressMonitor, "Loading " +
					// _path.toString());

					if (!progressMonitor.isCanceled()) {
						load(progressMonitor);
					} else {
						status = Status.CANCEL_STATUS;
					}
				} else {
					status = Status.CANCEL_STATUS;
				}
			}
			return status;
		}

		protected IStatus clearContainer() {
			// remove old widgets
			Iterator<AbstractWidgetModel> it = _container.getWidgets().iterator();
			while (it.hasNext()) {
				_container.removeWidget(it.next());
			}

			updateZoom();

			return Status.OK_STATUS;
		}

		protected void updateZoom() {
			new CheckedUiRunnable() {
				@Override
				protected void doRunInUi() {
					_figure.updateZoom();
				}
			};
		}

		protected void showMessage(String message) {
			// clear the container
			clearContainer();

			// add a temporary widget
			final LabelModel loadingMessage = new LabelModel();
			// loadingMessage.setForegroundColor(new RGB(0, 0, 200));
			loadingMessage.setTextValue(message);
			loadingMessage.setLocation(0, 0);
			int w = _container.getWidth();
			loadingMessage.setWidth(w);
			int h = _container.getHeight();
			loadingMessage.setHeight(h);
			_container.addWidget(loadingMessage);
		}

		/**
		 * Initialises the {@link ContainerModel} from the specified path.
		 */
		protected void load(final IProgressMonitor progressMonitor) {
			InputStream input = getInputStream(_path);

			if (input == null) {
				showMessage("Could not load display: " + _path.toPortableString());
			} else {
				tempModel = new DisplayModel();

				PersistenceUtil.syncFillModel(tempModel, input);

				// add new widgets
				widgets = tempModel.getWidgets();
				tempModel.removeWidgets(widgets);

				// update zoom
				if (!progressMonitor.isCanceled()) {
					updateZoom();
				}

				// use background-color of the loaded display
				// _container.setBackgroundColor(tempModel.getBackgroundColor());
				// _container.setForegroundColor(tempModel.getForegroundColor());

				// _container.setAliases(tempModel.getAliases());
				_container.setPrimarPv(tempModel.getPrimaryPV());
			}
		}

		/**
		 * Return the {@link InputStream} of the file that is available on the specified path.
		 * 
		 * @param path
		 *            The {@link IPath} to the file
		 * @return The corresponding {@link InputStream} or null
		 */
		private static InputStream getInputStream(final IPath path) {
			InputStream result = null;

			// try workspace
			IResource r = ResourcesPlugin.getWorkspace().getRoot().findMember(path, false);
			if (r instanceof IFile) {
				try {
					result = ((IFile) r).getContents();
				} catch (CoreException e) {
				}
			}

			if (result == null) {
				// try from local file system
				try {
					result = new FileInputStream(path.toFile());
				} catch (FileNotFoundException e) {
				}

			}

			return result;
		}
	}
}
