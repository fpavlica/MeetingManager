import java.util.Date;

public class Event implements Comparable<Event>{
	private String name;
	private Date startTime, endTime;
	/*
	EventTime startTime;
	EventTime endTime;
	Date dateOfEvent;
	boolean isEventMeeting;

	
	public Event(EventTime startTime, EventTime endTime, Date dateOfEvent, boolean isEventMeeting) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.dateOfEvent = dateOfEvent;
		this.isEventMeeting = isEventMeeting;
	
	}
	*/
	
	public Event(Date startTime, Date endTime, String name) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.name = name;
	}
	
	@Override
	public int compareTo(Event otherEvent) {
		return startTime.compareTo(otherEvent.getStartTime());
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the startTime
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
}
