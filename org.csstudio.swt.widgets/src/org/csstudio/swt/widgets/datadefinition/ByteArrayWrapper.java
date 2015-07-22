package org.csstudio.swt.widgets.datadefinition;

/**A wrapper for byte[].
 * @author Xihui Chen
 *
 */
public class ByteArrayWrapper implements IPrimaryArrayWrapper {
	
	private byte[] data;
	
	

	public ByteArrayWrapper(byte[] data) {
		this.data = data;
	}

	public void setData(byte[] data) {
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
