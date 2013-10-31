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

package gda.dal.propproxy;

import java.lang.reflect.Method;

import org.epics.css.dal.RemoteException;
import org.epics.css.dal.Request;
import org.epics.css.dal.ResponseListener;
import org.epics.css.dal.commands.CommandSupport;
import org.epics.css.dal.impl.RequestImpl;
import org.epics.css.dal.impl.ResponseImpl;
import org.epics.css.dal.proxy.CommandProxy;
import org.epics.css.dal.proxy.DeviceProxy;

public class CommandProxyImpl extends CommandSupport implements CommandProxy{
	protected DeviceProxy owner;

	/**
	 * Creates a new CommandProxyImpl object.
	 *
	 * @param owner command parent context
	 * @param host the host commands are called on
	 * @param method the method that the command executes
	 */
	public CommandProxyImpl(DeviceProxy owner, Object host, Method method){
		super(null, host, method);
		this.owner = owner;
	}

	@Override
	public boolean isAsynchronous(){
		return true;
	}

	@Override
	public <T> Request<T> execute(ResponseListener<T> callback, Object... parameters) throws RemoteException{
		RequestImpl<T> r = new RequestImpl<T>(owner, callback);
		Object ret = execute(parameters);
		r.addResponse(new ResponseImpl<T>(owner, r, (T)ret, getName(), true, null, null, null, true));
		return r;
	}
	
}