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

import org.epics.css.dal.context.AbstractApplicationContext;
import org.epics.css.dal.proxy.AbstractPlug;
import org.epics.css.dal.spi.AbstractPropertyFactory;
import org.epics.css.dal.spi.LinkPolicy;
import org.epics.css.dal.spi.Plugs;

public class GdaPropertyFactory extends AbstractPropertyFactory {
	/**
	 * @see org.epics.css.dal.spi.AbstractFactorySupport#getPlugClass()
	 */
	@Override
	protected Class<? extends AbstractPlug> getPlugClass() {
		return GdaPlug.class;
	}

	/**
	 * Creates a new PropertyFactoryImpl object.
	 */
	public GdaPropertyFactory() {
		super();
	}

	@Override
	public void initialize(AbstractApplicationContext ctx, LinkPolicy policy) {
		ctx.getConfiguration().setProperty(Plugs.PROPERTIES_FROM_CACHE, "true");
		super.initialize(ctx, policy);
	}
}
