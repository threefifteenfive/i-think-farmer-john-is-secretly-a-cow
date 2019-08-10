/*
ID: andrew.83
LANG: JAVA
TASK: humble
*/

import java.io.*;
import java.util.*;

/**
 * Humble Numbers (USACO 3.1.3)
 *
 * given a set of prime numbers and an "N"
 * return the Nth humble number using the set of prime numbers
 *  humble number is a number whose prime factorization is comprised of a combination of the given primes
 *
 * Ex:
 * 4 19 (4 primes given, N = 19)
 * 2 3 5 7 (set of primes)
 *
 * humble numbers:
 * 2,3,4,5,6,7,8,9,10,12,14,15,16,18,20,21,24,25,27
 * 1,2,3,4,5,6,7,8,9 ,10,11,12,13,14,15,16,17,18,19
 *                                                ^-19th humble number is 27
 * return 27
 *
 * First Successful Runs
 *    Test 1: TEST OK [0.147 secs, 23416 KB]
 *    Test 2: TEST OK [0.194 secs, 23828 KB]
 *    Test 3: TEST OK [0.089 secs, 23592 KB]
 *    Test 4: TEST OK [0.105 secs, 23868 KB]
 *    Test 5: TEST OK [0.119 secs, 23820 KB]
 *    Test 6: TEST OK [0.131 secs, 24124 KB]
 *    Test 7: TEST OK [0.110 secs, 24004 KB]
 *    Test 8: TEST OK [0.112 secs, 23552 KB]
 *    Test 9: TEST OK [0.096 secs, 22940 KB]
 *    Test 10: TEST OK [0.091 secs, 23832 KB]
 *    Test 11: TEST OK [0.091 secs, 23620 KB]
 *    Test 12: TEST OK [0.189 secs, 25260 KB]
 */
public class humble {

    //int MAX = 10000;

    public static void main(String[] args){

        try{
            humble h = new humble();
            //System.out.println(h.work("humble.in"));
            PrintWriter out = new PrintWriter("humble.out");
            out.println(h.work("humble.in"));
            out.close();
        }catch(Exception e){
            System.out.println(e.toString());
        }

    }

    public long work(String filename) throws Exception{
        long humbleNum = 0;
        //int numSkips = 0;

        /**
         * reading input
         * storing number of primes as numPrimes and N
         * primeArray[] contains the set of given primes
         */
        BufferedReader in = new BufferedReader(new FileReader(filename));
        StringTokenizer st = new StringTokenizer(in.readLine());
        int numPrimes = Integer.parseInt(st.nextToken());
        int N = Integer.parseInt(st.nextToken());
        int[] primeArray = new int[numPrimes];
        st = new StringTokenizer(in.readLine());
        for(int i = 0; i < numPrimes; i++){
            int prime = Integer.parseInt(st.nextToken());
            primeArray[i] = prime;
        }

        /**
         * humbleArray[] stores all the humble numbers in order
         *  humbleArray[N] is the Nth humble number
         *  humbleArray[0] is set to 1 by default for multiplication (even though 1 isn't a humble number)
         * nextMult stores the index of the best humble number for each prime to multiply
         *  goal is to multiply each prime (x) by the lowest humble number (y) so that xy > largest current humble number
         *  Ex:
         *      2 3 5 7 (primes)
         *            indexes 0          1 2 3 4
         *      humbleArray = 1(ignored),2,3,0,0....
         *      nextMult = 0,0,0,0
         *      2*humbleArray[0] <= 3
         *      2*humbleArray[1] > 3 (4)
         *      nextMult = 1,0,0,0
         *      3*humbleArray[0] <= 3
         *      3*humbleArray[1] > 3 (6)
         *      nextMult = 1,1,0,0
         *      5*humbleArray[0] > 3 (5)
         *      nextMult = 1,1,0,0
         *      7*humbleArray[0] > 3 (7)
         *      nextMult = 1,1,0,0
         *      next humble number is min(each prime * its nextMult)
         *      min(2*humble[1], 3*humble[1], 5*humble[0], 7*humble[0]) = 2*humble[1] = 2*2 = 4
         *
         *      humbleArray = 1,2,3,4,0....
         */
        int[] humbleArray = new int[N+1];
        humbleArray[0] = 1;
        int[] nextMult = new int[numPrimes];

        for(int hNum = 1; hNum <= N; hNum++){
            //nextHumble is the humble number that is put into humbleArray[hNum]
            //nextHumble is the lowest humble number strictly greater than the current largest number in humbleArray
            int nextHumble = Integer.MAX_VALUE;
            //update nextMult values for each prime
            for(int mult = 0; mult < numPrimes; mult++){
                //while loop so that primeArray[i] * humbleArray[nextMult[i]] > humbleArray[hNum-1]
                //                      any prime  *  chosen&existing humble  >  current largest humble
                while(nextMult[mult] < hNum && primeArray[mult] * humbleArray[nextMult[mult]] <= humbleArray[hNum-1]){
                    nextMult[mult]++;
                }
                //find the lowest humble number strictly greater than the current largest humble
                nextHumble = Math.min(nextHumble, primeArray[mult] * humbleArray[nextMult[mult]]);
            }
            //update humbleArray[]
            humbleArray[hNum] = nextHumble;
        }

        //Nth humble number is stored in humbleArray[N]
        humbleNum = humbleArray[N];

        /**
         * Original idea of iterating through a number line array
         *  idea came from USACO Prefix problem
         * boolean[] tells whether or not a number is a humble number
         *  to avoid excessive memory usage boolean[] numLine size = 10,000
         *  has to have overFlow list for next generations of numLine
         *         ArrayList<Integer> overFlow = new ArrayList<>();
         *         boolean[] numLine = new boolean[MAX];
         *         int hNum = 0;
         *         st = new StringTokenizer(in.readLine());
         *         for(int i = 0; i < numPrimes; i++){
         *             int prime = Integer.parseInt(st.nextToken());
         *             primeArray[i] = prime;
         *             overFlow.add(prime);
         *         }
         *
         *         int multiplier = 0;
         *         while(!overFlow.isEmpty()) {
         *             Arrays.fill(numLine, false);
         *             ArrayList<Integer> tempList = new ArrayList<>();
         *             boolean doSkip = true;
         *             for(int oF : overFlow){
         *                 if(oF < MAX) {
         *                     numLine[oF] = true;
         *                     doSkip = false;
         *                 }else{
         *                     tempList.add(oF-MAX);
         *                 }
         *             }
         *             overFlow.clear();
         *             overFlow.addAll(tempList);
         *             if(!doSkip) {
         *                 for (int index = 0; index < numLine.length; index++) {
         *                     if (numLine[index]) {
         *                         hNum++;
         *                         if (hNum == N) {
         *                             humbleNum = index;
         *                             break;
         *                         }
         *                         for (int prime : primeArray) {
         *                             try {
         *                                 numLine[prime * (index + (multiplier * MAX))] = true;
         *                             } catch (ArrayIndexOutOfBoundsException oob) {
         *                                 overFlow.add((prime * (index + (MAX * multiplier))) - (MAX * (multiplier + 1)));
         *                             }
         *                         }
         *                     }
         *                 }
         *             }else{
         *                 numSkips++;
         *             }
         *             multiplier++;
         *         }
         *
         *         System.out.println(numSkips);
         *         return humbleNum + (MAX * (multiplier-1));
         */

        return humbleNum;
    }

}
