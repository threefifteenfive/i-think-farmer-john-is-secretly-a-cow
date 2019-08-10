/*
ID: andrew.83
LANG: JAVA
TASK: crypt1
*/

import java.util.*;
import java.io.*;

public class crypt1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
			crypt1 c1 = new crypt1();
			PrintWriter out = new PrintWriter("crypt1.out");
			out.println(c1.work("crypt1.in"));
			out.close();
			System.out.println(c1.work("crypt1.in"));
		}catch(Exception e) {
			System.out.println(e.toString());
		}
		
	}

	public int work(String filename) throws Exception {
		int rt = 0;
		
		BufferedReader in = new BufferedReader(new FileReader(filename));
		StringTokenizer s0 = new StringTokenizer(in.readLine());
		int totalDigits = Integer.parseInt(s0.nextToken());
		StringTokenizer st = new StringTokenizer(in.readLine());
		ArrayList<Integer> digits = new ArrayList<Integer>();
		for(int i = 0; i < totalDigits; i++) {
			digits.add(Integer.parseInt(st.nextToken()));
		}
		
		int top;
		int bot;
		for(int topHun : digits) {
			for(int topTen : digits) {
				for(int topOne : digits) {
					for(int botTen : digits) {
						for(int botOne : digits) {
							top = topHun * 100 + topTen * 10 + topOne;
							bot = botTen * 10 + botOne;
							if(top * botOne < 1000 && top * botTen < 1000) {
								if(checkDigits(top * botOne, digits) && checkDigits(top * botTen, digits)) {
									if(checkDigits(top * bot, digits)) {
										rt++;
									}
								}
							}
						}
					}
				}
			}
		}
		
		return rt;
	}
	
	public boolean checkDigits(int num, ArrayList<Integer> digits) {
		if(num <= 0) {
			return true;
		}else {
			if(!digits.contains(num%10)) {
				return false;
			}
			return checkDigits(num / 10, digits);
		}
	}
	
}
