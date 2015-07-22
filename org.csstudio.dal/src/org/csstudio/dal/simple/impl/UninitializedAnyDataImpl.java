package org.csstudio.dal.simple.impl;

import org.csstudio.dal.DynamicValueProperty;
import org.csstudio.dal.simple.Severity;

public class UninitializedAnyDataImpl<T> extends AbstractAnyDataImpl<T> {
	
	private Severity severity = new Severity() {

		@Override
		public boolean hasValue() {
			return false;
		}

		@Override
		public boolean isInvalid() {
			return true;
		}

		@Override
		public boolean isMajor() {
			return false;
		}

		@Override
		public boolean isMinor() {
			return false;
		}

		@Override
		public boolean isOK() {
			return false;
		}

        @Override
		public String descriptionToString() {
            return null;
        }

		@Override
		public String getSeverityInfo() {
			return "UNKNOWN";
		}};

	public UninitializedAnyDataImpl(DynamicValueProperty<T> property) {
		super(property,Long.MIN_VALUE);
	}
	public UninitializedAnyDataImpl(DynamicValueProperty<T> property, long beamID) {
		super(property, beamID);
	}

	/* (non-Javadoc)
	 * @see org.csstudio.dal.simple.impl.AbstractAnyDataImpl#getSeverity()
	 */
	@Override
	public Severity getSeverity() {
		return severity;
	}
	/* (non-Javadoc)
	 * @see org.csstudio.dal.simple.impl.AbstractAnyDataImpl#isValid()
	 */
	@Override
	public boolean isValid() {
		return false;
	}
	/* (non-Javadoc)
	 * @see org.csstudio.dal.simple.impl.AbstractAnyDataImpl#getQuality()
	 */
	@Override
	public Quality getQuality() {
		return Quality.Invalid;
	}

	/* (non-Javadoc)
	 * @see org.csstudio.dal.simple.impl.AbstractAnyDataImpl#getStatus()
	 */
	@Override
	public String getStatus() {
		return "Uninitialized";
	}

	@Override
	public Object[] anySeqValue() {
		return null;
	}

	@Override
	public Object anyValue() {
		return null;
	}

	@Override
	public double[] doubleSeqValue() {
		return null;
	}

	@Override
	public double doubleValue() {
		return Double.NaN;
	}

	@Override
	public long[] longSeqValue() {
		return null;
	}

	@Override
	public long longValue() {
		return 0;
	}

	@Override
	public Number[] numberSeqValue() {
		return null;
	}

	@Override
	public Number numberValue() {
		return null;
	}

	@Override
	public String[] stringSeqValue() {
		return null;
	}

	@Override
	public String stringValue() {
		return null;
	}
	
	@Override
	protected T confirmValue(T value) { 
		return value;
	}

}
