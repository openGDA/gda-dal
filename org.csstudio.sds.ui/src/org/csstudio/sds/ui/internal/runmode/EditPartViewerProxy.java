package org.csstudio.sds.ui.internal.runmode;

import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.AccessibleEditPart;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.SelectionManager;
import org.eclipse.gef.dnd.TransferDragSourceListener;
import org.eclipse.gef.dnd.TransferDropTargetListener;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * A proxy for an {@link EditPartViewer}. The class is only used in a
 * workarround to ensure that the wrapped EditPartViewer gets garbage
 * collected when the shell is disposed.
 * 
 * @author Sven Wende
 */
public class EditPartViewerProxy implements EditPartViewer {
	private EditPartViewer _delegate;

	public EditPartViewerProxy(EditPartViewer delegate) {
		super();
		_delegate = delegate;
	}

	/**
	 * Forge
	 */
	public void dispose() {
		_delegate = null;
	}

	@Override
	public void addDragSourceListener(TransferDragSourceListener listener) {
		if (_delegate != null) {
			_delegate.addDragSourceListener(listener);
		}
	}

	@Override
	public void addDragSourceListener(org.eclipse.jface.util.TransferDragSourceListener listener) {
		if (_delegate != null) {
			_delegate.addDragSourceListener(listener);
		}

	}

	@Override
	public void addDropTargetListener(TransferDropTargetListener listener) {
		if (_delegate != null) {
			_delegate.addDropTargetListener(listener);
		}

	}

	@Override
	public void addDropTargetListener(org.eclipse.jface.util.TransferDropTargetListener listener) {
		if (_delegate != null) {
			_delegate.addDropTargetListener(listener);
		}
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		if (_delegate != null) {
			_delegate.addPropertyChangeListener(listener);
		}
	}

	@Override
	public void appendSelection(EditPart editpart) {
		if (_delegate != null) {
			_delegate.appendSelection(editpart);
		}
	}

	@Override
	public Control createControl(Composite composite) {
		return _delegate != null ? _delegate.createControl(composite) : null;
	}

	@Override
	public void deselect(EditPart editpart) {
		if (_delegate != null) {
			_delegate.deselect(editpart);
		}

	}

	@Override
	public void deselectAll() {
		if (_delegate != null) {
			_delegate.deselectAll();
		}
	}

	@Override
	public EditPart findObjectAt(Point location) {
		return _delegate != null ? _delegate.findObjectAt(location) : null;
	}

	@Override
	public EditPart findObjectAtExcluding(Point location, Collection exclusionSet) {
		return _delegate != null ? _delegate.findObjectAtExcluding(location, exclusionSet) : null;
	}

	@Override
	public EditPart findObjectAtExcluding(Point location, Collection exclusionSet, Conditional conditional) {
		return _delegate != null ? _delegate.findObjectAtExcluding(location, exclusionSet, conditional) : null;
	}

	@Override
	public void flush() {
		if (_delegate != null) {
			_delegate.flush();
		}
	}

	@Override
	public EditPart getContents() {
		return _delegate != null ? _delegate.getContents() : null;
	}

	@Override
	public MenuManager getContextMenu() {
		return _delegate != null ? _delegate.getContextMenu() : null;
	}

	@Override
	public Control getControl() {
		return _delegate != null ? _delegate.getControl() : null;
	}

	@Override
	public EditDomain getEditDomain() {
		return _delegate != null ? _delegate.getEditDomain() : null;
	}

	@Override
	public EditPartFactory getEditPartFactory() {
		return _delegate != null ? _delegate.getEditPartFactory() : null;
	}

	@Override
	public Map getEditPartRegistry() {
		return _delegate != null ? _delegate.getEditPartRegistry() : null;
	}

	@Override
	public EditPart getFocusEditPart() {
		return _delegate != null ? _delegate.getFocusEditPart() : null;
	}

	@Override
	public KeyHandler getKeyHandler() {
		return _delegate != null ? _delegate.getKeyHandler() : null;
	}

	@Override
	public Object getProperty(String key) {
		return _delegate != null ? _delegate.getProperty(key) : null;
	}

	@Override
	public ResourceManager getResourceManager() {
		return _delegate != null ? _delegate.getResourceManager() : null;
	}

	@Override
	public RootEditPart getRootEditPart() {
		return _delegate != null ? _delegate.getRootEditPart() : null;
	}

