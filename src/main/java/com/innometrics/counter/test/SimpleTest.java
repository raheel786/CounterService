package com.innometrics.counter.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.junit.Test;

import com.innometrics.counter.model.Counter;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;

/***
 * 27 July, 2015
 * 
 * @author Raheel Tanvir - A very simple unit test class that tests all the 3
 *         methods of the service which demonstrates usage of this web service
 *         API too. ****** Note ******* This test works only at the start of the
 *         webservice when all the counters have initial 0 value, any subsequent
 *         call will result in failure.
 * 
 */
public class SimpleTest {

	private static final int STATUS_OK = 200;
	private static final int NOT_FOUND = 404;

	private Counter testGet(String counterName) {
		final String url = "http://localhost:8080/CounterService/rest/counter/getCounter/"
				+ counterName;
		Client client = Client.create();
		WebResource wr = client.resource(url);
		ClientResponse response = null;
		response = wr.accept(MediaType.APPLICATION_XML).get(
				ClientResponse.class);
		Counter result = null;
		if (response.getStatus() == STATUS_OK) {
			result = response.getEntity(Counter.class);
		}
		return result;
	}

	private int testIncrement(String counterName) {
		final String url = "http://localhost:8080/CounterService/rest/counter/incrementCounter/"
				+ counterName;
		Client client = Client.create();
		WebResource wr = client.resource(url);
		ClientResponse response = null;
		response = wr.accept(MediaType.APPLICATION_XML).post(
				ClientResponse.class);
		return response.getStatus();
	}

	private List<Counter> getAllCounters() {
		final String url = "http://localhost:8080/CounterService/rest/counter/getAll";
		List<Counter> result = Client.create().resource(url)
				.get(new GenericType<List<Counter>>() {
				});
		return result;
	}

	@Test
	public void testAll() {
		// First lets check an error condition, counter99 does not exist so
		// Counter object should be null
		String counterName = "counter99";
		Counter counter = testGet(counterName);
		// Verify request failed and object returned is null
		assertNull(counter);
		// Now lets test with a legitimate request, counter1 exists so request
		// should succeed
		counterName = "counter1";
		counter = testGet(counterName);
		// Verify request succeeded and object returned is not null
		assertNotNull(counter);
		String expectedName = counterName;
		String actualName = counter.getName();
		// Make sure name fetched matches with the name provided
		assertEquals(expectedName, actualName);
		// Since this is initial fetch without performing any increment so
		// expected value is 0
		long expectedValue = 0;
		long actualValue = counter.getValue();
		// Make sure the value fetched matches with the expected value
		assertEquals(expectedValue, actualValue);
		// Checking failure condition now for increment method
		counterName = "counter99"; // counter99 does not exist
		int status = testIncrement(counterName);
		// Make sure server returned the NOT_FOUND status
		assertEquals(NOT_FOUND, status);
		// Now lets perform an increment on a named counter i.e. counter1
		counterName = "counter1";
		status = testIncrement(counterName);
		// Make sure server returned the OK status
		assertEquals(STATUS_OK, status);
		counter = testGet(counterName);
		actualName = counter.getName();
		// Make sure name fetched matches with the name provided
		assertEquals(expectedName, actualName);
		// Since an increment has been performed so expected value is 1
		expectedValue = 1;
		actualValue = counter.getValue();
		// Make sure the value fetched matches with the expected value
		assertEquals(expectedValue, actualValue);
		// To make it bit more interesting, lets increment other counters
		// multiple times
		testIncrement("counter2");
		testIncrement("counter2");
		testIncrement("counter2");
		testIncrement("counter3");
		testIncrement("counter3");
		// Lets get now list of counters and their values
		List<Counter> list = getAllCounters();
		// List shouldn't be null
		assertNotNull(list);
		// List should have 3 counters
		expectedValue = 3;
		actualValue = list.size();
		assertEquals(expectedValue, actualValue);
		for (Counter tmpCounter : list) {
			if (tmpCounter.getName().equals("counter1")) {
				// This is the counter for which increment has been called so
				// it's value MUST be 1
				expectedValue = 1;
				actualValue = tmpCounter.getValue();
				assertEquals(expectedValue, actualValue);
			} else if (tmpCounter.getName().equals("counter2")) {
				// Thrice increment called for this counters so this should have
				// value of 3
				expectedValue = 3;
				actualValue = tmpCounter.getValue();
				assertEquals(expectedValue, actualValue);
			} else if (tmpCounter.getName().equals("counter3")) {
				// twice increment called for this counters so this should have
				// value of 2
				expectedValue = 2;
				actualValue = tmpCounter.getValue();
				assertEquals(expectedValue, actualValue);
			}

		}

	}
}