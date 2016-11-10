/*
 * Copyright 2016 EPAM Systems
 *
 *
 * This file is part of EPAM Report Portal.
 * https://github.com/epam/ReportPortal
 *
 * Report Portal is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Report Portal is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Report Portal.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.epam.reportportal.listeners;

import static com.epam.ta.reportportal.ws.model.launch.Mode.DEBUG;
import static com.epam.ta.reportportal.ws.model.launch.Mode.DEFAULT;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Test;
import org.slf4j.Logger;

import com.epam.reportportal.restclient.endpoint.exception.RestEndpointIOException;

public class ListenerUtilsTest {

	@Test
	public void getLaunchMode() {
		assertEquals(DEFAULT, ListenersUtils.getLaunchMode("notvalid"));
		assertEquals(DEBUG, ListenersUtils.getLaunchMode("Debug"));
	}

	@Test
	public void handleException_restEndpointIOExceptionAndNullLogger() {
		String message = "console message";
		RestEndpointIOException exception = new RestEndpointIOException(message);
		Logger nullLogger = null;

		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));

		ListenersUtils.handleException(exception, nullLogger, "");
		// check System.out.println(...) output
		assertEquals(String.format("%s%n", message), outContent.toString());
	}

	@Test
	public void handleException_restEndpointIOExceptionAndNonNullLogger() {
		String logMessage = "some log message";
		RestEndpointIOException exception = new RestEndpointIOException("");
		Logger loggerMock = mock(Logger.class);

		ListenersUtils.handleException(exception, loggerMock, logMessage);

		verify(loggerMock, times(1)).error(logMessage, exception);
	}

	@After
	public void cleanUpOutputStream() {
		System.setOut(null);
	}
}
