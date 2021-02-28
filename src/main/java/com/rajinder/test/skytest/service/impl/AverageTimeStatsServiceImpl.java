package com.rajinder.test.skytest.service.impl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.rajinder.test.skytest.exceptions.FileException;
import com.rajinder.test.skytest.exceptions.InvalidRecordException;
import com.rajinder.test.skytest.service.TimeStatsService;
import com.rajinder.test.skytest.tuple.DriverTimeTuple;

@Service
public class AverageTimeStatsServiceImpl implements TimeStatsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AverageTimeStatsServiceImpl.class);

	public List<DriverTimeTuple> calculateStats(String inputFilePath, boolean orderAscending, int maxResultsToReturn,
			boolean failOnInvalidRecords) {
		Map<String, List<BigDecimal>> map = readFileData(inputFilePath, failOnInvalidRecords);
		List<DriverTimeTuple> list = calculateAverageResults(map, orderAscending, maxResultsToReturn);
		return list;
	}

	public Map<String, List<BigDecimal>> readFileData(String filePath, boolean failOnInvalidRecords) {
		Map<String, List<BigDecimal>> driverLapInfoMap = new HashMap<String, List<BigDecimal>>();
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;

			while ((line = reader.readLine()) != null) {
				BigDecimal lapTime = null;
				String driverName = null;

				String[] splitRow = line.split(",");
				boolean invalidRecord = false;
				if (splitRow.length < 2) {
					invalidRecord = true;
				} else {
					driverName = splitRow[0];
					if (driverName == null || driverName.trim().isEmpty()) {
						invalidRecord = true;
					}

					try {
						lapTime = new BigDecimal(splitRow[1]);
					} catch (NumberFormatException e) {
						invalidRecord = true;
					}
				}

				if (invalidRecord) {

					if (failOnInvalidRecords) {
						LOGGER.error("Invalid record found,Failing for Record [{}] ", line);
						throw new InvalidRecordException("Invalid record in file , thowing error , Record : " + line);
					}
					LOGGER.warn("Invalid record found,Skipping Record [{}] ", line);
					continue;
				}
				driverLapInfoMap.computeIfAbsent(driverName, k -> new LinkedList<>()).add(lapTime);
			}

		} catch (FileNotFoundException e) {
			// This has been checked already so wont happen
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new FileException("Error happened when reading file", e);
		}

		return driverLapInfoMap;
	}

	private List<DriverTimeTuple> calculateAverageResults(Map<String, List<BigDecimal>> mappedData,
			boolean orderAscending, int maxResultsToReturn) {

		List<DriverTimeTuple> driverAvgLapInfoList = new LinkedList<>();

		mappedData.entrySet().forEach(e -> {
			BigDecimal sum = e.getValue().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
			BigDecimal avg = sum.divide(new BigDecimal(e.getValue().size()), BigDecimal.ROUND_HALF_UP);
			DriverTimeTuple tuple = new DriverTimeTuple(e.getKey(), avg);
			driverAvgLapInfoList.add(tuple);
		});

		if (orderAscending) {
			Collections.sort(driverAvgLapInfoList);
		} else {
			Collections.sort(driverAvgLapInfoList, Collections.reverseOrder());
		}
		if (driverAvgLapInfoList.size() > maxResultsToReturn) {
			return driverAvgLapInfoList.stream().limit(maxResultsToReturn).collect(Collectors.toList());
		}

		return driverAvgLapInfoList;
	}

}
