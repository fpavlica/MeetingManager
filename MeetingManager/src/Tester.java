import java.util.Date;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

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
		Tester tester = new Tester();
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
		
		int thisYear = 118, july = 6;
		dArr[0].addEvent(new Event(new Date(thisYear, july, 21, 11, 0, 0), new Date(thisYear, july, 21, 11, 15, 0), "Birthday1"));
		dArr[0].addEvent(new Event(new Date(thisYear, july, 21, 11, 30, 0), new Date(thisYear, july, 21, 13, 45, 0), "Birthday2"));
		dArr[2].addEvent(new Event(new Date(thisYear, july, 21, 11, 15, 0), new Date(thisYear, july, 21, 13, 30, 0), "Birthday3"));
		dArr[2].addEvent(new Event(new Date(thisYear, july, 21, 14, 15, 0), new Date(thisYear, july, 21, 15, 0, 0), "Birthday4"));
		
		for (Diary d: dArr) {
			System.out.println(d);
			d.printAllEvents();
		}
		Date now1 = new Date();
		Diary sd = tester.findMeetingSlot(dArr);
		//TODO temp delay:
		try {
			TimeUnit.MILLISECONDS.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Date now2 = new Date();
		long timeDifference = now2.getTime() - now1.getTime();
		sd.printAllEvents();
		System.out.println("Search took " + timeDifference + " miliseconds.");
		
		System.out.println("Enter the index of " + dArr[0].getName() + "'s event you would like to edit.");
		dArr[0].editEventByIndex(UserInput.nextInt());
		
		System.out.println("Adding event to 0 and 3");
		Diary[] tempArr = new Diary[2];
		tempArr[0] = dArr[0];
		tempArr[1] = dArr[3];
		Event addToAll = new Event(new Date(thisYear, july, 21, 18,0,0), new Date(thisYear, july, 21, 20,30,0), "Afterparty");
		tester.addEventToAll(tempArr, addToAll);
		

		for (Diary d: dArr) {
			System.out.println(d);
			d.printAllEvents();
		}
		sd = tester.findMeetingSlot(dArr);
		sd.printAllEvents();
	}
	public boolean addEventToAll(Diary[] diaries, Event event) {
		boolean added = true;
		for (Diary d: diaries) {
			if (!d.addEvent(event)) {
				//if any add was unsuccessful
				added = false;
			}
		}
		return added;
	}
	
	//TODO move elsewhere
	public Diary findMeetingSlot(Diary[] empDiaries) { //TODO probably can be static
		TreeSet<EventTime> timeset = new TreeSet<EventTime>();
		for (int i = 0; i < empDiaries.length; i++) {
			for (Event event : empDiaries[i].getEvents()) {
				EventTime startTime = new EventTime(event.getStartTime(), true);
				EventTime endTime = new EventTime(event.getEndTime(), false);
				if(!timeset.add(startTime)) {
					//if an EventTime with this time is already in the set
					timeset.remove(startTime);
				}
				if (!timeset.add(endTime)) {
					timeset.remove(endTime);
				}
			}
		}
		
		int timeCounter = 0;
		boolean wasZero = true;
		Diary superDiary = new Diary("soup","");
		Event av = new Event(new Date(0), "Available");
		superDiary.addEvent(av);
		for (EventTime et : timeset) {
			if (et.isStart()) {
				timeCounter++;
			}else {
				timeCounter--;
			}
			if (timeCounter == 0) {
				Event available = new Event(et.getTime(), "Available");
				superDiary.addEvent(available);
				wasZero = true;
			} else if (timeCounter == 1 && wasZero) {
				superDiary.getEvents().last().setEndTime(et.getTime());
				wasZero = false;
			}
		}
		superDiary.getEvents().last().setEndTime(new Date(Long.MAX_VALUE));
		return superDiary;
	}
}
