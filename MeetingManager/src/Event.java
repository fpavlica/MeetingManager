import java.io.Serializable;
import java.util.Date;

public class Event implements Comparable<Event>, Serializable{

	private static final long serialVersionUID = 2809278511859046638L; //compiler-generated, for object saving
	private String name;
	private Date startTime, endTime;
	private int index; //for selecting in the console

	/**
	 * Constructor for a new event, setting all its values
	 * @param startTime	The starttime of the event
	 * @param endTime	The endtime of the event
	 * @param name	The name of the event
	 * @param index	The index of the event in a diary, for selecting it in the console
	 */
	public Event(Date startTime, Date endTime, String name, int index) {
		this.startTime = startTime;
		this.setEndTime (endTime);
		this.name = name;
		this.setIndex(index);
	}
	
	/**
	 * Constructor for a new event, setting its important values
	 * @param startTime	The starttime of the event
	 * @param endTime	The endtime of the event
	 * @param name	The name of the event
	 */
	public Event(Date startTime, Date endTime, String name) {
		this.startTime = startTime;
		this.setEndTime(endTime);
		this.name = name;
	}
	
	/**
	 * Basic constructor, setting only its starttime and name
	 * @param startTime	the starttime of the event
	 * @param name	the name of the event
	 */
	public Event(Date startTime, String name) {
		this.startTime = startTime;
		this.name = name;
	}
	
	/**
	 * Constructor which copies data from another event to this new one
	 * @param eventToCopy	The event whose data is to be copied
	 */
	public Event(Event eventToCopy) {
		this.copyDataFrom(eventToCopy);
	}
	
	/**
	 * Set all data of this event to that of another event
	 * @param eventToCopy	The event to copy the data from
	 */
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
		return name +", from " + startTime + " to " + endTime + "\t\t";
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
		if (endTime.getTime() >= this.startTime.getTime()) {
			this.endTime = endTime;
		} else {
			//switch the two to make sure starttime is not before endtime
			this.endTime = this.startTime;
			this.startTime = endTime;
		}
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
