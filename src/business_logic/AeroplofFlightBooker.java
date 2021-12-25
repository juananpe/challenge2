package business_logic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import domain.ConcreteFlight;
import domain.Flight;

/**
 * AeroplofFlightBooker
 * 
 * The class that implements the business logic controller for the "Select Flight"
 * use case in the project "Aeroplof". It provides domain related information to
 * the GUI's of the use cases that deal with flights
 */
public class AeroplofFlightBooker implements FlightBooker {
	
	List<Flight> availableFlights;

	public AeroplofFlightBooker () {
		super();

		// This section is for testing purposes and it's not truly related to
		// the construction process. A couple of flights are created and
		// included in the list 'availableFlights'. Then associated concrete
		// flights are also created and uploaded in main memory.
		// Please note that they will be accessed from the flights to whom they
		// belong (which are saved in 'availableFlights').
		// 
		// These fictitious flights and concrete flights serve to test if the 
		// use case is correctly implemented prior to establishing persistence.

		availableFlights = new ArrayList<Flight>();
		Flight flight1 = new Flight("EAS-PNA","Donostia","Iruña");
		availableFlights.add(flight1);	
		Flight flight2 = new Flight("EAS-LSK","Donostia","Lesaka");
		availableFlights.add(flight2);	

		SimpleDateFormat format = new SimpleDateFormat("d'-'M'-'yy", Locale.ENGLISH);
		try {
			new ConcreteFlight("PLOF324", format.parse("6-6-22"),1,2,3,"10:00",flight1); 
			new ConcreteFlight("PLOF020", format.parse("7-6-22"),4,3,20,"11:00",flight1);
			new ConcreteFlight("PLOF021", format.parse("7-7-22"),0,0,0,"12:00",flight1);
			new ConcreteFlight("PLOF022", format.parse("7-7-22"),1,3,2,"13:00",flight1);
			new ConcreteFlight("PLOF023", format.parse("7-7-22"),0,3,7,"14:00",flight1);
			new ConcreteFlight("PLOF024", format.parse("7-7-22"),0,0,1,"15:00",flight1);
			new ConcreteFlight("PLOF025", format.parse("7-7-22"),2,4,1,"16:00",flight1);
			new ConcreteFlight("PLOF026", format.parse("7-7-22"),3,3,0,"17:00",flight1);
			new ConcreteFlight("PLOF027", format.parse("7-7-22"),3,5,12,"18:00",flight1);
			new ConcreteFlight("PLOF028", format.parse("7-7-22"),3,3,0,"19:00",flight1);
			new ConcreteFlight("PLOF029", format.parse("7-7-22"),2,4,1,"20:00",flight1);
			new ConcreteFlight("PLOF030", format.parse("7-7-22"),3,6,10,"21:00",flight1);
			new ConcreteFlight("PLOF031", format.parse("7-7-22"),0,3,4,"22:00",flight1);
			new ConcreteFlight("PLOF032", format.parse("7-7-22"),0,2,11,"23:00",flight1);
			new ConcreteFlight("PLOF087", format.parse("6-6-22"),13,0,0,"10:00",flight2);
			new ConcreteFlight("PLOF264", format.parse("7-6-22"),3,6,10,"11:00",flight2);
			new ConcreteFlight("PLOF433", format.parse("7-7-22"),3,3,0,"12:00",flight2);
		}
		catch(ParseException pe) {
			System.out.println("ERROR: At least one of the provided initial " +
					"textual dates is incorrect");
		}	
	}

	
	/**
	 * 'getConFlights' method provides a List of concrete flights matching
	 * some user's requirements
	 * 
	 * @param intendedDepartureCity    introduced by user
	 * @param intendedArrivalCity      introduced by user
	 * @param intendedDate             introduced/selected by user
	 * @return                         List of concrete flights
	 */
	public List<ConcreteFlight> getMatchingConFlights(String intendedDepartureCity, 
			String intendedArrivalCity, Date intendedDate) {

		List<ConcreteFlight> matchingConFlights = new ArrayList<ConcreteFlight>();
		
		for (Flight fli : availableFlights) {
			if (fli.getArrivalCity().equals(intendedArrivalCity) && 
					fli.getDepartureCity().equals(intendedDepartureCity))
				matchingConFlights.addAll(fli.getConcreteFlights(intendedDate));
		}

		return matchingConFlights;

	}

	/**
	 * @param conFli		The concrete flight in which a free seat is to be booked
	 * @param fare			The fare of the ticket
	 * @return				The number of remaining free seats for this fare after 
	 * 						the booking, or -1 if no available seat to book
	 */
	public int bookSeat(ConcreteFlight conFli, String fare) {
		return conFli.allocateSeat(fare, 1);
	}

}