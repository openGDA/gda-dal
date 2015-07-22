package org.csstudio.dal.simple.impl;

import org.csstudio.dal.AccessType;
import org.csstudio.dal.simple.MetaData;

@Deprecated
public final class NumericMetaDataImpl implements MetaData {
	
	private final double alarmHigh;
	private final double alarmLow;
	private final double displayHigh;
	private final double displayLow;
	private final double warnHigh;
	private final double warnLow;
	private final int precision;
	private final String units;
	
	public NumericMetaDataImpl(double displayLow, double displayHigh, double warnLow, double warnHigh, double alarmLow, double alarmHigh, int precision, String units) {
		this.alarmHigh = alarmHigh;
		this.alarmLow = alarmLow;
		this.displayHigh = displayHigh;
		this.displayLow = displayLow;
		this.warnHigh = warnHigh;
		this.warnLow = warnLow;
		this.precision = precision;
		this.units = units;
	}

	@Override
	public double getAlarmHigh() {
		return alarmHigh;
	}

	@Override
	public double getAlarmLow() {
		return alarmLow;
	}

	@Override
	public double getDisplayHigh() {
		return displayHigh;
	}

	@Override
	public double getDisplayLow() {
		return displayLow;
	}

	@Override
	public int getPrecision() {
		return precision;
	}

	@Override
	public String getState(int state) {
		// default value
		return null;
	}

	@Override
	public String[] getStates() {
		return new String[0];
	}
	
	@Override
	public Object getStateValue(int state) {
		//default value
		return null;
	}
	
	@Override
	public Object[] getStateValues() {
		return new Object[0];
	}
	

	@Override
	public String getUnits() {
		return units;
	}

	@Override
	public double getWarnHigh() {
		return warnHigh;
	}

	@Override
	public double getWarnLow() {
		return warnLow;
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
