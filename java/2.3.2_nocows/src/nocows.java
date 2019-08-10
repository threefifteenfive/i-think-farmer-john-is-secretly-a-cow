/*
ID: andrew.83
LANG: JAVA
TASK: nocows
*/

import java.io.*;
import java.util.*;

/**
 * Cow Pedigrees (2.3.2)
 *
 * given a number of nodes and a number of levels
 * return the number of uniquely-shaped binary tress with the given number of levels and nodes
 *  all nodes will either have 0 or 2 children (never 1, 3+ is impossible because of BINARY structure)
 * Ex: 5 3 (number of nodes, number of levels)
 * return 2 (@ = individual node)
 *            @                   @
 *           / \                 / \
 *          @   @      and      @   @
 *         / \                     / \
 *        @   @                   @   @
 * both trees use 5 nodes total, each node has 0 or 2 children, 3 total levels
 *
 * First successful run
 *    Test 1: TEST OK [0.161 secs, 23440 KB]
 *    Test 2: TEST OK [0.098 secs, 23900 KB]
 *    Test 3: TEST OK [0.093 secs, 23924 KB]
 *    Test 4: TEST OK [0.093 secs, 24016 KB]
 *    Test 5: TEST OK [0.093 secs, 23724 KB]
 *    Test 6: TEST OK [0.093 secs, 23920 KB]
 *    Test 7: TEST OK [0.096 secs, 23084 KB]
 *    Test 8: TEST OK [0.103 secs, 23108 KB]
 *    Test 9: TEST OK [0.105 secs, 23888 KB]
 *    Test 10: TEST OK [0.098 secs, 23460 KB]
 *    Test 11: TEST OK [0.112 secs, 24800 KB]
 *    Test 12: TEST OK [0.110 secs, 24476 KB]
 */

public class nocows {

    public static void main(String[] args){

        try{
            nocows nc = new nocows();
            //System.out.println(nc.work("nocows.in"));
            PrintWriter out = new PrintWriter("nocows.out");
            out.println(nc.work("nocows.in"));
            out.close();
        }catch(Exception e){
            System.out.println(e.toString());
        }

    }

    public long work(String filename)throws Exception{
        long nocows = 0;

        /**
         * reading input and storing variables
         * because of rule (nodes need 0 or 2 children)
         *  if number of nodes is even, it is impossible to create
         *  (always have individual capstone node at the top)
         */
        BufferedReader in = new BufferedReader(new FileReader(filename));
        StringTokenizer st = new StringTokenizer(in.readLine());
        int numNodes = Integer.parseInt(st.nextToken());
        int numRows = Integer.parseInt(st.nextToken());
        if(numNodes%2 == 0){
            return 0;
        }

        /**
         * storing calculations (catalan number calculations used)
         * catalan numbers are perfect for calcuations (1 1 2 5 14 42 132 429 1430 4862)
         * https://www.geeksforgeeks.org/program-nth-catalan-number/
         * C0 = 1, Cn+1 = sum(i from 0 to n) of(Ci * Cn-i)
         * for 0 levels, can only use 0 nodes, 1 possible way
         * for 1 level, can only use 1 node, 1 possible way
         * for 2 levels, can use 2 or 3 nodes (2 possible ways for 2 nodes, 1 possible way for 3 nodes)
         * for 3 levels, can use 3,4,5,6,7 nodes
             *   0 1 2 3 4 5 6 7 8 9 10...
             * 0 1
             * 1   1
             * 2     2 1
             * 3       4 6 6 4 1
             * 4
         * distributionSums stores the catalan numbers themselves
         *  rows are the number of levels (numRows)
         *  columns are the number of nodes used (numNodes)
             *   0 1 2 3 4 5 6 7 8 9 10...
             * 0 1
             * 1 1 1
             * 2 1 1 2 1
             * 3 1 1 2 5 6 6 4 1
             * 4 1 1 2 5 14...
         * to find the unique combinations for a certain level and certain number of nodes, (3 levels, 5 nodes)
         *  goto 3,5 (5) subtract the noise (previous row) 5-1 = 4
         * however, due to USACO rule of 0 or 2 children for each node, use conversion to simplify.
         * individual node @ = @
         *                    / \
         *                   @  @
         * to convert, numRows--, (numNodes-1)/2
         * to find 3 levels, 5 nodes with USACO rule of 0 or 2 children for each node,
         *  goto 3-1,(5-1)/2 (2) subtract noise (none in this case)
         * NOTE: it is necessary to pull down values in each column before column #(row)
         */
        long[][] distributionSums = new long[100][250];
        int convertRow = numRows-1;
        int convertNodes = (numNodes-1)/2;

        //need to initialize 0,0 to 1
        distributionSums[0][0] = 1;
        /**
         * only need to compute numbers up until convertRow and convertNodes
         *  convertRow = numRows-1
         *  convertNodes = (numNodes-1)/2
         */
        for(int row = 1; row <= convertRow; row++){
            int nodes = row;
            long tempSum = 2;
            while(nodes <= convertNodes && tempSum > 1){
                //use catalanSum() method to fill out row
                //stop after tempSum = 1 because the last number in each row is always one
                    //only 1 way to make 3 rows with 7 nodes
                tempSum = catalanSum(row, nodes, distributionSums);
                nodes++;
            }
        }

        //conversion
        //mod by 9901 because the numbers get so large they surpass max limit of long
        nocows = distributionSums[convertRow][convertNodes] - distributionSums[convertRow-1][convertNodes] + 9901;
        nocows = nocows%9901;
        return nocows;
    }

    public long catalanSum(int row, int nodes, long[][] distSums){
        long sum = 0;
        row--;
        //(row,nodes) = sum of (row-1,m)(row-1,nodes-m-1)
        for(int m = 0; m < nodes; m++){
            if(distSums[row+1][m] == 0 && m < row+1){
                distSums[row+1][m] += distSums[row][m];
            }
            sum += distSums[row][m] * distSums[row][nodes-m-1];
            //making sure to mod by 9901 to keep numbers manageable
            sum %= 9901;
        }
        distSums[row+1][nodes] = sum;
        return sum;
    }

}
