package Sim;

import java.util.Comparator;

public class Customer implements Comparable<Customer>{

	private Double arrival;
	private Integer items;
	private Double shopping;
	private Double endShopping;
	private Double checkout; //time it takes for the checkout
	private Lane lane;
	
	public Customer(double arrival, int items, double shopping) {
		this.arrival = arrival;
		this.items = items;
		this.shopping = shopping;
		this.endShopping = arrival + (items * shopping);
	}

	public double getArrival() {
		return arrival;
	}
	
	public int getItems() {
		return items;
	}
	
	public double getShopping() {
		return shopping;
	}
	
	public Double getEndShopping() {
		return endShopping;
	}
	
	public Double getCheckout() {
		return this.checkout;
	}
	
	public void setCheckout(Double check) {
		this.checkout = check;
	}
	
	
	public Lane getLane() {
		return lane;
	}
	public void setLane(Lane ln) {
		this.lane = ln;
	}
	
	public String toString() {
		return "Arrives at " + arrival + " mins, picking "  +  items + " items, at average speed of " + shopping + " mins per item";
	}

	@Override
	public int compareTo(Customer other) {
		return this.arrival.compareTo(other.arrival);
	}

	
	public static Comparator<Customer> lanePick = new Comparator<Customer>() {
		@Override
		public int compare(Customer here, Customer other) {
			return here.endShopping.compareTo(other.endShopping);
		}
	};
	
	
}
