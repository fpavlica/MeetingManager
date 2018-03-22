import java.util.Date;
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
