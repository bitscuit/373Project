package task1.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import task1.OSI;

public class OSITest {

	@Test
	public void testCallAppLayer_to_accept_input() {
		String s = "test"; 	// this string should be same as what you will input
		OSI.callAppLayer();
		assertEquals(s, OSI.message);
	}

}
