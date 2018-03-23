import java.io.IOException;
import java.util.Date;
import java.util.TreeSet;
import java.util.Scanner;

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
		Scanner s1 = new Scanner( System.in );
		Diary[] dArr = new Diary[4];
		dArr[0] = new Diary("Will", "Smith");
		dArr[1] = new Diary("John", "Smith");
		dArr[2] = new Diary("Jane", "Doe");
		dArr[3] = new Diary("Will", "Smith");
		
		for (int i = 0; i < dArr.length; i++) {
			dtree.add(dArr[i]);
		}
		
		dtree.print();
		
		dArr[0].addEvent(new Event(new Date(2018, 7, 21, 11, 0, 0), new Date(2018, 7, 21, 12, 30, 0), "Birthday1"));
		dArr[0].addEvent(new Event(new Date(2018, 7, 21, 11, 30, 0), new Date(2018, 7, 21, 13, 45, 0), "Birthday2"));
		dArr[2].addEvent(new Event(new Date(2018, 7, 21, 11, 15, 0), new Date(2018, 7, 21, 13, 30, 0), "Birthday3"));
		dArr[2].addEvent(new Event(new Date(2018, 7, 21, 14, 15, 0), new Date(2018, 7, 21, 15, 0, 0), "Birthday4"));
		
		Diary sd = tester.findMeetingSlot(dArr);
		sd.printAllEvents();

        FileWriter file = new FileWriter();
        UserInput input = new UserInput();

        try
        {
            file.writeFile(dtree);
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }

        try
        {
            System.out.println( "Please enter the name of the file you want to open > " );
            file.readFile(s1.nextLine());
        }
        catch(ClassNotFoundException | IOException e)
        {
            System.out.println(e.getMessage());
        }
	}
	
	public Diary findMeetingSlot(Diary[] empDiaries) { //TODO probably can be static
		TreeSet<EventTime> timeset = new TreeSet<EventTime>();
		for (int i = 0; i < empDiaries.length; i++) {
			for (Event event : empDiaries[i].getEvents()) {
				EventTime startTime = new EventTime(event.getStartTime(), true);
				EventTime endTime = new EventTime(event.getEndTime(), false);
				timeset.add(startTime);
				timeset.add(endTime);
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
