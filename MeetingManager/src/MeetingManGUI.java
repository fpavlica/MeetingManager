import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

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

public class MeetingManGUI implements ActionListener{

	protected static final String DEFAULT_FILEPATH = "defaultsave.ser";

	private Manager manager;
	
    private JFrame mainFrame, diaryFrame, editEventFrame, nameFrame;
    private JButton viewDiaryB, addDiaryB, removeDiaryB, addEventB, removeEventB, editEventB, findSlotB,
    		refreshDiariesB, editNameB, addToAllB;
    public static final String VIEW_DIARY = "View Diary", ADD_DIARY = "Add Diary", REMOVE_DIARY = "Remove Diary", 
    		EDIT_NAME = "Edit Name", FIND_SLOT = "Find a Meeting Slot", ADD_TO_ALL = "Add an event to selected diaries",
    		ADD_EVENT = "Add Event", REMOVE_EVENT = "Remove Event", EDIT_EVENT = "Edit Event", SAVE_NAME = "Save", 
    		CANCEL_NAME = "Cancel", REFRESH_DIARIES = "Refresh (sort)", SAVE_EVENT = "Save ", CANCEL_EVENT = "Cancel ";
    private JList<Diary> diaryList;
    private JList<Event> eventList;
    private JSpinner[] startSpinners, endSpinners;
    private boolean saved, addingToAll; 
	private JTextField firstnameField, surnameField, eventNameField;

	private Diary editingDiary, diaryBeingViewed;
	private Event editingEvent;
    
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
	
