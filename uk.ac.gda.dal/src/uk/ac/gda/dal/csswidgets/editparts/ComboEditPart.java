package uk.ac.gda.dal.csswidgets.editparts;


import java.util.List;

import org.csstudio.sds.components.ui.internal.figures.RefreshableLabelFigure;
import org.csstudio.sds.model.properties.actions.AbstractWidgetActionModel;
import org.csstudio.sds.ui.editparts.AbstractWidgetEditPart;
import org.csstudio.sds.ui.editparts.ExecutionMode;
import org.csstudio.sds.ui.editparts.IWidgetPropertyChangeHandler;
import org.csstudio.sds.ui.widgetactionhandler.WidgetActionHandlerService;
import org.eclipse.core.runtime.Platform;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.model.IWorkbenchAdapter;

import uk.ac.gda.dal.csswidgets.model.ComboModel;

public class ComboEditPart extends AbstractWidgetEditPart {

	@Override
	protected IFigure doCreateFigure() {
		ComboModel model = (ComboModel) getWidgetModel();
		RefreshableLabelFigure label = new RefreshableLabelFigure();
		label.setTextValue(model.getLabel());
		label.setFont(getModelFont(ComboModel.PROP_FONT));
		label.setTextAlignment(model.getTextAlignment());
		label.setTransparent(false);
		label.setEnabled(model.isAccesible() && getExecutionMode().equals(ExecutionMode.RUN_MODE));
		label.addMouseListener(new MouseListener() {
			@Override
			public void mouseDoubleClicked(final MouseEvent me) {
			}

			@Override
			public void mousePressed(final MouseEvent me) {
				if (me.button == 1 && getExecutionMode().equals(ExecutionMode.RUN_MODE)) { 
					org.eclipse.swt.graphics.Point cursorLocation = Display.getCurrent().getCursorLocation();
					performDirectEdit(me.getLocation(), cursorLocation.x, cursorLocation.y);
				}
			}

			@Override
			public void mouseReleased(final MouseEvent me) {
			}
		});
		return label;
	}

	/**
	 * Open the cell editor for direct editing.
	 * 
	 * @param point
	 *            the location of the mouse-event
	 */
	private void performDirectEdit(final Point point, final int absolutX, final int absolutY) {
		if (this.getCastedModel().isAccesible() && getExecutionMode().equals(ExecutionMode.RUN_MODE)) {
			final List<AbstractWidgetActionModel> widgetActions = ((ComboModel) this.getCastedModel()).getActionData().getWidgetActions();
			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			MenuManager menuManager = new MenuManager();
			Menu menu = menuManager.createContextMenu(shell);
			if (widgetActions.size() == 1) {
				Display.getCurrent().asyncExec(new Runnable() {
					@Override
					public void run() {
						WidgetActionHandlerService.getInstance().performAction(getCastedModel(), widgetActions.get(0));
					}
				});
			} else {
				for (AbstractWidgetActionModel action : widgetActions)
					menuManager.add(new MenuAction(action));
				int x = absolutX;
	            int y = absolutY;
	            x = x - point.x + this.getCastedModel().getX();
	            y = y - point.y + this.getCastedModel().getY() + this.getCastedModel().getHeight();
				menu.setLocation(x, y);
				menu.setVisible(true);
			}

			while (!menu.isDisposed() && menu.isVisible())
				if (!Display.getCurrent().readAndDispatch())
					Display.getCurrent().sleep();
			menu.dispose();
			shell.getParent().setFocus();
		}
	}

	/**
	 * Returns the Figure of this EditPart.
	 * 
	 * @return RefreshableActionButtonFigure The RefreshableActionButtonFigure of this EditPart
	 */
	protected RefreshableLabelFigure getCastedFigure() {
		return (RefreshableLabelFigure) getFigure();
	}

	@Override
	protected void registerPropertyChangeHandlers() {
		// label
		IWidgetPropertyChangeHandler labelHandler = new IWidgetPropertyChangeHandler() {
			@Override
			public boolean handleChange(final Object oldValue, final Object newValue, final IFigure refreshableFigure) {
				RefreshableLabelFigure figure = getCastedFigure();
				figure.setTextValue(newValue.toString());
				return true;
			}
		};
		setPropertyChangeHandler(ComboModel.PROP_LABEL, labelHandler);

		// text alignment
		IWidgetPropertyChangeHandler alignmentHandler = new IWidgetPropertyChangeHandler() {
			@Override
			public boolean handleChange(final Object oldValue, final Object newValue, final IFigure refreshableFigure) {
				RefreshableLabelFigure figure = getCastedFigure();
				figure.setTextAlignment((Integer) newValue);
				return true;
			}
		};
		setPropertyChangeHandler(ComboModel.PROP_TEXT_ALIGNMENT, alignmentHandler);
	}

	/**
	 * An Action, which encapsulates a {@link AbstractWidgetActionModel}.
	 * 
	 * @author Kai Meyer
	 */
	private final class MenuAction extends Action {

		private AbstractWidgetActionModel _widgetAction;

		public MenuAction(final AbstractWidgetActionModel widgetAction) {
			_widgetAction = widgetAction;
			this.setText(_widgetAction.getActionLabel());
			IWorkbenchAdapter adapter = (IWorkbenchAdapter) Platform.getAdapterManager().getAdapter(widgetAction, IWorkbenchAdapter.class);
			if (adapter != null)
				this.setImageDescriptor(adapter.getImageDescriptor(widgetAction));
			this.setEnabled(_widgetAction.isEnabled());
		}

		@Override
		public void run() {
			Display.getCurrent().asyncExec(new Runnable() {
				@Override
				public void run() {
					WidgetActionHandlerService.getInstance().performAction(getCastedModel(), _widgetAction);
				}
			});
		}
	}

	@Override
	protected boolean forceDisabledInEditMode() {
		return true;
	}

}