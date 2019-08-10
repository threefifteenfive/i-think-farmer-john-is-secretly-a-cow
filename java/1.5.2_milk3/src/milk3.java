/*
ID: andrew.83
LANG: JAVA
TASK: milk3
*/

import java.util.*;
import java.io.*;

/**
 * Breadth-First Search:
 *  - keep adding to a queue until nothing is left
 *  - "edges" (previously visited nodes) are tracked and avoided
 *
 * What are all the possible liters of milk that  bucket C can have after any number of pours  if:
 *  there are three buckets A, B, and C
 *  each has a capacity of 1 to 20 (inclusive) liters
 *  pouring either empties the bucket entirely or fills up the other bucket entirely
 *      (C = 10, has 10 liters. A = 5, has 0 liters. C pouring into A -> A has 5, C has 5)
 *  bucket A is empty
 */
public class milk3 {

    public static void main(String[] args){

        try{
            milk3 m3 = new milk3();
            System.out.println(m3.work("milk3.in"));
            PrintWriter out = new PrintWriter("milk3.out");
            out.println(m3.work("milk3.in"));
            out.close();
        }catch(Exception e){
            System.out.println(e.toString());
        }

    }

    public String work(String filename)throws Exception{
        String possibleAmounts = "";

        /**
         * reading input (a b c)
         * a is the capacity of bucket A, b is the capacity of bucket B, c is the capacity of bucket C
         */
        BufferedReader in = new BufferedReader(new FileReader(filename));
        StringTokenizer st = new StringTokenizer(in.readLine());
        int aBucket = Integer.parseInt(st.nextToken());
        int bBucket = Integer.parseInt(st.nextToken());
        int cBucket = Integer.parseInt(st.nextToken());

        /**
         * initializing necessary containers.
         * capacities stores the capacities of each bucket.
         * initialCombo stores the initial state of each bucket
         * queue is a LinkedList (queue) that contains and processes each possible distribution of milk
         *  stores combos as int[]
         *  index 0 = bucket A, index 1 = bucket B, index 2 = bucket C
         * used stores the processed combos as strings, allows queue to stop running
         * pAmounts stores the possible volumes of C given A is empty
         */
        int[] capacities = {aBucket, bBucket, cBucket};
        int[] initialCombo = {0, 0, cBucket};
        LinkedList<int[]> queue = new LinkedList<>();
        HashSet<String> used = new HashSet<>();
        HashSet<Integer> pAmounts = new HashSet<>();

        /**
         * exploring all possible distributions of milk among buckets A, B and C
         * combos at the front of the queue are removed and their following distributions are added to the end of queue
         * the ID of a combo is made using getComboID() and stored in used
         * if the ID of a combo exists in used, the no need to process this combo
         * if bucket A is empty, the value of bucket C is added to pAmounts as an answer.
         * Ex: 0 0 15 (first combo) (A = 5, B = 11, C = 15)
         * ID = 000015 & 15 is added to pAmounts
         * next combos 5 0 10, 0 11 4 are added to end of queue
         */
        queue.add(initialCombo);
        while(queue.size() != 0){
            int[] tempCombo = queue.remove(0);
            String tempComboID = getComboID(tempCombo);
            if(!used.contains(tempComboID)){
                if(tempCombo[0] == 0){
                    pAmounts.add(tempCombo[2]);
                }
                queue.addAll(nextCombos(tempCombo, capacities));
            }
            used.add(tempComboID);
        }


        //all answers are added to a list to be sorted and returned as a string
        ArrayList<Integer> answers = new ArrayList<>(pAmounts);
        Collections.sort(answers);
        for(Integer i : answers){
            possibleAmounts += (i + " ");
        }

        //removing the space because of USACO requirements
        return possibleAmounts.substring(0, possibleAmounts.length()-1);
    }

    /**
     * returns the unique ID of a distribution of milk among three buckets
     *  each number is represented as a two-digit code (i.e. 1 -> 01)
     *  Ex: avoid false duplicates
     *      single-digit concatenation: 0 11 4 -> 0114 | 0 1 14 -> 0114
     *      double-digit concatentation: 0 11 4 -> 001104 | 0 1 14 -> 000114
     * @param combo
     * @return
     */
    public String getComboID(int[] combo){
        String id = "";
        for(int i : combo){
            id += i;
            id += '.';
        }
        return id;
    }

    /**
     * returns a list of possible combinations/distributions of milk given the root combo as a starting point
     *  checks each bucket's volume
     *  if the bucket is empty, try next one
     *  if the bucket has something (full or partly-full), create next two possible combinations
     *  first combo is pouring into one of the other two buckets
     *  second combo is pouring into other one of the other two buckets
     *  Ex: 0 6 9 (A = 5 B = 11 C = 15)
     *  try 0, empty, do nothing
     *  try 6: creates 5 1 9, 0 0 15
     *  try 9: creates 5 6 4, 0 11 4
     *  returns list {5 1 9, 0 0 15, 5 6 4, 0 11 4}
     * @param root
     * @param capacities
     * @return
     */
    public ArrayList<int[]> nextCombos(int[] root, int[] capacities){
        ArrayList<int[]> nextCombos = new ArrayList<>();
        //test each bucket
        for(int pourFrom = 0; pourFrom < 3; pourFrom++){
            int tempAmount = root[pourFrom];
            //if something is in there, create next possibilities
            if(tempAmount != 0){
                //two possible combos are created. pouring into one of two other buckets
                for(int j = 1; j <= 2; j++){
                    //what bucket to pour into
                    int pourTo = (pourFrom+j)%3;
                    //can we pour into that bucket?
                    if(root[pourTo] < capacities[pourTo]){
                        //since we can pour into that bucket an change distribution, create possible combo
                        int[] combo = new int[3];
                        /**
                         * identifying the untouched bucket. indexes 0 1 2
                         * already selected two distinct indexes for pourTo and pourFrom
                         * untouched = 3 - (pourTo+pourFrom)
                         * Ex: pourTo = 0 pourFrom = 2 -> 3 - (0+2) = 1
                         */
                        int unTouched = 3-(pourTo + pourFrom);
                        //untouched bucket remains the same
                        combo[unTouched] = root[unTouched];
                        int pAmount = tempAmount;
                        //pouring milk from pourFrom to pourTo until pourFrom is empty or pourTo is full
                        combo[pourTo] += (root[pourTo] + pAmount);
                        combo[pourFrom] = 0;
                        if(combo[pourTo] > capacities[pourTo]){
                            combo[pourFrom] += (combo[pourTo] - capacities[pourTo]);
                            combo[pourTo] = capacities[pourTo];
                        }
                        nextCombos.add(combo);
                    }
                }
            }
        }
        return nextCombos;
    }

}
