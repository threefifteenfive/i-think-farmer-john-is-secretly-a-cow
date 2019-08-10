/*
ID: andrew.83
LANG: JAVA
TASK: numtri
*/

/**
 * locating the largest sum that can be obtained by moving down a number pyramid.
 * movements can only go diagonally downwards. (index 0 can go down to 0 and 1)
 * 5 rows:
 *     7
 *    3 8
 *   8 1 0
 *  2 7 4 4
 * 4 5 2 6 5
 * Optimal path is 7->3->8->7->5 ([0][0]->[1][0]->[2][0]->[3][1]->[4][1])
 */

import java.util.*;
import java.io.*;



public class numtri {

    public static void main(String[] args){

        try{
            numtri nt = new numtri();
            System.out.println(nt.work("numtri.in"));
            PrintWriter out = new PrintWriter("numtri.out");
            out.println(nt.work("numtri.in"));
            out.close();
        }catch(Exception e){
            System.out.println(e.toString());
        }

    }

    public int work(String filename)throws Exception{
        int maxResult = 0;

        /**
         * reading input and storing the data
         * because of frequent access of simple ints, decided to use irregular 2D array (int[][] triangle)
         */
        BufferedReader in = new BufferedReader(new FileReader(filename));
        int numRows = Integer.parseInt(in.readLine());
        int[][] triangle = new int[numRows][];
        for(int i = 1; i <= numRows; i++){
            StringTokenizer st = new StringTokenizer(in.readLine());
            int[] row = new int[i];
            for(int j = 0; j < i; j++){
                row[j] = Integer.parseInt(st.nextToken());
            }
            triangle[i-1] = row;
        }

        /**
         * initializing dual arrays to move down the triangle
         * one array to store the maximum sums of the previous row, another array to compute the current row
         * both prevSums & currSums are set to size numRows for simplicity
         *  until loop iterates down to last row, prevSums and currSums will have empty/unused indexes
         * initial set-up of setting previous sums to the capstone of triangle
         * loop starts from index 1 (second row)
         */
        int[] prevSums = new int[numRows];
        int[] currSums = new int[numRows];
        prevSums[0] = triangle[0][0];

        /**
         * prevSums is used to calculate the next row's maximum sums using tempRow
         * tempRow is "current" row. prevSums are the maximum sums from rows above. calculated sums are put into currSums
         * values of currSums are copied into prevSums before the next iteration ('copy' not '=')
         * row = 1:
         * prevSums = {7,0,0,0,0}
         * currSums = {0,0,0,0,0}
         *  7
         * 3 8
         * currSums = {10,18,0,0,0}
         */
        for(int row = 1; row < numRows; row++){
            int[] tempRow = triangle[row];
            for(int i = 0; i < tempRow.length; i++) {
                /**
                 * if index of currSums is beginning of array or end of array, there is only one path that leads to it
                 *                                      7
                 *                                     3 8
                 * to go to 8, have to go through 3 ->8 1 0
                 * to go to 1, can go through 3 or 8, save maximum route (7->3->1 (11) vs 7->8->1 (16))
                 * to go to 0, have to go through 8
                 */
                if (i == 0) {
                    //just add first value to sum of path of all index 0
                    currSums[i] = prevSums[0] + tempRow[i];
                }else if(i == (tempRow.length)-1){
                    //just add last value to sum of path of all last values
                    currSums[i] = prevSums[triangle[row-1].length-1] + tempRow[i];
                }else{
                    //find local max between two routes to get to one
                    currSums[i] = (int)Math.max(tempRow[i]+prevSums[i], tempRow[i]+prevSums[i-1]);
                }
            }
            //copy currSums to prevSums for the next iteration
            prevSums = Arrays.copyOf(currSums, numRows);
        }

        //find the max result at the end and return it
        for(int sum : currSums){
            maxResult = Math.max(sum, maxResult);
        }

        return maxResult;
    }

}
