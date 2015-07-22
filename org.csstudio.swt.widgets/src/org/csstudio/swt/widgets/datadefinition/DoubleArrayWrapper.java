package org.csstudio.swt.widgets.datadefinition;

/**A wrapper for double[].
 * @author Xihui Chen
 *
 */
public class DoubleArrayWrapper implements IPrimaryArrayWrapper {
	
	public double[] data;
	
	

	public DoubleArrayWrapper(double[] data) {
		this.data = data;
	}

	public void setData(double[] data) {
		this.data = data;
	}
	
	@Override
	public double get(int i) {
		return data[i];
	}

	@Override
	public int getSize() {
		return data.length;
	}

}
