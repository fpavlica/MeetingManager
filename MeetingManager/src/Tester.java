import java.util.Date;

/**
 * 
 */

/**
 * @author fpavlica
 *
 */
public class Tester {
	
	/**
	 * A major mess
	 */
	public static void runTests() {
		Manager manager = new Manager();
		DiaryTree dtree = manager.getdTree();
		Diary[] dArr = new Diary[4];
		dArr[0] = new Diary("Will", "Smith");
		dArr[1] = new Diary("John", "Smith");
		dArr[2] = new Diary("Jane", "Doe");
		dArr[3] = new Diary("Will", "Smith");
		
		for (int i = 0; i < dArr.length; i++) {
			dtree.add(dArr[i]);
		}
		
		dtree.print();
		
		int thisYear = 118, july = 6;
		dArr[0].addEvent(new Event(new Date(thisYear, july, 21, 11, 0, 0), new Date(thisYear, july, 21, 11, 15, 0), "Birthday1"));
		dArr[0].addEvent(new Event(new Date(thisYear, july, 21, 11, 30, 0), new Date(thisYear, july, 21, 13, 45, 0), "Birthday2"));
		dArr[2].addEvent(new Event(new Date(thisYear, july, 21, 11, 15, 0), new Date(thisYear, july, 21, 13, 30, 0), "Birthday3"));
		dArr[2].addEvent(new Event(new Date(thisYear, july, 21, 14, 15, 0), new Date(thisYear, july, 21, 15, 0, 0), "Birthday4"));

		dtree.print();
		
		Date now1 = new Date();
		Diary sd = manager.findMeetingSlotPrintTime(dArr);
		sd.printAllEvents();
		
		System.out.println("Enter the index of " + dArr[0].getName() + "'s event you would like to edit.");
		//dArr[0].editEventByIndex(UserInput.nextInt());
		Event ete = dArr[0].findEventByIndex(UserInput.nextInt());
		if (manager.editEvent(dArr[0], ete)) {
			System.out.println();
			dArr[0].printAllEvents();
			System.out.println("\nUndoing:\n");
			manager.undo();
			dArr[0].printAllEvents();
		} else {
			System.out.println("Couldn't edit for whatever reason probably because the index doesn't exist");
		}
		
		System.out.println("Adding event to 0 and 3");
		Diary[] tempArr = new Diary[2];
		tempArr[0] = dArr[0];
		tempArr[1] = dArr[3];
		Event addToAll = new Event(new Date(thisYear, july, 21, 18,0,0), new Date(thisYear, july, 21, 20,30,0), "Afterparty");
		manager.addEventToAll(tempArr, addToAll);
		

		dtree.print();
		sd = manager.findMeetingSlot(dArr);
		System.out.println("\n"+sd);
		sd.printAllEvents();
		
		//temp
		manager.addEvent(dArr[0], (new Event(new Date(thisYear, july, 21, 15, 15, 0), new Date(thisYear, july, 21, 16, 30, 0), "Birthday5")));
		System.out.println();
		dArr[0].printAllEvents();
		System.out.println("Undoing");
		manager.undo();
		System.out.println();
		dArr[0].printAllEvents();
		

		//temp
		System.out.println("Enter the index of " + dArr[0].getName() + "'s event you would like to remove.");
		//dArr[0].editEventByIndex(UserInput.nextInt());
		Event etr = dArr[0].findEventByIndex(UserInput.nextInt()); 
		if (etr != null) {
			manager.removeEvent(dArr[0], etr);
			System.out.println();
			dArr[0].printAllEvents();
			System.out.println("Undoing");
			manager.undo();
			System.out.println();
			dArr[0].printAllEvents();
		} else {
			System.out.println("doesn't exist");
		}
		
		System.out.println("\n");
		manager.printTree();
		System.out.println("Saving to a file and then undoing the last whatever");
		manager.saveToFile("testSave.txt");
		manager.undo(); //undoes the last whatever
		manager.printTree();
		System.out.println("Loading from a file");
		manager.loadFromFile("testSave.txt");
		manager.printTree();
		
	}
}