	@Override
	public List getSelectedEditParts() {
		return _delegate != null ? _delegate.getSelectedEditParts() : null;
	}

	@Override
	public ISelection getSelection() {
		return _delegate != null ? _delegate.getSelection() : null;
	}

	@Override
	public SelectionManager getSelectionManager() {
		return _delegate != null ? _delegate.getSelectionManager() : null;
	}

	@Override
	public Map getVisualPartMap() {
		return _delegate != null ? _delegate.getVisualPartMap() : null;
	}

	@Override
	public void registerAccessibleEditPart(AccessibleEditPart acc) {
		if (_delegate != null) {
			_delegate.registerAccessibleEditPart(acc);
		}
	}

	@Override
	public void removeDragSourceListener(TransferDragSourceListener listener) {
		if (_delegate != null) {
			_delegate.removeDragSourceListener(listener);
		}
	}

	@Override
	public void removeDragSourceListener(org.eclipse.jface.util.TransferDragSourceListener listener) {
		if (_delegate != null) {
			_delegate.removeDragSourceListener(listener);
		}
	}

	@Override
	public void removeDropTargetListener(TransferDropTargetListener listener) {
		if (_delegate != null) {
			_delegate.removeDropTargetListener(listener);
		}
	}

	@Override
	public void removeDropTargetListener(org.eclipse.jface.util.TransferDropTargetListener listener) {
		if (_delegate != null) {
			_delegate.removeDropTargetListener(listener);
		}
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		if (_delegate != null) {
			_delegate.removePropertyChangeListener(listener);
		}
	}

	@Override
	public void reveal(EditPart editpart) {
		if (_delegate != null) {
			_delegate.reveal(editpart);
		}
	}

	@Override
	public void select(EditPart editpart) {
		if (_delegate != null) {
			_delegate.select(editpart);
		}
	}

	@Override
	public void setContents(EditPart editpart) {
		if (_delegate != null) {
			_delegate.setContents(editpart);
		}
	}

	@Override
	public void setContents(Object contents) {
		if (_delegate != null) {
			_delegate.setContents(contents);
		}
	}

	@Override
	public void setContextMenu(MenuManager contextMenu) {
		if (_delegate != null) {
			_delegate.setContextMenu(contextMenu);
		}
	}

	@Override
	public void setControl(Control control) {
		if (_delegate != null) {
			_delegate.setControl(control);
		}
	}

	@Override
	public void setCursor(Cursor cursor) {
		if (_delegate != null) {
			_delegate.setCursor(cursor);
		}
	}

	@Override
	public void setEditDomain(EditDomain domain) {
		if (_delegate != null) {
			_delegate.setEditDomain(domain);
		}
	}

	@Override
	public void setEditPartFactory(EditPartFactory factory) {
		if (_delegate != null) {
			_delegate.setEditPartFactory(factory);
		}
	}

	@Override
	public void setFocus(EditPart focus) {
		if (_delegate != null) {
			_delegate.setFocus(focus);
		}
	}

	@Override
	public void setKeyHandler(KeyHandler keyHandler) {
		if (_delegate != null) {
			_delegate.setKeyHandler(keyHandler);
		}
	}

	@Override
	public void setProperty(String propertyName, Object value) {
		if (_delegate != null) {
			_delegate.setProperty(propertyName, value);
		}
	}

	@Override
	public void setRootEditPart(RootEditPart root) {
		if (_delegate != null) {
			_delegate.setRootEditPart(root);
		}
	}

	@Override
	public void setRouteEventsToEditDomain(boolean value) {
		if (_delegate != null) {
			_delegate.setRouteEventsToEditDomain(value);
		}
	}

	@Override
	public void setSelectionManager(SelectionManager manager) {
		if (_delegate != null) {
			_delegate.setSelectionManager(manager);
		}
	}

	@Override
	public void unregisterAccessibleEditPart(AccessibleEditPart acc) {
		if (_delegate != null) {
			_delegate.unregisterAccessibleEditPart(acc);
		}
	}

	@Override
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		if (_delegate != null) {
			_delegate.addSelectionChangedListener(listener);
		}
	}

	@Override
	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		if (_delegate != null) {
			_delegate.removeSelectionChangedListener(listener);
		}
	}

	@Override
	public void setSelection(ISelection selection) {
		if (_delegate != null) {
			_delegate.setSelection(selection);
		}
	}

}