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

import gda.dal.propproxy.DoublePropertyProxyImpl;
import gda.dal.propproxy.HighLimitPropertyProxyImpl;
import gda.dal.propproxy.LongPropertyProxyImpl;
import gda.dal.propproxy.LowLimitPropertyProxyImpl;
import gda.dal.propproxy.MoveByPropertyProxyImpl;
import gda.dal.propproxy.OffsetPropertyProxyImpl;
import gda.dal.propproxy.PropertyProxyImpl;
import gda.dal.propproxy.StopPropertyProxyImpl;
import gda.dal.propproxy.StringPropertyProxyImpl;
import gda.dal.propproxy.StringScannableStatusPropertyProxyImpl;
import gda.dal.propproxy.UnitsPropertyProxyImpl;

import java.util.Properties;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;

import org.epics.css.dal.DoubleProperty;
import org.epics.css.dal.NumericPropertyCharacteristics;
import org.epics.css.dal.PropertyCharacteristics;
import org.epics.css.dal.RemoteException;
import org.epics.css.dal.SimpleProperty;
import org.epics.css.dal.context.AbstractApplicationContext;
import org.epics.css.dal.context.ConnectionException;
import org.epics.css.dal.context.RemoteInfo;
import org.epics.css.dal.device.AbstractDevice;
import org.epics.css.dal.directory.DirectoryUtilities;
import org.epics.css.dal.impl.PropertyUtilities;
import org.epics.css.dal.proxy.AbstractPlug;
import org.epics.css.dal.proxy.DeviceProxy;
import org.epics.css.dal.proxy.DirectoryProxy;
import org.epics.css.dal.proxy.PropertyProxy;

import com.cosylab.naming.URIName;

public class GdaPlug extends AbstractPlug {
	public static final String PLUG_TYPE = "GDA";
	public static final String DEFAULT_AUTHORITY = "DEFAULT";
	public static final String SCHEME_SUFFIX = "GDA";
	private static GdaPlug instance;
	
	private DirContext gdaContext;

	protected GdaPlug(Properties configuration) {
		super(configuration);
	}

	public GdaPlug(AbstractApplicationContext ctx) {
		super(ctx);
	}


	public static GdaPlug getInstance() {
		return getInstance((Properties) null);
	}

	public static synchronized GdaPlug getInstance(Properties conf) {
		if (instance == null)
			instance = new GdaPlug(conf);
		return instance;
	}

	public static synchronized GdaPlug getInstance(AbstractApplicationContext ctx) {
		return new GdaPlug(ctx);
	}

	@Override
	protected <T extends DeviceProxy> T createNewDeviceProxy(String uniqueName, Class<T> type)
			throws ConnectionException {
		throw new UnsupportedOperationException("Devices not supported");
	}

	@Override
	protected DirectoryProxy createNewDirectoryProxy(String uniqueName) throws ConnectionException {
		throw new RuntimeException("Error in factory implementation, PropertyProxy must be created first.");
	}

	@Override
	protected <TT extends PropertyProxy<?>> TT createNewPropertyProxy(String uniqueName, Class<TT> type)
			throws ConnectionException {
		try {
			PropertyProxy<?> p = type.getConstructor(String.class).newInstance(uniqueName);
			// add to directory cache
			if (p instanceof DirectoryProxy)
				putDirectoryProxyToCache((DirectoryProxy) p);
			return type.cast(p);
		} catch (Exception e) {
			throw new ConnectionException(this, "Failed to instantiate property proxy '" + uniqueName + "' for type '"
					+ type.getName() + "'.", e);
		}
	}

	@Override
	protected Class<? extends AbstractDevice> getDeviceImplementationClass(String uniqueDeviceName) {
		throw new UnsupportedOperationException("Devices not supported");
	}

	@Override
	protected Class<? extends DeviceProxy> getDeviceProxyImplementationClass(String uniqueDeviceName)
			throws RemoteException {
		throw new UnsupportedOperationException("Devices not supported");
	}

	@Override
	public String getPlugType() {
		return PLUG_TYPE;
	}

	@Override
	public Class<? extends SimpleProperty<?>> getPropertyImplementationClass(Class<? extends SimpleProperty<?>> type,
			String propertyName) throws RemoteException {
		if (type == null)
			type = DoubleProperty.class;
		Class<? extends SimpleProperty<?>> impl = super.getPropertyImplementationClass(type, propertyName);
		if (impl == null)
			return PropertyUtilities.getImplementationClass(type);
		return impl;
	}

	@Override
	protected Class<? extends SimpleProperty<?>> getPropertyImplementationClass(String uniquePropertyName) throws RemoteException {
		throw new RuntimeException("Unsupported property type.");
	}

