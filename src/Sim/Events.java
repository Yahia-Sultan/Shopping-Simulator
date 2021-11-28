package Sim;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Events {
	
	private Double eventClock = 0.0;
	
	private ArrayList<Customer> customers = new ArrayList<Customer>();
	private ArrayList<Customer> endShopping = new ArrayList<Customer>();
	private ArrayList<Lane> lanes = new ArrayList<Lane>();
	private int lastShop = 0;
	
	HashMap<Double, String> log = new HashMap<Double, String>();	
	ArrayList<Double> sortedKeys = new ArrayList<Double>();
	
	public Events(ArrayList<Customer> cust, ArrayList<Lane> lane, int round, File prime, int reg, int exp, int cld) throws IOException {
		
		for(Customer c : cust) {
			customers.add(c);
			endShopping.add(c);
		}
		for(Lane ln : lane){
			lanes.add(ln);
		}
		
		Collections.sort(customers); //All customers sorted by the time they arrive
		Collections.sort(endShopping, Customer.lanePick); //All customers sorted by the time they finish shopping and have to decide on the lane they pick

		
			for(Customer cArrival : customers) {
				sortedKeys.add(cArrival.getArrival());
				log.put(cArrival.getArrival(), "Arrival: " + cArrival.getArrival() + " minutes");
			}
			
			
			for(Customer cFinish : endShopping) {
				sortedKeys.add(cFinish.getEndShopping());
				log.put(cFinish.getEndShopping(), 
						"End of shopping trip: " + cFinish.getEndShopping() + " minutes");
				shopLog();
			}
			
			
			int checked = 0;
			while(checked < customers.size()) {
				for(Lane l : lanes) {
					if(l.inUse()) {
						sortedKeys.add(l.getLeave());
						log.put(l.getLeave(), "Checked out at: " + l.getLeave() + " minutes");
						l.move();
						checked++;
					}
				}			
		}
		Collections.sort(sortedKeys);
		eventClock = sortedKeys.get(sortedKeys.size()-1);
		
		FileWriter fw = new FileWriter(prime);
		fw.write(customers.size() + " Customers. " + reg + " Regular lanes, " + exp + " Express lanes, " + cld + 
				" Closed lanes. Total cost of operation: " + (exp+reg) + " units\n");
		for(int i = 0; i < sortedKeys.size(); i++) {
			fw.write(log.get(sortedKeys.get(i)) + "\n");
		}
		fw.write("Finished Log " + round);
		fw.close();
		
		
	}
	
	
	public void shopLog() {
		Lane least = null;
		for(Lane l : lanes) {
			if(l.getStatus() == "closed") continue;	
			if((endShopping.get(lastShop).getItems() > 12) && l.getStatus() == "express") continue;
			if(least == null) {
				least = l;
				continue;
			}
			if((l.getLine() + (l.inUse() ? 1 :0)) < (least.getLine() + (least.inUse() ? 1 :0) ))
				least = l;
			else if((l.getLine() == least.getLine()) && (l.getStatus() == "express") && (least.getStatus() != "express"))
				least = l;
		}
		endShopping.get(lastShop).setCheckout(
				(least.getStatus() == "express" ? 
						((endShopping.get(lastShop).getItems() * 0.1) + 1.0) :  //is express lane 0.1 minutes per item + 1 minute payment
							((endShopping.get(lastShop).getItems() * 0.05) + 2.0))); //not express lane (regular) 0.05 minutes per item + 2 minute payment
		if(least.inUse()) least.addCustomer(endShopping.get(lastShop));
		else least.setCustomer(endShopping.get(lastShop));
		
		lastShop++;
	}
	
	public Double getEventTime() {
		return eventClock;
	}
	
}
