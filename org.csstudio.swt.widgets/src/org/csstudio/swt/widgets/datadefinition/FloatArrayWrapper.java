package org.csstudio.swt.widgets.datadefinition;

/**A wrapper for float[].
 * @author Xihui Chen
 *
 */
public class FloatArrayWrapper implements IPrimaryArrayWrapper {
	
	private float[] data;
	
	

	public FloatArrayWrapper(float[] data) {
		this.data = data;
	}

	public void setData(float[] data) {
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
