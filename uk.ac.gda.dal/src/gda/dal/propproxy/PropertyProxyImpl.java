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

import gda.dal.GdaPlug;
import gda.dal.MonitorProxyImpl;
import gda.dal.dataprovider.ProvideRunnable;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;

import org.epics.css.dal.DataExchangeException;
import org.epics.css.dal.DynamicValueCondition;
import org.epics.css.dal.DynamicValueState;
import org.epics.css.dal.RemoteException;
import org.epics.css.dal.Request;
import org.epics.css.dal.ResponseListener;
import org.epics.css.dal.SimpleProperty;
import org.epics.css.dal.Timestamp;
import org.epics.css.dal.context.ConnectionState;
import org.epics.css.dal.impl.PropertyUtilities;
import org.epics.css.dal.impl.RequestImpl;
import org.epics.css.dal.impl.ResponseImpl;
import org.epics.css.dal.proxy.AbstractProxyImpl;
import org.epics.css.dal.proxy.DirectoryProxy;
import org.epics.css.dal.proxy.MonitorProxy;
import org.epics.css.dal.proxy.PropertyProxy;

import com.cosylab.naming.URIName;

public class PropertyProxyImpl<T> extends AbstractProxyImpl
implements PropertyProxy<T>, DirectoryProxy {
	
	protected ProvideRunnable<T> dataProvider=null;
	protected List<MonitorProxyImpl<T>> monitors = new ArrayList<MonitorProxyImpl<T>>(1);
	protected DynamicValueCondition condition;

	public PropertyProxyImpl(String name) {
		super(name);
	}

	protected void connect()
	{
		condition = new DynamicValueCondition(EnumSet.of(DynamicValueState.NORMAL), new Timestamp(),"NORMAL");
		// *** Race condition, the state can't go to Connected until all the listeners have
		// been setup. 
		Timer t = new Timer();
		t.schedule(new TimerTask() {
				@Override
				public void run()
				{
					setConnectionState(ConnectionState.CONNECTED);
					dataProvider.refresh();
				}
			}, 1000);
		
	}



	@Override
	public void destroy() {
		dataProvider.stop();
		destroyMonitors();
		super.destroy();
	}

	/**
	 * Destroy all monitors.
	 */
	@SuppressWarnings("unchecked")
	private void destroyMonitors()
	{
		MonitorProxyImpl<T>[] array = new MonitorProxyImpl[monitors.size()];
		synchronized (monitors) { 
			array = monitors.toArray(array);
		}
		
		// destroy all
		for (int i = 0; i < array.length; i++)
			array[i].destroy();
	}
	
	@Override
	public MonitorProxy createMonitor(ResponseListener<T> callback, Map<String,Object> parameters)
			throws RemoteException {
		if (getConnectionState() == ConnectionState.DESTROYED)
			throw new RemoteException(this, "Proxy destroyed.");

		MonitorProxyImpl<T> m = new MonitorProxyImpl<T>(this, callback, dataProvider);
		monitors.add(m);
		return m;
	}

	@Override
	public DynamicValueCondition getCondition() {
		return condition;
	}

	@Override
	public Request<T> getValueAsync(ResponseListener<T> callback)
			throws DataExchangeException {
		// TODO Auto-generated method stub
		if (getConnectionState() != ConnectionState.CONNECTED) {
			throw new DataExchangeException(this, "Proxy not connected");
		}

		RequestImpl<T> r = new RequestImpl<T>(this, callback);
		r.addResponse(new ResponseImpl<T>(this, r, dataProvider.getCurrentValue(), "value",
		        true, null, getCondition(), null, true));

		return r;
	}

	@Override
	public boolean isSettable() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Request<T> setValueAsync(T value,
			ResponseListener<T> callback) throws DataExchangeException {
		if (getConnectionState() != ConnectionState.CONNECTED) {
			throw new DataExchangeException(this, "Proxy not connected");
		}
		
		dataProvider.setTargetValue(value);

		RequestImpl<T> r = new RequestImpl<T>(this, callback);
		r.addResponse(new ResponseImpl<T>(this, r, value, "", true, null,
		        condition, null, true));

		return r;
	}

	/*@Override
	public Object getCharacteristic(String characteristicName)
			throws DataExchangeException {
		if ("Custom Characteristic".equals(characteristicName)) {
			return "Hello!";
		} else if (NumericPropertyCharacteristics.C_GRAPH_MIN.equals(characteristicName)) {
			return new Double(0);
		} else if (NumericPropertyCharacteristics.C_GRAPH_MAX.equals(characteristicName)) {
			return new Double(100);
		}
		return null;
	}

	@Override
	public String[] getCharacteristicNames() throws DataExchangeException {
		return new String[] {"Custom Characteristic",
				NumericPropertyCharacteristics.C_GRAPH_MIN,
				NumericPropertyCharacteristics.C_GRAPH_MAX};
	}

	@Override
	public Request<? extends Object> getCharacteristics(
			String[] characteristics,
			ResponseListener<? extends Object> callback)
			throws DataExchangeException {
		// TODO Auto-generated method stub
		return null;
	}
	
	
*/
	
	@Override
	public Object getCharacteristic(String characteristicName)
			throws DataExchangeException {
	
			DirContext ctx = GdaPlug.getInstance().getDefaultDirectory();

			try {
				URIName uri = new URIName(null, GdaPlug.DEFAULT_AUTHORITY,
					    this.getUniqueName(), null);
				Attributes attr = ctx.getAttributes(uri);
				Object characteristic = null;

				if (attr instanceof org.epics.css.dal.directory.Attributes) {
					org.epics.css.dal.directory.Attributes at = (org.epics.css.dal.directory.Attributes)attr;
					characteristic = at.getAttributeValue(characteristicName);
				} else if (attr != null) {
					characteristic = attr.get(characteristicName).get();
				}

				if (characteristic == null) {
					uri = new URIName(null, GdaPlug.DEFAULT_AUTHORITY,
						    this.getClass().getSimpleName(), null);
					attr = ctx.getAttributes(uri);

					if (attr instanceof org.epics.css.dal.directory.Attributes) {
						org.epics.css.dal.directory.Attributes at = (org.epics.css.dal.directory.Attributes)attr;
						characteristic = at.getAttributeValue(characteristicName);
					} else if (attr != null) {
						characteristic = attr.get(characteristicName).get();
					}
				}

				return characteristic;
			} catch (NamingException e) {
				throw new RuntimeException("Cannot instanitate URIName.", e);
			}
		}

		@SuppressWarnings("null")
		public static Object putCharacteristic(String characteristicName,
			    String propertyUniqueName, Object value)
		{
			DirContext ctx = GdaPlug.getInstance().getDefaultDirectory();

			try {
				URIName uri = new URIName(null, GdaPlug.DEFAULT_AUTHORITY,
					    propertyUniqueName, null);
				Attributes attr = ctx.getAttributes(uri);
				
				if (attr==null) {
					attr=new org.epics.css.dal.directory.Attributes();
					ctx.bind(uri, null, attr);
				}
				
				Object characteristic = null;

				if (attr instanceof org.epics.css.dal.directory.Attributes) {
					org.epics.css.dal.directory.Attributes at = (org.epics.css.dal.directory.Attributes)attr;
					characteristic = at.putAttributeValue(characteristicName, value);
				} else if (attr != null) {
					characteristic = attr.put(characteristicName, value);
				}


				return characteristic;
			} catch (NamingException e) {
				throw new RuntimeException("Cannot instanitate URIName.", e);
			}
		}

		/**
		 * @see DirectoryProxy#getCharacteristicNames()
		 */
		@Override
		public String[] getCharacteristicNames() throws DataExchangeException
		{
			DirContext ctx = GdaPlug.getInstance().getDefaultDirectory();

			try {
				URIName uri = new URIName(null, GdaPlug.DEFAULT_AUTHORITY,
					    this.getUniqueName(), null);
				Attributes attr = ctx.getAttributes(uri);

				if (attr == null) {
					uri = new URIName(null, GdaPlug.DEFAULT_AUTHORITY,
						    this.getClass().getSimpleName(), null);
					attr = ctx.getAttributes(uri);
				}

				NamingEnumeration<String> en = attr.getIDs();
				ArrayList<String> list = new ArrayList<String>();

				while (en.hasMore()) {
					list.add(en.next());
				}

				String[] names = new String[list.size()];

				return list.toArray(names);
			} catch (NamingException e) {
				throw new RuntimeException("Cannot instanitate URIName.", e);
			}
		}
		/**
		 * @see DirectoryProxy#getCharacteristics(String[], ResponseListener)
		 */
		@Override
		@SuppressWarnings("unchecked")
		public Request<? extends Object> getCharacteristics(String[] characteristics,
		    ResponseListener<? extends Object> callback) throws DataExchangeException
		{
			RequestImpl<Object> r = new RequestImpl<Object>(this, (ResponseListener<Object>) callback);

			for (int i = 0; i < characteristics.length; i++) {
				Object value = PropertyUtilities.verifyCharacteristic(this, characteristics[i], getCharacteristic(characteristics[i]));
				r.addResponse(new ResponseImpl<Object>(this, r, value, characteristics[i],
				        value != null, null, condition, null, true));
			}

			return r;
		}
	@Override
	public String[] getCommandNames() throws DataExchangeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getPropertyNames() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<? extends SimpleProperty<?>> getPropertyType(
			String propertyName) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}

//	@Override
//	public MonitorProxy createMonitor(ResponseListener<T> callback, Map<String, Object> parameters)
//			throws RemoteException {
//		// TODO Auto-generated method stub
//		return null;
//	}


}
