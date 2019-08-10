/*
ID: andrew.83
LANG: JAVA
TASK: money
*/

import java.io.*;
import java.util.*;

/**
 * Money Systems (2.3.4)
 *
 * given a number of values (coins) and a target sum
 * return the number of unique ways to create the sum using only the given values
 *
 * Ex:
 * 3 10 (3 coin values given, 10 is target)
 * 1 2 5 (coin values are 1,2,5)
 *
 * return 10
 * 1x10, 2+(1x8), (2x2)+(1x6), (2x3)+(1x4), (2x4)+(1x2), 2x5, 5+(2x2)+1, 5+2+(1x3), 5+(1x5), 5x2
 * NOTE:
 *  This is an exercise in dynamic programming (using tables)
 *  Initially attempted to use recursive method. Failed when given 10 coins and target sum of 100 (~2.4 seconds)
 *  In comparison, dp solution always less than 0.15 seconds (max was 0.112 seconds, given 17 coins, 2000 target sum)
 *  recursive complexity is exponential O(n^numCoins)
 *      ended up computing 160 million combinations on the third case
 *  dp is O(n*numCoins)
 *      on same case, computed only 10 thousand calculations (all addition)
 *
 * NOTE:
 *  encountered overflow error (NUMBERS GET UNPREDICTABLE WHEN OVERFLOWING)
 *  last answer was 18,390,132,498 (18 billion)
 *  max int value is 2,147,483,647 (2 billion)
 *  long can store up to 9,223,372,036,854,775,807 (9 quintillion ~ 9*10^18)
 *
 * First Successful Run
 *    Test 1: TEST OK [0.089 secs, 23636 KB]
 *    Test 2: TEST OK [0.091 secs, 23224 KB]
 *    Test 3: TEST OK [0.091 secs, 23492 KB]
 *    Test 4: TEST OK [0.091 secs, 23768 KB]
 *    Test 5: TEST OK [0.208 secs, 23484 KB]
 *    Test 6: TEST OK [0.110 secs, 26000 KB]
 *    Test 7: TEST OK [0.096 secs, 23972 KB]
 *    Test 8: TEST OK [0.182 secs, 23916 KB]
 *    Test 9: TEST OK [0.093 secs, 23492 KB]
 *    Test 10: TEST OK [0.096 secs, 23852 KB]
 *    Test 11: TEST OK [0.091 secs, 23560 KB]
 *    Test 12: TEST OK [0.100 secs, 24468 KB]
 *    Test 13: TEST OK [0.096 secs, 23540 KB]
 */
public class money {

    /**
     * return variables
     * numWays is number of unique combinations of coins
     * numTries used for tracking number of calculations used in recursive method
     */
    long numWays;
    int numTries;

    public money(){
        numWays = 0;
    }

    public static void main(String[] args){

        try{
            money m = new money();
            //System.out.println(m.work("money.in"));
            PrintWriter out = new PrintWriter("money.out");
            out.println(m.work("money.in"));
            out.close();
        }catch(Exception e){
            System.out.println(e.toString());
        }

    }

