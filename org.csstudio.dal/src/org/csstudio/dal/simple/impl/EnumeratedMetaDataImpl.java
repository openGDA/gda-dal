package org.csstudio.dal.simple.impl;

import org.csstudio.dal.AccessType;
import org.csstudio.dal.simple.MetaData;

@Deprecated
public final class EnumeratedMetaDataImpl implements MetaData {
	
	private final String[] states;
	private final Object[] values;
	
	public EnumeratedMetaDataImpl(String[] states, Object[] values) {
		this.states = states;
		this.values = values;
	}

	@Override
	public double getAlarmHigh() {
		// default value
		return Double.NaN;
	}

	@Override
	public double getAlarmLow() {
		// default value
		return Double.NaN;
	}

	@Override
	public double getDisplayHigh() {
		// default value
		return Double.NaN;
	}

	@Override
	public double getDisplayLow() {
		// default value
		return Double.NaN;
	}

	@Override
	public int getPrecision() {
		// default value
		return 0;
	}

	@Override
	public String getState(int state) {
		return states[state];
	}

	@Override
	public String[] getStates() {
		String[] s= new String[states.length];
		System.arraycopy(states, 0, s, 0, s.length);
		return s;
	}
	
	@Override
	public Object[] getStateValues() {
		Object[] v = new Object[values.length];
		System.arraycopy(values, 0, v, 0, v.length);
		return v;
	}
	
	@Override
	public Object getStateValue(int state) {
		return values[state];
	}

	@Override
	public String getUnits() {
		return null;
	}

	@Override
	public double getWarnHigh() {
		// default value
		return Double.NaN;
	}

	@Override
	public double getWarnLow() {
		// default value
		return Double.NaN;
	}

	@Override
	public AccessType getAccessType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDataType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFormat() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHostname() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getSequenceLength() {
		// TODO Auto-generated method stub
		return 0;
	}

}
