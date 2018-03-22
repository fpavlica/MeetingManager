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
	** edits existing event info according to what the user wants to change
	** TODO make the input validation for dates a single method (possibly used for creating events?)
	**/
		public boolean editEvent(Event editingEvent) {
		String command = "";
		Calendar today = Calendar.getInstance();
		boolean check = false;
		if (editingEvent != null) {
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

			System.out.println("\nDo you wish to change the start time? Y/N");
			while (!command.equalsIgnoreCase("N")) {
				command = UserInput.nextString();
				if (command.compareToIgnoreCase("Y") == 0) {
					int yearS = 0;
					int monthS = 0;
					int dayS = 0;
					int hourS = 0;
					int minuteS = 0;

					while (check == false) {
						System.out.println("\nInput year of meeting.");
						yearS = UserInput.nextInt();
						if (yearS >= today.get(Calendar.YEAR)) {
							check = true;
						} else {
							System.out.println("\nInvalid year.");
						}
					}
					check = false;

					while (check == false) {
						System.out.println("\nInput month of meeting.");
						monthS = UserInput.nextInt();
						if (monthS > 0 && monthS <= 12) {
							check = true;
						} else {
							System.out.println("\nInvalid month.");
						}
					}
					check = false;

					while (check == false) {
						System.out.println("\nInput day of meeting.");
						dayS = UserInput.nextInt();
						if (monthS == 1 || monthS == 3 || monthS == 5 || monthS == 7 || monthS == 9 || monthS == 11) {
							if (dayS > 0 && dayS <= 31) {
								check = true;
							} else {
								System.out.println("\nInvalid day.");
							}
						} else if (monthS == 2) {
							if (dayS > 0 && dayS <= 29) {
								check = true;
							} else {
								System.out.println("\nInvalid day.");
							}
						} else {
							if (dayS > 0 && dayS <= 30) {
								check = true;
							} else {
								System.out.println("\nInvalid day.");
							}
						}
					}
					check = false;

					while (check == false) {
						System.out.println("\nInput hour start of meeting. (24 hour format)");
						hourS = UserInput.nextInt();
						if (hourS > 0 && hourS <= 23) {
							check = true;
						} else {
							System.out.println("\nInvalid hour.");
						}
					}
					check = false;

					while (check == false) {
					System.out.println("\nInput minute start of meeting.");
					minuteS = UserInput.nextInt();
					if (minuteS > 0 && minuteS <= 59) {
						check = true;
					} else {
						System.out.println("\nInvalid hour.");
					}
				} check = false;
				
					Calendar newStartFormat = new GregorianCalendar(yearS, monthS, dayS, hourS, minuteS);
					Date newStart = newStartFormat.getTime();
					editingEvent.setStartTime(newStart);
				} else {
					System.out.println("\nInput not recognized. Respond with 'Y' or 'N'");
				}
			}//
			command = " ";
			
			System.out.println("\nDo you wish to change the end time? Y/N");
			while (!command.equalsIgnoreCase("N")) {
				command = UserInput.nextString();
				if (command.compareToIgnoreCase("Y") == 0) {
					int yearE = 0;
					int monthE = 0;
					int dayE = 0;
					int hourE = 0;
					int minuteE = 0;

					while (check == false) {
						System.out.println("\nInput year of meeting.");
						yearE = UserInput.nextInt();
						if (yearE >= today.get(Calendar.YEAR)) {
							check = true;
						} else {
							System.out.println("\nInvalid year.");
						}
					}
					check = false;

					while (check == false) {
						System.out.println("\nInput month of meeting.");
						monthE = UserInput.nextInt();
						if (monthE > 0 && monthE <= 12) {
							check = true;
						} else {
							System.out.println("\nInvalid month.");
						}
					}
					check = false;

					while (check == false) {
						System.out.println("\nInput day of meeting.");
						dayE = UserInput.nextInt();
						if (monthE == 1 || monthE == 3 || monthE == 5 || monthE == 7 || monthE == 9 || monthE == 11) {
							if (dayE > 0 && dayE <= 31) {
								check = true;
							} else {
								System.out.println("\nInvalid day.");
							}
						} else if (monthE == 2) {
							if (dayE > 0 && dayE <= 29) {
								check = true;
							} else {
								System.out.println("\nInvalid day.");
							}
						} else {
							if (dayE > 0 && dayE <= 30) {
								check = true;
							} else {
								System.out.println("\nInvalid day.");
							}
						}
					}
					check = false;

					while (check == false) {
						System.out.println("\nInput hour start of meeting. (24 hour format)");
						hourE = UserInput.nextInt();
						if (hourE > 0 && hourE <= 23) {
							check = true;
						} else {
							System.out.println("\nInvalid hour.");
						}
					}
					check = false;

					while (check == false) {
					System.out.println("\nInput minute start of meeting.");
					minuteE = UserInput.nextInt();
					if (minuteE > 0 && minuteE <= 59) {
						check = true;
					} else {
						System.out.println("\nInvalid hour.");
					}
				} check = false;
				
					Calendar newEndFormat = new GregorianCalendar(yearE, monthE, dayE, hourE, minuteE);
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
