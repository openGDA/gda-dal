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

import gda.configuration.properties.LocalProperties;

import org.csstudio.sds.internal.preferences.PreferenceConstants;
import org.csstudio.sds.ui.SdsUiPlugin;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkingSetManager;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.gda.ClientManager;

public class DALStartup implements IStartup {

	private static final Logger logger = LoggerFactory.getLogger(DALStartup.class);
	
	IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();

	/**
	 * Currently normal users can see these projects, however they should not edit them. 
	 * Hence we set them hidden.
	 */
	@Override
	public void earlyStartup() {

		if (!ClientManager.isClient()) return;
		
		checkFoldersExistAndImport();

		//checkExistsAndImport(LocalProperties.get("gda.dal.images") + "oe images/", "oe images");
		//checkExistsAndImport(LocalProperties.get("gda.dal.images")+ "oe thumb images/", "oe thumb images");
		//checkExistsAndImport(LocalProperties.get("gda.dal.images") + "arrow images/", "arrow images");

		//setHidden("SDS Script Rules", true);
		//setHidden("SDS",              true);
		//setHidden("SDS Cursors",      true);
		
		
		
		IPreferenceStore store = SdsUiPlugin.getCorePreferenceStore();
		String scriptsLocation = "/scripts/scr";
		if(!store.getString(PreferenceConstants.PROP_RULE_FOLDERS).equals(scriptsLocation))
			store.setValue(PreferenceConstants.PROP_RULE_FOLDERS, scriptsLocation);


		
		
		IWorkingSetManager workingSetManager = PlatformUI.getWorkbench().getWorkingSetManager();
		if(workingSetManager.getWorkingSet("Synoptic")==null){
			IProject screens = root.getProject("screens");
			IProject scripts = root.getProject("scripts");
			IAdaptable[] sds = {screens, scripts};
			workingSetManager.addWorkingSet(workingSetManager.createWorkingSet("Synoptic", sds));
		}
		
	}

	private void checkFoldersExistAndImport() {
		String dalScreens = LocalProperties.get("gda.dal.screens");
		String dalScripts = LocalProperties.get("gda.dal.scripts");
		if (dalScreens == null || dalScripts == null){
			// do not proceed if these properties have not been setup
			return;
		}
		
		checkExistsAndImport(dalScreens, "screens");
		checkExistsAndImport(dalScripts, "scripts");

		
	}
	
	public void checkExistsAndImport(String projectLocation, String name) {
	
		if (!root.getProject(name).exists()) {

			try {
				importExisitingProject(new Path(projectLocation));
			} catch (Throwable e) {
				logger.error("Cannot load "+name+" from configuration.", e);
			}
		}
	}
	
	public void checkExistsAndDelete(String projectName) {

		if (root.getProject(projectName).exists()) {
			try {
				root.getProject(projectName).delete(true, null);
			} catch (Throwable e) {
				logger.error("Cannot delete "+projectName, e);
			}
			
		}
	}

	/**
	 * @param projectPath
	 * @throws CoreException
	 */
	public void importExisitingProject(IPath projectPath) throws CoreException {

		IWorkspace workspace = ResourcesPlugin.getWorkspace();

		final IProjectDescription description = workspace
				.loadProjectDescription(projectPath.append(IPath.SEPARATOR
						+ IProjectDescription.DESCRIPTION_FILE_NAME));
		final IProject project = workspace.getRoot().getProject(
				description.getName());

		if (!project.exists()) {
			IWorkspaceRunnable runnable = new IWorkspaceRunnable() {
				@Override
				public void run(IProgressMonitor monitor) throws CoreException {

					project.create(description, monitor);
					project.open(IResource.NONE, monitor);
					//project.setHidden(true);
				}
			};
			workspace.run(runnable, workspace.getRuleFactory().modifyRule(
					workspace.getRoot()), IResource.NONE, null);
		}
	}

}
