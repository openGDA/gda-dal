package org.csstudio.sds.internal.connection;

import org.csstudio.dal.ResponseEvent;
import org.csstudio.dal.ResponseListener;
import org.csstudio.dal.simple.ConnectionParameters;
import org.csstudio.dal.simple.RemoteInfo;
import org.csstudio.dal.simple.SimpleDALBroker;
import org.csstudio.platform.model.pvs.ControlSystemEnum;
import org.csstudio.platform.model.pvs.IProcessVariableAddress;
import org.csstudio.platform.model.pvs.ValueType;
import org.csstudio.sds.model.DynamicsDescriptor;
import org.csstudio.sds.model.IPropertyChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SinglePropertyWriteConnector implements IPropertyChangeListener {
	private IProcessVariableAddress pv;
	private SimpleDALBroker broker;
    private static final Logger LOG = LoggerFactory.getLogger(SinglePropertyWriteConnector.class);
    
	public SinglePropertyWriteConnector(IProcessVariableAddress pv, ValueType valueType, SimpleDALBroker broker) {
		assert pv != null;
		assert valueType != null;
		this.pv = pv;
		this.broker = broker;
	}

	public void propertyManualValueChanged(String propertyId, Object manualValue) {
		ControlSystemEnum controlSystem = pv.getControlSystem();
		String responsibleDalPlugId = controlSystem.getResponsibleDalPlugId();
		String cs = "";
		if(responsibleDalPlugId!=null)
			cs = RemoteInfo.DAL_TYPE_PREFIX + responsibleDalPlugId;
		String property = pv.getProperty();
		RemoteInfo rinfo = new RemoteInfo(cs, property, null, null);
		try {
			ConnectionParameters connectionParameters = null;
			if(manualValue instanceof java.lang.String){
				connectionParameters = new ConnectionParameters(rinfo, java.lang.String.class);
				//setBrokerValue(connectionParameters, "dummy");
				setBrokerValue(connectionParameters, manualValue);
				Thread.sleep(1000);
			}
			else
				connectionParameters = new ConnectionParameters(rinfo);
			setBrokerValue(connectionParameters, manualValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setBrokerValue(ConnectionParameters connectionParameters, Object manualValue) throws Exception{
		broker.setValueAsync(connectionParameters, manualValue, new ResponseListener<Object>() {
			public void responseError(ResponseEvent event) {
				LOG.error("Could not set value for ["+pv.toString()+"].");
			}
			public void responseReceived(ResponseEvent event) {
				LOG.info("Value for ["+pv.toString()+"] was set.");
			}
		});
	}
	
	public void dynamicsDescriptorChanged(DynamicsDescriptor dynamicsDescriptor) {

	}

	public void propertyValueChanged(Object oldValue, Object newValue) {

	}
	
}