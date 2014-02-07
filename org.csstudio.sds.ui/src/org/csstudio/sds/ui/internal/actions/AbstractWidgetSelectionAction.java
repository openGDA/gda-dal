/*
 * Copyright (c) 2008 Stiftung Deutsches Elektronen-Synchrotron,
 * Member of the Helmholtz Association, (DESY), HAMBURG, GERMANY.
 *
 * THIS SOFTWARE IS PROVIDED UNDER THIS LICENSE ON AN "../AS IS" BASIS.
 * WITHOUT WARRANTY OF ANY KIND, EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR PARTICULAR PURPOSE AND
 * NON-INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR
 * THE USE OR OTHER DEALINGS IN THE SOFTWARE. SHOULD THE SOFTWARE PROVE DEFECTIVE
 * IN ANY RESPECT, THE USER ASSUMES THE COST OF ANY NECESSARY SERVICING, REPAIR OR
 * CORRECTION. THIS DISCLAIMER OF WARRANTY CONSTITUTES AN ESSENTIAL PART OF THIS LICENSE.
 * NO USE OF ANY SOFTWARE IS AUTHORIZED HEREUNDER EXCEPT UNDER THIS DISCLAIMER.
 * DESY HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS,
 * OR MODIFICATIONS.
 * THE FULL LICENSE SPECIFYING FOR THE SOFTWARE THE REDISTRIBUTION, MODIFICATION,
 * USAGE AND OTHER RIGHTS AND OBLIGATIONS IS INCLUDED WITH THE DISTRIBUTION OF THIS
 * PROJECT IN THE FILE LICENSE.HTML. IF THE LICENSE IS NOT INCLUDED YOU MAY FIND A COPY
 * AT HTTP://WWW.DESY.DE/LEGAL/LICENSE.HTM
 */

package org.csstudio.sds.ui.internal.actions;

import java.util.ArrayList;
import java.util.List;

import org.csstudio.sds.model.AbstractWidgetModel;
import org.csstudio.sds.ui.editparts.AbstractBaseEditPart;
import org.csstudio.sds.ui.internal.editor.DisplayEditor;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Base class for selection dependent actions.
 * 
 * @author Sven Wende
 * 
 */
public abstract class AbstractWidgetSelectionAction extends SelectionAction {

	private final GraphicalViewer _viewer;

	public AbstractWidgetSelectionAction(IWorkbenchPart part, GraphicalViewer viewer) {
		super(part);
		this._viewer = viewer;
	}

	protected abstract Command doCreateCommand(List<AbstractWidgetModel> selectedWidgets);

	protected abstract boolean doCalculateEnabled(List<AbstractWidgetModel> selectedWidgets);

	/**
	 *{@inheritDoc}
	 */
	@Override
	public final void run() {
		Command cmd = doCreateCommand(getSelectedWidgetModels());
		if (cmd != null) {
			execute(cmd);
		}
	}

	/**
	 *{@inheritDoc}
	 */
	@Override
	protected final boolean calculateEnabled() {
		return doCalculateEnabled(getSelectedWidgetModels());
	}

	protected EditPartViewer getGraphicalViewer() {
		return ((DisplayEditor) getWorkbenchPart()).getGraphicalViewer();
	}
	
	private final List<AbstractWidgetModel> getSelectedWidgetModels() {
		List<AbstractWidgetModel> selectedWidgetModels = new ArrayList<AbstractWidgetModel>();
		for (Object o : getSelectedObjects()) {
			if (o instanceof AbstractBaseEditPart) {
				selectedWidgetModels.add(((AbstractBaseEditPart) o).getWidgetModel());
			}
		}
		
		return selectedWidgetModels;
	}

}
