import java.util.Date;

/**
 * Used just for searching for an available timeslot
 * @author fpavlica
 *
 */
public class EventTime implements Comparable<EventTime> {
	private Date time;
	private boolean isStart;
	
	/**
	 * Constructor, setting a time and a boolean value which is true if the time was a start time
	 * @param time	the time
	 * @param isStart	true if it was a start time
	 */
	public EventTime(Date time, boolean isStart) {
		this.time = time;
		this.isStart = isStart;
	}
	
	/**
	 * @return the time
	 */
	public Date getTime() {
		return time;
	}
	/**
	 * @param time the time to set
	 */
	public void setTime(Date time) {
		this.time = time;
	}
	/**
	 * @return the isStart
	 */
	public boolean isStart() {
		return isStart;
	}
	/**
	 * @param isStart the isStart to set
	 */
	public void setStart(boolean isStart) {
		this.isStart = isStart;
	}

	@Override
	public int compareTo(EventTime o) {
		// TODO Auto-generated method stub
		return time.compareTo(o.getTime());
	}
}
