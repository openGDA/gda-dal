/* 
 * Copyright (c) 2006 Stiftung Deutsches Elektronen-Synchroton, 
 * Member of the Helmholtz Association, (DESY), HAMBURG, GERMANY.
 *
 * THIS SOFTWARE IS PROVIDED UNDER THIS LICENSE ON AN "../AS IS" BASIS. 
 * WITHOUT WARRANTY OF ANY KIND, EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED 
 * TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR PARTICULAR PURPOSE AND 
 * NON-INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE 
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, 
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR 
 * THE USE OR OTHER DEALINGS IN THE SOFTWARE. SHOULD THE SOFTWARE PROVE DEFECTIVE 
 * IN ANY RESPECT, THE USER ASSUMES THE COST OF ANY NECESSARY SERVICING, REPAIR OR 
 * CORRECTION. THIS DISCLAIMER OF WARRANTY CONSTITUTES AN ESSENTIAL PART OF THIS LICENSE. 
 * NO USE OF ANY SOFTWARE IS AUTHORIZED HEREUNDER EXCEPT UNDER THIS DISCLAIMER.
 * DESY HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, 
 * OR MODIFICATIONS.
 * THE FULL LICENSE SPECIFYING FOR THE SOFTWARE THE REDISTRIBUTION, MODIFICATION, 
 * USAGE AND OTHER RIGHTS AND OBLIGATIONS IS INCLUDED WITH THE DISTRIBUTION OF THIS 
 * PROJECT IN THE FILE LICENSE.HTML. IF THE LICENSE IS NOT INCLUDED YOU MAY FIND A COPY 
 * AT HTTP://WWW.DESY.DE/LEGAL/LICENSE.HTM
 */
package org.csstudio.sds.internal.model;

import org.csstudio.sds.model.PropertyTypesEnum;
import org.csstudio.sds.model.WidgetProperty;
import org.csstudio.sds.model.WidgetPropertyCategory;

/**
 * A property, which is able to handle arrays of double values.
 * 
 * @author Sven Wende
 * @version $Revision: 1.1 $
 * 
 */
public final class DoubleArrayProperty extends WidgetProperty {

	/**
	 * Constructor.
	 * 
	 * @param description
	 *            a description
	 * @param category
	 *            a category
	 * @param defaultValue
	 *            the default value
	 */
	public DoubleArrayProperty(final String description,
			final WidgetPropertyCategory category, final double[] defaultValue) {
		super(PropertyTypesEnum.DOUBLEARRAY, description, category, defaultValue,
				null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object checkValue(final Object value) {
		assert value != null : "value!=null"; //$NON-NLS-1$

		double[] acceptedValue = null;

		// check type
		if (!(value instanceof double[])) {
			if (value instanceof Number) {
				acceptedValue = new double[]{((Number) value).intValue()};
			} else {
				try {
					double v = Double.parseDouble(value.toString());
					acceptedValue = new double[]{v};
				} catch (NumberFormatException e) {
					acceptedValue = null;
				}
			}
		} else {
			acceptedValue = (double[]) value;
			for (Double d : acceptedValue) {
				if (d.isNaN()) {
					d = new Double(0);
				}
				if (d.isInfinite()) {
					if (d==Double.NEGATIVE_INFINITY) {
						d=-Double.MAX_VALUE;
					} else {
						d=Double.MAX_VALUE;
					}
				}
			}
		}

		return acceptedValue;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class[] getCompatibleJavaTypes() {
		return new Class[]{Number.class};
	}
}
