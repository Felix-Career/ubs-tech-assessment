package com.ubs.tech.assessment;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * @author Felix Wu	
 * @since Nov 13, 2018
 */
public class StringCalculatorTest {

	private StringCalculator calc = new StringCalculatorImpl();

	@Test
	public void addEmpty() {
		assertEquals(0, calc.add(""));
	}

	@Test
	public void addOneNumber() {
		assertEquals(1, calc.add("1"));
	}

	@Test
	public void addTwoNumbers() {
		assertEquals(3, calc.add("1, 2"));
	}

	@Test
	public void addThreeNumbers() {
		assertEquals(6, calc.add("1, 2, 3"));
	}

	@Test
	public void addWithLineBreak() {
		assertEquals(6, calc.add("1\n2,3"));
	}

	@Test(expected=NumberFormatException.class)
	public void addWithLineBreakExExpected() {
		calc.add("1\n,");
	}

	@Test
	public void addUsingSemicolon() {
		assertEquals(3, calc.add("//;\n1;2"));
	}

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	@Test
	public void addNegativeExExpected() throws Exception {
	    expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("negatives not allowed: -2, -5");
		calc.add("1, -2, 5, -5");
	}

	@Test
	public void addNegativeUsingSemicolonExExpected() throws Exception {
	    expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("negatives not allowed: -2, -5");
		calc.add("//;\n1; -2; 5; -5");
	}

	@Test
	public void addZeroWithBigNumberIgnored() {
		assertEquals(1000, calc.add("1000"));
		assertEquals(0, calc.add("1001"));
	}

	@Test
	public void addWithBigNumberIgnored() {
		assertEquals(6, calc.add("1\n2,3,1003"));
	}

	@Test
	public void addUsingLongDelimiter() {
		assertEquals(6, calc.add("//***\n1***2***3"));
	}

	@Test
	public void addUsingMultipleDelimiters() {
		assertEquals(6, calc.add("//*|%\n1*2%3"));
	}

	@Test
	public void addUsingMultipleLongDelimiters() {
		assertEquals(6, calc.add("//**|%%\n1**2%%3"));
		assertEquals(10, calc.add("//??|+++\n1+++2??3??4"));
	}

	@Test
	public void addComplex() {
		assertEquals(15, calc.add("//x|**|%%|?.+\n1**2%%3\n4?.+5x1100"));
	}
}
