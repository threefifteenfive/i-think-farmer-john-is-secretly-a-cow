/*
ID: andrew.83
LANG: JAVA
TASK: ariprog
*/

import java.util.*;
import java.io.*;

/**
 * stores the necessary parts of a sequence for return
 * a is the starting bi-square
 * b is the interval between each bi-square in the sequence
 */
class Sequence {
    public Sequence(int a_, int b_) {
        a=a_;
        b=b_;
    }
    public String toString() {
        return String.valueOf(a) + " " + String.valueOf(b);
    }
    public int getA() {
        return a;
    }
    public int getB() {
        return b;
    }
    int a;
    int b;
}

public class ariprog {

    public static void main(String[] args){

        try{
            ariprog a = new ariprog();
            System.out.println(a.work("ariprog.in"));
            PrintWriter out = new PrintWriter("ariprog.out");
            out.print(a.work("ariprog.in"));
            out.close();
        }catch(Exception e){
            System.out.println(e.toString());
        }

    }

    public String work(String filename)throws Exception{
        /**
         * reading input
         * n is the required length for a sequence to be considered an answer
         * m is the highest value p and q can be for calculating possible bi-squares (p^2 + q^2)
         */
        BufferedReader in = new BufferedReader(new FileReader(filename));
        StringTokenizer sl = new StringTokenizer(in.readLine());
        int n = Integer.parseInt(sl.nextToken());
        StringTokenizer sb = new StringTokenizer(in.readLine());
        int m = Integer.parseInt(sb.nextToken());

        /**
         * creating potential values to be used in a sequence using getBiSquares() method
         * (HashSet: unique values, temporary storage)
         * (int[]: easy to access, easy to sort)
         */
        String finalSequences = "";
        HashSet<Integer> bSValues = getBiSquares(m);
        /**
         * yes, you can create arraylist with any collection in constructor
         */
        ArrayList<Integer> list = new ArrayList<>(bSValues);
        /**
         * using lambda
         */
        Collections.sort(list, Comparator.comparingInt(p -> p.intValue()));
        /**
         * after java8, you can to this to int[] from arraylist<Integer></Integer>
         */
        int[] biSquares = list.stream().mapToInt(i -> i).toArray();


        /**
         * calculating all possible sequences of bi-squares
         * limit of bi-squares = sum of the greatest values of p squared * greatest values of q squared (2m^2)
         */
        ArrayList<Sequence> sequences = new ArrayList<>();
        int last = biSquares[biSquares.length-1];

        //tracking total sequences tried
        int totalCount = 0;

        /**
         * loops through all possible bi-squares as starting points (a)
         * uses the rest of the bi-squares to calculate potential gap values (b) and sequences
         *  try one of the next bi-squares (temp), b = the difference between temp and a
         *  if a complete sequence can exist (difference from a to last bi-square is less than (n-2) * b)
         *  from temp add b another n-2 times to see if a completed sequences exists
         *  if a sequences if complete, store a and b as sequence object in list
         */
        for(int start = 0; start < biSquares.length-n; start++){
            int a = biSquares[start];
            for(int next = start + 1; next < biSquares.length; next++){
                int b = biSquares[next] - a;
                int temp = biSquares[next];
                if(temp <= (last - ((n-2) * b))) {
                    //totalCount++;
                    int count = 1;
                    while (count < n && bSValues.contains(temp)) {
                        temp += b;
                        count++;
                    }
                    if (count == n) {
                        sequences.add(new Sequence(a, b));
                    }
                }
            }
        }

        /**
         * sorting the list of sequences by b value length (USACO requirement) and returning as a string
         * if there are no possible sequences, return NONE
         */
        sequences.sort(Comparator.comparingInt((Sequence p) -> p.getB()).thenComparingInt((Sequence p) -> p.getA()));
        if(sequences.size() == 0){
            finalSequences += "NONE\n";
        }else{
            for(Sequence s : sequences){
                finalSequences += s.getA() + " " + s.getB() + "\n";
            }
        }

        //returns number of sequences attempted (removed because its not part of USACO's output requirements)
        //finalSequences = totalCount + "\n" + finalSequences;
        return finalSequences;
    }

    /**
     * calculates all possible unique bi-squares given an upper bound to its calculation
     *  m is upper value of p and q for bi-square = p^2 + q^2
     * @param m
     * @return
     */
    public HashSet<Integer> getBiSquares(int m){
        HashSet<Integer> values = new HashSet<Integer>();
        for(int i = 0; i <= m; i++){
            for(int j = i; j <= m; j++){
                int bS = (int)Math.pow(i, 2) + (int)Math.pow(j, 2);
                values.add(bS);
            }
        }
        return values;
    }

}
