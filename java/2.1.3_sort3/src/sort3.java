/*
ID: andrew.83
LANG: JAVA
TASK: sort3
*/

import java.util.*;
import java.io.*;

/**
 * Sorting A Three-Valued Sequence (2.1.3)
 *
 * given a sequence of numbers (1,2,3 only)
 * return the minimum number of swaps needed to sort the sequence in ascending order
 *
 * Ex:
 * 9 (number of values in sequence)
 * 2 2 1 3 3 3 2 3 1 (sequence)
 * ^a^b^a          ^b
 * 1 1 2 3 3 3 2 3 2
 *       ^c^d  ^c  ^d
 * 4 swaps minimum
 *
 * Test Data:
 *    Test 1: TEST OK [0.128 secs, 23892 KB]
 *    Test 2: TEST OK [0.182 secs, 23584 KB]
 *    Test 3: TEST OK [0.093 secs, 24116 KB]
 *    Test 4: TEST OK [0.093 secs, 23668 KB]
 *    Test 5: TEST OK [0.093 secs, 24008 KB]
 *    Test 6: TEST OK [0.098 secs, 23812 KB]
 *    Test 7: TEST OK [0.098 secs, 23704 KB]
 *    Test 8: TEST OK [0.112 secs, 24588 KB]
 */
public class sort3 {

    public static void main(String[] args){

        try{
            sort3 s3 = new sort3();
            System.out.println(s3.work("sort3.in"));
            PrintWriter out = new PrintWriter("sort3.out");
            out.println(s3.work("sort3.in"));
            out.close();
        }catch(Exception e){
            System.out.println(e.toString());
        }

    }

    public int work(String filename)throws Exception{
        int totalSwaps = 0;

        /**
         * reading input
         * records (int[]) stores the sequence of values
         * oneIndex (LinkedList) store the indexes of 1's
         */
        BufferedReader in = new BufferedReader(new FileReader(filename));
        StringTokenizer st = new StringTokenizer(in.readLine());
        int numRecords = Integer.parseInt(st.nextToken());
        LinkedList<Integer> oneIndex = new LinkedList<>();
        int numOne = 0;
        int numTwo = 0;
        int[] records = new int[numRecords];
        for(int i = 0; i < numRecords; i++){
            st = new StringTokenizer(in.readLine());
            records[i] = Integer.parseInt(st.nextToken());
            if(records[i] == 1){
                oneIndex.add(i);
                numOne++;
            }
            else if(records[i] == 2){
                numTwo++;
            }
        }
        //removing 1's that shouldn't be moved (0, < # of 1's)
        for(int i = 0; i < oneIndex.size(); i++){
            if(oneIndex.get(i) < numOne){
                oneIndex.remove(i);
                i--;
            }
        }

        //iterates through numRecords once
        for(int rec = 0; rec < numRecords; rec++){
            int temp = records[rec];
            if(rec < numOne){
                /**
                 * if switching with a two, try to switch so that the two ends up in the two range
                 *  two range = [numOne, numOne+numTwo)
                 * if not possible, just switch with closest in 3 range (doesn't matter)
                 * that's why, pull from the front of oneIndex (closest to the front of the sequence)
                 */
                if(temp == 2) {
                    records[oneIndex.removeFirst()] = 2;
                    totalSwaps++;
                }
                /**
                 * if switching with a three, try to switch so that the three ends up in the three range
                 *  three range = [numOne+numTwo, end)
                 * if not possible, will have to switch with 2
                 * pull from the back of oneIndex (closest to the back of the sequence)
                 */
                /**
                 * need to actually swap when dealing with 1 (inside one range [0, numOne))
                 * when leaving one range, directly process 2's, only need one for loop
                 */
                else if(temp == 3){
                    records[oneIndex.removeLast()] = 3;
                    totalSwaps++;
                }
                records[rec] = 1;
            }else if(rec >= numOne && rec < numTwo+numOne){
                /**
                 * don't need to actually swap 3 with 2 (since counting number of swaps)
                 * how many 3's are in territory of 2 [numOne, numOne+numTwo) will need to be swapped
                 */
                if(temp != 2){
                    totalSwaps++;
                }
            }
        }

        return totalSwaps;
    }

}
