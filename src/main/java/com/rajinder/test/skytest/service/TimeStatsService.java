package com.rajinder.test.skytest.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.rajinder.test.skytest.tuple.DriverTimeTuple;

public interface TimeStatsService {

	public Map<String, List<BigDecimal>> readFileData(String filePath, boolean failOnInvalidRecords);

	public List<DriverTimeTuple> calculateStats(String inputFilePath, boolean orderAscending, int maxResultsToReturn,
			boolean failOnInvalidRecords);
}
