import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.TreeSet;

/**
 * Manages the whole MeetingManager project
 * @author AC12001 17/18 group 2
 *
 */
public class Manager {
	
	private transient Stack<Diary> undoDiaryStack, redoDiaryStack;
	private DiaryTree dTree;
	private enum Action {ADD_DIARY, REMOVE_DIARY, EDIT_NAME, EDIT_EVENT};
	private Stack<Action> undoActionStack, redoActionStack;
	private Stack<String> taskStack;
	
	
	/**
	 * Constructor, initialises the tree and the undo and redo stacks
	 */
	public Manager() {
		undoDiaryStack = new Stack<Diary>();
		undoActionStack = new Stack<Action>();
		redoDiaryStack = new Stack<Diary>();
		redoActionStack = new Stack<Action>();
		dTree = new DiaryTree();
		taskStack = dTree.getTaskStack();
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
		List<Diary> dlist = new LinkedList<Diary>();
		for (Diary d: diaries) {
			dlist.add(d);
		}
		return addEventToAll(dlist, event);
	}
	
	/**
	 * Add an event to multiple diaries, only keeping its data
	 * @param diaries	the list of diaries to add the event to
	 * @param event	the event to add to the diaries
	 * @return	true if all added successfully
	 */
	public boolean addEventToAll(List<Diary> diaries, Event event) {
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
	
	/**
	 * Edit an event in the specified diary
	 * @param diary	The diary where the event is to be changed
	 * @param oldEvent	the event to change
	 * @param newEvent	the new event
	 * @return	true if edited succefsully
	 */
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

	/** 
	 * Remove a new diary from the tree, pushing the change onto the undo stack.
	 * @param toRemove the Diary to remove
	 * @return true if added successfully
	 */
	public boolean removeDiary(Diary toRemove) {
		undoDiaryStack.push(toRemove);
		undoActionStack.push(Action.REMOVE_DIARY);
		return removeDiaryNoStack(toRemove);
	}
	
	/** 
	 * Remove a new diary from the tree without pushing the change onto the undo stack.
	 * @param toRemove the Diary to remove
	 * @return true if added successfully
	 */
	public boolean removeDiaryNoStack(Diary toRemove) {
		return dTree.remove(toRemove);
	}

	/**
	 * 
	 * Add a new diary to the tree without pushing the change onto the undo stack.
	 * @param toAdd	the Diary to add
	 * @return	true if added successfully
	 */
	public boolean addDiary(Diary toAdd) {
		undoDiaryStack.push(toAdd);
		undoActionStack.push(Action.ADD_DIARY);
		return addDiaryNoStack(toAdd);
	}
	
	/**
	 * 
	 * Add a new diary to the tree, pushing the change onto the undo stack.
	 * @param toAdd	the Diary to add
	 * @return	true if added successfully
	 */
	public boolean addDiaryNoStack(Diary toAdd) {
		return dTree.add(toAdd);
	}
	
	/**
	 * Add a new diary to the tree with the selected first and last names, pushing the change onto the undo stack.
	 * @param firstname	the firstname of the new diary
	 * @param lastname	the lastname of the new diary
	 * @return	true if added successfully
	 */
	public boolean addDiary(String firstname, String lastname) {
		return addDiary(new Diary(firstname, lastname));
	}

	/**
	 * 
	 * Remove a diary from a tree and add a new one without pushing it onto the undo stack, somehow keeping all the events.
	 * @param oldDiary	The diary that is to be changed
	 * @param firstname	The new firstname
	 * @param lastname	the new lastname
	 * @return	true if edited successfully
	 */
	public boolean editDiaryName(Diary oldDiary, String firstname, String lastname) {
		Diary newDiary = new Diary(firstname, lastname);
		newDiary.setEvents(oldDiary.getEvents());
		return editDiaryName(oldDiary, newDiary);
	}

	/**
	 * Remove a diary from a tree and add a new one and push it onto the undo stack, somehow keeping all the events.
	 * @param oldDiary	The diary that is to be changed
	 * @param newDiary	The new diary name
	 * @return	true if edited successfully
	 */
	public boolean editDiaryName(Diary oldDiary, Diary newDiary) {
		undoDiaryStack.push(oldDiary);
		undoDiaryStack.push(newDiary);
		undoActionStack.push(Action.EDIT_NAME);
		return editDiaryNameNoStack(oldDiary, newDiary);
	}
	
	/**
	 * Remove a diary from a tree and add a new one without pushing it onto the undo stack, somehow keeping all the events.
	 * @param oldDiary	The diary that is to be changed
	 * @param newDiary	The new diary name
	 * @return	true if edited successfully
	 */
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
	 * @return true if undone successfully
	 */
	public boolean undo() {
		
		if (!undoActionStack.isEmpty()) {
			Action action = undoActionStack.pop();
			Diary toUndo = undoDiaryStack.pop();

			if (action == Action.ADD_DIARY) {
				//last event was added, remove it
				redoActionStack.push(Action.REMOVE_DIARY);
				redoDiaryStack.push(toUndo);
				return removeDiaryNoStack(toUndo);
			} else if (action == Action.REMOVE_DIARY) {
				//last event was removed, add it back
				redoActionStack.push(Action.ADD_DIARY);
				redoDiaryStack.push(toUndo);
				return addDiaryNoStack(toUndo);
			} else if (action == Action.EDIT_NAME){
				//last event was edited, remove the changed one and add the old one
				Diary secondDiary = undoDiaryStack.pop();
				redoActionStack.push(Action.EDIT_NAME);
				redoDiaryStack.push(toUndo);
				redoDiaryStack.push(secondDiary);
				return editDiaryName(toUndo, secondDiary);
			} else if (action == Action.EDIT_EVENT) {
				redoActionStack.push(Action.EDIT_EVENT);
				redoDiaryStack.push(toUndo);
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
	 * @return true if undone successfully
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
	@Deprecated
	public Diary findMeetingSlotPrintTime(Diary[] empDiaries) {

		Date now1 = new Date();
		Diary sd = findMeetingSlot(empDiaries);
		Date now2 = new Date();
		long timeDifference = now2.getTime() - now1.getTime();
		System.out.println("Search took " + timeDifference + " miliseconds.");
		return sd;
	}
	
	/**
	 * Find a meeting slot for all employees in the array between years 1000 and 3000.
	 * @param empDiaries	an array of diaries for which the meeting slot is to be found
	 * @return	a Diary containing times when everyone is available
	 */
	public Diary findMeetingSlot(Diary[] empDiaries) { //probably can be static?
		
		List<Diary> empList = new LinkedList<Diary>();
		for (Diary d: empDiaries) {
			empList.add(d);
		}
		return findMeetingSlot(empList);
	}

	/**
	 * Find a meeting slot for all employees in the array between the specified bounds
	 * @param empDiaries	a list of diaries for which the meeting slot is to be found
	 * @param lowerBound	the lower bound of the search
	 * @param upperBound	the upper bound of the search
	 * @return	a Diary containing times when everyone is available
	 */
	public Diary findMeetingSlot(List<Diary> empDiaries, Date lowerBound, Date upperBound) {
		
		TreeSet<EventTime> timeset = new TreeSet<EventTime>();
		long lowerBoundTime = lowerBound.getTime();
		long upperBoundTime = upperBound.getTime();
		for (Diary d: empDiaries) {
			for (Event event : d.getEvents()) {
				Date startDate = event.getStartTime();
				Date endDate = event.getEndTime();
				if (startDate.getTime() >= lowerBoundTime && endDate.getTime() <= upperBoundTime) {
						//if it is between the searching bounds
					EventTime startTime = new EventTime(startDate, true);
					EventTime endTime = new EventTime(endDate, false);
					if(!timeset.add(startTime)) {
						//if an EventTime with this time is already in the set
						timeset.remove(startTime);
					}
					if (!timeset.add(endTime)) {
						timeset.remove(endTime);
					}
				}
			}
		}
		
		int timeCounter = 0;
		boolean wasZero = true;
		Diary superDiary = new Diary("Available","");
		Event av = new Event(lowerBound, "Available");
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
		superDiary.getEvents().last().setEndTime(upperBound);
		return superDiary;
	}
	
	/**
	 * Find a meeting slot for all employees in the array at times between the years 1000 and 3000.
	 * @param empDiaries	an list of diaries for which the meeting slot is to be found
	 * @return	a Diary containing times when everyone is available
	 */
	public Diary findMeetingSlot(List<Diary> empDiaries) {
		Calendar startCal = new GregorianCalendar(1001,0,0,0,0);
		Calendar endCal = new GregorianCalendar(3001, 0,0,0,0);
		return findMeetingSlot(empDiaries, startCal.getTime(), endCal.getTime());
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
	        this.taskStack = (Stack<String>) dTree.getTaskStack();
	        if (taskStack == null) {
	        	dTree.setTaskStack(new Stack<String>());
		        this.taskStack = (Stack<String>) dTree.getTaskStack();
	        }
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
	 * add a task to the to-do list
	 * @param task	the name of the task that is to be added
	 * @return	true if added successfully
	 */
	public boolean addToTaskStack(String task) {
		return taskStack.add(task);
	}
	/**
	 * remove a task from the to-do list 
	 * @param task	the name of the task to remove
	 * @return	true if removed successfully, ie the task was in the stack before
	 */
	public boolean removeFromTaskStack(String task) {
		return taskStack.remove(task);
	}
	/**
	 * get an array of diaries from the tree
	 * @return the sorted array of diaries from the tree
	 */
	public Diary[] getDiaryArray() {
		return dTree.toArray();
	}
	
	/**
	 * @return the diaryTree
	 */
	public DiaryTree getdTree() {
		return dTree;
	}

	/**
	 * @return the taskStack
	 */
	public Stack<String> getTaskStack() {
		return taskStack;
	}

}
