/*
ID: andrew.83
LANG: JAVA
TASK: wormhole
*/

/**
 *  algo used:
 *  1. recursive
 *  2. sort with collection.sort with inline comparator
 *  3. arraylist.toarray
 *  4. array access is the best comparing with hashtable search
 *
 */

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.io.*;

/**
 * @class: represent a point of (x,y)
 */
class Portal{
	
	private int x;
	private int y;
	
	public Portal(int a, int b) {
		x = a;
		y = b;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
}

public class wormhole {

	public static void main(String[] args) {
		
		try {
			Instant start = Instant.now();
			wormhole w = new wormhole();
			PrintWriter out = new PrintWriter("wormhole.out");
			out.println(w.work("wormhole.in"));
			out.close();
			System.out.println(w.work("wormhole.in"));
			Instant end = Instant.now();
			Duration time = Duration.between(start, end);
			System.out.println("time: " + time.toMillis() + "ms");
		}catch(Exception e) {
			System.out.println(e.toString());
		}
		
	}
	
	public int work(String filename)throws Exception {
		int rt = 0;
		
		BufferedReader in = new BufferedReader(new FileReader(filename));
		StringTokenizer s0 = new StringTokenizer(in.readLine());
		int numWormholes = Integer.parseInt(s0.nextToken());
		ArrayList<Portal> portals = new ArrayList<Portal>();
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		for(int i = 0; i < numWormholes; i++) {
			StringTokenizer st = new StringTokenizer(in.readLine());
			int x = Integer.parseInt(st.nextToken());
			int y = Integer.parseInt(st.nextToken());
			portals.add(new Portal(x, y));
			indexes.add(i);
		}

		/**
		 * sort wormArray from least x to greatest x
		 */
		Collections.sort(portals, new Comparator<Portal>() {
					public int compare(Portal obj1, Portal obj2) {
						return obj1.getX() - obj2.getX();
					}
		});
		/**
		 * arraylist.toarray usage
		 */
		Portal wormArray[] = new Portal[portals.size()];
		wormArray = portals.toArray(wormArray);

		//creating an array of right side results for each portal
		//index of array represents same index of wormArray
		//value is the index of the other result: i if there is another portal on greater x and same y, -1 if nothing
		int[] rightSide = new int[wormArray.length];
		for(int i = 0; i < rightSide.length; i++){
			Portal temp = wormArray[i];
			//use -1 because null is not allowed
			rightSide[i] = -1;
			for(int j = i; j < rightSide.length; j++) {
				//conditions: same y and greater x (.compareX() is greater than 0)
				if (wormArray[j].getY() == temp.getY() && wormArray[j].getX() > temp.getX()) {
					rightSide[i] = j;
					j = rightSide.length;
				}
			}
		}

		/**
		 * create all of possible combinations
		 * each entry of arraylist contains arraylist of int (represent portal)
		 * for 4 as numWormholes:
		 * arraylist<arraylist<0,1,2,3>>
		 * arraylist<arraylist<0,2,1,3>>
		 * arraylist<arraylist<0,3,1,2>>
		 */
		ArrayList<ArrayList<Integer>> combos = new ArrayList<ArrayList<Integer>>();
		recursiveCombo(indexes, combos, numWormholes);
		
		//checking conditions and counting the number of combinations with an infinite loop in them
		/**
		 * loop each combination (such as 0,1,2,3)
		 * 	start each point to see if there is infinite loop (2*numWorm means infinite loop)
		 * 		since infinite loop is very rare, so efficiency is no big deal here, most of them will be out (-1)
		 *  for abot combination 0,1,2,3
		 *  	start 0
		 *  	start 1
		 *  	start 2
		 *  	start 3
		 *  	notes: for any portal encounters, find its right portal, if it is -1, out
		 *  			if it is another port, immediate jump to another side of portal
		 */
		for(ArrayList<Integer> arl : combos){
			for(int i = 0; i < arl.size(); i++){
				//tempPortal is the index of the portal being explored
				int tempPortal = arl.get(i);
				//count tracks whether or not it is infinte loop
				int count = 0;
				while(count < 2*arl.size()){
					//if there is no other portal on right side (same y, greater x), exit the loop (no infinite loop)
					int rResult = rightSide[tempPortal];
					if(rResult == -1){
						count = 0;
						break;
					}else{
						count++;
						//there is another portal on the right side (same y, greater x)
						//finds the pair of the portal in arl
						int pairIndex = arl.indexOf(rResult);
						if(pairIndex%2 == 0){
							// another side of wormhole is next portal (0,1,2,3) 0 jumps to 1, 2 jumps to 3
							tempPortal = arl.get(pairIndex+1);
						}else {
							// another side of wormhole is previous portal (0,1,2,3) 1 jumps to 0, 3 jump to 2
							tempPortal = arl.get(pairIndex - 1);
						}
					}
				}
				/**
				 * we are out of while, let's see if it caused by infinite loop (2*numWormholes)
				 */
				if(count >= 2*arl.size()){
					rt++;
					break;
				}
			}
		}
		
		return rt;
	}
	
	
	public void recursiveCombo(ArrayList<Integer> indexes, ArrayList<ArrayList<Integer>> rt, int numWormholes) {
		if(indexes.size() == 2) {
			rt.get(rt.size()-1).add(indexes.get(0));
			rt.get(rt.size()-1).add(indexes.get(1));
			return;
		}
		for(int i = 1; i < indexes.size(); i++) {
			if(indexes.size() == numWormholes) {
				rt.add(new ArrayList<Integer>());
			}else if(rt.get(rt.size()-1).size() == numWormholes) {
				ArrayList<Integer> tempList = new ArrayList<Integer>();
				int copyNum = numWormholes - indexes.size();
				ArrayList<Integer> previousList = rt.get(rt.size()-1);
				for(int j = 0; j < copyNum; j++) {
					tempList.add(previousList.get(j));
				}
				rt.add(tempList);
			}
			rt.get(rt.size()-1).add(indexes.get(0));
			rt.get(rt.size()-1).add(indexes.get(i));
			ArrayList<Integer> dupIndex = (ArrayList<Integer>)indexes.clone();
			//removes the two used numbers
			dupIndex.remove(i);
			dupIndex.remove(0);
			recursiveCombo(dupIndex, rt, numWormholes);
		}
	}
	
}
