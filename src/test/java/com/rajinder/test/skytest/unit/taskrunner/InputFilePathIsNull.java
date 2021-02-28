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
import com.rajinder.test.skytest.exceptions.InvalidArgumentsException;
import com.rajinder.test.skytest.taskrunner.AverageTaskRunner;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
		SkytestApplication.class }, initializers = ConfigDataApplicationContextInitializer.class)
class InputFilePathIsNull {

	@Autowired
	private AverageTaskRunner avgTaskRunner;

	@BeforeAll
	public static void beforeAll() {
		
		System.setProperty("inputFilePath", "");
		System.setProperty("outputFilePath", "src/main/resources/application.properties");
	}
	
	@Test
	void testInputFilePathIsNull() throws Exception {
		assertThrows(InvalidArgumentsException.class, () -> avgTaskRunner.run(null));
	}

}
