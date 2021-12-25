package presentation;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import business_logic.AeroplofFlightBooker;
import business_logic.FlightBooker;
import domain.ConcreteFlight;

public class FlightBookingGUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel panel;

	private JLabel departureLabel = new JLabel("Departure city:");
	private JTextField departureInput = new JTextField();
	private JLabel arrivalLabel = new JLabel("Arrival city:");
	private JTextField arrivalInput = new JTextField();

	private JLabel yearLabel = new JLabel("Year:");
	private JTextField yearInput = new JTextField();
	private JLabel monthLabel = new JLabel("Month:");
	private JComboBox<String> monthCombo;
	private JLabel dayLabel = new JLabel("Day:");
	private JTextField dayInput = new JTextField();

	private JLabel fareLabel = new JLabel("Fare:");
	private JRadioButton firstRB = new JRadioButton("First class", true); 
	private JRadioButton businessRB = new JRadioButton("Business class", false);
	private JRadioButton economyRB = new JRadioButton("Economy class", false);
	private ButtonGroup fareButtonGroup = new ButtonGroup();

	private JButton searchConFlightsButton = new JButton("Search matching flights");
	private JLabel searchResultAnswer = new JLabel("", 0);

	private DefaultListModel<ConcreteFlight> conFlightInfo = 
			new DefaultListModel<ConcreteFlight>();	
	private JList<ConcreteFlight> conFlightList = 
			new JList<ConcreteFlight>(conFlightInfo);
	private JScrollPane conFlightListScrollPane = new JScrollPane();

	private JButton bookSelectedConFlightButton = new JButton("");

	private FlightBooker businessLogic;	
	private ConcreteFlight selectedConFlight;

	/**
	 * setupInputComponents method (1st block of standard constructor)
	 * 
	 * It configures and adds to the GUI's panel all elements needed to
	 * capture the input options of the user (flight route, date and fare)
	 */
	private void setupInputComponents() {
		
		String [] monthNames = {"January", "February", "March","April", "May",
				"June", "July", "August", "September", "October", "November",
				"December"};
		DefaultComboBoxModel<String> cbContent = 
				new DefaultComboBoxModel<String>(monthNames);
		this.monthCombo = new JComboBox<String>(cbContent);

		this.fareButtonGroup.add(firstRB);
		this.fareButtonGroup.add(businessRB);
		this.fareButtonGroup.add(economyRB);
	}

	/**
	 * setupSearchFlight method (2nd block of standard constructor)
	 * 
	 * It configures, provides behavior and adds to the  GUI's panel a) the
	 * button that starts the search of the concrete flights that match the 
	 * user's criteria, and b) the label "searchResultAnswer" that publishes 
	 * the answers of the system to the user within each search.
	 * 
	 * The button sends a request to the business logic to fetch a list of 
	 * concrete flights, which then is loaded into "conFlightInfo", the content
	 * of the JList "conFlightList" used to display the flights and allow 
	 * their selection by the user.
	 */
	private void setupSearchFlightsButton() {

		this.searchConFlightsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				conFlightInfo.clear();

				String chosenDateString = monthCombo.getSelectedItem() + " " + 
						dayInput.getText() + " " + yearInput.getText();
				SimpleDateFormat format = new SimpleDateFormat("MMMM' 'd' 'yyyy", 
						Locale.ENGLISH);
				format.setLenient(false);

				try {
					Date chosenDate = format.parse(chosenDateString);
					List<ConcreteFlight> foundConFlights = businessLogic.
							getMatchingConFlights(departureInput.getText(), 
									arrivalInput.getText(), chosenDate);	
					for (ConcreteFlight v : foundConFlights)
						conFlightInfo.addElement(v); 
					if (foundConFlights.isEmpty())
						searchResultAnswer.setText("No matching flights found. " +
								"Please change your options");
					else
						searchResultAnswer.setText("Choose an available flight" +
								" in the following list:");
				}
				catch(ParseException pe) {
					searchResultAnswer.setText("The chosen date " + chosenDateString + 
							" is not valid. Please correct it");
				}
			}
		});
	}

	/**
	 * setupConflightList method (3rd block of standard constructor)
	 * 
	 * It configures, provides behavior (including scrollability) and adds to 
	 * the GUI's panel the "conFlightList" JList that displays the found 
	 * matching concrete flights and allow their selection by the user.
	 * 
	 * When the user selects a flight the "bookSelectedConFlightButton" is
	 * enabled and displays an invitation to book it
	 */
	private void setupConFlightList() {	

		this.conFlightList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) return;
				if (!conFlightList.isSelectionEmpty()) { 					
					selectedConFlight = conFlightList.getSelectedValue();
					bookSelectedConFlightButton.setEnabled(true);
					bookSelectedConFlightButton.setText("Book a ticket in selected "
							+ "flight");
				}
			}
		});
		this.conFlightListScrollPane.setViewportView(conFlightList);
	}

	/**
	 * setupBookSelectedConFlightButton method (4th block of standard constructor)
	 * 
	 * It configures, provides behavior and adds to the  GUI's panel the
	 * button that books the concrete flight selected by the user. Normally
	 * disabled, excepting when the user's choice takes place.
	 */
	private void setupBookSelectedConFlightButton() {

		this.bookSelectedConFlightButton.setEnabled(false);
		this.bookSelectedConFlightButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int remaining = 0;
				if (firstRB.isSelected()) { 
					remaining = businessLogic.bookSeat(selectedConFlight, "First"); 
				}
				else if (businessRB.isSelected()) {
					remaining = businessLogic.bookSeat(selectedConFlight, "Business");
				}
				else if (economyRB.isSelected()) {
					remaining = businessLogic.bookSeat(selectedConFlight, "Economy");
				}
				if (remaining < 0) 
					bookSelectedConFlightButton.setText("Error: This flight had no "
							+ "ticket for the requested fare!");
				else 
					bookSelectedConFlightButton.
						setText("Your ticket has been booked. Remaining tickets = " + 
								remaining);
				bookSelectedConFlightButton.setEnabled(false);
			}
		});
	}
	
	private void layoutComponents() {
		
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] {20, 50, 80, 55, 145, 45, 35, 30, 50, 20};
		gbl_panel.rowHeights = new int[] {20, 35, 35, 35, 35, 35, 35, 120, 35, 20};
		gbl_panel.columnWeights = new double[]{1.0, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 
				0.5, 0.5, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{1.0, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5,
				0.5, 1.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		GridBagConstraints gbc_departureLabel = new GridBagConstraints();
		gbc_departureLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_departureLabel.insets = new Insets(0, 0, 5, 5);
		gbc_departureLabel.gridwidth = 2;
		gbc_departureLabel.gridx = 1;
		gbc_departureLabel.gridy = 1;
		panel.add(departureLabel, gbc_departureLabel);
		
		GridBagConstraints gbc_departureInput = new GridBagConstraints();
		gbc_departureInput.fill = GridBagConstraints.HORIZONTAL;
		gbc_departureInput.insets = new Insets(0, 0, 5, 0);
		gbc_departureInput.gridwidth = 6;
		gbc_departureInput.gridx = 3;
		gbc_departureInput.gridy = 1;
		panel.add(departureInput, gbc_departureInput);
		
		GridBagConstraints gbc_arrivalLabel = new GridBagConstraints();
		gbc_arrivalLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_arrivalLabel.insets = new Insets(0, 0, 5, 5);
		gbc_arrivalLabel.gridwidth = 2;
		gbc_arrivalLabel.gridx = 1;
		gbc_arrivalLabel.gridy = 2;
		panel.add(arrivalLabel, gbc_arrivalLabel);
		
		GridBagConstraints gbc_arrivalInput = new GridBagConstraints();
		gbc_arrivalInput.fill = GridBagConstraints.HORIZONTAL;
		gbc_arrivalInput.insets = new Insets(0, 0, 5, 0);
		gbc_arrivalInput.gridwidth = 6;
		gbc_arrivalInput.gridx = 3;
		gbc_arrivalInput.gridy = 2;
		panel.add(arrivalInput, gbc_arrivalInput);
		
		GridBagConstraints gbc_yearLabel = new GridBagConstraints();
		gbc_yearLabel.anchor = GridBagConstraints.WEST;
		gbc_yearLabel.insets = new Insets(0, 0, 5, 5);
		gbc_yearLabel.gridx = 1;
		gbc_yearLabel.gridy = 3;
		panel.add(yearLabel, gbc_yearLabel);
		
		GridBagConstraints gbc_yearInput = new GridBagConstraints();
		gbc_yearInput.fill = GridBagConstraints.HORIZONTAL;
		gbc_yearInput.insets = new Insets(0, 0, 5, 5);
		gbc_yearInput.gridx = 2;
		gbc_yearInput.gridy = 3;
		panel.add(yearInput, gbc_yearInput);
		
		GridBagConstraints gbc_monthLabel = new GridBagConstraints();
		gbc_monthLabel.anchor = GridBagConstraints.EAST;
		gbc_monthLabel.insets = new Insets(0, 0, 5, 5);
		gbc_monthLabel.gridx = 3;
		gbc_monthLabel.gridy = 3;
		panel.add(monthLabel, gbc_monthLabel);
		
		GridBagConstraints gbc_monthCombo = new GridBagConstraints();
		gbc_monthCombo.fill = GridBagConstraints.HORIZONTAL;
		gbc_monthCombo.insets = new Insets(0, 0, 5, 5);
		gbc_monthCombo.gridwidth = 3;
		gbc_monthCombo.gridx = 4;
		gbc_monthCombo.gridy = 3;
		panel.add(monthCombo, gbc_monthCombo);
		
		GridBagConstraints gbc_dayLabel = new GridBagConstraints();
		gbc_dayLabel.anchor = GridBagConstraints.WEST;
		gbc_dayLabel.insets = new Insets(0, 0, 5, 5);
		gbc_dayLabel.gridx = 7;
		gbc_dayLabel.gridy = 3;
		panel.add(dayLabel, gbc_dayLabel);
		
		GridBagConstraints gbc_dayInput = new GridBagConstraints();
		gbc_dayInput.fill = GridBagConstraints.HORIZONTAL;
		gbc_dayInput.insets = new Insets(0, 0, 5, 0);
		gbc_dayInput.gridx = 8;
		gbc_dayInput.gridy = 3;
		panel.add(dayInput, gbc_dayInput);
		
		GridBagConstraints gbc_fareLabel = new GridBagConstraints();
		gbc_fareLabel.anchor = GridBagConstraints.WEST;
		gbc_fareLabel.insets = new Insets(0, 0, 5, 5);
		gbc_fareLabel.gridx = 1;
		gbc_fareLabel.gridy = 4;
		panel.add(fareLabel, gbc_fareLabel);
		
		GridBagConstraints gbc_firstRB = new GridBagConstraints();
		gbc_firstRB.anchor = GridBagConstraints.EAST;
		gbc_firstRB.insets = new Insets(0, 0, 5, 5);
		gbc_firstRB.gridwidth = 2;
		gbc_firstRB.gridx = 2;
		gbc_firstRB.gridy = 4;
		panel.add(firstRB, gbc_firstRB);
		
		GridBagConstraints gbc_businessRB = new GridBagConstraints();
		gbc_businessRB.anchor = GridBagConstraints.EAST;
		gbc_businessRB.insets = new Insets(0, 0, 5, 5);
		gbc_businessRB.gridx = 4;
		gbc_businessRB.gridy = 4;
		panel.add(businessRB, gbc_businessRB);
		
		GridBagConstraints gbc_economyRB = new GridBagConstraints();
		gbc_economyRB.anchor = GridBagConstraints.WEST;
		gbc_economyRB.insets = new Insets(0, 0, 5, 0);
		gbc_economyRB.gridwidth = 3;
		gbc_economyRB.gridx = 6;
		gbc_economyRB.gridy = 4;
		panel.add(economyRB, gbc_economyRB);
		
		GridBagConstraints gbc_searchConFlightsButton = new GridBagConstraints();
		gbc_searchConFlightsButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_searchConFlightsButton.insets = new Insets(0, 0, 5, 5);
		gbc_searchConFlightsButton.gridwidth = 6;
		gbc_searchConFlightsButton.gridx = 2;
		gbc_searchConFlightsButton.gridy = 5;
		panel.add(searchConFlightsButton, gbc_searchConFlightsButton);
		
		GridBagConstraints gbc_searchResultAnswer = new GridBagConstraints();
		gbc_searchResultAnswer.fill = GridBagConstraints.BOTH;
		gbc_searchResultAnswer.insets = new Insets(0, 0, 5, 0);
		gbc_searchResultAnswer.gridwidth = 6;
		gbc_searchResultAnswer.gridx = 2;
		gbc_searchResultAnswer.gridy = 6;
		panel.add(searchResultAnswer, gbc_searchResultAnswer);
		
		GridBagConstraints gbc_conFlightListScrollPane = new GridBagConstraints();
		gbc_conFlightListScrollPane.fill = GridBagConstraints.BOTH;
		gbc_conFlightListScrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_conFlightListScrollPane.gridwidth = 8;
		gbc_conFlightListScrollPane.gridx = 1;
		gbc_conFlightListScrollPane.gridy = 7;
		panel.add(conFlightListScrollPane, gbc_conFlightListScrollPane);
		
		GridBagConstraints gbc_bookSelectedConFlightButton = new GridBagConstraints();
		gbc_bookSelectedConFlightButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_bookSelectedConFlightButton.gridwidth = 6;
		gbc_bookSelectedConFlightButton.gridx = 2;
		gbc_bookSelectedConFlightButton.gridy = 8;
		panel.add(bookSelectedConFlightButton, gbc_bookSelectedConFlightButton);
	}

	/**
	 * FlightBookingGUI
	 * 
	 * Default constructor of the GUI designed to implement the use case 
	 * "Select Flight"
	 */
	public FlightBookingGUI() {
		super("Book flights");
		setTitle("Aeroplof Booking Center");
		this.panel = new JPanel();
		setContentPane(panel);
		this.setupInputComponents();
		this.setupSearchFlightsButton();
		this.setupConFlightList();
		this.setupBookSelectedConFlightButton();
		this.layoutComponents();
		this.setSize(525, 360);
	}

	/**
	 * Method setBusinessLogic
	 * 
	 * @param g      the business logic controller 
	 *               (it must implement the interface FlightBooker)
	 */
	public void setBusinessLogic(FlightBooker g) {
		businessLogic = g;
	}

	public static void main(String[] args) {
		FlightBookingGUI frame = new FlightBookingGUI();
		frame.setBusinessLogic(new AeroplofFlightBooker());
		frame.setVisible(true);
		frame.pack();
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
}