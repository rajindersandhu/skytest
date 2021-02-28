package com.rajinder.test.skytest.taskrunner;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.rajinder.test.skytest.exceptions.FileException;
import com.rajinder.test.skytest.exceptions.InvalidArgumentsException;
import com.rajinder.test.skytest.service.TimeStatsService;
import com.rajinder.test.skytest.tuple.DriverTimeTuple;

@Component
public class AverageTaskRunner implements ApplicationRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(AverageTaskRunner.class);

	@Value("${inputFilePath:}")
	private String inputFilePath;

	@Value("${outputFilePath:}")
	private String outputFilePath;

	@Value("${sortAsc:TRUE}")
	private String sortAsc;

	private boolean sortAscOrder = true;

	@Value("${maxResults:3}")
	private String maxResults;

	private int maxResultsToReturn = 3;

	@Value("${failOnInvalidRecord:false}")
	private boolean failOnInvalidRecord;

	@Autowired
	private TimeStatsService stats;

	@Override
	public void run(ApplicationArguments args) throws Exception {

		LOGGER.info(args.toString());

		try {
			validateAruments();
		} catch (RuntimeException e) {
			LOGGER.error("Error occured [{}]", e.getMessage());
			printUsage();
			return;
		}

		List<DriverTimeTuple> results = stats.calculateStats(inputFilePath, sortAscOrder, maxResultsToReturn,
				failOnInvalidRecord);
		writeResults(outputFilePath, results);

	}

	public boolean checkInputFileDoesNotExists(String path) {

		File f = new File(path);
		if (f.exists() && f.isFile()) {
			return false;
		}
		return true;
	}

	public boolean checkOutputFileDoesExists(String path) {
		File f = new File(path);
		if (f.exists()) {
			return true;
		}
		return false;
	}

	public void writeResults(String outputFilePath, List<DriverTimeTuple> results) {

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
			for (DriverTimeTuple t : results) {
				writer.write(t.getName() + "," + t.getTime().toPlainString());
				writer.write(System.lineSeparator());
			}
			writer.close();
		} catch (IOException e) {
			LOGGER.error("Error : Cannot write results to output file [{}]", outputFilePath, e);
			throw new FileException("Error writing results to output file", e);
		}
		LOGGER.info("Output written to file succesfully, File [{}}", outputFilePath);
	}

	public void validateAruments() {

		if (null == inputFilePath || inputFilePath.trim().isEmpty()) {
			String msg = "Input file location not provided. Please provide a input file using option --inputFilePath=/file/path/filename.csv";
			LOGGER.error(msg);
			throw new InvalidArgumentsException(msg);
		}

		if (null == outputFilePath || outputFilePath.trim().isEmpty()) {
			String msg = "Output file location not provided. Please provide a output file using option --outputFilePath=/file/path/filename.csv";
			LOGGER.error(msg);
			throw new InvalidArgumentsException(msg);
		}

		if (checkInputFileDoesNotExists(inputFilePath)) {
			String msg = "Input file specified does not exist , file path specified : " + inputFilePath;
			LOGGER.error(msg);
			throw new FileException(msg);
		}

		if (checkOutputFileDoesExists(outputFilePath)) {

			String msg = "Output file specified exists please specify a new file path , file path specified : "
					+ outputFilePath;
			LOGGER.error(msg);
			throw new FileException(msg);
		}

		if (sortAsc != null && sortAsc.equalsIgnoreCase("FALSE")) {
			sortAscOrder = false;
		}

		if (maxResults != null) {
			try {
				maxResultsToReturn = Integer.parseInt(maxResults);
			} catch (NumberFormatException e) {
				// Fallback to default 3
			}
		}

		LOGGER.info("Input/output parameter validated successfully");

	}

	public void printUsage() {
		LOGGER.info("*********************** Usage Guide *****************************");
		LOGGER.info("Required    --inputFilePath=/file/path/filename.csv");
		LOGGER.info("Required    --outputFilePath=/file/path/filenam.csv");
		LOGGER.info("Optional    --sortAsc=true/false                    deafult true");
		LOGGER.info("Optional    --maxResults=int                        default 3   ");
		LOGGER.info("Optional    --failOnInvalidRecord=false/true        default fal");
	}

}
