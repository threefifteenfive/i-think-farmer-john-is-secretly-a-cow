/*
ID: andrew.83
LANG: JAVA
TASK: barn1
*/

import java.io.*;
import java.util.*;

public class barn1 {

	public static void main(String[] args) {
		
		try {
			barn1 b1 = new barn1();
			PrintWriter out = new PrintWriter("barn1.out");
			out.println(b1.work("barn1.in"));
			out.close();
			System.out.println(b1.work("barn1.in"));
		}catch(Exception e) {
			System.out.println(e.toString());
		}
		
	}
	
	public int work(String filename) throws Exception {
		int rt = 0;
		
		//input, firstLine
		BufferedReader in = new BufferedReader(new FileReader(filename));
		String firstLine = in.readLine();
		StringTokenizer s0 = new StringTokenizer(firstLine);
		int boards = Integer.parseInt(s0.nextToken());
		int totalStalls = Integer.parseInt(s0.nextToken());
		int numCow = Integer.parseInt(s0.nextToken());
		
		if(boards >= numCow) {
			return numCow;
		}else {
			//storing cow data
			int[] occupied = new int[numCow];
			for(int i = 0; i < numCow; i++) {
				StringTokenizer st = new StringTokenizer(in.readLine());
				int stall = Integer.parseInt(st.nextToken());
				occupied[i] = stall;
			}
			quickSort(occupied, 0, occupied.length-1);
			
			//finding distances
			/*TreeSet<Integer> gaps = new TreeSet<Integer>();
			if(occupied.length < 2) {
				return boards;
			}
			*/
			
			int[] gaps = new int[numCow-1];
			for(int i = 1; i < occupied.length ; i++) {
				int gap = occupied[i] - occupied[i-1];
				gaps[i-1] = gap;
			}
			
			quickSort(gaps, 0, gaps.length-1);
			
			//finding largest (number of boards-1) gaps
			rt = occupied[occupied.length-1] - occupied[0];
			int ind = gaps.length-1;
			for(int i = 0; i < boards-1; i++) {
				int largestTempGap = gaps[ind];
				rt -= largestTempGap;
				ind--;
			}
			/*
			Iterator<Integer> it = gaps.descendingIterator();
			for(int i = 0; i < boards-1; i++) {
				int largestTempGap = it.next();
				rt -= largestTempGap;
			}
			*/
			
			return rt + boards;
		}
		
	}
	
	public static int partition(int[] arr, int start, int end) {
		int pivot = arr[end];
		int low = start - 1;
		for(int indF = start; indF < end; indF++) {
			if(arr[indF] <= pivot) {
				low++;
				swap(arr, low, indF);
			}
		}
		swap(arr, low + 1, end);
		return low + 1;
	}
	
	public static void swap(int[] arr, int indF, int indS) {
		int temp = arr[indF];
		arr[indF] = arr[indS];
		arr[indS] = temp;
	}
	
	public static void quickSort(int[] arr, int start, int end) {
		if(start < end) {
			int div = partition(arr, start, end);
			quickSort(arr, start, div-1);
			quickSort(arr, div + 1, end);
		}
	}
	
}
