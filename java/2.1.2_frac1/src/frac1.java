/*
ID: andrew.83
LANG: JAVA
TASK: frac1
*/

import java.util.*;
import java.io.*;

/**
 * given a number N
 * return a sorted unique list of all possible fractions with denominators less than or equal to N
 * Ex:
 * N = 5
 * 0/1
 * 1/5
 * 1/4
 * 1/3
 * 2/5
 * 1/2
 * 3/5
 * 2/3
 * 3/4
 * 4/5
 * 1/1
 */

/**
 * stores the numerator and denominator
 * calculates the decimal value of the fraction for storage/sorting purposes
 *  lazy loading: calculate once instead of recalculating each time using stored values (num/denom)
 */
class Fraction{
    private int numerator;
    private int denominator;
    private double value;

    public Fraction(int num, int denom){
        numerator = num;
        denominator = denom;
        value = ((double)(num)) / ((double)(denom));
    }

    public String toString(){
        return numerator + "/" + denominator;
    }

    public double getValue(){
        return value;
    }
}

/**
 * custom comparator for sorting
 */
class SortFraction implements Comparator<Fraction>{

    public int compare(Fraction a, Fraction b){
        if(a.getValue() > b.getValue()){
            return 1;
        }else{
            return -1;
        }
    }

}

public class frac1 {

    private int[] smallPrimes;

    /**
     * simplifying fractions requires division by prime numbers
     * dividing by all primes less than 160/2
     */
    public frac1(){
        int[] temp = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79};
        smallPrimes = temp;
    }

    public static void main(String[] args){
        try{
            frac1 f1 = new frac1();
            PrintWriter out = new PrintWriter("frac1.out");
            out.print(f1.work("frac1.in"));
            out.close();
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }

    public String work(String filename)throws Exception{
        String orderedFractions = "";

        //reading input
        BufferedReader in = new BufferedReader(new FileReader(filename));
        StringTokenizer s0 = new StringTokenizer(in.readLine());
        int N = Integer.parseInt(s0.nextToken());
        ArrayList<Fraction> allFractions = new ArrayList<Fraction>();

        /**
         * creating corner cases 0/1 and 1/1 will never be overridden (never touch 0 or = denominator)
         * tests if fraction can be simplified
         *  if can be simplified, no need to store it
         */

        allFractions.add(new Fraction(0,1));
        allFractions.add(new Fraction(1,1));
        for(int denom = 1; denom <= N; denom++){
            for(int num = 1; num < denom; num++){
                if(!canSimplify(num, denom)){
                    allFractions.add(new Fraction(num, denom));
                }
            }
        }

        /**
         * sorting using custom comparator
         */
        Collections.sort(allFractions, new SortFraction());
        for(Fraction f : allFractions){
            orderedFractions += f.toString() + "\n";
        }

        return orderedFractions;
    }

    /**
     * checking to see if fraction can be simplified
     * only need to check primes less than or equal to half of largest term
     * @param num
     * @param denom
     * @return
     */
    public boolean canSimplify(int num, int denom){
        for(int prime : smallPrimes){
            if(prime <= denom/2){
                if(num%prime == 0 && denom%prime == 0){
                    return true;
                }
            }else{
                break;
            }
        }
        return false;
    }

}
