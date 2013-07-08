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

package gda.dal;

import org.csstudio.sds.ui.runmode.RunModeService;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IPerspectiveListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

public class SDSPerspective implements IPerspectiveFactory {
		
		public final static String ID = "gda.dal.sdsperspective";
		Path sdsDisplay = new Path("/screens/synoptic.css-sds");
		boolean displayOpened = false;
		
		/**
		 * Creates the initial layout for a page.
		 */
		@Override
		public void createInitialLayout(IPageLayout layout) {
				layout.setEditorAreaVisible(false);
				DALStartup s = new DALStartup();
				s.earlyStartup();
				
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().addPerspectiveListener(new IPerspectiveListener(){

					@Override
					public void perspectiveActivated(IWorkbenchPage page,
							IPerspectiveDescriptor perspective) {
						
						if(perspective.getId().equals(ID) && !displayOpened){
							RunModeService.getInstance().openDisplayViewInRunMode(sdsDisplay);
							displayOpened = true;
						}
					}

					@Override
					public void perspectiveChanged(IWorkbenchPage page,
							IPerspectiveDescriptor perspective, String changeId) {	
					}
				});		
		}
}
