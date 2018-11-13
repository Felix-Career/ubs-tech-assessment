package com.ubs.tech.assessment;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Felix Wu
 * @since Nov 13, 2018
 */
public class StringCalculatorImpl implements StringCalculator {
	private static Logger log = LoggerFactory.getLogger(StringCalculatorImpl.class);
	private String lineBreak = "\n";
	private String regexChars = "[] {}*+^$.?";

	private String escape(String regex) {
		StringBuilder sBuilder = new StringBuilder();
		char[] chars = regex.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (regexChars.indexOf(chars[i]) > -1) {
				sBuilder.append("\\");
			}
			sBuilder.append(chars[i]);
		}
		return sBuilder.toString();
	}

	@Override
	public int add(String numbers) {
		String[] delims = null;
		if (numbers.startsWith("//")) {
			int firstLineBreakIndex = numbers.indexOf(lineBreak);
			String delimsLine = numbers.substring(2, firstLineBreakIndex);
			numbers = numbers.substring(firstLineBreakIndex + 1);
			delims = delimsLine.split("\\|");
		} else {
			delims = new String[] { "," };
		}
		return add(numbers, delims);
	}

	private int add(String input, String[] delims) {
		input = input.replaceAll(lineBreak, delims[0]).trim();
		for (int i = 1; i < delims.length; i++) {
			String delim = escape(delims[i]);
			input = input.replaceAll(delim, delims[0]);
		}
		if (input.startsWith(delims[0])) {
			input = "0" + input;
		}
		if (input.endsWith(delims[0])) {
			input = input + "0";
		}
		String delim = escape(delims[0]);
		String[] oprandTexts = input.split(delim);
		List<String> negatives = new ArrayList<>();

		// Use BigDecimal in case of big numbers. int is used here for illustration.
		int total = 0;
		for (String operandStr : oprandTexts) {
			String operandText = operandStr.trim();
			int operand = operandText.length() == 0 && oprandTexts.length == 1 ? 0 : Integer.parseInt(operandText);
			if (operand < 0) {
				negatives.add(operandText);
				log.error("Negative: {}", operand);
			} else if (operand > 1000) {
				log.warn("Ignored: {}", operand);
			} else {
				log.debug("Adding... : {}", operand);
				total += operand;
			}
		}
		if (!negatives.isEmpty()) {
			String allNegatives = negatives.stream().collect(Collectors.joining(", "));
			throw new IllegalArgumentException("negatives not allowed: " + allNegatives);
		}
		return total;
	}
}
