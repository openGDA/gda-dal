package uk.ac.gda.dal;

import org.csstudio.dal.proxy.AbstractPlug;
import org.csstudio.dal.spi.AbstractPropertyFactory;

public class PropertyFactoryImpl extends AbstractPropertyFactory{
	
	@Override
	protected Class<? extends AbstractPlug> getPlugClass() {
		return GdaPlug.class;
	}

	public PropertyFactoryImpl() {
		super();
	}

}