package domain;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ConcreteFlight
 * 
 *  An object of this class represents an actual scheduled flight for an air
 *  route of the class 'Flight'
 */
public class ConcreteFlight {

	private String cfCode;
	private Flight flight;
	private Date date;	
	private String departureTime; // Departure hours are just strings: "13:28" 
	private int freeFirstSeats;
	private int freeBusinessSeats;
	private int freeEconomySeats;	
	
	public ConcreteFlight(String code, Date date, int firstSeats, int businessSeats,
			int economySeats, String time, Flight flight) {
		this.cfCode = code;
		this.date = date;
		this.departureTime = time;		
		this.flight = flight;
		
		this.freeFirstSeats = firstSeats;
		this.freeBusinessSeats = businessSeats;
		this.freeEconomySeats = economySeats;
		
		flight.addConcreteFlight(this); //'flight -- concrete flight' relation must be kept two-way
	}
	

	public String getCfCode() {
		return cfCode;
	}
	

	public void setCfCode(String code) {
		this.cfCode = code;
	}
	

	public Date getDate() {
		return date;
	}
	
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	
	public String getDepartureTime() {
		return departureTime;
	}
	
	
	public void setDepartureTime(String time) {
		departureTime = time;
	}
	
	
	public int getFreeFirstSeatNo() {
		return freeFirstSeats;
	}
	
	
	public void setFreeFirstSeatNo(int seatNo) {
		freeFirstSeats = seatNo;
	}
	
	
	public int getFreeBusinessSeatNo() {
		return freeBusinessSeats;
	}
	
	
	public void setFreeBusinessSeatNo(int seatNo) {
		freeBusinessSeats = seatNo;
	}
	
	
	public int getFreeEconomySeatNo() {
		return freeEconomySeats;
	}
	
	
	public void setFreeEconomySeatNo(int seatNo) {
		freeEconomySeats = seatNo;
	}
	
	
	/**
	 * Reduces the number of free seats in the concrete flight for the indicated 
	 * fare
	 * 
	 * @param fare		for which a seat has just been booked
	 * @return			an integer value with the remaining seats after allocation,
	 * 					or -1 if allocation was not successful
	 */
	public int allocateSeat(String fare, int numSeats) {
		if (fare.equals("First"))
			if (freeFirstSeats >= numSeats) {
				freeFirstSeats -= numSeats;
				return freeFirstSeats;
			}
			else return -1;
		else if (fare.equals("Business"))
			if (freeBusinessSeats >= numSeats) {
				freeBusinessSeats -= numSeats;
				return freeBusinessSeats;
			}
			else return -1;
		else if (fare.equals("Economy"))
			if (freeEconomySeats >= numSeats) {
				freeEconomySeats -= numSeats;
				return freeEconomySeats;
			}
			else return -1;
		return -1;
	}
	
	
	public String toString() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("EEE', 'd' 'MMM' 'yyyy");
		return flight.toString() + " | " + dateFormat.format(date) + " [" + departureTime + "]";
		}

}
