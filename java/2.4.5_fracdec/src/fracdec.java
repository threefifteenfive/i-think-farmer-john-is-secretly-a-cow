/*
ID: andrew.83
LANG: JAVA
TASK: fracdec
*/

import java.io.*;
import java.util.*;

/**
 * Fractions to Decimals (Usaco 2.4.5)
 *
 * given a numerator and denominator
 * return the decimal form of the fraction created by dividing the numerator by the denominator
 *  if the decimal form doesn't repeat, return the decimal
 *  if the decimal form does repeat, return the decimal with the repeated section enclosed in parentheses
 *
 * Ex:
 * 22 5
 * 22/5 = 4.4 (return 4.4)
 * Ex:
 * 45 56
 * 45/56 = 0.803571428... (return 0.803(571428))
 * NOTE: if the decimal format is really long, each line of the return can only have 76 characters
 * Ex:
 * 1 99991
 * 1/99991 = (really freaking long: 50,656 characters, 657 lines)
 * 0.(0000100009000810072906561590543148883399505955535998239841585742716844516
 * 0064405796521686951825664309787880909281835365182866457981218309647868308147
 * 7332959966396975727815503395305577501975177765998939904591413227190447140242...
 * (76 characters per line)
 *
 * First Successful Runs:
 *    Test 1: TEST OK [0.124 secs, 23492 KB]
 *    Test 2: TEST OK [0.177 secs, 23644 KB]
 *    Test 3: TEST OK [0.093 secs, 23216 KB]
 *    Test 4: TEST OK [0.114 secs, 24880 KB]
 *    Test 5: TEST OK [0.096 secs, 23560 KB]
 *    Test 6: TEST OK [0.091 secs, 23336 KB]
 *    Test 7: TEST OK [0.196 secs, 28524 KB]
 *    Test 8: TEST OK [0.198 secs, 33668 KB]
 *    Test 9: TEST OK [0.208 secs, 23880 KB]
 */

public class fracdec {

    public static void main(String[] args){

        try{
            fracdec fd = new fracdec();
            System.out.println(fd.work("fracdec.in"));
            PrintWriter out = new PrintWriter("fracdec.out");
            out.println(fd.work("fracdec.in"));
            out.close();
        }catch(Exception e){
            System.out.println(e.toString());
        }

    }

    public String work(String filename) throws Exception{
        String repeatFraction = "";

        /**
         * reading input is very standard and simple
         */
        BufferedReader in = new BufferedReader(new FileReader(filename));
        StringTokenizer st = new StringTokenizer(in.readLine());
        int num = Integer.parseInt(st.nextToken());
        int denom = Integer.parseInt(st.nextToken());
        /**
         * decList stores all the digits of the decimal (after the decimal point)
         * remainderArray tells whether or not a remainder has been visited before
         *  45 56 (max remainder will be 55) all sub-remainders will be from 0-55 inclusive
         * remainderIndex records the first index of each remainder (parallel) array
         *  accessed after a remainder has been discovered twice
         * multiples[] stores all multiples of the denominator 0-9, no need to repeatedly multiply the denominator
         *  index[i] = denominator * i
         */
        ArrayList<Integer> decList = new ArrayList<>();
        boolean[] remainderArray = new boolean[denom];
        int[] remainderIndex = new int[denom];
        int[] multiples = new int[10];
        for(int i = 1; i < 10; i++){
            multiples[i] = multiples[i-1] + denom;
        }
        //gets the number on left of the decimal
        int beforeDecimal = num/denom;
        repeatFraction += beforeDecimal + ".";
        /**
         * Algo: code the process of division and test for repetition
         *  use % to find remainder
         *  use remainder to find next digit in decimal
         *  if a remainder is encountered twice, there is a repeat (repeated portion goes back to last time remainder found)
         * Ex:
         * 1 < 7, decimal = 0.
         * remainderArray[7] 0,1,2,3,4,5,6 (all false)
         * remainderIndex[7] 0,0,0,0,0,0,0
         * 1%7 = 1 -> 1*10 = 10 -> 7*1 < 10 -> 0.1        (0)
         * 10%7 = 3 -> 3*10 = 30 -> 7*4 < 30 -> 0.14      (1)
         * 30%28 = 2 -> 2*10 = 20 -> 7*2 < 20 -> 0.142    (2)
         * 20%14 = 6 -> 6*10 = 60 -> 7*8 < 60 -> 0.1428   (3)
         * 60%56 = 4 -> 4*10 = 40 -> 7*5 < 40 -> 0.14285  (4)
         * 40%35 = 5 -> 5*10 = 50 -> 7*7 < 50 -> 0.142857 (5)
         * 50%49 = 1 (remainder of 1 was used before)
         * remainderIndex[7] 0,0,2,1,4,5,3
         * remainderArray[7] (all except 0 are now true)
         */

        //first remainder is the remainder of the numerator and denominator itself
        int remain = num%denom;
        boolean done = false;
        //whether or not there is a repeat
        boolean noRepeat = false;
        //initialization of where the repeated portion begins (if there is a such thing)
        int startCut = 0;
        //tracks the index of the decList as digits are added the index increments as well, used to store remainderIndex
        int index = 0;
        while(!done){
            //if previous remainder has been used already
            if(remainderArray[remain]){
                //repeated portion begins at the first occurence of the remainder
                startCut = remainderIndex[remain];
                //exit the loop
                done = true;
            }else {
                //multiply previous remainder by 10
                int tempNum = remain * 10;
                //find the largest multiple of the denominator less than tempNum
                int max = 9;
                while(multiples[max] > tempNum){
                    max--;
                }
                //check to see if the decimal ends or not
                if(tempNum == 0){
                    //if the remainder is 0, exit loop, no repeated portion
                    done = true;
                    noRepeat = true;
                }else {
                    //if decimal keeps going, add the multiplier of the denominator to decList
                    decList.add(max);
                    //set remainderIndex of the remainder to the current index
                    remainderIndex[remain] = index;
                    //flag that this remainder has been accessed before
                    remainderArray[remain] = true;
                    //calculate the new remainder
                    remain = tempNum - multiples[max];
                }
                //increment index after
                index++;
            }
        }

        //because there can be enormous string sizes, StringBuilder is preferable
        StringBuilder sb = new StringBuilder();
        //repeatFraction only has the beginning portion and a decimal point (Ex: 4.))
        sb.append(repeatFraction);
        //track the length of the string in order to fit the 76 character per line rule
        int sLength = sb.length();
        for(int i = 0; i < decList.size(); i++){
            //if there is a repeat, when reaching the beginning of the repeated portion, add a "("
            if(!noRepeat && i == startCut){
                sb.append("(");
                //increment the length (this counts as a character too)
                sLength++;
                //if the length is now a multiple of 76, add a line break
                if(sLength%76 == 0){
                    sb.append("\n");
                }
            }
            //add the next digit to the string
            sb.append(decList.get(i));
            //increment length
            sLength++;
            //if the length is now a multiple of 76, add a line break
            if(sLength%76 == 0){
                sb.append("\n");
            }
        }

        //if there is a repeat, close it off with ")" end paren
        if(!noRepeat) {
            sb.append(")");
        }
        //convert StringBuilder to a string
        repeatFraction = sb.toString();
        /**
         * case of numerator being multiple of denominator
         * Ex: 20 4
         *  at this point will have 5. (want us to return 5.0)
         *  if last character is '.', add a '0' at the end
         */
        if(repeatFraction.charAt(repeatFraction.length()-1) == '.'){
            repeatFraction += "0";
        }

        return repeatFraction;
    }

}
