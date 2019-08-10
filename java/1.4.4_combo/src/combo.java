/*
ID: andrew.83
LANG: JAVA
TASK: combo
*/

import java.io.*;
import java.util.*;

public class combo {

	public static void main(String[] args) {
		
		try {
			combo c = new combo();
			PrintWriter out = new PrintWriter("combo.out");
			out.println(c.work("combo.in"));
			out.close();
			System.out.println(c.work("combo.in"));
		}catch(Exception e) {
			System.out.println(e.toString());
		}
		
	}
	
	public int work(String filename) throws Exception{
		int rt = 1;
		
		int range;
		int[] trueCombo = new int[3];
		int[] mastCombo = new int[3];
		BufferedReader in = new BufferedReader(new FileReader(filename));
		
		range = Integer.parseInt(removeSpace(in.readLine()));
		
		String temp = in.readLine();
		StringTokenizer st = new StringTokenizer(temp);
		for(int i = 0; i < 3; i++) {
			trueCombo[i] = Integer.parseInt(st.nextToken());	
		}
		st = new StringTokenizer(in.readLine());
		for(int i = 0; i < 3; i++) {
			mastCombo[i] = Integer.parseInt(st.nextToken());	
		}
		
		int[][] tPoss = new int[3][5];
		int[][] mPoss = new int[3][5];
		for(int i = 0; i < 3; i++) {
			int mastDigit = mastCombo[i];
			int trueDigit = trueCombo[i];
			
			for(int dif = 0; dif < 5; dif++) {
				int tTemp = inRange(trueDigit + dif - 2, range);
				tPoss[i][dif] = tTemp;
				int mTemp = inRange(mastDigit + dif - 2, range);
				mPoss[i][dif] = mTemp;
			}
		}
		
		//calculating trueCombo's possibilities
		for(int digit = 0; digit < 3; digit++) {
			int prev = range + 1;
			int digitPossibilities = 0;
			for(int poss = 0; poss < 5; poss++) {
				int tempDigit = tPoss[digit][poss];
				ArrayList<Integer> uniquePoss = new ArrayList<Integer>();
				for(int td : tPoss[digit]) {
					if(!uniquePoss.contains(td)) {
						uniquePoss.add(td);
					}
				}
				digitPossibilities = uniquePoss.size();
			}
			rt *= digitPossibilities;
		}
		
		//finding the overlap for each digit
		int[] overlap = new int[3];
		for(int digit = 0; digit < 3; digit++) {
			int ol = 0;
			for(int poss = 0; poss < 5; poss++) {
				int mastDigit = mPoss[digit][poss];
				ol = 0;
				for(int tDigit = 0; tDigit < 5; tDigit++) {
					int tempTrue = tPoss[digit][tDigit];
					if(tempTrue == mastDigit) {
						ol++;
					}
				}
				overlap[digit] += ol;
			}
		}
		int cutoff = 1;
		for(int ol : overlap) {
			cutoff *= ol;
		}
		if(cutoff > rt) {
			cutoff = rt;
		}
		
		return 2 * rt - cutoff;
	}
	
	public int inRange(int num, int range) {
		if(num < 1) {
			num += range;
			if(num < 1) {
				num = 1;
			}
		}
		if(num > range) {
			num -= range;
			if(num > range) {
				num = range;
			}
		}
		return num;
	}
	
	public String removeSpace(String r) {
		while(r.substring(r.length()-1).equals(" ")) {
			r = r.substring(0, r.length()-1);
		}
		return r;
	}
	
}
