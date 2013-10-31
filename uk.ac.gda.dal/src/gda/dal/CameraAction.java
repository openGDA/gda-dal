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

package gda.dal;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class CameraAction extends Action {
	private String _viewIdToOpen = null;
	private String var;
	
	public CameraAction(String viewIdToOpen, String var) {
		_viewIdToOpen = viewIdToOpen;
		this.var=var;
	}

	@Override
	public void run() {
		try {
			IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			if (window != null) {
				IWorkbenchPage page = window.getActivePage();
				if (page != null) {
					page.showView(_viewIdToOpen, var, IWorkbenchPage.VIEW_CREATE);
					page.showView(_viewIdToOpen, var, IWorkbenchPage.VIEW_ACTIVATE);
				}
			}
		}

		catch (Exception err) {
		}
	}
}