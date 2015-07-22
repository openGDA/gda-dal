package org.csstudio.dal.simple.impl;

import org.csstudio.dal.DynamicValueProperty;

public final class LongAnyDataImpl extends AbstractAnyDataImpl<Long> {
	
	public static final Long UNINITIALIZED_VALUE = Long.MIN_VALUE;
	
	public LongAnyDataImpl(DynamicValueProperty<Long> property, long beamID) {
		super(property,beamID);
	}
	public LongAnyDataImpl(DynamicValueProperty<Long> property) {
		super(property,Long.MIN_VALUE);
	}

	@Override
	public Object[] anySeqValue() {
		return new Object[]{response.getValue()};
	}

	@Override
	public Object anyValue() {
		return response.getValue();
	}

	@Override
	public double[] doubleSeqValue() {
		return new double[]{response.getValue()};
	}

	@Override
	public double doubleValue() {
		return response.getValue();
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
	protected Long confirmValue(Long value) {
		if (value != null) return value;
		return UNINITIALIZED_VALUE;
	}

}
