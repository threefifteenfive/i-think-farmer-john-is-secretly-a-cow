/*
ID: andrew.83
LANG: JAVA
TASK: pprime
*/

import java.util.*;
import java.io.*;
import java.time.*;

/**
 * compile a list of all palindromes in a given range that are also prime numbers.
 * ranges [a,b] can be from a >= 5 && b <= 10,000,000
 * Ex:
 * a = 5, b = 10,000
 * 5 7 11 101 131 151 181 191 313 353 373 383 727 757 787 797 919 929
 * utilized Instant and Duration for tracking time
 * recursive method to create combinations for the middle
 */
public class pprime {

    private ArrayList<String> preset;
    private int[] oddNumbers;

    public pprime(){
        int[] tempOdd = {1, 3, 5, 7, 9};
        oddNumbers = tempOdd;
        String[] temp = {"5","7","11"};
        preset = new ArrayList<String>(Arrays.asList(temp));
    }

    public static void main(String[] args){

        try{
            pprime pp = new pprime();
            System.out.print(pp.work("pprime.in"));
            PrintWriter out = new PrintWriter("pprime.out");
            //creating start time
            Instant start = Instant.now();
            out.print(pp.work("pprime.in"));
            out.close();
            //creating end time
            Instant end = Instant.now();
            //finding the time elapsed
            Duration duration = Duration.between(start, end);
            System.out.println("time elpased: " + duration.toMillis() + "ms");
        }catch(Exception e){
            System.out.println(e.toString());
        }

    }

    public String work(String filename)throws Exception{
        String allPalPrimes = "";

        /**
         * reading input, finding the low and high range
         * because low range is used and changed during program, trueLow is created to remember original lower bound
         */
        BufferedReader in = new BufferedReader(new FileReader(filename));
        StringTokenizer st = new StringTokenizer(in.readLine());
        int lowRange = Integer.parseInt(st.nextToken());
        int trueLow = lowRange;
        int highRange = Integer.parseInt(st.nextToken());

        /**
         * creating a list of the palindromes
         * because only 5, 7, 11 are palindromes and primes under 100, can skip directly to 100 if necessary
         * 5, 7, 11 are added into palindromes immediately using preset
         * skip if lowRange is under 100 because no need to drop down to 100 if lowRange is already above it
         */
        ArrayList<String> palindromes = new ArrayList<>(preset);
        if(highRange > 100 && lowRange < 100){
            lowRange = 100;
        }

        /**
         * find the possible first and last digits (ones digit and first digit)
         * ABCBA (A)
         * create possible palindromes using these markers using getPalindromes()
         */
        while(lowRange <= highRange){
            //finding the first digit
            //setting the length of the number
            int first = lowRange;
            int length = 1;
            while(first >= 10){
                first /= 10;
                length++;
            }
            /**
             * if the first digit is even or five, it cannot be a prime palindrome
             * 232 is not prime and cannot be because it is even
             * anything that ends in 5 is not prime (except for 5)
             */
            if(first == 2 || first == 6 || first == 8){
                //if 200, += 100 to get to valid 300 range
                //if 6000, += 1000 to get to valid 7000 range
                //if 80000, += 10000 to get to valid 90000 range
                lowRange += (int)Math.pow(10, length-1);
            }else if(first == 4){
                //if 400, skip directly to 700 because 600 and 500 don't have any possible palindromic primes
                lowRange += 3 * (int)Math.pow(10, length-1);
            }else if(first == 5){
                //in case of starting at a number that begins with 5
                lowRange += 2 * (int)Math.pow(10, length-1);
            }else{
                /**
                 * calling on getPalindromes() to get all palindromes for:
                 *  begin with A and end with A
                 *  between A * 10^n + 10^n
                 */
                palindromes.addAll(getPalindromes(first, length));
                lowRange += (int)Math.pow(10, length-1);
            }
        }

        //USACO wants it to be returned sorted
        ArrayList<Integer> why = new ArrayList<Integer>();

        /**
         * checking whether or not the palindrome is prime using optimized school method
         * original idea of dividing n by every prime less than half of n is too expensive
         * second idea of implementing AKS primality test is too expensive because of necessary coefficient construction
         * checks to see if pal is in range (removes 5, 7, 11 if lowRange > 11 and possible clipping bounds)
         * Ex:
         * high range 950
         * palindromes will contain {909, 919, 929, 939, 949, (following palindromes out of range) 959, 969, 979, 989, 999}
         */
        for(String pal : palindromes){
            int temp = Integer.parseInt(pal);
            if(temp >= trueLow  && temp <= highRange){
                if(is_prime_optimized_school_way(temp)){
                    why.add(temp);
                }
            }
        }

        //sorting valid palindromic primes
        Collections.sort(why);
        for(Integer i : why){
            allPalPrimes += i.toString() + "\n";
        }

        return allPalPrimes;
    }

    /**
     * returns list of palindromes that begin and end with int sides and are of size length
     * uses recursive getMidSet() to fill in the middle
     * @param sides
     * @param length
     * @return
     */
    public ArrayList<String> getPalindromes(int sides, int length){
        ArrayList<String> palSet = new ArrayList<>();
        ArrayList<String> midSet = getMidSet(length-2);
        for(String mid : midSet){
            palSet.add(sides + mid + sides);
        }
        return palSet;
    }

    /**
     * takes in a length to create a set of the middles
     * if length is even, the middle two numbers will be the same
     * if length is odd, the middle number will be unique
     * moves in to the center first before adding onto the sides
     * @param totalLength
     * @return
     */
    public ArrayList<String> getMidSet(int totalLength){
        ArrayList<String> palSet = new ArrayList<>();
        if(totalLength == 1){
            for(int i = 0; i < 10; i++) {
                palSet.add(Integer.toString(i));
            }
        }else if(totalLength == 2){
            for(int i = 0; i < 10; i++) {
                palSet.add(Integer.toString(i) + Integer.toString(i));
            }
        } else if(totalLength > 2){
            palSet = getMidSet(totalLength-2);
            int start = palSet.get(0).length();
            while(palSet.get(0).length() == start){
                String tempMid = palSet.remove(0);
                for(int i = 0; i < 10; i++){
                    palSet.add(i + tempMid + i);
                }
            }
        }
        return palSet;
    }

    /**
     * School Method
     * A simple solution is to iterate through all numbers from 2 to n-1 and for every number check if it
     * divides n. If we find any number that divides, we return false
     *
     * We can do following optimizations:
     *
     * Instead of checking till n, we can check till sqrt(n) because a larger factor of n must be a multiple of
     * smaller factor that has been already checked.
     * The algorithm can be improved further by observing that all primes are of the form 6k +/- 1, with the
     * exception of 2 and 3. This is because all integers can be expressed as (6k + i) for some integer k and
     * for i = ?1, 0, 1, 2, 3, or 4; 2 divides (6k + 0), (6k + 2), (6k + 4); and 3 divides (6k + 3).
     * So a more efficient method is to test if n is divisible by 2 or 3, then to check through all the
     * numbers of form 6k +/- 1. (Source: wikipedia)
     *
     * notes: aks requires huge coeff array, run out of array
     * @param n
     * @return
     */
    public static boolean is_prime_optimized_school_way(int n) {
        if (n <= 1) return false;
        if (n <= 3) return true;

        // This is checked so that we can skip
        // middle five numbers in below loop
        if (n % 2 == 0 || n % 3 == 0) return false;

        for (int i = 5; i * i <= n; i = i + 6)
            if (n % i == 0 || n % (i + 2) == 0)
                return false;

        return true;
    }

}