	@Override
	public Class<? extends PropertyProxy<?>> getPropertyProxyImplementationClass(Class<? extends SimpleProperty<?>> type, Class<? extends SimpleProperty<?>> implType, String propertyName) throws RemoteException {
		Class<? extends PropertyProxy<?>> impl = super.getPropertyProxyImplementationClass(type, implType, propertyName);
		if (impl == null) {
			int index = propertyName.lastIndexOf(".");
			if (index != -1) {
				String methodName = propertyName.substring(index + 1);
				if (methodName.equals("isBusy"))
					return StringScannableStatusPropertyProxyImpl.class;
				if (methodName.equals("position"))
					return DoublePropertyProxyImpl.class;
				if (methodName.equals("strPos"))
					return StringPropertyProxyImpl.class;
				if (methodName.equals("stop"))
					return StopPropertyProxyImpl.class;
				if (methodName.equals("lowlimit"))
					return LowLimitPropertyProxyImpl.class;
				if (methodName.equals("highlimit"))
					return HighLimitPropertyProxyImpl.class;
				if (methodName.equals("userunits"))
					return UnitsPropertyProxyImpl.class;
				if (methodName.equals("moveby"))
					return MoveByPropertyProxyImpl.class;
				if (methodName.equals("offset"))
					return OffsetPropertyProxyImpl.class;
			}
			if (type != null) {
				if (type.equals(org.epics.css.dal.DoubleProperty.class)) {
					return DoublePropertyProxyImpl.class;
				} else if (type.equals(org.epics.css.dal.LongProperty.class)) {
					return LongPropertyProxyImpl.class;
				}
			}
			return StringPropertyProxyImpl.class;
		}
		return impl;
	}

	@Override
	protected Class<? extends PropertyProxy<?>> getPropertyProxyImplementationClass(String uniquePropertyName) throws RemoteException {
		return null;
	}

	@Override
	public void releaseInstance() throws Exception {
		// noop, since it is a singleton
	}

	@Override
	public DirContext getDefaultDirectory() {
		try {
			DirContext initialContext = DirectoryUtilities.getInitialContext();

			URIName name = new URIName(RemoteInfo.SCHEME_PREFIX + SCHEME_SUFFIX, null, null, null);
			URIName nameS = (URIName) name.getPrefix(1);
			Object simContext = initialContext.lookup(nameS);

			if (simContext == null)
				gdaContext = (DirContext) initialContext.createSubcontext(nameS);
			else
				gdaContext = (DirContext) simContext;

			// bind proxy implementations
			URIName ppi = new URIName(null, DEFAULT_AUTHORITY, "PropertyProxyImpl", null);
			Attributes characteristics = new org.epics.css.dal.directory.Attributes();
			characteristics.put(PropertyCharacteristics.C_DESCRIPTION, "Simulated Property");
			characteristics.put(PropertyCharacteristics.C_POSITION, new Double(0));
			characteristics.put(PropertyCharacteristics.C_PROPERTY_TYPE, "property");
			characteristics.put(NumericPropertyCharacteristics.C_RESOLUTION, 0xFFFF);
			characteristics.put(NumericPropertyCharacteristics.C_SCALE_TYPE, "linear");
			characteristics.put(NumericPropertyCharacteristics.C_UNITS, "amper");
			gdaContext.bind(ppi, new PropertyProxyImpl<Object>(ppi.toString()), characteristics);

			ppi = new URIName(null, DEFAULT_AUTHORITY, "DoublePropertyPropertyImpl", null);

			Attributes characteristicsD = new org.epics.css.dal.directory.Attributes();
			characteristicsD.put(PropertyCharacteristics.C_DESCRIPTION, "Simulated Property");
			characteristicsD.put(PropertyCharacteristics.C_POSITION, new Double(0));
			characteristicsD.put(PropertyCharacteristics.C_PROPERTY_TYPE, "property");
			characteristicsD.put(NumericPropertyCharacteristics.C_RESOLUTION, 0xFFFF);
			characteristicsD.put(NumericPropertyCharacteristics.C_SCALE_TYPE, "linear");
			characteristicsD.put(NumericPropertyCharacteristics.C_UNITS, "amper");
			characteristicsD.put(NumericPropertyCharacteristics.C_FORMAT, "%.4f");
			characteristicsD.put(NumericPropertyCharacteristics.C_GRAPH_MAX, new Double(10));
			characteristicsD.put(NumericPropertyCharacteristics.C_GRAPH_MIN, new Double(-10));
			characteristicsD.put(NumericPropertyCharacteristics.C_MAXIMUM, new Double(10));
			characteristicsD.put(NumericPropertyCharacteristics.C_MINIMUM, new Double(-10));
			gdaContext.bind(ppi, new DoublePropertyProxyImpl(ppi.toString()), characteristicsD);

			ppi = new URIName(null, DEFAULT_AUTHORITY, "StringPropertyProxyImpl", null);

			Attributes characteristicsS = new org.epics.css.dal.directory.Attributes();
			characteristicsS.put(PropertyCharacteristics.C_DESCRIPTION, "Simulated Property");
			characteristicsS.put(PropertyCharacteristics.C_POSITION, new Double(0));
			characteristicsS.put(PropertyCharacteristics.C_PROPERTY_TYPE, "property");

			gdaContext.bind(ppi, new StringScannableStatusPropertyProxyImpl(ppi.toString()), characteristicsS);

			return gdaContext;
		} catch (NamingException e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to instantiate context.", e);
		}
	}

	@Override
	public org.epics.css.dal.simple.RemoteInfo createRemoteInfo(String uniqueName) throws NamingException {
		// TODO Auto-generated method stub
		return null;
	}

}