    public long work(String filename)throws Exception{

        /**
         * reading input
         * because the number of coins per line is variable
         *  used while loop to take in lines and StringTokenizer to parse indefinite coin values
         * since input can have duplicates but there is no need to store them
         *  add to HashSet to save unique coin values
         */
        BufferedReader in = new BufferedReader(new FileReader(filename));
        StringTokenizer st = new StringTokenizer(in.readLine());
        int sum = Integer.parseInt(st.nextToken());
        HashSet<Integer> uniqueCoins = new HashSet<>();
        String tempLine = in.readLine();
        while(tempLine != null){
            st = new StringTokenizer(tempLine);
            while(st.hasMoreTokens()){
                uniqueCoins.add(Integer.parseInt(st.nextToken()));
            }
            tempLine = in.readLine();
        }
        int numCoins = uniqueCoins.size();
        Iterator<Integer> it = uniqueCoins.iterator();
        int[] coinValues = new int[numCoins];
        for(int index = 0; index < numCoins; index++){
            coinValues[index] = it.next();
        }
        Arrays.sort(coinValues);
        //coinValues is int[] that stores the unique values of the coins (sorted)

        /**
         * dpSums is implementation of dynamic programming (storing past calculations for future use)
         * rows (0-number of coins), columns (0-sum)
         * Ex:
         *        0  1  2  3  4  5  6  7  8  9  10
         * (1) 0| 1  1  1  1  1  1  1  1  1  1  1
         * (2) 1| 1  1  2  2  3  3  4  4  5  5  6
         * (5) 2| 1  1  2  2  3  4  5  6  7  8  10
         *
         * dpSums[0][0] always set to 1 (starting point) 1 way to make 0
         * for each row, start from the value of the coin
         * (coinValues[] = {1,2,5})
         * Ex: r0,c1 means using coins of value 1, there is 1 way to make 1
         *     r0,c4 means using coins of value 1, there is 1 way to make 4
         *     r1,c6 means using coins of value 1 or 2 or both, there are 4 ways to make 6
         * computation of each index is based off of past calcuations
         */
        long[][] dpSums = new long[numCoins][sum+1];

        //starting point
        dpSums[0][0] = 1;

        //iterate through each row
        for(int coin = 0; coin < numCoins; coin++){
            //getting the corresponding value of the coin for the row
            int coinVal = coinValues[coin];
            //need to iterate from 0 to end column (sum)
            for(int i = 0; i <= sum; i++){
                /**
                 * if the value (column index) is less than the coin value
                 * drag down the value directly above
                 * Ex: index 2 of coinValue[] = 5
                 * r2,c3 = r1,c3
                 * because there is no way a coin of value 5 can make 3
                 *  everything before coinVal is the same as the row above it
                 */
                if(i < coinVal){
                    if(coin > 0){
                        dpSums[coin][i] = dpSums[coin-1][i];
                    }
                }
                /**
                 * from coinVal to sum, all columns are computed off of the past values
                 * prevSum is the above row (if there is an above row, avoid indexOutOfBounds in first row)
                 * dpSums[coin][i] += dpSums[coin][i-coinVal]+prevSum;
                 * Ex:
                 *        0  1  2  3  4  5  6  7  8  9  10
                 * (1) 0| 1  1  1  1  1  1  1  1  1  1  1
                 * (2) 1| 1  1  2  2  3  3  4  4  5  5  6
                 * (5) 2| 1  1  2  2  3  4  5  6  7  8  10
                 * r1,c3 = 3
                 * prevSum = 1 (r0,c3): 1 way to make 3 using a coin with value of 1
                 * current coin value is 2, find remainder of 3-2 = 1;
                 * prevSum(1) + dpSums[r1][c3-1=1](1) = 2
                 */
                else{
                    long prevSum = 0;
                    if(coin > 0){
                        prevSum = dpSums[coin-1][i];
                    }
                    dpSums[coin][i] += dpSums[coin][i-coinVal]+prevSum;
                }
            }
        }

        /**
         * answer is located at bottom right corner
         *  bottom right corner is number of ways using any combination of all coin values to add up to sum
         */
        numWays = dpSums[numCoins-1][sum];

        return numWays;
    }

    /**
     * recursive method of calcuation
     * creates all combinations for a given row
     * for each value of a coin
     *  iterate through and add all combinations of the other coin values
     * @param first
     * @param index
     * @param coinValues
     * @param prevSum
     * @param goalSum
     */
    public void recursivePossibilities(boolean first, int index, int[] coinValues, int prevSum, int goalSum){
        numTries++;
        if(index < coinValues.length) {
            int coinVal = coinValues[index];
            int numTries = (goalSum - prevSum) / coinVal;
            for (int i = 0; i <= numTries; i++) {
                if(first){
                    i=1;
                    first = false;
                }
                int nextSum = prevSum + (coinVal * i);
                if(nextSum < goalSum) {
                    recursivePossibilities(first, index + 1, coinValues, nextSum, goalSum);
                }
            }
        }else{
            if(prevSum == goalSum){
                numWays++;
            }
        }
    }

}
