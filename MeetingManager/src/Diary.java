import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TreeSet;

public class Diary implements Comparable<Diary>{
	private String firstname, lastname;
	private long id;
	private TreeSet<Event> events;

	public Diary(String firstname, String lastname) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.events = new TreeSet<Event>();
	}
	
	public boolean addEvent(Event event) {
		return events.add(event);
	}
	
	public void printAllEvents() {
		for (Event e: events) {
			System.out.println(e);
		}
	}
	
	public Event findEventByStartTime(Date starttime) {
		Event startTimeEvent = new Event(starttime, new Date(), "");
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
	
	/**
	 * edits chosen event data by user choice
	 * 
	 * @param editingEvent
	 * @return true if editing was conducted or false if none happened
	 */
	public boolean editEvent(Event editingEvent) {
		String command = ""; // Used for user to answer with yes or no to editing specific parts of event

		if (editingEvent != null) {

			// Asks user for event name change
			System.out.println("\nDo you wish to change the name? Y/N");
			while (!command.equalsIgnoreCase("N")) {
				command = UserInput.nextString();
				if (command.compareToIgnoreCase("Y") == 0) {
					String newName = UserInput.nextString();
					editingEvent.setName(newName);
				} else {
					System.out.println("\nInput not recognized. Respond with 'Y' or 'N'");
				}
			}
			command = " ";

			// Asks user to change start time
			System.out.println("\nDo you wish to change the start time? Y/N");
			while (!command.equalsIgnoreCase("N")) {
				command = UserInput.nextString();
				if (command.compareToIgnoreCase("Y") == 0) {
					Calendar newStartFormat = changeTime();
					Date newStart = newStartFormat.getTime();
					editingEvent.setStartTime(newStart);
				} else {
					System.out.println("\nInput not recognized. Respond with 'Y' or 'N'");
				}
			} //
			command = " ";

			// Asks user to change ending time
			System.out.println("\nDo you wish to change the end time? Y/N");
			while (!command.equalsIgnoreCase("N")) {
				command = UserInput.nextString();
				if (command.compareToIgnoreCase("Y") == 0) {

					Calendar newEndFormat = changeTime();
					Date newEnd = newEndFormat.getTime();
					editingEvent.setEndTime(newEnd);
				} else {
					System.out.println("\nInput not recognized. Respond with 'Y' or 'N'");
				}
			}
			return true;
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
			System.out.println("\nInput year of meeting.");
			year = UserInput.nextInt();
		

		//inputs new month
			System.out.println("\nInput month of meeting.");
			month = UserInput.nextMonth();

		//inputs new day
			System.out.println("\nInput day of meeting.");
			day = UserInput.nextDayOfMonth();

		//inputs new hour
			System.out.println("\nInput hour start of meeting. (24 hour format)");
			hour = UserInput.nextHour();

		//inputs new minute
		
			System.out.println("\nInput minute start of meeting.");
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
