package com.rajinder.test.skytest.tuple;

import java.math.BigDecimal;

public final class DriverTimeTuple implements Comparable<DriverTimeTuple>{

	private String name;
	private BigDecimal time;

	public DriverTimeTuple(String driverName, BigDecimal lapTime) {
		super();
		this.name = driverName;
		this.time = lapTime;
	}

	public String getName() {
		return name;
	}

	public BigDecimal getTime() {
		return time;
	}

	@Override
	public String toString() {
		return "DriverTimeTuple [name=" + name + ", lapTime=" + time + "]";
	}

	@Override
	public int compareTo(DriverTimeTuple o) {
		return this.getTime().compareTo(o.getTime());
	}

}
