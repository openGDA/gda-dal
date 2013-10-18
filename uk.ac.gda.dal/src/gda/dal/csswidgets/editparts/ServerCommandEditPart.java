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

import gda.dal.csswidgets.model.ServerCommandModel;
import gda.jython.JythonServerFacade;
import java.util.Timer;
import java.util.TimerTask;
import org.csstudio.sds.components.ui.internal.figures.RefreshableLabelFigure;
import org.csstudio.sds.ui.editparts.AbstractWidgetEditPart;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.widgets.Display;

public final class ServerCommandEditPart extends AbstractWidgetEditPart {
	private String lastReadback;
	private ServerCommandModel model;

	@Override
	protected ServerCommandModel getCastedModel() {
		return (ServerCommandModel) getModel();
	}

	@Override
	protected IFigure doCreateFigure() {
		model = getCastedModel();
		final RefreshableLabelFigure figure = new RefreshableLabelFigure();
		
		final Job updateReadbackJob = new Job("updateReadback") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
					Display.getDefault().asyncExec(new Runnable() {
						@Override
						public void run() {
							String serverCommand = model.getServerCommand();
							String readback = JythonServerFacade.getInstance().evaluateCommand(serverCommand);
							if(!readback.equals(lastReadback)){
								figure.setTextValue(readback);
								figure.repaint();
							}
							lastReadback = readback;
						}
					});
				return Status.OK_STATUS;
			}
		};
		
		Timer timer = new Timer();
		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				if (model.isLive())
					updateReadbackJob.schedule();
			}
		};
		timer.schedule(timerTask, 100, 100);
		
		return figure;
	}

	@Override
	protected void registerPropertyChangeHandlers() {
	}
}
