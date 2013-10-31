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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class SDSView extends ViewPart {
	public static final String ID = "gda.dal.sdsview";
	private String url = "screens/synoptic.css-sds";
	
	public SDSView() {
		super();
		RunModeService.getInstance().openDisplayViewInRunMode(new Path(url));
	}
	
	@Override
	public void createPartControl(Composite parent) {
	}
	
	@Override
	public void setFocus() {
	}
	
}