package Sim;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

	
	public static void main(String[] args) throws IOException {
		ArrayList<Customer> customers = new ArrayList<Customer>();
		Scanner fr = new Scanner(new File("arrival.txt"));
		while(fr.hasNext()) {
			String[] info = fr.nextLine().trim().split("\t+");
			double arrival = Double.parseDouble(info[0]);
			int items = Integer.parseInt(info[1]);
			double shop = Double.parseDouble(info[2]);
			customers.add(new Customer(arrival, items, shop));
		}fr.close();
		
		
		
		int reg = 12, express = 0, closed =0, round = 1;
		
		Double[] fastest = new Double[12];
		HashMap<Double, String> bestLog = new HashMap<Double, String>();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH_mm_ss MM_dd_yyyy");
		String formatted = dtf.format(LocalDateTime.now());
		while(closed != 12) {
			ArrayList<Lane> lanes = new ArrayList<Lane>();
			for(int i = 0; i < reg; i++)
				lanes.add(new Lane("regular", lanes.size()));
			for(int j = 0; j < express; j++)
				lanes.add(new Lane("express", lanes.size()));
			for(int k = 0; k < closed; k++)
				lanes.add(new Lane("closed", lanes.size()));
			

			File primary = new File("Logs/" + formatted +"/Log_"+round+".txt");
			primary.getParentFile().mkdirs();
			try {
				primary.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			Events curr = new Events(customers, lanes, round, primary, reg,express,closed);
			lanes.get(0).reset();
			
			if(fastest[closed] == null) { 
				fastest[closed] = curr.getEventTime();
				bestLog.put((double) closed, "info: Log#" + round +", " + reg + " regular, " + express + " express, " + closed +" closed.");
			}
			else if(curr.getEventTime() < fastest[closed]) {
				fastest[closed] = curr.getEventTime();
				bestLog.put((double) closed, "info: Log#" + round +", " + reg + " regular, " + express + " express, " + closed +" closed.");
			}
			
			
			
			reg--;
			express++;
			if(reg == 0) {
				closed++;
				reg = 12-closed;
				express = 0;
			}
			round++;
		}
		for(int i = 0; i < 12; i++) {
			System.out.println(fastest[i] + " minutes");
			System.out.println(bestLog.get((double) i));
		}
		
	}
	
}
