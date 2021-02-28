package com.rajinder.test.skytest.unit.taskrunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.rajinder.test.skytest.SkytestApplication;
import com.rajinder.test.skytest.taskrunner.AverageTaskRunner;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
		SkytestApplication.class }, initializers = ConfigDataApplicationContextInitializer.class)
class AverageTaskRunnerTest {

	@BeforeAll
	public static void beforeAll() {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss");
		String dateStr = formatter.format(LocalDateTime.now());
		// System.out.println(dateStr);
		System.setProperty("inputFilePath", "src/test/resources/data/TestData.csv");
		System.setProperty("outputFilePath", "src/test/resources/data/TestDataResult_" + dateStr + ".csv");
	}

	@Autowired
	private AverageTaskRunner avgTaskRunner;

	@Test
	void test() throws Exception {
		avgTaskRunner.run(null);
	}

}
