import java.util.Date;

public class EventTime {
	private Date time;
	private boolean isStart;
	
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
}
