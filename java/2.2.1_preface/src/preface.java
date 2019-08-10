/*
ID: andrew.83
LANG: JAVA
TASK: preface
*/

import java.util.*;
import java.io.*;

/**
 * Preface Numbering (2.2.1)
 *
 * given number N (decimal)
 * return the number of each roman numeral used when expressing each number from 1 to N (inclusive)
 * Ex:
 * 5 (N)
 *  1,2,3,4,5 -> I, II, III, IV, V (7 I's, 2 V's)
 * return
 *  I 7
 *  V 2
 * first successful run (first try, first time this worked first try, first time beating Dad [prob cuz i did roman numerals b4])
 *    Test 1: TEST OK [0.093 secs, 23828 KB]
 *    Test 2: TEST OK [0.098 secs, 23596 KB]
 *    Test 3: TEST OK [0.107 secs, 24112 KB]
 *    Test 4: TEST OK [0.117 secs, 24728 KB]
 *    Test 5: TEST OK [0.126 secs, 26376 KB]
 *    Test 6: TEST OK [0.154 secs, 29492 KB]
 *    Test 7: TEST OK [0.156 secs, 29548 KB]
 *    Test 8: TEST OK [0.170 secs, 30948 KB]
 *    avg time = 0.1276 secs
 * second successful run (using switch() and charAt method instead of using HashMap<> and .substring())
 *    Test 1: TEST OK [0.093 secs, 23856 KB]
 *    Test 2: TEST OK [0.093 secs, 23840 KB]
 *    Test 3: TEST OK [0.093 secs, 23916 KB]
 *    Test 4: TEST OK [0.114 secs, 25588 KB]
 *    Test 5: TEST OK [0.135 secs, 26068 KB]
 *    Test 6: TEST OK [0.140 secs, 28512 KB]
 *    Test 7: TEST OK [0.145 secs, 29152 KB]
 *    Test 8: TEST OK [0.145 secs, 29344 KB]
 *    avg time = 0.11975 secs
 */

public class preface {

    //result is parallel array of rChar
    //index of I = 0 (rChar), index 0 of result stores the number of I's counted
    private final String[] rChar = {"I", "V", "X", "L", "C", "D", "M"};
    int[] result = new int[7];

    /**
     * given a character (must be I,V,X...)
     * return the index of result
     * @param ch
     * @return
     */
    private int getIdx(char ch) {
        switch(ch) {
            case 'I':
                return 0;
            case 'V':
                return 1;
            case 'X':
                return 2;
            case 'L':
                return 3;
            case 'C':
                return 4;
            case 'D':
                return 5;
            case 'M':
                return 6;
        }
        return -1;
    }

    public preface(){
        Arrays.fill(result, 0);
    }

    public static void main(String[] args){

        try{
            preface p = new preface();
            //System.out.print(p.work("preface.in"));
            PrintWriter out = new PrintWriter("preface.out");
            out.print(p.work("preface.in"));
            out.close();
        }catch(Exception e){
            System.out.println(e.toString());
        }

    }

    public String work(String filename)throws Exception{
        String numLetters = "";

        //reading input, storing N as pageNum
        BufferedReader in = new BufferedReader(new FileReader(filename));
        StringTokenizer st = new StringTokenizer(in.readLine());
        int numPages = Integer.parseInt(st.nextToken());

        /**
         * iterate through each number from 1 to pageNum (inclusive)
         * convert pageNum into roman numeral form
         * check each char in pageNum (roman form) and increment corresponding index in result
         */
        for(int pageNum = 1; pageNum <= numPages; pageNum++){
            String roman = makeRoman(pageNum);
            for(int i = 0; i < roman.length(); i++){
                int index = getIdx(roman.charAt(i));
                result[index]++;
            }
        }

        /**
         * if a char in rChar is used, add it to the return along with the number of times it is used
         */
        for(int i = 0; i < result.length; i++){
            if(result[i] != 0){
                numLetters += rChar[i] + " " + result[i] + "\n";
            }
        }
        return numLetters;
    }

    /**
     * given a number in decimal form, return it as a roman numeral (string) (number has to be less than 3500)
     * iterate through the number using % & / to get each digit and convert it to its corresponding roman token
     * tenMult tracks the location of the digit, use to access character in rChar
     * because rChar = 0-I-1 | 1,V,5 | 2,X,10 | 3,L,50 | 4,C,100 | 5,D,500 | 6,M,1000
     * when dealing with one's digit, require indexes 0,1,2
     * when dealing with ten's digit, require indexes 2,3,4
     * when dealing with hundred's digit, require indexes 4,5,6
     * @param num
     * @return
     */
    public String makeRoman(int num){
        String roman = "";
        int tenMult = 0;
        while(num > 0){
            String temp = "";
            //isolating the right most digit
            int digit = num%10;
            /**
             * if greater than 5, two options
             *  greater than 8 (lowest character of this tier + next tier character) [Ex: 9 = I(1) + X(10) if 90 = X(10) + C(100)]
             *  less than 9 (middle character of this tier + necessary lowest character of this tier)
             *      [Ex: 6 = V(5) + I(1) 7 = V(5) + I(1) + I(1)...]
             * less than 5 is the similar thing but inverted (two options)
             * only remaining case is that digit = 5
             */
            if(digit > 5) {
                if (digit > 8) {
                    temp += rChar[(2 * tenMult)] + rChar[(2 * tenMult) + 2];
                } else{
                    int trail = digit-5;
                    temp += rChar[(2*tenMult)+1];
                    for(int i = 0; i < trail; i++){
                        temp += rChar[2*tenMult];
                    }
                }
            }else if(digit < 5){
                if(digit < 4){
                    for(int i = 0; i < digit; i++){
                        temp += rChar[2*tenMult];
                    }
                }else{
                    temp += rChar[2*tenMult] + rChar[(2*tenMult)+1];
                }
            }else{
                temp = rChar[(2*tenMult)+1];
            }
            tenMult++;
            num /= 10;
            roman = temp + roman;
        }
        return roman;
    }

}
