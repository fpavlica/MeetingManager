import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.TreeSet;


public class Manager {
	
	private transient Stack<Diary> undoDiaryStack, redoDiaryStack;
	private DiaryTree dTree;
	private enum Action {ADD_DIARY, REMOVE_DIARY, EDIT_NAME, EDIT_EVENT};
	private Stack<Action> undoActionStack, redoActionStack;
	
	/*
	 * The main class
	 * 
	 * @param args Default parameters
	 *//*
	public static void main(String[] args) {
		Manager manager = new Manager();
		manager.processUserChoices();
	}
	*/
	
	/**
	 * Constructor, initialises the tree and the undo and redo stacks
	 */
	public Manager() {
		undoDiaryStack = new Stack<Diary>();
		undoActionStack = new Stack<Action>();
		redoDiaryStack = new Stack<Diary>();
		redoActionStack = new Stack<Action>();
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
	
	/**
	 * Add an event to multiple diaries, only keeping its data
	 * @param diaries	An array of diaries to add the event to
	 * @param event	The event to add 
	 * @return	true if all were added successfully
	 */
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
	
	/**
	 * Add an event to a diary in the tree
	 * @param diary	The diary to add the event to
	 * @param event	The event to add
	 * @return	true if added successfully
	 */
	public boolean addEvent(Diary diary, Event event) {
		this.undoDiaryStack.push(diary);
		this.undoActionStack.push(Action.EDIT_EVENT);
		return diary.addEvent(event);
	}
	
	/**
	 * Edit an event of a diary 
	 * @param diary The diary whose event is to be edited
	 * @param event	The event to edit
	 * @return	true if edited successfully
	 */
	@Deprecated
	public boolean editEvent(Diary diary, Event event) {
		//console only
		this.undoDiaryStack.push(diary);
		this.undoActionStack.push(Action.EDIT_EVENT);
		return diary.editEventConsole(event);
	}
	
	public boolean editEvent(Diary diary, Event oldEvent, Event newEvent) {
		this.undoDiaryStack.push(diary);
		this.undoActionStack.push(Action.EDIT_EVENT);
		return diary.editEvent(oldEvent, newEvent);
	}
	
	/**
	 * Remove an event from a diary in the tree
	 * @param diary	The diary whose event is to be removed
	 * @param event	The event to remove
	 * @return	true if removal was successful (i.e. The diary did contain the event)
	 */
	public boolean removeEvent(Diary diary, Event event) {
		this.undoDiaryStack.push(diary);
		this.undoActionStack.push(Action.EDIT_EVENT);
		return diary.removeEvent(event);
	}
	
	public boolean removeDiary(Diary toRemove) {
		undoDiaryStack.push(toRemove);
		undoActionStack.push(Action.REMOVE_DIARY);
		return removeDiaryNoStack(toRemove);
	}
	public boolean removeDiaryNoStack(Diary toRemove) {
		return dTree.remove(toRemove);
	}
	
	public boolean addDiary(Diary toAdd) {
		undoDiaryStack.push(toAdd);
		undoActionStack.push(Action.ADD_DIARY);
		return addDiaryNoStack(toAdd);
	}
	
	public boolean addDiaryNoStack(Diary toAdd) {
		return dTree.add(toAdd);
	}
	
	public boolean addDiary(String firstname, String lastname) {
		return addDiary(new Diary(firstname, lastname));
	}
	
	public boolean editDiaryName(Diary oldDiary, String firstname, String lastname) {
		Diary newDiary = new Diary(firstname, lastname);
		newDiary.setEvents(oldDiary.getEvents());
		return editDiaryName(oldDiary, newDiary);
	}
	
	public boolean editDiaryName(Diary oldDiary, Diary newDiary) {
		undoDiaryStack.push(oldDiary);
		undoDiaryStack.push(newDiary);
		undoActionStack.push(Action.EDIT_NAME);
		return editDiaryNameNoStack(oldDiary, newDiary);
	}
	
	public boolean editDiaryNameNoStack(Diary oldDiary, Diary newDiary) {
		return (removeDiaryNoStack(oldDiary) && addDiaryNoStack(newDiary));		
	}
	
	/**
	 * Print out the tree
	 */
	public void printTree() {
		dTree.print();
	}
	
	/**
	 * Undo the last change to a Diary in the tree
	 */
	public boolean undo() {
		
		if (!undoActionStack.isEmpty()) {
			Action action = undoActionStack.pop();
			Diary toUndo = undoDiaryStack.pop();

			if (action == Action.ADD_DIARY) {
				//last event was added, remove it
				//redoActionStack.push(Action.REMOVE_DIARY);
				return removeDiaryNoStack(toUndo);
			} else if (action == Action.REMOVE_DIARY) {
				//last event was removed, add it back
				//redoActionStack.push(Action.ADD_DIARY);
				return addDiaryNoStack(toUndo);
			} else if (action == Action.EDIT_NAME){
				//last event was edited, remove the changed one and add the old one
				Diary secondDiary = undoDiaryStack.pop();
				//redoEventStack.push(secondEvent);
				//redoActionStack.push(Action.EDIT_DIARY);
				return editDiaryName(toUndo, secondDiary.getFirstname(), secondDiary.getLastname());
			} else if (action == Action.EDIT_EVENT) {
				return toUndo.undo();
			}
			//diaryRedoStack.push(toUndo);
		} else {
			System.out.println("Undo stack is empty.");
		}
		return false;
	}
	
	/**
	 * Redo the last undone action
	 */
	public boolean redo() {
		if (!redoActionStack.isEmpty()) {
			Action action = redoActionStack.pop();
			Diary toRedo = redoDiaryStack.pop();
			undoDiaryStack.push(toRedo);
			undoActionStack.push(action);
			
			if (action == Action.ADD_DIARY) {
				//last event was added, remove it
				return removeDiary(toRedo);
			} else if (action == Action.REMOVE_DIARY) {
				//last event was removed, add it back
				return addDiary(toRedo);
			} else if (action == Action.EDIT_NAME){
				//last event was edited, remove the changed one and add the old one
				return editDiaryName(toRedo, redoDiaryStack.pop()); 
			} else if (action == Action.EDIT_EVENT) {
				return toRedo.redo();
			}
		} else {
			System.out.println("Redo stack is empty.");
		}	
		//if the code somehow manages to get here, nothing was redone and something must be wrong
		return false;	
	}
	
	/**
	 * Find a meeting slot and print how long it took to find it
	 * @param empDiaries	an array of diaries for which the meeting slot is to be found
	 * @return	a Diary containing times when everyone is available
	 */
	public Diary findMeetingSlotPrintTime(Diary[] empDiaries) {

		Date now1 = new Date();
		Diary sd = findMeetingSlot(empDiaries);
		Date now2 = new Date();
		long timeDifference = now2.getTime() - now1.getTime();
		System.out.println("Search took " + timeDifference + " miliseconds.");
		return sd;
	}
	
	/**
	 * Find a meeting slot for all employees in the array
	 * @param empDiaries	an array of diaries for which the meeting slot is to be found
	 * @return	a Diary containing times when everyone is available
	 */
	public Diary findMeetingSlot(Diary[] empDiaries) { //TODO probably can be static
		
		List<Diary> empList = new LinkedList<Diary>();
		for (Diary d: empDiaries) {
			empList.add(d);
		}
		return findMeetingSlot(empList);
		/*
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
		
		//TODO add start and end time for searches
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
		*/
	}
	
	public Diary findMeetingSlot(List<Diary> empDiaries) {

		TreeSet<EventTime> timeset = new TreeSet<EventTime>();
		for (Diary d: empDiaries) {
			for (Event event : d.getEvents()) {
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
		
		//TODO add start and end time for searches
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
	
	/**
	 * Save the diary tree to a file
	 * @param filepath	the file path where the diary tree should be saves
	 * @return	true if saved successfully
	 */
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
	
	/**
	 * Load the diary tree from a file but first ask the user if they really want to overwrite the current tree.
	 * @param filepath	the path of the saved diary tree file to load from
	 * @return true if loaded successfully
	 */
	public boolean loadFromFileButAsk(String filepath) {
		System.out.println("Loading from a file (" + filepath + ") will overwrite the current Set of Diaries.\n"
				+ "Do you wish to proceed?");
		if (UserInput.nextAnswerYN()) {
			return loadFromFile(filepath);
		} else {
			return false;
		}
	}
	
	/**
	 * Load the diary tree from a file, overwriting the current diary tree
	 * @param filepath	the path of the saved diary tree file to load from
	 * @return	true if loaded successfully
	 */
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

	public Diary[] getDiaryArray() {
		return dTree.toArray();
	}
	
	/**
	 * @return the diaryTree
	 */
	public DiaryTree getdTree() {
		return dTree;
	}

}
