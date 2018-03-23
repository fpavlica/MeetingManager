
public class EventState {
	private Event eventRef, eventData;
	public enum Action {ADD, EDIT, REMOVE};
	private Action action;
	
	/**
	 * @return the eventRef
	 */
	public Event getEventRef() {
		return eventRef;
	}
	/**
	 * @return the eventData
	 */
	public Event getEventData() {
		return eventData;
	}
	/*
	public EventState(Event event) {
		this.eventRef = event;
		this.eventData = new Event(event);
	}
	*/
	public EventState(Event event, Action action) {
		this.eventRef = event;
		this.eventData = new Event(event);
		this.setAction(action);
	}
	
	public void undoEdits() {
		eventRef.copyDataFrom(eventData);
	}
	/**
	 * @return the action
	 */
	public Action getAction() {
		return action;
	}
	/**
	 * @param action the action to set
	 */
	public void setAction(Action action) {
		this.action = action;
	}
	
}
