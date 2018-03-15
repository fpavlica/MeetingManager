/**
 * 
 */

/**
 * @author fpavlica
 *
 */
public class Tester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		runTests();
	}

	public static void runTests() {
		DiaryTree dtree = new DiaryTree();
		Diary[] dArr = new Diary[4];
		dArr[0] = new Diary("Will", "Smith");
		dArr[1] = new Diary("John", "Smith");
		dArr[2] = new Diary("Jane", "Doe");
		dArr[3] = new Diary("Will", "Smith");
		
		for (int i = 0; i < dArr.length; i++) {
			dtree.add(dArr[i]);
		}
		
		dtree.print();
	}
}
