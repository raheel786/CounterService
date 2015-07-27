package com.innometrics.counter.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.innometrics.counter.model.Counter;

/***
 * July 27, 2015
 * 
 * @author Raheel Tanvir - Web Service to handle the requests
 */

@Path("/counter")
public class CounterService {

	/*
	 * Static class to hold the counter values, required for lazy initialization
	 * of final fields.
	 */
	private static class CounterHolder {
		// Immutable object references
		private final static AtomicLong counter1 = new AtomicLong(0);
		private final static AtomicLong counter2 = new AtomicLong(0);
		private final static AtomicLong counter3 = new AtomicLong(0);
		// Hashmap required to access a named counter, where key is the name of
		// the counter and value is the current value of the counter
		private final static ConcurrentHashMap<String, AtomicLong> countersMap = new ConcurrentHashMap<String, AtomicLong>();
		// Initializing the hashmap with arbitrary keys (counter names)
		static {
			countersMap.put("counter1", counter1);
			countersMap.put("counter2", counter2);
			countersMap.put("counter3", counter3);
		}
	}

	@GET
	@Path("/getCounter/{param}")
	@Produces(MediaType.APPLICATION_XML)
	public Response getCounter(@PathParam("param") String counterName) {
		// First check if the required counter request is legitimate i.e.
		// counter exists in the hashmap
		if (CounterHolder.countersMap.containsKey(counterName)) {
			// Yes it does, create a new object of Counter wrapper class with
			// the current counter name and it's value
			Counter counter = new Counter(counterName,
					CounterHolder.countersMap.get(counterName).get());
			// Return the newly created Counter object
			return Response.status(Response.Status.OK).entity(counter).build();

		}
		// Counter not found so send the appropriate response
		else {
			return Response.status(Response.Status.NOT_FOUND)
					.entity("counter " + counterName + " not found").build();
		}
	}

	@POST
	@Path("/incrementCounter/{param}")
	@Produces(MediaType.APPLICATION_XML)
	public Response incrementAndGet(@PathParam("param") String counterName) {
		// First check if the required counter request is legitimate i.e.
		// counter exists in hashmap
		if (CounterHolder.countersMap.containsKey(counterName)) {
			// Atomically increment the counter by 1, don't need the returned
			// value by this method so just discarding that
			CounterHolder.countersMap.get(counterName).incrementAndGet();
			return Response
					.status(Response.Status.OK)
					.entity("Counter " + counterName
							+ " incremented successfully!").build();

		}
		// Counter not found so send the appropriate response
		else {
			return Response.status(Response.Status.NOT_FOUND)
					.entity("counter " + counterName + " not found").build();
		}

	}

	@GET
	@Path("/getAll")
	@Produces(MediaType.APPLICATION_XML)
	public List<Counter> getAll() {
		// ArrayList to return
		final List<Counter> list = new ArrayList<Counter>();
		for (Entry<String, AtomicLong> entry : CounterHolder.countersMap
				.entrySet()) {
			list.add(new Counter(entry.getKey(), entry.getValue().get()));
		}

		return list;
	}

}