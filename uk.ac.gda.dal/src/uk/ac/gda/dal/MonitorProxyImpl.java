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

package uk.ac.gda.dal;


import org.csstudio.dal.DataExchangeException;
import org.csstudio.dal.Request;
import org.csstudio.dal.ResponseListener;
import org.csstudio.dal.impl.RequestImpl;
import org.csstudio.dal.impl.ResponseImpl;
import org.csstudio.dal.proxy.MonitorProxy;

import uk.ac.gda.dal.dataprovider.ProvideDataEvent;
import uk.ac.gda.dal.dataprovider.ProvideDataEventListener;
import uk.ac.gda.dal.dataprovider.ProvideRunnable;
import uk.ac.gda.dal.propproxy.PropertyProxyImpl;

public class MonitorProxyImpl<T> extends RequestImpl<T> implements MonitorProxy, ProvideDataEventListener<T> {
	private Object valueSync = new Object();
	private PropertyProxyImpl<T> proxy;
	private ProvideRunnable<T> dataProvider;

	public MonitorProxyImpl(PropertyProxyImpl<T> proxy, ResponseListener<T> callback, ProvideRunnable<T> dataProvider) {
		super(proxy, callback);
		this.proxy = proxy;
		this.dataProvider = dataProvider;
		dataProvider.addListener(this);
	}
	@Override
	public void destroy() {
		dataProvider.removeListener(this);		
	}

	@Override
	public Request<Double> getRequest() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
	}

	@Override
	public long getDefaultTimerTrigger() throws DataExchangeException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getTimerTrigger() throws DataExchangeException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isDefault() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDestroyed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isHeartbeat() {
		return true;
	}

	@Override
	public void setHeartbeat(boolean heartbeat) throws DataExchangeException, UnsupportedOperationException {
		// TODO Auto-generated method stub
	}

	@Override
	public void setTimerTrigger(long trigger) throws DataExchangeException, UnsupportedOperationException {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void newData(ProvideDataEvent<T> event) {
		synchronized (valueSync){
			ResponseImpl<T> r = new ResponseImpl<T>(proxy, this, event.value, "value", true, null, proxy.getCondition(), event.timestamp, false);
			if( r.getValue() != null)
				addResponse(r);
		}	
	}
	
}