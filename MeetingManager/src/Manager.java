import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.Stack;
import java.util.TreeSet;

public class Manager {
	
	Stack<Diary> diaryUndoStack;
	DiaryTree dTree;
	
	/**
	 * The main class
	 * 
	 * @param args Default parameters
	 */
	public static void main(String[] args) {
		Manager manager = new Manager();
		manager.processUserChoices();
	}
	
	public Manager() {
		diaryUndoStack = new Stack<Diary>();
		dTree = new DiaryTree();
	}
	
	/**
	 * Prints out the available menu options to the console.
	 */
	public void displayMenu() {
		System.out.println();
		System.out.println("Please select one of the options below:");
		System.out.println("8. Run automated tests.");
		System.out.println("9. Exit");
	}
	
	/**
	 * Calls <code>displayMenu()</code> and then processes the user's choice. 
	 * Keeps doing this until the exit option is selected.
	 */
	public void processUserChoices() {
		
		int choice;
		do {
			displayMenu();
			choice = UserInput.nextInt();
			
			switch(choice) {
				case 8:
					Tester.runTests(); //actually only semi-automated as of now, takes console input for some stuff
					break;
				case 9:
					//exit
					System.out.println("Goodbye.");
					break;
				default:
					System.out.println("Option " + choice + " is not available");
					break;
			}
		}while(choice != 9);
	}
	

	public boolean addEventToAll(Diary[] diaries, Event event) {
		boolean added = true;
		for (Diary d: diaries) {
			if (!addEvent(d, new Event(event))) {
				//if any add was unsuccessful
				added = false;
			}
		}
		return added;
	}
	
	public boolean addEvent(Diary diary, Event event) {
		this.diaryUndoStack.push(diary);
		return diary.addEvent(event);
	}
	
	public boolean editEvent(Diary diary, Event event) {
		this.diaryUndoStack.push(diary);
		return diary.editEventConsole(event);
	}
	public void removeEvent(Diary diary, Event event) {
		this.diaryUndoStack.push(diary);
		diary.removeEvent(event);
	}
	
	public void printTree() {
		dTree.print();
	}
	
	public void undo() {
		if (!diaryUndoStack.isEmpty()) {
			this.diaryUndoStack.pop().undo();
		} else {
			System.out.println("Undo stack is empty.");
		}
	}
	
	public Diary findMeetingSlotPrintTime(Diary[] empDiaries) {

		Date now1 = new Date();
		Diary sd = findMeetingSlot(empDiaries);
		Date now2 = new Date();
		long timeDifference = now2.getTime() - now1.getTime();
		System.out.println("Search took " + timeDifference + " miliseconds.");
		return sd;
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
	
	public boolean saveToFile(String filepath) {
		FileOutputStream fos;
		ObjectOutputStream oos;
		try {
			fos = new FileOutputStream(filepath);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(this.dTree); //undo stack doesn't need to be saved
			oos.close();
			fos.close();
			return true;
		} catch (FileNotFoundException e) {
			System.out.println(e.toString());
		} catch (IOException e) {
			System.out.println(e.toString());
		}
		return false;
	}
	
	public boolean loadFromFileButAsk(String filepath) {
		System.out.println("Loading from a file (" + filepath + ") will overwrite the current Set of Diaries.\n"
				+ "Do you wish to proceed?");
		if (UserInput.nextAnswerYN()) {
			return loadFromFile(filepath);
		} else {
			return false;
		}
	}
	
	public boolean loadFromFile(String filepath) {

        FileInputStream fis;
        ObjectInputStream ois;
        try {
			fis = new FileInputStream(filepath);
	        ois= new ObjectInputStream(fis);
	        this.dTree = (DiaryTree) ois.readObject(); //Reads the tree object
	        ois.close();
	        fis.close();
	        return true;
		} catch (FileNotFoundException e) {
			System.out.println(e.toString());
		} catch (IOException e) {
			System.out.println(e.toString());
		} catch (ClassNotFoundException e) {
			System.out.println(e.toString());
		}
        return false;
	}

	/**
	 * @return the dTree
	 */
	public DiaryTree getdTree() {
		return dTree;
	}

}
