package com.matthieu.aoc_2020.controller;

import java.lang.reflect.InvocationTargetException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matthieu.aoc_2020.exception.PrepareDataException;
import com.matthieu.aoc_2020.exception.SolveException;
import com.matthieu.aoc_2020.model.resolver.Resolver;
import com.matthieu.aoc_2020.service.InputReader;

@RestController
@RequestMapping("day")
public class DayController {

	private static final Logger logger = LoggerFactory.getLogger(DayController.class);

	@Autowired
	private InputReader inputReader;
	
	
	@GetMapping("{dayNumber}/{partNumber}")
	public ResponseEntity<String> resolve(@PathVariable int dayNumber, @PathVariable int partNumber, @RequestBody String body) {
		String resolverName = String.format("com.matthieu.aoc_2020.model.resolver.Resolver%s_%s", dayNumber, partNumber);
		Class<?> resolverClass;
		
		// Get resolver class
		try {
			resolverClass = Class.forName(resolverName);
		} catch (ClassNotFoundException e) {
			String message = String.format("No resolver found for day %s part %s", dayNumber, partNumber);
			logger.error(message, e);
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		}
		
		// Create resolver instance
		Resolver resolver;
		try {
			resolver = (Resolver) resolverClass.getConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			String message = "An error occurred during creating resolver instance";
			logger.error(message, e);
			return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		// Prepare data
		try {
			resolver.prepareData(this.inputReader.getInputAsStringArray(body));
		} catch (PrepareDataException e) {
			String message = "An error occurred during preparing data";
			logger.error(message, e);
			return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		logger.info("Data prepared for day {} part {}", dayNumber, partNumber);
		
		// Solve problem
		try {
			while(!resolver.solve()) {
				logger.info("Solve loop executed.");
			}
		} catch (SolveException e) {
			String message = "An error occurred during solving problem";
			logger.error(message, e);
			return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		logger.info("Problem solved for day {} part {}", dayNumber, partNumber);
		
		// Send response
		return ResponseEntity.ok(resolver.get());
	}
}
