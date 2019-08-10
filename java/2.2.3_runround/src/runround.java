/*
ID: andrew.83
LANG: JAVA
TASK: runround
*/

import java.io.*;
import java.util.*;

/**
 * Runaround Numbers (Usaco 2.2.3)
 * given a single number (9 digits or less)
 * return the next lowest number that is a runaround number and has entirely unique digits
 * Ex:
 * 99 (given number)
 * return: 147
 * start at (1)47, move 1 digit to the right to 4
 * now at 1(4)7, move 4 digits to the right 7
 * now at 14(7), move 7 digits to the right back to 1
 *  NOTE: 111 is runaround but does not have unique digits
 * RUNROUND NUMBER = number with unique digits. by iterating using digit values, all digits are used and end up at the beginning
 *
 * First successful test runs:
 *    Test 1: TEST OK [0.093 secs, 24036 KB]
 *    Test 2: TEST OK [0.229 secs, 33736 KB]
 *    Test 3: TEST OK [0.110 secs, 23980 KB]
 *    Test 4: TEST OK [0.201 secs, 29728 KB]
 *    Test 5: TEST OK [0.422 secs, 38340 KB]
 *    Test 6: TEST OK [0.301 secs, 38044 KB]
 *    Test 7: TEST OK [0.488 secs, 40816 KB]
 */
public class runround {

    public static void main(String[] args){

        try{
            runround rr = new runround();
            //System.out.println(rr.work("runround.in"));
            PrintWriter out = new PrintWriter("runround.out");
            out.println(rr.work("runround.in"));
            out.close();
        }catch(Exception e){
            System.out.println(e.toString());
        }

    }

    public String work(String filename)throws Exception{

        /**
         * reading input and increasing test number (runround) by 1
         * creating comparison long (allComplete) for use in checking method
         */
        BufferedReader in = new BufferedReader(new FileReader(filename));
        StringTokenizer st = new StringTokenizer(in.readLine());
        String runround = st.nextToken();
        int temp = Integer.parseInt(runround) + 1;
        runround = Integer.toString(temp);
        long allComplete = 0;
        /**
         * if all digits are encountered, all digits in allComplete are set to on (set to 1)
         * iterate through digits, set bit at digit to 1
         */
        for(int i = 0; i < runround.length(); i++){
            allComplete |= (1<<i);
        }
        /**
         * increment runround until it satisfies checking method isRunRound()
         */
        while(!isRunRound(runround, allComplete)){
            temp = Integer.parseInt(runround) + 1;
            runround = Integer.toString(temp);
        }

        return runround;

    }

    /**
     * given a number (form of a string) and a long (for comparison to see if all digits have been encountered)
     * return whether or not the number is runaround
     * first checks whether all digits are unique using digit (HashSet<Character>)
     * then walks through the number using the digits to find the next index
     * @param num
     * @param allComplete
     * @return
     */
    public boolean isRunRound(String num, long allComplete){
        int numLength = num.length();
        /**
         * checking to see if all digits are unique
         * iterate through, compare with HashSet
         */
        HashSet<Character> digits = new HashSet<>();
        for(int i = 0; i < numLength; i++){
            digits.add(num.charAt(i));
        }
        if(digits.size() != numLength){
            return false;
        }

        /**
         * track used digits/indexes using a long (bitmask)
         * nextIndex is calculated by (value of digit + currentIndex)%numLength
         * check if usedDigits has already recorded the current digit, if not, set the digit to 1 and find nextIndex
         */
        long usedDigits = 1;
        int nextIndex = (num.charAt(0)-'0')%numLength;
        while(((usedDigits >>> nextIndex) & 1) == 0){
            usedDigits |= (1 << nextIndex);
            nextIndex = (nextIndex+(num.charAt(nextIndex)-'0'))%numLength;
        }
        //if both conditions (end back at index 0 and every digit has been used (comparison with allComplete)) return true
        if(nextIndex == 0 && usedDigits == allComplete){
            return true;
        }else{
            return false;
        }
    }

}
