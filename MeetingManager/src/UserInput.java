import java.util.Scanner;

/**
 * Handles reading numbers entered by the user to the console.
 * <p>
 * Should prevent crashes caused by the user entering letters where numbers should be 
 * but may easily be stuck in an endless loop of the user trying to input wrong values and the system
 * not accepting them.
 * 
 * @author frantisekpavlica
 *
 */
public class UserInput {

	/**
	 * Tries to read an integer from the console. If it fails because an invalid value 
	 * was entered, it prompts the user to try again until a valid integer is entered.
	 * 
	 * @return	The integer that is read successfully from the <code>nextInt()</code> method of Scanner.
	 */
	public static int nextInt() {
		int value = 0;
		boolean pass = false;
		Scanner s = new Scanner(System.in);
		do {
			try {
				value = s.nextInt();
				pass = true;
			} catch (java.util.InputMismatchException e) {
				System.out.println("Incorrect input. Please enter an integer. Try again:");
				s.next(); //moves the pointer of the scanner to the next place to prevent reading the same incorrect input again.
			}
		} while (!pass);
		return value;
	}

	
	/**
	 * Tries to read a long from the console. If it fails because an invalid value 
	 * was entered, it prompts the user to try again until a valid long is entered.
	 * 
	 * @return	The long that is read successfully from the <code>nextInt()</code> method of Scanner.
	 */
	public static long nextLong() {

		long value = 0;
		boolean pass = false;
		Scanner s = new Scanner(System.in);
		do {
			try {
				value = s.nextLong();
				pass = true;
			} catch (java.util.InputMismatchException e) {
				System.out.println("Incorrect input. Please enter an integer. Try again:");
				s.next(); //moves the pointer of the scanner to the next place to prevent reading the same incorrect input again.
			}
		} while (!pass);
		return value;
	}
	
	public static String nextString() {
		Scanner s = new Scanner(System.in);
		return s.nextLine();
	}

	public static int nextMonth() {
		int value = nextInt();
		while (value<1 || value>12) {
			System.out.println("This number does not correspond to a month. Please try again:");
			value = nextInt();
		}
		return --value; //because months in java start at 0 but at 1 in natural language
	}
	
	public static int nextHour() {

		int value = nextInt();
		while (value<0 || value>23) {
			System.out.println("This number does not correspond to an hour of the day. Please try again:");
			value = nextInt();
		}
		return value;
	}
	
	public static int nextMinute() {

		int value = nextInt();
		while (value<0 || value>59) {
			System.out.println("This number does not correspond to a minute. Please try again:");
			value = nextInt();
		}
		return value;
	}
	
	public static int nextDayOfMonth() {

		int value = nextInt();
		while (value<1 || value>31) {
			System.out.println("This number does not correspond to a day of a month. Please try again:");
			value = nextInt();
		}
		return value;
	}
	
	/**
	 * Tries to read a number from the console. If it fails because an invalid value
	 * was entered, it prompts the user to try again until a valid <code>float</code> is entered.
	 * 
	 * @return	The number that is read successfully from the <code>nextFloat()</code> method of Scanner.
	 */
	public static float nextFloat() {
		float value = 0f;
		boolean pass = false;
		Scanner s = new Scanner(System.in);
		do {
			try {
				value = s.nextFloat();
				pass = true;
			} catch (java.util.InputMismatchException e) {
				System.out.println("Incorrect input. Please enter a number. Try again:");
				s.next(); //moves the pointer of the scanner to the next place to prevent reading the same incorrect input again.
			}
		} while (!pass);
		return value;
	}
	

	/**
	 * Tries to read a number from the console. If it fails because an invalid value
	 * was entered, it prompts the user to try again until a valid <code>double</code> is entered.
	 * 
	 * @return	The number that is read successfully from the <code>nextDouble()</code> method of Scanner.
	 */
	public static double nextDouble() {
		double value = 0f;
		boolean pass = false;
		Scanner s = new Scanner(System.in);
		do {
			try {
				value = s.nextDouble();
				pass = true;
			} catch (java.util.InputMismatchException e) {
				System.out.println("Incorrect input. Please enter a number. Try again:");
				s.next(); //moves the pointer of the scanner to the next place to prevent reading the same incorrect input again.
			}
		} while (!pass);
		return value;
	}
}
