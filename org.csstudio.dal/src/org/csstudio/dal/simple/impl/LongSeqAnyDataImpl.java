package org.csstudio.dal.simple.impl;

import java.util.Arrays;

import org.csstudio.dal.DynamicValueProperty;

public final class LongSeqAnyDataImpl extends AbstractAnyDataImpl<long[]> {
	
	public static final long[] UNINITIALIZED_VALUE = new long[]{Long.MIN_VALUE};
	
	public LongSeqAnyDataImpl(DynamicValueProperty<long[]> property, long beamID) {
		super(property,beamID);
	}
	public LongSeqAnyDataImpl(DynamicValueProperty<long[]> property) {
		super(property, Long.MIN_VALUE);
	}

	@Override
	public Object[] anySeqValue() {
		return DataUtil.toNumberSeq(response.getValue());
	}

	@Override
	public Object anyValue() {
		return response.getValue();
	}

	@Override
	public double[] doubleSeqValue() {
		return DataUtil.toDoubleSeq(response.getValue());
	}

	@Override
	public double doubleValue() {
		return response.getValue()[0];
	}

	@Override
	public long[] longSeqValue() {
		return response.getValue();
	}

	@Override
	public long longValue() {
		return response.getValue()[0];
	}

	@Override
	public Number[] numberSeqValue() {
		return DataUtil.toNumberSeq(response.getValue());
	}

	@Override
	public Number numberValue() {
		return response.getNumber();
	}

	@Override
	public String[] stringSeqValue() {
		return DataUtil.toStringSeq(response.getValue());
	}

	@Override
	public String stringValue() {
		return Arrays.toString(response.getValue());
	}
	@Override
	protected long[] confirmValue(long[] value) {
		if (value != null) return value;
		return UNINITIALIZED_VALUE;
	}

}
