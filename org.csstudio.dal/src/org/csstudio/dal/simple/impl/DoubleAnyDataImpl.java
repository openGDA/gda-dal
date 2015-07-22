package org.csstudio.dal.simple.impl;

import org.csstudio.dal.DynamicValueProperty;

public final class DoubleAnyDataImpl extends AbstractAnyDataImpl<Double> {
	
	public static final Double UNINITIALIZED_DOUBLE_VALUE = Double.NaN;
	
	public DoubleAnyDataImpl(DynamicValueProperty<Double> property) {
		super(property,Long.MIN_VALUE);
	}
	public DoubleAnyDataImpl(DynamicValueProperty<Double> property, long beamID) {
		super(property, beamID);
	}

	@Override
	public Object[] anySeqValue() {
		return new Object[]{response.getNumber()};
	}

	@Override
	public Object anyValue() {
		return response.getNumber();
	}

	@Override
	public double[] doubleSeqValue() {
		return new double[]{response.getValue()};
	}

	@Override
	public double doubleValue() {
		/*
		 * beware: 		java.lang.Long cannot be cast to java.lang.Double
		 * old: return response.getValue();
		 */
		return DataUtil.castTo(response.getValue(), Double.class);
	}

	@Override
	public long[] longSeqValue() {
		return new long[]{response.getNumber().longValue()};
	}

	@Override
	public long longValue() {
		return response.getNumber().longValue();
	}

	@Override
	public Number[] numberSeqValue() {
		return new Number[]{response.getNumber()};
	}

	@Override
	public Number numberValue() {
		return response.getNumber();
	}

	@Override
	public String[] stringSeqValue() {
		return new String[]{response.getNumber().toString()};
	}

	@Override
	public String stringValue() {
		return response.getNumber().toString();
	}
	
	@Override
	protected Double confirmValue(Double value) {
		if (value != null) return value;
		return UNINITIALIZED_DOUBLE_VALUE;
	}

}
