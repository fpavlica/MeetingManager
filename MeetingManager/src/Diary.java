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
	private transient Stack<EventState> undoStack, redoStack;

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
		this.undoStack = new Stack<EventState>();
		this.redoStack = new Stack<EventState>();
	}
	
	/**
	 * add an existing event to the diary
	 * @param event	the event to add
	 * @return true if added successfully
	 */
	public boolean addEvent(Event event) {
		event.setIndex(currentEventIndex);
		currentEventIndex++;
		undoStack.push(new EventState(event, EventState.Action.ADD));
		return events.add(event);
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
		currentEventIndex--;
		undoStack.push(new EventState(event, EventState.Action.REMOVE));
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
	public void undo() {
		EventState lastState = undoStack.pop();
		redoStack.push(lastState);
		undoEvent(lastState);
	}
	
	/**
	 * Redo the last undone action
	 */
	public void redo() {
		EventState lastState = redoStack.pop();
		undoStack.push(lastState);
		undoEvent(lastState);
	}
	
	/**
	 * Return an event to its previous state
	 * @param previousState	the previous state of the event
	 */
	private void undoEvent(EventState previousState) {
		
		if (previousState.getAction() == EventState.Action.ADD) {
			//last event was added, should now remove
			removeEvent(previousState.getEventRef());
		} else if (previousState.getAction() == EventState.Action.REMOVE) {
			//last event was removed, should be added back
			addEvent(previousState.getEventData());
		} else {//i.e. if (event.getAction() == EventState.Action.EDIT) {
			//last event was just edited, simply undo edits
			previousState.undoEdits(); //TODO feels too tightly coupled maybe
		}
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
	public boolean editEvent(Event eventToEdit, Event newEventData) {
		undoStack.push(new EventState(eventToEdit, EventState.Action.EDIT));
		eventToEdit.copyDataFrom(newEventData);
		return true; //TODO bad
	}
	
	/**
	 * edits chosen event data by user choice
	 * 
	 * @param editingEvent	the event to edit
	 * @return true if editing was conducted or false if none happened
	 */
	public boolean editEventConsole(Event editingEvent) {
		
		if (editingEvent != null) {

			undoStack.push(new EventState(editingEvent, EventState.Action.EDIT));
			// Asks user for event name change
			System.out.println("\nDo you wish to change the name? Y/N");
			if (UserInput.nextAnswerYN()) {
				System.out.println("Please enter a new name for this event:");
				String newName = UserInput.nextString();
				editingEvent.setName(newName);
			}
			
			// Asks user to change start time
			System.out.println("\nDo you wish to change the start time? Y/N");
			if (UserInput.nextAnswerYN()) {
				Calendar newStartFormat = changeTime();
				Date newStart = newStartFormat.getTime();
				editingEvent.setStartTime(newStart);//TODO this may fuck up the tree
			} 
			
			// Asks user to change ending time
			System.out.println("\nDo you wish to change the end time? Y/N");
			if (UserInput.nextAnswerYN()) {
			
				Calendar newEndFormat = changeTime();
				Date newEnd = newEndFormat.getTime();
				editingEvent.setEndTime(newEnd);
			}
			
			
			return true;
		} else {
			return false;
		}
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
	
	@Override
	public int compareTo(Diary otherDiary) {
		String comparingName = otherDiary.getSortableName();
		return this.getSortableName().compareTo(comparingName);
	}
	
	@Override
	public String toString() {
		//TODO temp
		String s =  firstname + " " + lastname;
		for (Event e: events) {
			s+="\n" + e;
		}
		return s;
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
}
