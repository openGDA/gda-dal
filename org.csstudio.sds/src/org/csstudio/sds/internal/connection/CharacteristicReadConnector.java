package org.csstudio.sds.internal.connection;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.csstudio.platform.model.pvs.ValueType;

public class CharacteristicReadConnector implements PropertyChangeListener {
	private ChannelInputProcessor channelInputProcessor;
	private String characteristic;

	public CharacteristicReadConnector(ChannelInputProcessor channelInputProcessor, ValueType valueType, String characteristic) {
		assert channelInputProcessor != null;
		assert valueType != null;
		assert characteristic != null;
		this.channelInputProcessor = channelInputProcessor;
		this.characteristic = characteristic;
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (characteristic.equalsIgnoreCase(event.getPropertyName()))
			channelInputProcessor.valueChanged(event.getNewValue());
	}
}
