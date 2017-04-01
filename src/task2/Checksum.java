package task2;

import java.util.Scanner;

public class Checksum {

	public static int blockSize = 16;

	public static void main(String[] args) {
		System.out.println("Please enter bitstring from Task 1");
		Scanner user = new Scanner(System.in);
		String s = user.nextLine();
		String message = stripString(s);
		message = normalizeString(message);
		
		String codeword = generate(message);
		System.out.println("The codeword (message + checksum) is: " + codeword);
		System.out.println("Verifying message...");
		verify(codeword);
		System.out.println();
		
		// flip bit specified by user input
		System.out.println("Plese enter bit to flip in the codeword");
		s = user.nextLine();
		int index = Integer.valueOf(s);
		System.out.println("Altering message...");
		codeword = alter(codeword, index);
		System.out.println("Verifying altered message...");
		verify(codeword);
	}

	public static String generate(String s) {
		String checksum = getChecksum(s);
		String codeword = s + checksum;
		return codeword;
	}

	/**
	 * Method to strip string of anything that is not 1 or 0
	 * @param s: The bit string to strip
	 * @return The stripped bit string
	 */
	private static String stripString(String s) {
		String message = "";
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '0' || s.charAt(i) == '1') {
				message += s.charAt(i);
			}
		}
		return message;
	}
	
	private static String normalizeString(String s) {
		// pad string with 0's if String s cannot be grouped evenly
		if (s.length() % blockSize != 0) {
			int zeroes = blockSize - (s.length() % blockSize);
			for (int i = 0; i < zeroes; i++) {
				s = '0' + s;
			}
		}
		return s;
	}

	/**
	 * Method to calculate the checksum given a message
	 * @param s: The message used to calculate the checksum
	 * @return The checksum bit string
	 */
	public static String getChecksum(String s) {
		int sum = getSum(s);
		sum = ~sum;
		String checksum = Integer.toBinaryString(sum);
		// gets rid of the sign extension on the checksum
		checksum = checksum.substring(checksum.length() - blockSize);
		System.out.println("Checksum for this message is: " + checksum);
		return checksum;
	}

	/**
	 * Method to sum all the blocks of the message and perform modulo arithmetic
	 * @param s: The message
	 * @return The sum
	 */
	private static int getSum(String s) {
		int sum = 0;
		String temp = "";
		// group bits into blocks and add to sum
		for (int i = 0; i < s.length(); i++) {
			temp += s.charAt(i);
			if (temp.length() % blockSize == 0) {
				sum += Integer.parseInt(temp, 2);
				temp = "";
			}
		}
		
		int pow = (int) Math.pow(2, blockSize);
		sum = (int) (sum % (pow - 1));
		return sum;
	}

	public static void verify(String s) {
		int sum = getSum(s);
		if (sum == 0) {
			System.out.println("Verifier: Message has no error");
		} else {
			System.out.println("Verifier: Message has error");
		}
	}

	public static String alter(String s, int bit) {
		String altered = "";
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			if (i == bit) {
				ch = (ch == '0') ? '1' : '0';
			}
			altered += ch;
		}
		return altered;
	}

}
