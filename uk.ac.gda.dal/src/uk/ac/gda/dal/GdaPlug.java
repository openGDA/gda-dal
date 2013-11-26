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


import java.util.Properties;

import javax.naming.NamingException;
import javax.naming.directory.DirContext;

import org.csstudio.dal.DoubleProperty;
import org.csstudio.dal.LongProperty;
import org.csstudio.dal.NumericPropertyCharacteristics;
import org.csstudio.dal.PropertyCharacteristics;
import org.csstudio.dal.RemoteException;
import org.csstudio.dal.SimpleProperty;
import org.csstudio.dal.context.AbstractApplicationContext;
import org.csstudio.dal.context.ConnectionException;
import org.csstudio.dal.device.AbstractDevice;
import org.csstudio.dal.impl.PropertyUtilities;
import org.csstudio.dal.proxy.AbstractPlug;
import org.csstudio.dal.proxy.DeviceProxy;
import org.csstudio.dal.proxy.DirectoryProxy;
import org.csstudio.dal.proxy.PropertyProxy;
import org.csstudio.dal.simple.RemoteInfo;
import org.epics.css.dal.directory.Attributes;
import org.epics.css.dal.directory.DirectoryUtilities;

import uk.ac.gda.dal.propproxy.DoublePropertyProxyImpl;
import uk.ac.gda.dal.propproxy.HighLimitPropertyProxyImpl;
import uk.ac.gda.dal.propproxy.LongPropertyProxyImpl;
import uk.ac.gda.dal.propproxy.LowLimitPropertyProxyImpl;
import uk.ac.gda.dal.propproxy.MoveByPropertyProxyImpl;
import uk.ac.gda.dal.propproxy.OffsetPropertyProxyImpl;
import uk.ac.gda.dal.propproxy.PropertyProxyImpl;
import uk.ac.gda.dal.propproxy.StopPropertyProxyImpl;
import uk.ac.gda.dal.propproxy.StringPropertyProxyImpl;
import uk.ac.gda.dal.propproxy.StringScannableStatusPropertyProxyImpl;
import uk.ac.gda.dal.propproxy.UnitsPropertyProxyImpl;

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
	protected DirectoryProxy createNewDirectoryProxy(String uniqueName) throws ConnectionException {
		throw new RuntimeException("Error in factory implementation, PropertyProxy must be created first.");
	}

	@Override
	protected Class<? extends AbstractDevice> getDeviceImplementationClass(String uniqueDeviceName) {
		throw new UnsupportedOperationException("Devices not supported");
	}

	@Override
	protected Class<? extends DeviceProxy<?>> getDeviceProxyImplementationClass(String uniqueDeviceName) throws RemoteException {
		throw new UnsupportedOperationException("Devices not supported");
	}

	@Override
	public String getPlugType() {
		return PLUG_TYPE;
	}

	@Override
	public Class<? extends SimpleProperty<?>> getPropertyImplementationClass(Class<? extends SimpleProperty<?>> type, String propertyName) throws RemoteException {
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
	protected Class<? extends PropertyProxy<?, ?>> getPropertyProxyImplementationClass(String uniquePropertyName) throws RemoteException {
		return null;
	}

	
	@Override
	public Class<? extends PropertyProxy<?,?>> getPropertyProxyImplementationClass(Class<? extends SimpleProperty<?>> type, Class<? extends SimpleProperty<?>> implType, String propertyName) throws RemoteException { 
		Class<? extends PropertyProxy<?,?>> impl = super.getPropertyProxyImplementationClass(type, implType, propertyName);
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
				if (type.equals(DoubleProperty.class))
					return DoublePropertyProxyImpl.class;
				else if (type.equals(LongProperty.class))
					return LongPropertyProxyImpl.class;
			}
			return StringPropertyProxyImpl.class;
		}
		return impl;
	}


	@Override
	public void releaseInstance() throws Exception {
		// noop, since it is a singleton
	}

	@Override
	public DirContext getDefaultDirectory() {
		try {
			DirContext initialContext = DirectoryUtilities.getInitialContext();

			URIName name = new URIName(RemoteInfo.DAL_TYPE_PREFIX + SCHEME_SUFFIX, null, null, null);
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
			gdaContext.bind(ppi, new PropertyProxyImpl<Object>(ppi.toString(), instance), characteristics);

			ppi = new URIName(null, DEFAULT_AUTHORITY, "DoublePropertyPropertyImpl", null);

			Attributes characteristicsD = new org.epics.css.dal.directory.Attributes(); //TODO Attribute from Dal Simulator plugin
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
			gdaContext.bind(ppi, new DoublePropertyProxyImpl(instance, ppi.toString()), characteristicsD);

			ppi = new URIName(null, DEFAULT_AUTHORITY, "StringPropertyProxyImpl", null);

			Attributes characteristicsS = new org.epics.css.dal.directory.Attributes();
			characteristicsS.put(PropertyCharacteristics.C_DESCRIPTION, "Simulated Property");
			characteristicsS.put(PropertyCharacteristics.C_POSITION, new Double(0));
			characteristicsS.put(PropertyCharacteristics.C_PROPERTY_TYPE, "property");

			gdaContext.bind(ppi, new StringScannableStatusPropertyProxyImpl(instance, ppi.toString()), characteristicsS);

			return gdaContext;
		} catch (NamingException e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to instantiate context.", e);
		}
	}

	@Override
	public RemoteInfo createRemoteInfo(String uniqueName) throws NamingException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected <T extends PropertyProxy<?, ?>> T createNewPropertyProxy(String uniqueName, Class<T> type) throws ConnectionException {
		
		try {
			final PropertyProxy<?,?> p = type.getConstructor(GdaPlug.class, String.class).newInstance(this, uniqueName);
			// add to directory cache
			if (p instanceof DirectoryProxy) {
                putDirectoryProxyToCache((DirectoryProxy<?>) p);
            }
			return type.cast(p);
		} catch (final Exception e) {
			throw new ConnectionException(this,
					"Failed to instantiate property proxy '" + uniqueName
							+ "' for type '" + type.getName() + "'.", e);
		}
	}
	
	@Override
	protected <T extends DeviceProxy<?>> T createNewDeviceProxy(String uniqueName, Class<T> type) throws ConnectionException {
		throw new UnsupportedOperationException("Devices not supported");
	}

}