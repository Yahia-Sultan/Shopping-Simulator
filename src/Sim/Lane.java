package Sim;

import java.util.ArrayList;
import java.util.List;

public class Lane {
	private String status;
	private int num;
	private static int total = 0;
	private Customer c;
	private Double leftStore;
	private List<Customer> line = new ArrayList<Customer>();
	private Double lineWait = 0.0;
	
	public Lane(String status, int num) {
		this.status = status;
		this.num = num;
		
		if(this.status != "closed") total++;
	}
	
	public void reset() {
		total =0;
	}

	public void addCustomer(Customer cust) {
		this.line.add(cust);
		lineWait += cust.getCheckout();
		cust.setCheckout(lineWait + cust.getEndShopping());
		cust.setLane(this);
	}
	
	public void move() {
		if(line.size() > 0) {
			c = line.get(0);
			leftStore = c.getCheckout();
			line.remove(0);
		}
	}
	
	public int getNumber() {
		return num;
	}

	public int getTotal() {
		return total;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setCustomer(Customer c) {
		this.c = c;
		leftStore = c.getCheckout() + c.getEndShopping();
		c.setLane(this);
	}
	public Double getLeave() {
		return leftStore;
	}
	
	public Customer getCustomer() {
		return c;
	}
	
	
	public int getLine() {
		if(line == null) return 0;
		else return line.size();
	}
	
	public boolean inUse() {
		return (c != null);
	}
	
}
