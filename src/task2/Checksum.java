package task2;

import java.util.Scanner;

public class Checksum {
	
	public static void main(String[] args) {
		String codeword = generate();
		verify(codeword);
		alter(codeword);
		verify(codeword);
	}

	public static String generate() {
		System.out.println("Please enter bitstring from Task 1");
		Scanner in = new Scanner(System.in);
		String s = in.nextLine();
		/*
		 * Perform checksum algorithm here
		 */
		s += getChecksum(s);
		return s;
	}
	
	public static String getChecksum(String s) {
		int result = 0;
		// Add up bits in s
		// s mod 2^size = 1
		String checksum = Integer.toString(result, 2);
		return checksum;
	}
	
	public static void verify(String s) {
		
	}
	
	public static void alter(String s) {
		
	}

}
