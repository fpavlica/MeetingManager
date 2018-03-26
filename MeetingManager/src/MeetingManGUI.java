import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Stack;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;

/**
 * A GUI class for the meeting manager.
 * It is ugly but we had no time
 * @author AC12001 17/18 group 2
 *
 */
public class MeetingManGUI implements ActionListener{

	protected static final String DEFAULT_FILEPATH = "defaultsave.ser";

	private Manager manager;
	
    private JFrame mainFrame, diaryFrame, editEventFrame, nameFrame, taskFrame, taskNameFrame;
    private JButton viewDiaryB, addDiaryB, removeDiaryB, addEventB, removeEventB, editEventB, findSlotB,
    		refreshDiariesB, editNameB, addToAllB, addTaskB, removeTaskB, closeTasksB, showTasksB;
    public static final String VIEW_DIARY = "View Diary", ADD_DIARY = "Add Diary", REMOVE_DIARY = "Remove Diary", 
    		EDIT_NAME = "Edit Name", FIND_SLOT = "Find a Meeting Slot", ADD_TO_ALL = "Add an event to selected diaries",
    		ADD_EVENT = "Add Event", REMOVE_EVENT = "Remove Event", EDIT_EVENT = "Edit Event", SAVE_NAME = "Save", 
    		CANCEL_NAME = "Cancel", REFRESH_DIARIES = "Refresh (sort)", SAVE_EVENT = "Save ", CANCEL_EVENT = "Cancel ",
    		ADD_TASK = "Add task", REMOVE_TASK = "Remove task", SAVE_TASK = "Save  ", CANCEL_TASK = "Cancel  ",
    		CLOSE_TASKS = "Close", SHOW_TASKS = "Show To-Do List";
    private JList<Diary> diaryList;
    private JList<Event> eventList;
	private JList<String> taskList;
    private JSpinner[] startSpinners, endSpinners;
    private boolean saved, addingToAll; 
	private JTextField firstnameField, surnameField, eventNameField, taskField;

