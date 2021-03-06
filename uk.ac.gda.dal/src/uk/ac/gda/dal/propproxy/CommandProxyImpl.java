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

package uk.ac.gda.dal.propproxy;

import java.lang.reflect.Method;

import org.csstudio.dal.RemoteException;
import org.csstudio.dal.Request;
import org.csstudio.dal.ResponseListener;
import org.csstudio.dal.commands.CommandSupport;
import org.csstudio.dal.impl.RequestImpl;
import org.csstudio.dal.impl.ResponseImpl;
import org.csstudio.dal.proxy.CommandProxy;
import org.csstudio.dal.proxy.DeviceProxy;

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

	@SuppressWarnings("unchecked")
	@Override
	public <T> Request<T> execute(ResponseListener<T> callback, Object... parameters) throws RemoteException{
		RequestImpl<T> r = new RequestImpl<T>(owner, callback);
		Object execute = execute(parameters);
		r.addResponse(new ResponseImpl<T>(owner, r, (T)execute, getName(), true, null, null, null, true));
		return r;
	}
	
}