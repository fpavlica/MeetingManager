import java.util.Date;
import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Stack;
import java.util.TreeSet;

public class Diary implements Comparable<Diary>, Serializable{

	private static final long serialVersionUID = -790683766257112951L; //compiler-generated, for object saving
	private String firstname, lastname;
	private long id;
	private TreeSet<Event> events;
	private int currentEventIndex;
	private enum Action {ADD, REMOVE, EDIT};
	private transient Stack<Event> undoEventStack, redoEventStack;
	private transient Stack<Action> undoActionStack, redoActionStack;

	/**
	 * Constructor, setting the first and last names of the employee this diary belongs to.
	 * @param firstname	The first name of the employee
	 * @param lastname	The surname of the employee
	 */
	public Diary(String firstname, String lastname) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.events = new TreeSet<Event>();
		this.currentEventIndex = 1; //starting at 1 because it's going to be used for user interaction mostly
		initStacks();
	}
	public void initStacks(){
		this.undoEventStack = new Stack<Event>();
		this.redoEventStack = new Stack<Event>();
		this.undoActionStack = new Stack<Action>();
		this.redoActionStack = new Stack<Action>();
	}
	
	public void makeSureStacksAreInitialised() {
		if (undoActionStack == null || redoActionStack == null) {
			initStacks();
		}
	}
	
	/**
	 * add an existing event to the diary
	 * @param event	the event to add
	 * @return true if added successfully
	 */
	private boolean addEventNoStack(Event event) {
		event.setIndex(currentEventIndex);
		currentEventIndex++;
		return events.add(event);
	}
	
	public boolean addEvent(Event event) {
		makeSureStacksAreInitialised();
		undoEventStack.push(event);
		undoActionStack.push(Action.ADD);
		return addEventNoStack(event);
	}
	
	/**
	 * add an event to the diary
	 * @param startTime	the start time of the event
	 * @param endTime	the end time of the event
	 * @param name		the name of the event
	 * @return			true if added successfully
	 */
	public boolean addEvent(Date startTime, Date endTime, String name) {
		return addEvent(new Event(startTime,endTime,name));
	}
	
	/**
	 * Remove an event from the diary
	 * @param event	The event reference to remove
	 * @return	true if removed successfully (false if the diary did not contain the event)
	 */
	public boolean removeEvent(Event event) {
		makeSureStacksAreInitialised();
		undoEventStack.push(event);
		undoActionStack.push(Action.REMOVE);
		return removeEventNoStack(event);
	}
	
	private boolean removeEventNoStack(Event event) {
		//currentEventIndex--;
		return events.remove(event);
	}
	
	/**
	 * Prints out all events in the diary
	 */
	public void printAllEvents() {
		for (Event e: events) {
			System.out.println(e);
		}
	}
	
	/**
	 * Undo the last change done to an event in this diary
	 */
	public boolean undo() {
		Action action = undoActionStack.pop();
		Event event = undoEventStack.pop();
		redoEventStack.push(event);
		if (action == Action.ADD) {
			//last event was added, remove it
			redoActionStack.push(Action.REMOVE);
			return removeEventNoStack(event);
		} else if (action == Action.REMOVE) {
			//last event was removed, add it back
			redoActionStack.push(Action.ADD);
			return addEventNoStack(event);
		} else if (action == Action.EDIT){
			//last event was edited, remove the changed one and add the old one
			Event secondEvent = undoEventStack.pop();
			redoEventStack.push(secondEvent);
			redoActionStack.push(Action.EDIT);
			return editEventNoStack(event, secondEvent);
		}
		//if the code somehow manages to get here, nothing was undone and something must be very wrong
		return false;
	}
	
	/**
	 * Redo the last undone action
	 */
	public boolean redo() {
		Action action = redoActionStack.pop();
		Event event =redoEventStack.pop();
		
		if (action == Action.ADD) {
			//last event was added, remove it
			return removeEvent(event);
		} else if (action == Action.REMOVE) {
			//last event was removed, add it back
			return addEvent(event);
		} else if (action == Action.EDIT){
			//last event was edited, remove the changed one and add the old one
			return editEvent(event, redoEventStack.pop());
		}
		//if the code somehow manages to get here, nothing was redone and something must be wrong
		return false;
	}
	
	
	/**
	 * Find an event in the diary by its start time. O(log(n))
	 * @param starttime	the start time of the event to look for
	 * @return	the found event, or null if the diary does not contain such event
	 */
	public Event findEventByStartTime(Date starttime) {
		Event timeEvent = new Event(starttime, "");	
		Event floor = events.floor(timeEvent);
		if (floor != null) {
			//starttime has left children, floor is the rightmost element in otherList's left subtree
			if (floor.compareTo(timeEvent) == 0) {
				//same starttime, found
				return floor;
			}
		} 
		//not found
		return null;	
	}
	
	/**
	 * Edit an event using the console, using its index to specify which event to edit
	 * @param index	the index of the event to edit
	 * @return	true if edited successfully
	 */
	public boolean editEventByIndex(int index) {
		Event theEvent = findEventByIndex(index);
		if (theEvent != null) { //if such event exists
			 return editEventConsole(theEvent);
		} else {
			//event with this ID does not exist
			System.out.println("An event with this ID does not exist.");
			return false;
		}
	}
	
	/**
	 * Find an event with this index. O(n)
	 * <p> If two events have the same index (which should not happen), it returns the first one only.
	 * @param index	the index of the event to look for
	 * @return	the found event
	 */
	public Event findEventByIndex(int index) {
		//inefficient probably, has to traverse the whole tree to find it
		for (Event e: events) {
			if (e.getIndex()==index) {
				return e;
			}
		}
		return null; //code only gets here if nothing was found.
	}

	/**
	 * 
	 * @param eventToEdit
	 * @param newEventData
	 * @return
	 */
	public boolean editEventNoStack(Event eventToEdit, Event newEventData) {
		//remove old one, add new one
		return (removeEventNoStack(eventToEdit) && addEventNoStack(newEventData));
		
		/*
		undoStack.push(new UndoAction(eventToEdit, UndoAction.Action.EDIT));
		eventToEdit.copyDataFrom(newEventData);	
		*/
	
	}
	public boolean editEvent(Event eventToEdit, Event newEventData) {
		makeSureStacksAreInitialised();
		undoEventStack.push(eventToEdit);
		undoEventStack.push(newEventData);
		undoActionStack.push(Action.EDIT);
		return editEventNoStack(eventToEdit, newEventData);
	}
	
	/**
	 * edits chosen event data by user choice
	 * 
	 * @param editingEvent	the event to edit
	 * @return true if editing was conducted or false if none happened
	 */
	@Deprecated
	public boolean editEventConsole(Event editingEvent) {
		
		if (editingEvent != null) {
			
			Event newEvent = new Event(editingEvent);
			boolean aChangeWasMade = false;
			
			// Asks user for event name change
			System.out.println("\nDo you wish to change the name? Y/N");
			if (UserInput.nextAnswerYN()) {
				System.out.println("Please enter a new name for this event:");
				String newName = UserInput.nextString();
				newEvent.setName(newName);
				aChangeWasMade = true;
			}
			
			// Asks user to change start time
			System.out.println("\nDo you wish to change the start time? Y/N");
			if (UserInput.nextAnswerYN()) {
				Calendar newStartFormat = changeTime();
				Date newStart = newStartFormat.getTime();
				newEvent.setStartTime(newStart);
				aChangeWasMade = true;
			} 
			
			// Asks user to change ending time
			System.out.println("\nDo you wish to change the end time? Y/N");
			if (UserInput.nextAnswerYN()) {
			
				Calendar newEndFormat = changeTime();
				Date newEnd = newEndFormat.getTime();
				newEvent.setEndTime(newEnd);
				aChangeWasMade = true;
			}
			if (aChangeWasMade) {
				return editEvent(editingEvent, newEvent);
			}
		}
		return false;
	}

	/**
	 * Lets the user input year, month, day, hour and minutes and checks correct
	 * values are entered
	 * 
	 * @return Calendar containing new date information
	 */
	private Calendar changeTime() {

		int year = 0;
		int month = 0;
		int day = 0;
		int hour = 0;
		int minute = 0;

		//inputs new year
			System.out.println("\nEnter year of event:");
			year = UserInput.nextInt();
		

		//inputs new month
			System.out.println("\nEnter month of event:");
			month = UserInput.nextMonth();

		//inputs new day
			System.out.println("\nEnter day of event:");
			day = UserInput.nextDayOfMonth();

		//inputs new hour
			System.out.println("\nEnter hour of event (24 hour format):");
			hour = UserInput.nextHour();

		//inputs new minute
		
			System.out.println("\nEnter minute of event:");
			minute = UserInput.nextMinute();
			
		Calendar newFormat = new GregorianCalendar(year, month, day, hour, minute);
		return newFormat;
	}
	
	public String getAllInfo() {
		String s =  firstname + " " + lastname;
		for (Event e: events) {
			s+="\n" + e;
		}
		return s;
	}
	
	@Override
	public int compareTo(Diary otherDiary) {
		String comparingName = otherDiary.getSortableName();
		return this.getSortableName().compareToIgnoreCase(comparingName);
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	/**
	 * @return the name in the form 'lastname firstname' for sorting
	 */
	public String getSortableName() {
		return lastname + " " + firstname;
	}
	
	/**
	 * 
	 * @return the name in the form 'firstname lastname' for displaying
	 */
	public String getName() {
		return firstname + " " + lastname;
	}
	/**
	 * @return the events
	 */
	public TreeSet<Event> getEvents() {
		return events;
	}

	/**
	 * @return the firstname
	 */
	public String getFirstname() {
		return firstname;
	}

	/**
	 * @param firstname the firstname to set
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	/**
	 * @return the lastname
	 */
	public String getLastname() {
		return lastname;
	}

	/**
	 * @param lastname the lastname to set
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @param events the events to set
	 */
	public void setEvents(TreeSet<Event> events) {
		this.events = events;
	}
}
