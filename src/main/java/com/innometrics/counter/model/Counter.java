package com.innometrics.counter.model;

import javax.xml.bind.annotation.XmlRootElement;

/***
 * July 27, 2015
 * 
 * @author Raheel Tanvir - Wrapper class for holding counter name and it's
 *         value.
 */

@XmlRootElement
public final class Counter {
	private String name;
	private long value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}

	public Counter(String name, long value) {
		this.name = name;
		this.value = value;
	}

	public Counter() {
	}

}
