/*
ID: andrew.83
LANG: JAVA
TASK: milk
*/

import java.io.*;
import java.util.*;

public class milk {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
			milk m = new milk();
			PrintWriter out = new PrintWriter("milk.out");
			out.println(m.work("milk.in"));
			out.close();
			//System.out.println(m.work("milk.in"));
		}catch(Exception e) {
			System.out.println(e.toString());
		}
		
	}

	public int work(String filename) throws Exception{
		int rt = 0;
		
		BufferedReader in = new BufferedReader(new FileReader(filename));
		String firstLine = in.readLine();
		StringTokenizer s0 = new StringTokenizer(firstLine);
		//int daily total
		int total = Integer.parseInt(s0.nextToken());
		//int number of farmers
		int numFarmers = Integer.parseInt(s0.nextToken());
		//storage of farmer's data
		Map<Integer, Integer> farmer = new HashMap<Integer, Integer>();
		//storage of prices for sorting
		HashSet<Integer> prices = new HashSet<Integer>();
		//take in farmer data
		for(int i = 0; i < numFarmers; i++) {
			String tempLine = in.readLine();
			StringTokenizer st = new StringTokenizer(tempLine);
			//price
			int price = Integer.parseInt(st.nextToken());
			//farmer's total availability
			int amount = Integer.parseInt(st.nextToken());
			//duplicate prices (multiple amounts for one price)
			if(farmer.containsKey(price)) {
				amount += farmer.get(price);
				farmer.put(price, amount);
			}else {
				farmer.put(price, amount);
			}
			
			//sorting
			prices.add(price);
			/*int index = 0;
			if(prices.isEmpty()) {
				prices.add(price);
			}else {
				if(!prices.contains(price)) {
					while(index < prices.size() && prices.get(index) < price) {
						index++;
					}
					prices.add(index, price);
				}
			}
			*/
		}
		
		Iterator<Integer> i = prices.iterator();
		while(total > 0 && prices.size() > 0) {
			int tempPrice = i.next();
			int tempAmount = farmer.get(tempPrice);
			if(total < tempAmount) {
				tempAmount = total;
			}
			total -= tempAmount;
			rt += (tempAmount * tempPrice);
		}
		
		
		return rt;
	}
	
}
