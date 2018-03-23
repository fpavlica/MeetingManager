import java.io.Serializable;
import java.util.Date;

public class Event implements Comparable<Event>, Serializable{
	private String name;
	private Date startTime, endTime;
	private int index; //for selecting in the console

	public Event(Date startTime, Date endTime, String name, int index) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.name = name;
		this.setIndex(index);
	}
	
	public Event(Date startTime, Date endTime, String name) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.name = name;
	}
	
	
	public Event(Date startTime, String name) {
		this.startTime = startTime;
		this.name = name;
	}
	
	public Event(Event eventToCopy) {
		this.copyDataFrom(eventToCopy);
	}
	
	public void copyDataFrom(Event eventToCopy) {		
		this.name = eventToCopy.getName();
		this.startTime = eventToCopy.getStartTime();
		this.endTime = eventToCopy.getEndTime();
		this.index = eventToCopy.getIndex();
	}
	
	@Override
	public int compareTo(Event otherEvent) {
		return startTime.compareTo(otherEvent.getStartTime());
	}
	
	@Override
	public String toString() {
		//TODO maybe not like this
		return name +", from " + startTime + " to " + endTime + "\t\t"+ index;
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
	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}
	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}
	
}
