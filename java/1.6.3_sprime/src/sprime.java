/*
ID: andrew.83
LANG: JAVA
TASK: sprime
*/

import java.io.*;
import java.util.*;

/**
 * return a list of all the superprimes in a given length
 * superprimes (remove from the right): a prime number, that when its rightmost digits are removed, remains prime
 * Ex:
 *  7331 -> 733 -> 73 -> 7
 */
public class sprime {

    public static void main(String[] args){

        try{
            sprime sp = new sprime();
            System.out.print(sp.work("sprime.in"));
            PrintWriter out = new PrintWriter("sprime.out");
            out.print(sp.work("sprime.in"));
            out.close();
        }catch(Exception e){
            System.out.println(e.toString());
        }

    }

    public String work(String filename)throws Exception{
        String allSuperPrimes = "";

        /**
         * reading input
         * length is recorded
         */
        BufferedReader in = new BufferedReader(new FileReader(filename));
        StringTokenizer st = new StringTokenizer(in.readLine());
        int length = Integer.parseInt(st.nextToken());

        /**
         * staggered array lists are used to compute next length
         * single (Integer[]) is the initializer array for arrayList current
         * int[] addOn are the possible add on numbers
         * current stores the current values of the superprime
         *  each number in addOn is added to a each value in current and tested if it is prime
         *  all numbers valid numbers are added to next which is copied to current
         *  next is cleared and the next iteration begins
         * NOTES:
         *  2, 3, 5, 7 are the only prime numbers that can stand alone (left most digit must be one of these)
         *  2 cannot be the left most digit and neither can 5. 1 can be the left most digit.
         *  addOn is set to 1, 3, 7, 9 since these are the only possible numbers for the right most digits
         */
        ArrayList<Integer> next = new ArrayList<>();
        Integer[] single = {2, 3, 5, 7};
        ArrayList<Integer> current = new ArrayList<Integer>();
        current.addAll(Arrays.asList(single));
        int[] addOn = {1, 3, 7, 9};

        /**
         * current is already initialized to its starting possibilities (single)
         * loop builds up numbers for length-1 since current is already initialized
         * next is cleared at the beginning of each iteration
         * for each "base" in current, each "add-on" in addOn appended and the result is tested for primality
         * using optimized school method for primality test
         * valid numbers are added to next list
         * current is cleared and made a copy of next
         * next iteration begins
         */
        int curLength = 1;
        while(curLength < length){
            next.clear();
            for(Integer base : current){
                for(int add : addOn){
                    int temp = (base*10) + add;
                    if(is_prime_optimized_school_way(temp)){
                        next.add(temp);
                    }
                }
            }
            curLength++;
            current.clear();
            current.addAll(next);
        }

        for(Integer sp : current){
            allSuperPrimes += sp + "\n";
        }

        if(allSuperPrimes == ""){
            allSuperPrimes += "0\n";
        }

        return allSuperPrimes;
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
