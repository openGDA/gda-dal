/*-
 * Copyright © 2010 Diamond Light Source Ltd.
 *
 * This file is part of GDA.
 *
 * GDA is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License version 3 as published by the Free
 * Software Foundation.
 *
 * GDA is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along
 * with GDA. If not, see <http://www.gnu.org/licenses/>.
 */

package uk.ac.gda.dal.dataprovider;

import gda.device.Scannable;
import gda.factory.Findable;
import gda.factory.Finder;
import gda.jython.JythonServerFacade;
import gda.observable.IObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.csstudio.dal.Timestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ProvideOffsetRunnable<T> implements ProvideRunnable<T> {
	public static final Logger logger = LoggerFactory.getLogger(ProvideHighLimitRunnable.class);
	private double currentValue = 0.0;
	private boolean running;
	private List<ProvideDataEventListener<T>> listeners = new ArrayList<ProvideDataEventListener<T>>(1);
	private String scannableName;
	private ProvideDataEvent<T> event = null;
	private Scannable scannable;
	private IObserver observer = null;
	private TimerTask timerTask;
	private Timer timer = new Timer();
	
	public ProvideOffsetRunnable(String scannableName) {
		createTimers(scannableName);
	}

	private void createTimers(String scannableName){
		int index = scannableName.lastIndexOf(".");
		if (index != -1)
			this.scannableName = scannableName.substring(0, index);
		updateListeners(readValue());
		Findable findable = Finder.getInstance().find(scannableName.substring(0, scannableName.indexOf('.')));
		if (findable instanceof Scannable) {
			scannable = (Scannable) findable;
			scannable.addIObserver(observer = new IObserver() {
				@Override
				public void update(Object source, Object arg) {
					if (source == null) {
						try {
							if (timerTask != null)
								timerTask.cancel();
							timerTask = new TimerTask() {
								@Override
								public void run() {
									refresh();
								}
							};
							timer.schedule(timerTask, 100, 300);
						} catch (IllegalStateException e) {
						}
					}
				}
			});
		}
		observer.update(null, null);
		running = true;
	}
	
	private Object readValue() {
		Double offset=0.0;
		String evaluated = JythonServerFacade.getInstance().evaluateCommand(this.scannableName+".getOffset()[0]");
		if(!evaluated.equals("")&&!evaluated.equals("nan"))
			offset = Double.parseDouble(evaluated);
		return offset; 
	}

	private void updateListeners(Object position) {
		event = new ProvideDataEvent<T>();
		try {
			event.value = createValue(position);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			event = null;
			return;
		}
		event.timestamp = new Timestamp();
		for (ProvideDataEventListener<T> listener : listeners)
			listener.newData(event);
	}


	@Override
	public void addListener(ProvideDataEventListener<T> newListener) {
		listeners.add(newListener);
		if (event != null)
			newListener.newData(event);
	}

	abstract T createValue(Object val);
	
	@Override
	public T getCurrentValue() {
		return createValue(currentValue);
	}

	@Override
	public boolean isRunning() {
		return running;
	}

	@Override
	public void refresh() {
		updateListeners(readValue());
	}

	@Override
	public void removeListener(ProvideDataEventListener<T> listenerToRemove) {
		listeners.remove(listenerToRemove);
	}

	@Override
	public void setTargetValue(Object targetValue) {
		String command = this.scannableName + ".setOffset(" + targetValue + ")";
		JythonServerFacade.getInstance().runCommand(command);
	}

	@Override
	public void stop() {
		running = true;
	}
	
}