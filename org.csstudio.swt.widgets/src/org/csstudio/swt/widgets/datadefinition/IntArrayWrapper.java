package org.csstudio.swt.widgets.datadefinition;

/**A wrapper for int[].
 * @author Xihui Chen
 *
 */
public class IntArrayWrapper implements IPrimaryArrayWrapper {
	
	private int[] data;
	
	

	public IntArrayWrapper(int[] data) {
		this.data = data;
	}

	public void setData(int[] data) {
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
