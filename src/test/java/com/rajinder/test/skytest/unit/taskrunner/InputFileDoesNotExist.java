package com.rajinder.test.skytest.unit.taskrunner;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.rajinder.test.skytest.SkytestApplication;
import com.rajinder.test.skytest.exceptions.FileException;
import com.rajinder.test.skytest.taskrunner.AverageTaskRunner;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
		SkytestApplication.class }, initializers = ConfigDataApplicationContextInitializer.class)
class InputFileDoesNotExist {

	@BeforeAll
	public static void beforeAll() {
		System.setProperty("inputFilePath", "nonexistingfile.csv");
		System.setProperty("outputFilePath", "nonexistingfile.csv");
	}

	@Autowired
	private AverageTaskRunner avgTaskRunner;

	@Test
	void testInputFileDoesNotExist() throws Exception {
		assertThrows(FileException.class, () -> avgTaskRunner.run(null));
	}

}


