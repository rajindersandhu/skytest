package com.rajinder.test.skytest.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.rajinder.test.skytest.SkytestApplication;
import com.rajinder.test.skytest.tuple.DriverTimeTuple;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
		SkytestApplication.class }, initializers = ConfigDataApplicationContextInitializer.class)

public class AverageTimeStatsServiceImplTest {

	@Autowired
	private TimeStatsService service;

	@BeforeAll
	public static void beforeAll() {
		System.setProperty("inputFilePath", "src/test/resources/data/TestData.csv");
		System.setProperty("outputFilePath", "testresults/TestDataResult.csv");
	}

	@Test
	public void testAvgStatsAsc() throws Exception {
		List<DriverTimeTuple> list = service.calculateStats(System.getProperty("inputFilePath"), true, 3, true);
		assertTrue(list.size() == 3);
		for (int i = 1; i < list.size(); i++) {
			assertTrue(list.get(i - 1).getTime().compareTo(list.get(i).getTime()) == -1);

		}
	}

	@Test
	public void testAvgStatsDesc() throws Exception {
		List<DriverTimeTuple> list = service.calculateStats(System.getProperty("inputFilePath"), false, 3, true);
		assertTrue(list.size() == 3);
		for (int i = 1; i < list.size(); i++) {
			assertTrue(list.get(i - 1).getTime().compareTo(list.get(i).getTime()) == 1);

		}
	}

	@Test
	public void testReadData() throws Exception {
		Map<String, List<BigDecimal>> map = service.readFileData(System.getProperty("inputFilePath"), true);
		assertTrue(map.size() == 3);

		map.forEach((k, v) -> {
			assertTrue(v.size() == 3);
		});

	}
}