	private Diary editingDiary, diaryBeingViewed;
	private Event editingEvent;

    
	/**
	 * Main method. create and show a gui.
	 * @param args	default parameters for a main method
	 */
	public static void main(String[] args) {
    	
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() 
        {
            public void run() 
            {
                createAndShowGUI();
            }
        });
    }
	
	/**
	 * Constructor, initialises a Manager object and loads the meetingmanager from the default save file
	 */
	public MeetingManGUI() {
		manager = new Manager();
		manager.loadFromFile(DEFAULT_FILEPATH);
		saved = true;
	}
	
    /**
     * Create the GUI and show it.
     * For thread safety, this method should be invoked from the event-dispatching thread.
     */
    private static void createAndShowGUI() 
    {
        // Create and set up the window.
        JFrame frame = new JFrame("Meeting Manager 2000");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // Create and set up the content pane.
        MeetingManGUI meetgui = new MeetingManGUI();
        meetgui.mainFrame = frame;
        frame.setJMenuBar(meetgui.createMenu());
        frame.setContentPane(meetgui.createContentPane()); 
        frame.addWindowListener(meetgui.createConfirmExitDialog(frame));

        // Display the window, setting the size
        frame.setSize(1200, 720);
        frame.setVisible(true);
    }
    

    /**
     * Modifies what happens when the exit button (the red 'X') is clicked, so that the user has an option to save the game before exiting.
     * @param frame The frame of which the exit button is going to be pressed
     * @return the WindowAdapter object created with this method.
     */
    public WindowAdapter createConfirmExitDialog(JFrame frame){
    	WindowAdapter wa = new WindowAdapter() 
    	{
    		/**
    		 * To be honest, I am not exactly sure how this works. But it does.
    		 * Most examples on the Internet suggested doing it like this, so I assume this is the right way to do it. 
    		 */
    		@Override
    		public void windowClosing(WindowEvent e) {
    			if(!saved) {
    				//show an exit confirmation dialog 
    				String[] options = {"Save and exit", "Exit without saving", "Cancel"};
    				int n = JOptionPane.showOptionDialog(frame, "Any unsaved progress will be lost. "
    						+ "Are you sure you want to exit?", 
    						"Confirm exit", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
    				System.out.println(n);
    				if (n == 0) {
    					String savePath = DEFAULT_FILEPATH;
    					if (savePath != null) {
    						manager.saveToFile(DEFAULT_FILEPATH);
	    					frame.dispose();
	    					System.exit(0);
    					} else {
    						//if the user pressed cancel when selecting the savePath, just do nothing,
    						//as if he pressed cancel on the previous dialog box.
    					}
    				} else if (n == 1) {
    					//exit without saving
    					frame.dispose();
    					System.exit(0);
    				} if (n == 2) {
    					//cancel
    				}
    			} else {
    				//if a game is already saved there's no need for an exit confirmation dialog
    				frame.dispose();
    			}
    		}
    	};
    	return wa;
    }
    

    /**
     * Create a simple menu for the main frame
     * @return the JMenuBar created by this method
     */
    public JMenuBar createMenu() 
    {
        JMenuBar menuBar  = new JMenuBar();;
        JMenu menu = new JMenu("Menu");
        JMenuItem menuItem;
       
        menuBar.add(menu);

        // A group of JMenuItems. You can create other menu items here if desired
        menuItem = new JMenuItem("Undo");
        menuItem.addActionListener(this);
        menu.add(menuItem);

        menuItem = new JMenuItem("Redo");
        menuItem.addActionListener(this);
        menu.add(menuItem);

        menuItem = new JMenuItem("Save");
        menuItem.addActionListener(this);
        menu.add(menuItem);

        //a submenu
        menu.addSeparator();
        return menuBar;
    }

    /**
     * Create the main frame. Contains diaries and relevant buttons
     * @return the content pane created by this method
     */
    public Container createContentPane() 
    {
    	JPanel bigPanel = new JPanel();
    	bigPanel.setLayout(new BoxLayout(bigPanel, BoxLayout.PAGE_AXIS));
       // int numButtons = gridSize * gridSize;
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        
       // Diary[] dArr = manager.getDiaryArray();
        diaryList = new JList<Diary>();

		diaryList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		diaryList.setLayoutOrientation(JList.VERTICAL);
		diaryList.setFont(new Font("Sans Serif", Font.PLAIN, 20));

		diaryList.addMouseListener(new MouseAdapter() {
			// from https://stackoverflow.com/a/4344762
		    public void mouseClicked(MouseEvent mouseE) {
		        JList<Diary> list = (JList<Diary>)mouseE.getSource();
		        if (mouseE.getClickCount() == 2) {
		
		            // Double-click detected
		            int index = list.locationToIndex(mouseE.getPoint());
	            	diaryBeingViewed = diaryList.getModel().getElementAt(index);
	            	viewDiary(diaryBeingViewed);
		            
		        }
		    }
		});
		
		refreshDiaryList();
				
		JScrollPane listScroller = new JScrollPane(diaryList);
		listScroller.setPreferredSize(new Dimension(250, 80));
		panel.add(listScroller);
		
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
		//JLabel spacerText = new JLabel("temporary spacer text asdfdsafgsdfga");
		//spacerText.setFont(new Font("Sans Serif", Font.PLAIN, 28));
		//buttonPanel.add(spacerText);
		
		viewDiaryB = new JButton(VIEW_DIARY);
		viewDiaryB.addActionListener(this);
		addDiaryB = new JButton(ADD_DIARY);
		addDiaryB.addActionListener(this);
		editNameB = new JButton(EDIT_NAME);
		editNameB.addActionListener(this);
		removeDiaryB = new JButton(REMOVE_DIARY);
		removeDiaryB.addActionListener(this);
		findSlotB = new JButton(FIND_SLOT);
		findSlotB.addActionListener(this);
		refreshDiariesB = new JButton(REFRESH_DIARIES);
		refreshDiariesB.addActionListener(this);
		addToAllB = new JButton(ADD_TO_ALL);
		addToAllB.addActionListener(this);
		showTasksB = new JButton(SHOW_TASKS);
		showTasksB.addActionListener(this);
		
		
		buttonPanel.add(viewDiaryB);
		buttonPanel.add(addDiaryB);
		buttonPanel.add(editNameB);
		buttonPanel.add(removeDiaryB);
		buttonPanel.add(findSlotB);
		buttonPanel.add(addToAllB);
		buttonPanel.add(showTasksB);
		//buttonPanel.add(refreshDiariesB);
		
		panel.add(buttonPanel);
		
		bigPanel.add(panel);
		
		JPanel labelPanel = new JPanel();
		labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.LINE_AXIS));
		JLabel label = new JLabel("Hold Ctrl to select multiple diaries.");
		label.setFont(new Font("Sans Serif", Font.PLAIN, 18));
		labelPanel.add(label);
		bigPanel.add(labelPanel);
		
		
        return bigPanel;
    }
    

	/**
	 * Creates and displays a diary frame with all its events.
	 * @param diary the diary to display
	 */
    public void createAndRunDiaryFrame(Diary diary) {
    	JFrame frame = new JFrame("Diary");
    	this.diaryFrame = frame;
    	
    	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    	frame.setContentPane(createDiaryFrame(diary));
    	
    	frame.setSize(1000, 400);
    	frame.setVisible(true);
    }
    

    /**
     * Create a frame for displaying the events of a diary and some related buttons too
     * @param diary	The diary whose events are to be shown
     * @return	the content pane created by this method
     */
    public Container createDiaryFrame(Diary diary) {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        
        this.eventList = new JList<Event>();
		eventList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		eventList.setLayoutOrientation(JList.VERTICAL);
		eventList.setFont(new Font("Sans Serif", Font.PLAIN, 20));
		refreshEventList();
		/*
		DefaultListModel<Event> listmodel = new DefaultListModel<Event>();
		for (Event e: diary.getEvents()) {
			listmodel.addElement(e);
		}
		eventList.setModel(listmodel);
		*/
				
		JScrollPane listScroller = new JScrollPane(eventList);
		listScroller.setPreferredSize(new Dimension(250, 80));
		panel.add(listScroller);
		
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
		JLabel nameText = new JLabel(diary.getName());
		nameText.setFont(new Font("Sans Serif", Font.PLAIN, 20));
		buttonPanel.add(nameText);
		
		addEventB = new JButton(ADD_EVENT);
		addEventB.addActionListener(this);
		editEventB = new JButton(EDIT_EVENT);
		editEventB.addActionListener(this);
		removeEventB = new JButton(REMOVE_EVENT);
		removeEventB.addActionListener(this);
		
		buttonPanel.add(addEventB);
		buttonPanel.add(editEventB);
		buttonPanel.add(removeEventB);
		panel.add(buttonPanel);
		
        return panel;
    }
    
    /**
     * create and show the to-do frame
     */
    public void createAndRunToDoFrame() {
    	JFrame frame = new JFrame("To-Do List");
    	this.taskFrame = frame;
    	
    	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    	frame.setContentPane(createToDoFrame());
    	
    	frame.setSize(800, 400);
    	frame.setVisible(true);
    }
    
    /**
     * Create a frame with the to do list 
     * @return	the content pane created by this method
     */
    public Container createToDoFrame() {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        
        this.taskList = new JList<String>();
		taskList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		taskList.setLayoutOrientation(JList.VERTICAL);
		taskList.setFont(new Font("Sans Serif", Font.PLAIN, 20));
		refreshTaskList();
				
		JScrollPane listScroller = new JScrollPane(taskList);
		listScroller.setPreferredSize(new Dimension(250, 80));
		panel.add(listScroller);
		
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
		JLabel nameText = new JLabel("To Do");
		nameText.setFont(new Font("Sans Serif", Font.PLAIN, 20));
		buttonPanel.add(nameText);
		
		addTaskB = new JButton(ADD_TASK);
		addTaskB.addActionListener(this);
		removeTaskB = new JButton(REMOVE_TASK);
		removeTaskB.addActionListener(this);
		this.closeTasksB = new JButton(CLOSE_TASKS);
		closeTasksB.addActionListener(this);
		
		buttonPanel.add(addTaskB);
		buttonPanel.add(removeTaskB);
		buttonPanel.add(closeTasksB);
		panel.add(buttonPanel);
		
        return panel;
    }
    


    /**
	 * Creates and displays a add task frame.
     */
    public void createAndRunTaskNameFrame() {
    	JFrame frame = new JFrame("new task");
    	this.taskNameFrame = frame;
    	
    	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    	frame.setContentPane(createTaskNameFrame());
    	
    	frame.setSize(600, 280);
    	frame.setVisible(true);
    }
    
    /**
     * Creates a frame for adding a task to the to-do list
     * @return the content pane created by this method.
     */
    public Container createTaskNameFrame() {
    	
        JPanel panel = new JPanel(new BorderLayout());
        
        	JPanel tasknameRow = new JPanel(new BorderLayout());
        	JLabel tasknameLabel = new JLabel("Task:" );
            tasknameLabel.setFont(new Font("Sans Serif", Font.PLAIN, 26));
        	tasknameRow.add(tasknameLabel);
        	
        	taskField = new JTextField(20);
        	tasknameRow.add(taskField, BorderLayout.LINE_END);
        
        panel.add(tasknameRow);
        
        //adding buttons to save or discard changes
        JPanel buttons = new JPanel(new GridLayout(1,2));
        JButton saveButton = new JButton(SAVE_TASK);
        saveButton.addActionListener(this);
        buttons.add(saveButton);
        
        JButton closeButton = new JButton(CANCEL_TASK);
        closeButton.addActionListener(this);
        buttons.add(closeButton);
        
        panel.add(buttons, BorderLayout.PAGE_END);
        return panel;
    }



    /**
	 * Creates and displays a change name frame.
     * @param diary the diary whose name is to be changed
     */
    public void createAndRunNameFrame(Diary diary) {
    	JFrame frame = new JFrame("Employee name");
    	this.nameFrame = frame;
    	
    	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    	frame.setContentPane(createNameFrame(diary));
    	
    	frame.setSize(600, 280);
    	frame.setVisible(true);
    }
    
    /**
     * Creates a pane with text fields to change the name
     * @param diary the diary whose name is to be changed
     * @return the content pane created by this method.
     */
    public Container createNameFrame(Diary diary) {
    	String defaultFirstName = "";
    	String defaultSurname = "";
    	if (diary != null) {
    		defaultFirstName = diary.getFirstname();
    		defaultSurname = diary.getLastname();
    	}
    	
        JPanel panel = new JPanel(new BorderLayout());
        JPanel nameChooser = new JPanel(new GridLayout(0,1));
        
        	JPanel firstnameRow = new JPanel(new BorderLayout());
        	JLabel firstnameLabel = new JLabel("First name:" );
            firstnameLabel.setFont(new Font("Sans Serif", Font.PLAIN, 26));
        	firstnameRow.add(firstnameLabel);
        	
        	firstnameField = new JTextField(20);
        	firstnameField.setText(defaultFirstName);
        	firstnameRow.add(firstnameField, BorderLayout.LINE_END);
        	nameChooser.add(firstnameRow);
        	

        	JPanel surnameRow = new JPanel(new BorderLayout());
        	JLabel surnameLabel = new JLabel("Surname:" );
            surnameLabel.setFont(new Font("Sans Serif", Font.PLAIN, 26));
        	surnameRow.add(surnameLabel);
        	
        	surnameField = new JTextField(20);
        	surnameField.setText(defaultSurname);
        	surnameRow.add(surnameField, BorderLayout.LINE_END);
        	nameChooser.add(surnameRow);
        
        panel.add(nameChooser);
        
        //adding buttons to save or discard changes
        JPanel buttons = new JPanel(new GridLayout(1,2));
        JButton saveButton = new JButton(SAVE_NAME);
        saveButton.addActionListener(this);
        buttons.add(saveButton);
        
        JButton closeButton = new JButton(CANCEL_NAME);
        closeButton.addActionListener(this);
        buttons.add(closeButton);
        
        panel.add(buttons, BorderLayout.PAGE_END);
        return panel;
    }
    

	/**
	 * Creates and displays an event editor frame.
	 * @param event the event that is to be edited
     */
    public void createAndRunEventFrame(Event event) {
    	JFrame frame = new JFrame("Event");
    	this.editEventFrame = frame;
    	
    	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    	frame.setContentPane(createEventFrame(event));
    	
    	frame.setSize(500, 780);
    	frame.setVisible(true);
    }
    
    /**
     * Creates a pane with options to create an event
	 * @param event the event that is to be edited. the frame will display values of this event by default.
     * @return the content pane created by this method.
     */
    public Container createEventFrame(Event event) {
    	String defaultName = "";
    	int[] startDateValues = new int[5];
    	int[] endDateValues = new int[5];
    	if (event != null) {
    		defaultName = event.getName();
    		startDateValues = getDefaultSpinnerValues(event.getStartTime());
    		endDateValues = getDefaultSpinnerValues(event.getEndTime());
    	} else {
    		Date now = new Date();
    		startDateValues = getDefaultSpinnerValues(now);
    		endDateValues = getDefaultSpinnerValues(now);
    	}
    	
        JPanel panel = new JPanel(new BorderLayout());
        JPanel options = new JPanel(new GridLayout(0,1));
        
        	JPanel nameRow = new JPanel(new BorderLayout());
        	JLabel nameLabel = new JLabel("Name:" );
            nameLabel.setFont(new Font("Sans Serif", Font.PLAIN, 26));
        	nameRow.add(nameLabel);
        	
        	eventNameField = new JTextField(20);
        	eventNameField.setText(defaultName);
        	nameRow.add(eventNameField, BorderLayout.LINE_END);
        	options.add(nameRow);

        	JPanel startTimeRow = new JPanel(new BorderLayout());
        	JLabel startTimeLabel = new JLabel("Start Time:" );
            startTimeLabel.setFont(new Font("Sans Serif", Font.PLAIN, 26));
        	startTimeRow.add(startTimeLabel);
        	
        	JPanel startPicker = this.createDateTimePicker(startDateValues, true);
        	startPicker.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 10));
        	startTimeRow.add(startPicker, BorderLayout.LINE_END);
        	options.add(startTimeRow);

        	JPanel endTimeRow = new JPanel(new BorderLayout());
        	JLabel endTimeLabel = new JLabel("End Time:" );
            endTimeLabel.setFont(new Font("Sans Serif", Font.PLAIN, 26));
        	endTimeRow.add(endTimeLabel);
        	
        	JPanel endPicker = this.createDateTimePicker(endDateValues, false);
        	endPicker.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 10));
        	endTimeRow.add(endPicker, BorderLayout.LINE_END);
        	options.add(endTimeRow);
        	
        panel.add(options);
        
        //adding buttons to save or discard changes
        JPanel buttons = new JPanel(new GridLayout(1,2));
        JButton saveButton = new JButton(SAVE_EVENT);
        saveButton.addActionListener(this);
        buttons.add(saveButton);
        
        JButton closeButton = new JButton(CANCEL_EVENT);
        closeButton.addActionListener(this);
        buttons.add(closeButton);
        
        panel.add(buttons, BorderLayout.PAGE_END);
        return panel;
    }
    
    /**
     * Get values from a date which can then be used as the default values for spinners
     * @param date	The date from which to retrieve the values
     * @return	a 5-member array of default spinner values, in the order: year, month, day of month, hour of day, minute
     */
    private int[] getDefaultSpinnerValues(Date date) {
    	int[] vals = new int[5];

		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		vals[0] = cal.get(Calendar.YEAR);
		vals[1] = cal.get(Calendar.MONTH) + 1;
		vals[2] = cal.get(Calendar.DAY_OF_MONTH);
		vals[3] = cal.get(Calendar.HOUR_OF_DAY);
		vals[4] = cal.get(Calendar.MINUTE);
		return vals;
    }
    
    /**
     * Creates a very simple and fairly wrong date/time picker
     * @param defaultValues	The default values of this date/time picker
     * @param isStart	if the date/time picker is for a starttime. this is probably bad coding practice but it works.
     * @return	the JPanel with the simple date/time picker
     */
    private JPanel createDateTimePicker(int[] defaultValues, boolean isStart) { 
    	JPanel panel = new JPanel(new GridLayout(0,1));
        
    	String[] labels = {"Year", "Month", "Day", "Hours", "Minutes"};
    	int[] minValues = {1900, 1, 1, 0, 0};
    	int[] maxValues = {3000, 12, 31, 23, 59};
    	if (defaultValues == null || defaultValues.length ==0) {
    		//int[] defVal = {2018, 1, 1, 0,0};
    		Date now = new Date();
    		Calendar nowCal = new GregorianCalendar();
    		nowCal.setTime(now);
    		defaultValues[0] = nowCal.get(Calendar.YEAR);
    		defaultValues[1] = nowCal.get(Calendar.MONTH) +1;
    		defaultValues[2] = nowCal.get(Calendar.DAY_OF_MONTH);
    		defaultValues[3] = nowCal.get(Calendar.HOUR_OF_DAY);
    		defaultValues[4] = nowCal.get(Calendar.MINUTE);
    	}
        JSpinner[] spinners = new JSpinner[labels.length];
        
        //adding the option rows
        for (int i=0; i<labels.length; i++) {
        	JPanel row = new JPanel(new BorderLayout());
        	JLabel label = new JLabel(labels[i]);
            label.setFont(new Font("Sans Serif", Font.PLAIN, 22));
        	row.add(label);
        	
        	//adding a spinner, default values based on current values, min values 0, max values limited depending on ship length
        	spinners[i] = new JSpinner(new SpinnerNumberModel(defaultValues[i], minValues[i], 
        			maxValues[i], 1));
        	spinners[i].setFont(new Font("Sans Serif", Font.PLAIN, 26));
        	row.add(spinners[i], BorderLayout.LINE_END);
        	panel.add(row);
        }
        if (isStart) {
        	//shouldn't be done like this
        	this.startSpinners = spinners;
        } else {
        	this.endSpinners = spinners;
        }
        return panel;
    }
    
    
    /**
     * This method handles events from the Menu and the board.
     *
     */
    public void actionPerformed(ActionEvent e) 
    {
        String classname = getClassName(e.getSource());
        //JComponent component = (JComponent)(e.getSource());
        //saved = false;
    
        if (classname.equals("JMenuItem"))
        {
            JMenuItem menusource = (JMenuItem)(e.getSource());
            String menutext  = menusource.getText();
            
            // Determine which menu option was chosen
            if (menutext.equals("Undo"))
            {
            	//undo
            	if (manager.undo()) {
            		saved = false;
            	}
            	refreshDiaryList();
            }
            else if (menutext.equals("Redo"))
            {
            	if (manager.redo()) {
            		saved = false;
            	}
            	refreshDiaryList();
            }
            else if (menutext.equals("Save"))
            {
            	if (manager.saveToFile(DEFAULT_FILEPATH)) {
            		saved = true;
            	}
            }
        }
        // Handle the event from the user clicking on a command button
        else if (classname.equals("JButton"))
        {
            JButton button = (JButton)(e.getSource());
            String buttonText = button.getText();
            //setActionCommand and getActionCommand should've been used instead of just the text 
            if (buttonText == VIEW_DIARY) { //using == because it feels safer than equals() and works with constants
            	diaryBeingViewed = diaryList.getSelectedValue();
            	viewDiary(diaryBeingViewed);
            } else if (buttonText == REMOVE_DIARY) {
            	saved = false;
            	this.removeDiaries(diaryList.getSelectedIndices());
            } else if (buttonText == ADD_DIARY) {
            	saved = false;
            	this.editingDiary = null;
            	createAndRunNameFrame(null);
            } else if (buttonText == EDIT_NAME) {
            	saved = false;
            	editingDiary = diaryList.getSelectedValue();
            	if (editingDiary != null) {
            		createAndRunNameFrame(editingDiary);
            	}
            } else if (buttonText == FIND_SLOT) {
            	this.findSlot(diaryList.getSelectedValuesList());
            } else if (buttonText == ADD_TO_ALL) {
            	saved = false;
            	addingToAll = true;
            	editingEvent = null;
            	createAndRunEventFrame(editingEvent);
            } else if (buttonText == REFRESH_DIARIES) {
            	this.refreshDiaryList();
            }            
            else if (buttonText == ADD_EVENT) {
            	saved = false;
            	addingToAll = false;
            	editingEvent = null;
            	this.createAndRunEventFrame(editingEvent);
            	//add event frame
            	
            } else if (buttonText == REMOVE_EVENT) {
            	saved = false;
            	this.removeEvents(this.eventList.getSelectedIndices());
            	this.refreshEventList();
            } else if (buttonText == EDIT_EVENT) {
            	//add event frame with values
            	saved = false;
            	addingToAll = false;
            	this.editingEvent = eventList.getSelectedValue();
            	this.createAndRunEventFrame(editingEvent);
            } else if (buttonText == SAVE_NAME) {
            	saved = false;
            	if (editingDiary == null) {
            		addDiary(this.firstnameField.getText(), this.surnameField.getText());
            	} else {
            		//editing
            		editDiary(editingDiary, firstnameField.getText(), surnameField.getText());
            	}
            	nameFrame.dispose();
            } else if (buttonText == CANCEL_NAME) {
            	nameFrame.dispose();
            }
            else if (buttonText == SAVE_EVENT) {
            	saved = false;
            	this.editEvent();
            	editEventFrame.dispose();
            } else if (buttonText == CANCEL_EVENT) {
            	editEventFrame.dispose();
            }
            else if (buttonText == SHOW_TASKS) {
            	this.createAndRunToDoFrame();
            } else if (buttonText == ADD_TASK) {
            	this.createAndRunTaskNameFrame();
            } else if (buttonText == REMOVE_TASK) {
            	saved = false;
            	manager.removeFromTaskStack(this.taskList.getSelectedValue());
            	refreshTaskList();
            } else if (buttonText == SAVE_TASK) {
            	saved = false;
            	manager.addToTaskStack(taskField.getText());   
            	refreshTaskList();
            	this.taskNameFrame.dispose();
            } else if (buttonText == CANCEL_TASK) {
            	this.taskNameFrame.dispose();
            } else if (buttonText == CLOSE_TASKS) {
            	this.taskFrame.dispose();
            }
        }  
    }

	/**
     *  Returns the class name
     *  @return the class name
     */
    protected String getClassName(Object o) 
    {
        String classString = o.getClass().getName();
        int dotIndex = classString.lastIndexOf(".");
        return classString.substring(dotIndex+1);
    }
    /**
     * Remove diaries from the diary tree/list at the selected diarylist indices
     * @param indices	the diarylist indices from which the diaries are to be removed
     */
    public void removeDiaries(int[] indices) {
    	if (indices.length != 0){
	    		
			int n = JOptionPane.showConfirmDialog(mainFrame, "Are you sure you want to remove the selected diaries?", 
					"Confirm removal", JOptionPane.YES_NO_CANCEL_OPTION);
			System.out.println(n);
			if (n==JOptionPane.YES_OPTION) {
				for (int i = indices.length-1; i>=0; i--) {
					DefaultListModel<Diary> model = ((DefaultListModel<Diary>)this.diaryList.getModel());
					Diary diary = model.getElementAt(indices[i]);
					model.remove(indices[i]);
					manager.removeDiary(diary);
				}
			}
    	}
    }
    
    /**
     * Remove events from a diary at the selected eventlist indices
     * @param indices	the indices of the eventList from which the events are to be removed
     */
    public void removeEvents(int[] indices) {

    	if (indices.length != 0){
	    		
			int n = JOptionPane.showConfirmDialog(mainFrame, "Are you sure you want to remove the selected events?", 
					"Confirm removal", JOptionPane.YES_NO_CANCEL_OPTION);
			System.out.println(n);
			if (n==JOptionPane.YES_OPTION) {
				for (int i = indices.length-1; i>=0; i--) {
					DefaultListModel<Event> model = ((DefaultListModel<Event>)this.eventList.getModel());
					Event event = model.getElementAt(indices[i]);
					model.remove(indices[i]);
					manager.removeEvent(diaryBeingViewed, event);
				}
			}
    	}
    }
    
    /**
     * Show the diary frame for the chosen diary
     * @param diary the diary to view
     */
    public void viewDiary(Diary diary) {
    	this.diaryBeingViewed = diary;
    	if (diary != null) {
    		createAndRunDiaryFrame(diary);
    	}
    }
    
    /**
     * Add a diary to the tree/list
     * @param firstname	the firstname of the new diary
     * @param lastname	the lastname of the new diary
     * @return	true if added successfully
     */
    public boolean addDiary(String firstname, String lastname) {
    	Diary toAdd = new Diary(firstname, lastname);
    	if (manager.addDiary(toAdd)) {
			//DefaultListModel<Diary> model = ((DefaultListModel<Diary>)this.diaryList.getModel());
			//model.addElement(toAdd);
			refreshDiaryList();
    		return true;
    	} else return false;
    }
    
    /**
     * Edit a diary's name
     * @param diary	The diary to edit
     * @param firstname	the new firstname
     * @param lastname	the new lastname
     * @return	true if edited successfully
     */
    public boolean editDiary(Diary diary, String firstname, String lastname) {
    	boolean toReturn = manager.editDiaryName(diary, firstname, lastname);
    	this.refreshDiaryList();
    	return toReturn;
    }
    
    /**
     * Add every diary in the tree to the listmodel of the diaryList
     */
    public void refreshDiaryList() {

		DefaultListModel<Diary> listmodel = new DefaultListModel<Diary>();
		for (DiaryList dl: manager.getdTree()) {
			for (Diary d: dl) {
				listmodel.addElement(d);
			}
		}
		diaryList.setModel(listmodel);
    }
    
    /**
     * add every event in the diary to the listmodel of the eventList
     */
    public void refreshEventList() {

		DefaultListModel<Event> listmodel = new DefaultListModel<Event>();
		for (Event e: diaryBeingViewed.getEvents()) {
			listmodel.addElement(e);
		}
		eventList.setModel(listmodel);
    }
    
    /**
     * refresh the to-do list in the listmodel of the taskList
     */
    public void refreshTaskList() {
    	DefaultListModel<String> listmodel = new DefaultListModel<String>();
    	List<String> tasks = manager.getTaskStack(); //was supposed to add them to the top of the list but no time left to fix this
    	if (tasks != null) {
	    		
	    	for (String task: tasks) {
	    		listmodel.addElement(task);
	    	}
    	}
    	taskList.setModel(listmodel);
    }
    
    /**
     * Find meeting slots for the selected diaries and show them
     * @param diaries the list of diaries for which to find the meeting slots
     */
    public void findSlot(List<Diary> diaries) {
    	Date now1 = new Date();
    	this.diaryBeingViewed = manager.findMeetingSlot(diaries);
    	Date now2 = new Date();
    	long timeDifference = now2.getTime()-now1.getTime();
    	
    	createAndRunDiaryFrame(diaryBeingViewed);
    	JOptionPane.showMessageDialog(diaryFrame,
    		    "Search took "+ timeDifference +" milisecond(s).",
    		    "search",
    		    JOptionPane.PLAIN_MESSAGE);
    }
    
    /**
     * Edit an event or if a <code>editingEvent</code> doesn't exist, add it.
     * If <code>addingToAll</code> is set to true, add this event to all selected diaries
     */
    public void editEvent() {
    	if (addingToAll) {
        	if (manager.addEventToAll(diaryList.getSelectedValuesList(), readEventFromFrame())) {

            	JOptionPane.showMessageDialog(mainFrame, "Added successfully.",
            		    "add event to all",
            		    JOptionPane.PLAIN_MESSAGE);
        	} else {

            	JOptionPane.showMessageDialog(mainFrame, "Couldn't add all.",
            		    "add event to all",
            		    JOptionPane.PLAIN_MESSAGE);
        	}
    	}else {
	    	Event newEvent = readEventFromFrame();
	    	if (this.editingEvent == null) {
	    		//add
	    		manager.addEvent(diaryBeingViewed, newEvent);
	    	} else {
	    		//edit
	    		manager.editEvent(diaryBeingViewed, editingEvent, newEvent);
	    	}
	    	this.refreshEventList();
    	}
    }

    /**
     * Get an event from the data that is in the fields of the event frame
     * @return the event with the data from the event frame fields
     */
	private Event readEventFromFrame() {
		String name = this.eventNameField.getText();
		Integer startYear = (Integer) this.startSpinners[0].getValue();
		Integer startMonth = (Integer) this.startSpinners[1].getValue() -1;
		Integer startDay = (Integer) this.startSpinners[2].getValue();
		Integer startHour = (Integer) this.startSpinners[3].getValue();
		Integer startMinute = (Integer) this.startSpinners[4].getValue();
		Calendar startCal = new GregorianCalendar(startYear, startMonth, startDay, startHour, startMinute);
		

		Integer endYear = (Integer) this.endSpinners[0].getValue();
		Integer endMonth = (Integer) this.endSpinners[1].getValue() -1;
		Integer endDay = (Integer) this.endSpinners[2].getValue();
		Integer endHour = (Integer) this.endSpinners[3].getValue();
		Integer endMinute = (Integer) this.endSpinners[4].getValue();
		Calendar endCal = new GregorianCalendar(endYear, endMonth, endDay, endHour, endMinute);
		
		return new Event(startCal.getTime(), endCal.getTime(), name);		
	}
}