	public MeetingManGUI() {
		manager = new Manager();
		//TODO temp:
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
        frame.setContentPane(meetgui.createContentPane()); //TODO probably change this slightly when a show events button
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
    					//save and exit TODO
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
		
		
		buttonPanel.add(viewDiaryB);
		buttonPanel.add(addDiaryB);
		buttonPanel.add(editNameB);
		buttonPanel.add(removeDiaryB);
		buttonPanel.add(findSlotB);
		buttonPanel.add(addToAllB);
		buttonPanel.add(refreshDiariesB);
		
		panel.add(buttonPanel);
		
		bigPanel.add(panel);
		
		JPanel labelPanel = new JPanel();
		labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.LINE_AXIS));
		JLabel label = new JLabel("Hold Ctrl to select multiple diaries.");
		label.setFont(new Font("Sans Serif", Font.PLAIN, 18));
		labelPanel.add(label);
		bigPanel.add(labelPanel);
		
        //JPanel grid = new JPanel(new GridLayout(gridSize,gridSize));
        //buttonArray = new JButton[numButtons];
        
        /*
        for (int i=0; i<numButtons; i++)
        {
            buttonArray[i] = new JButton(" ");
            
            buttonArray[i].setBackground(DEFAULT_COLOR);
			// This label is used to identify which button was clicked in the action listener
            buttonArray[i].setActionCommand("" + i); // String "0", "1" etc.
            buttonArray[i].addActionListener(this);
            grid.add(buttonArray[i]);
        }
        panel.add(grid);
        
        
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.PAGE_AXIS));
        
        //adding the score counter
        scoreIndicator = new JLabel("Shots fired: 0");
        scoreIndicator.setBorder(BorderFactory.createEmptyBorder(10, 20, 0, 180));
        scoreIndicator.setFont(new Font("Sans Serif", Font.PLAIN, 32));
        info.add(scoreIndicator);
        
        
        //adding the hit indicator label
        hitIndicator = new JLabel("");
        hitIndicator.setBorder(BorderFactory.createEmptyBorder(20, 10, 0, 0));
        hitIndicator.setFont(new Font("Sans Serif", Font.PLAIN, 26));
        info.add(hitIndicator);
        
        //adding a label for displaying when a ship has been sunk
        sunkShipIndicator = new JLabel("");
        sunkShipIndicator.setBorder(BorderFactory.createEmptyBorder(20, 10, 0, 0));
        sunkShipIndicator.setFont(new Font("Sans Serif", Font.PLAIN, 26));
        info.add(sunkShipIndicator);
		
        
        panel.add(info);
        */
        return bigPanel;
    }
    

	/*
	 * Creates and displays a leader board frame.
	 * @param players The list of Players on the leader board.
	 */
    public void createAndRunDiaryFrame(Diary diary) {
    	JFrame frame = new JFrame("Diary");
    	this.diaryFrame = frame;
    	
    	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    	frame.setContentPane(createDiaryFrame(diary));//TODO
    	
    	frame.setSize(1000, 400);
    	frame.setVisible(true);
    }
    

    /*
     * Creates the leader board frame content pane
     * @param players The list of names and scores on the leader board.
     * @return The content pane of the leader board.
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
	 * Creates and displays an options frame.
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
     * Creates a pane with options to change the fleet size.
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
	 * Creates and displays an options frame.
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
     * Creates a pane with options to change the fleet size.
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
        saved = false;
    
        if (classname.equals("JMenuItem"))
        {
            JMenuItem menusource = (JMenuItem)(e.getSource());
            String menutext  = menusource.getText();
            
            // Determine which menu option was chosen
            if (menutext.equals("Undo"))
            {
            	//undo
            	manager.undo();
            	refreshDiaryList();
            }
            else if (menutext.equals("Redo"))
            {
            	manager.redo();
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
            if (buttonText == VIEW_DIARY) { //using == because it feels safer than equals() and works with constants
            	diaryBeingViewed = diaryList.getSelectedValue();
            	viewDiary(diaryBeingViewed);
            } else if (buttonText == REMOVE_DIARY) {
            	this.removeDiaries(diaryList.getSelectedIndices());
            } else if (buttonText == ADD_DIARY) {
            	System.out.println(ADD_DIARY);
            	this.editingDiary = null;
            	createAndRunNameFrame(null);
            } else if (buttonText == EDIT_NAME) {
            	editingDiary = diaryList.getSelectedValue();
            	if (editingDiary != null) {
            		createAndRunNameFrame(editingDiary);
            	}
            } else if (buttonText == FIND_SLOT) {
            	this.findSlot(diaryList.getSelectedValuesList());
            } else if (buttonText == ADD_TO_ALL) {
            	addingToAll = true;
            	editingEvent = null;
            	createAndRunEventFrame(editingEvent);
            } else if (buttonText == REFRESH_DIARIES) {
            	this.refreshDiaryList();
            }            
            else if (buttonText == ADD_EVENT) {
            	addingToAll = false;
            	editingEvent = null;
            	this.createAndRunEventFrame(editingEvent);
            	//add event frame
            	
            } else if (buttonText == REMOVE_EVENT) {
            	this.removeEvents(this.eventList.getSelectedIndices());
            	this.refreshEventList();
            } else if (buttonText == EDIT_EVENT) {
            	//add event frame with values
            	addingToAll = false;
            	this.editingEvent = eventList.getSelectedValue();
            	this.createAndRunEventFrame(editingEvent);
            } else if (buttonText == SAVE_NAME) {
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
            	this.editEvent();
            	editEventFrame.dispose();
            } else if (buttonText == CANCEL_EVENT) {
            	editEventFrame.dispose();
            }
        }  
    }

	/**
     *  Returns the class name
     */
    protected String getClassName(Object o) 
    {
        String classString = o.getClass().getName();
        int dotIndex = classString.lastIndexOf(".");
        return classString.substring(dotIndex+1);
    }
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
    public void viewDiary(Diary diary) {
    	if (diary != null) {
    		createAndRunDiaryFrame(diary);
    	}
    }
    
    public boolean addDiary(String firstname, String lastname) {
    	Diary toAdd = new Diary(firstname, lastname);
    	if (manager.addDiary(toAdd)) {
			DefaultListModel<Diary> model = ((DefaultListModel<Diary>)this.diaryList.getModel());
			model.addElement(toAdd);
			refreshDiaryList();
    		return true;
    	} else return false;
    }
    
    
    public boolean editDiary(Diary diary, String firstname, String lastname) {
    	boolean toReturn = manager.editDiaryName(diary, firstname, lastname);
    	this.refreshDiaryList();
    	return toReturn;
    }
    
    public void refreshDiaryList() {

		DefaultListModel<Diary> listmodel = new DefaultListModel<Diary>();
		for (DiaryList dl: manager.getdTree()) {
			for (Diary d: dl) {
				listmodel.addElement(d);
			}
		}
		diaryList.setModel(listmodel);
    }
    
    public void refreshEventList() {

		DefaultListModel<Event> listmodel = new DefaultListModel<Event>();
		for (Event e: diaryBeingViewed.getEvents()) {
			listmodel.addElement(e);
		}
		eventList.setModel(listmodel);
    }
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
