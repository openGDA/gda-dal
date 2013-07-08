/*-
 * Copyright © 2009 Diamond Light Source Ltd.
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

package gda.dal.dataprovider;

import gda.device.DeviceException;
import gda.device.Scannable;
import gda.device.scannable.ScannableStatus;
import gda.factory.Findable;
import gda.factory.Finder;
import gda.observable.IObserver;

import java.util.ArrayList;
import java.util.List;

import org.epics.css.dal.Timestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class ProvideStatusRunnable<T> implements ProvideRunnable<T> {
	private static final Logger logger = LoggerFactory.getLogger(ProvideStatusRunnable.class);

	private Scannable scannable;

	private boolean running = false;

	protected int currentStatus = ScannableStatus.IDLE;
	// TODO: listeners list isn't used thread safely
	protected List<ProvideDataEventListener<T>> listeners = new ArrayList<ProvideDataEventListener<T>>(1);

	private IObserver observer;

	private ProvideDataEvent<T> event;

	@Override
	public T getCurrentValue() {
		return createValue(currentStatus);
	}

	@Override
	public void setTargetValue(Object targetValue) {
		try {
			if (scannable.isBusy()) {
				Runnable x = new Runnable() {
					@Override
					public void run() {

						try {
							scannable.stop();
						} catch (DeviceException e) {
							logger.error(e.getMessage(), e);
						}

					}
				};

				Thread t = new Thread(x, "stop");
				t.start();
			}
		} catch (DeviceException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public ProvideStatusRunnable(String scannableName) {
		int index = scannableName.indexOf(".");
		if (index != -1)
			scannableName = scannableName.substring(0, index);
		Findable findable = Finder.getInstance().find(scannableName);
		if (findable instanceof Scannable) {
			this.scannable = (Scannable) findable;
			this.scannable.addIObserver(observer = new IObserver() {

				@Override
				public void update(Object source, Object arg) {
					if (source instanceof Scannable && arg instanceof ScannableStatus) {
						sendStatusUpdate(((ScannableStatus) arg).getStatus());
					}
				}

			});
			try {
				sendStatusUpdate(scannable.isBusy()? ScannableStatus.BUSY : ScannableStatus.IDLE);
				running = true;
			} catch (DeviceException e) {
				logger.error(e.getMessage(), e);
			}
		} else {
			stop();
			throw new RuntimeException("ProvideDataRunnable. " + scannableName + " is not a Scannable");
		}
	}

	private void sendStatusUpdate(int isBusy) {
		event = new ProvideDataEvent<T>();
		event.value = createValue(isBusy);
		event.timestamp = new Timestamp();

		for (ProvideDataEventListener<T> listener : listeners) {
			listener.newData(event);
		}
	}

	abstract T createValue(int val);

	@Override
	public void stop() {
		running = false;
		if (scannable != null && observer != null) {
			scannable.deleteIObserver(observer);
			observer = null;
		}
	}

	@Override
	public boolean isRunning() {
		return running;
	}

	@Override
	public void addListener(ProvideDataEventListener<T> newListener) {
		listeners.add(newListener);
		if (event != null)
			newListener.newData(event);
	}

	@Override
	public void removeListener(ProvideDataEventListener<T> listenerToRemove) {
		listeners.remove(listenerToRemove);

	}

	@Override
	public void refresh() {
		
	}
}
