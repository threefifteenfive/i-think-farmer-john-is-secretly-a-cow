/*
ID: andrew.83
LANG: JAVA
TASK: hamming
*/

import java.util.*;
import java.io.*;

/**
 * given N required number of code numbers, B number of bits, and D hamming distance (minimum number of bits that are different)
 * return the first N number of numbers that have D hamming distance below B bits
 *
 * Ex:
 * 16 7 3 (N B D)
 * 0 7 25 30 42 45 51 52 75 76
 * 82 85 97 102 120 127
 *
 *      | (7 bits, take the right 7 bits)
 * 0  = 0000 0000
 * 7  = 0000 0111 (three bits are on)
 * 25 = 0001 1001 (four bits different from 7, three bits different from 0)
 *
 * first succesful test results
 *    Test 1: TEST OK [0.096 secs, 23980 KB]
 *    Test 2: TEST OK [0.091 secs, 23560 KB]
 *    Test 3: TEST OK [0.093 secs, 23604 KB]
 *    Test 4: TEST OK [0.096 secs, 23840 KB]
 *    Test 5: TEST OK [0.096 secs, 23840 KB]
 *    Test 6: TEST OK [0.093 secs, 23824 KB]
 *    Test 7: TEST OK [0.098 secs, 23400 KB]
 *    Test 8: TEST OK [0.103 secs, 23480 KB]
 *    Test 9: TEST OK [0.103 secs, 23660 KB]
 *    Test 10: TEST OK [0.100 secs, 23744 KB]
 *    Test 11: TEST OK [0.105 secs, 23628 KB]
 */

/**
 * iterate through numbers and shift bits B times and check for differences (using XOR symbol ^)
 */
public class hamming {

    public static void main(String[] args){

        try{
            hamming h = new hamming();
            //System.out.println(h.work("hamming.in"));
            PrintWriter out = new PrintWriter("hamming.out");
            out.println(h.work("hamming.in"));
            out.close();
        }catch(Exception e){
            System.out.println(e.toString());
        }

    }

    public String work(String filename)throws Exception{

        String rt = "";

        /**
         * reading input
         * storing N as numCode
         * storing B as numBits
         * storing D as hamming
         */
        BufferedReader in = new BufferedReader(new FileReader(filename));
        StringTokenizer st = new StringTokenizer(in.readLine());
        int numCode = Integer.parseInt(st.nextToken());
        int numBits = Integer.parseInt(st.nextToken());
        int hamming = Integer.parseInt(st.nextToken());

        /**
         * codes stores the successful code numbers
         * trialNum is the test number, continually incremented and tested for hamming distance for each of previous codes
         * iterate through successful codes (pastCodes)
         *  for numBits times, shift pastCodes and trialNumCopy down one bit
         *  Ex:
         *  25 = 0001 1001 25>>1 = 0000 1100 (12)>>1 = 0000 0110 (6)>>1 = 0000 0011 (3)>>1 = 0000 0001 (1)
         *  7  = 0000 0111  7>>1 = 0000 0011  (3)>>1 = 0000 0001 (1)>>1 = 0000 0000 (0)>>1 = 0000 0000 (0)
         *               |no difference    |difference         |difference        |difference        |difference
         */
        ArrayList<Integer> codes = new ArrayList<>();
        int trialNum = 0;
        while(codes.size() < numCode){
            int hTest = 0;
            for(Integer pastCodes : codes){
                //trialNumCopy so that trialNum isn't lost for comparison against the other previous codes
                int trialNumCopy = trialNum;
                int diff = 0;
                for(int shift = 0; shift < numBits; shift++){
                    if(((pastCodes&1)^(trialNumCopy&1)) == 1){
                        diff++;
                        if(diff >= hamming){
                            break;
                        }
                    }
                    trialNumCopy = trialNumCopy>>1;
                    pastCodes = pastCodes>>1;
                }
                if(diff >= hamming){
                    hTest++;
                }
            }
            if(hTest == codes.size()){
                codes.add(trialNum);
            }
            trialNum++;
        }

        /**
         * formatting for USACO output requirements
         */
        for(int c = 0; c < codes.size(); c++){
            if(c != 0 && c%10 == 0){
                rt = rt.substring(0, rt.length()-1);
                rt += "\n";
            }
            rt += codes.get(c) + " ";
        }

        return rt.substring(0, rt.length()-1);

    }

}
