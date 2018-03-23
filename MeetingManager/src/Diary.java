import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TreeSet;

public class Diary implements Comparable<Diary>{
	private String firstname, lastname;
	private long id;
	private TreeSet<Event> events;
	private int currentEventIndex;

	public Diary(String firstname, String lastname) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.events = new TreeSet<Event>();
		this.currentEventIndex = 1; //starting at 1 because it's going to be used for user interaction mostly
	}
	
	public boolean addEvent(Event event) {
		event.setIndex(currentEventIndex);
		currentEventIndex++;
		return events.add(event);
	}
	
	public void printAllEvents() {
		for (Event e: events) {
			System.out.println(e);
		}
	}
	
	public Event findEventByStartTime(Date starttime) {
		Event startTimeEvent = new Event(starttime, "");
		Event floor = events.floor(startTimeEvent);
		if (floor == null) {
			//name not found
			return null; //TODO hacky, just skips a part of the code
		}
		int temp = floor.compareTo(startTimeEvent);
		if (temp == 0) {
			//same name, found
			return floor;
		} else {
			//name not found
			return null;
		}
	}
	
	public boolean editEventByIndex(int index) {
		Event theEvent = findEventByIndex(index);
		if (theEvent != null) { //if such event exists
			 return editEvent(theEvent);
		} else {
			//event with this ID does not exist
			System.out.println("An event with this ID does not exist.");
			return false;
		}
	}
	
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
	 * edits chosen event data by user choice
	 * 
	 * @param editingEvent
	 * @return true if editing was conducted or false if none happened
	 */
	public boolean editEvent(Event editingEvent) {

		if (editingEvent != null) {

			// Asks user for event name change
			System.out.println("\nDo you wish to change the name? Y/N");
			if (getAnswerYN()) {
				System.out.println("Please enter a new name for this event:");
				String newName = UserInput.nextString();
				editingEvent.setName(newName);
			}
			
			// Asks user to change start time
			System.out.println("\nDo you wish to change the start time? Y/N");
			if (getAnswerYN()) {
				Calendar newStartFormat = changeTime();
				Date newStart = newStartFormat.getTime();
				editingEvent.setStartTime(newStart);
			} 
			
			// Asks user to change ending time
			System.out.println("\nDo you wish to change the end time? Y/N");
			if (getAnswerYN()) {
			
				Calendar newEndFormat = changeTime();
				Date newEnd = newEndFormat.getTime();
				editingEvent.setEndTime(newEnd);
			}
			return true;
		} else {
			return false;
		}
	}
	private boolean getAnswerYN() {
		boolean pass = false;
		boolean decision = false;
		do {
			String answer = UserInput.nextString();
			if (answer.equalsIgnoreCase("Y")) {
				decision = true;
				pass = true;
			} else if (answer.equalsIgnoreCase("N")) {
				decision = false;
				pass = true;
			} else {
				System.out.println("Input not recognized. Please respond with 'Y' or 'N'. Try again:");
				//invalid input, keep pass as false
			}
		} while (!pass);
		return decision;
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
		return firstname + " " + lastname;
	}
	
	public String getSortableName() {
		return lastname + " " + firstname;
	}
	
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
