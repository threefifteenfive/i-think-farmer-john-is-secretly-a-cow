/*
ID: andrew.83
LANG: JAVA
TASK: contact
*/

import java.io.*;
import java.util.*;

/**
 * Contact (USACO 3.1.4)
 *
 * given a sequence of '1's and '0's and 3 integers A,B,N
 * return the N most frequent patterns of '1's and '0's of lengths between A and B (inclusive)
 *
 * Ex:
 * A B N
 * 2 4 10
 * 01010010010001000111101100001010011001111000010010011110010000000
 * return
 * 23
 * 00
 * 15
 * 01 10
 * 12
 * 100
 * 11
 * 11 000 001
 * 10
 * 010
 * 8
 * 0100
 * 7
 * 0010 1001
 * 6
 * 111 0000
 * 5
 * 011 110 1000
 * 4
 * 0001 0011 1100
 *
 * 23
 * 00
 * (pattern 00 occurs 23 times)
 * 15
 * 01 10
 * (pattern 01 and pattern 10 both occur 15 times)[this only counts as 1 frequency]
 *
 * First Successful Runs
 *    Test 1: TEST OK [0.098 secs, 23816 KB]
 *    Test 2: TEST OK [0.096 secs, 23228 KB]
 *    Test 3: TEST OK [0.217 secs, 30216 KB]
 *    Test 4: TEST OK [0.173 secs, 25752 KB]
 *    Test 5: TEST OK [0.343 secs, 39008 KB]
 *    Test 6: TEST OK [0.296 secs, 39484 KB]
 *    Test 7: TEST OK [0.399 secs, 39836 KB]
 */

/**
 * pattern has an id and count
 * String id is the pattern itself
 * int count is the number of times it occurs
 */
class pattern{

    String id;
    int count;

    public pattern(String p){
        id = p;
        count = 0;
    }

    public int getCount(){ return count; }

    public String getPattern(){ return id; }

    public void increment(){ count++; }

}

public class contact {

    public static void main(String[] args){

        try{
            contact c = new contact();
            //System.out.println(c.work("contact.in"));
            PrintWriter out = new PrintWriter("contact.out");
            out.println(c.work("contact.in"));
            out.close();
        }catch(Exception e){
            System.out.println(e.toString());
        }

    }

    public String work(String filename)throws Exception{
        String rt = "";

        /**
         * reading input
         * use StringBuilder to avoid expensive String concatenation operations
         */
        BufferedReader in = new BufferedReader(new FileReader(filename));
        StringTokenizer st = new StringTokenizer(in.readLine());
        int A = Integer.parseInt(st.nextToken());
        int B = Integer.parseInt(st.nextToken());
        int N = Integer.parseInt(st.nextToken());
        StringBuilder sb = new StringBuilder();
        String tempLine = in.readLine();
        while(tempLine != null){
            sb.append(tempLine);
            tempLine = in.readLine();
        }
        String sequence = sb.toString();

        /**
         * using BFS-like code to make all combinations up to length B
         *  need to have all patterns length 1 and below A in order to build up
         *  only save patterns that are length [A,B]
         */
        int tempA = 1;
        //stores longest patterns
        LinkedList<String> queue = new LinkedList();
        //start with 0 and 1 individually
        queue.add("0");
        queue.add("1");
        //patterns stores all patterns (String id tied to a pattern object)
        HashMap<String, pattern> patterns = new HashMap<>();
        /*
        nextPatterns stores the follow up for each pattern (String id tied to a String[] array of size 2)
        Ex:
        pattern "01" stores array
            array[0] contains "010"
            array[1] contains "011"
         */
        HashMap<String, String[]> nextPatterns = new HashMap<>();
        ArrayList<pattern> allPatterns = new ArrayList<>();
        while(tempA <= B){
            //tempList contains all of the patterns of next generation, avoid infinite loop
            ArrayList<String> tempList = new ArrayList<>();
            while(!queue.isEmpty()){
                String tempString = queue.removeFirst();
                //create new pattern object
                pattern tempPattern = new pattern(tempString);
                //store in HashMap
                patterns.put(tempString, tempPattern);
                //if greater than A, put into nextPatterns
                if(tempA >= A) {
                    allPatterns.add(tempPattern);
                    //if pattern is length B, we don't care about the patterns that build from it
                    if(tempA < B) {
                        String[] tempArray = new String[2];
                        tempArray[0] = tempString + "0";
                        tempArray[1] = tempString + "1";
                        nextPatterns.put(tempString, tempArray);
                    }
                }
                tempList.add(tempString + "0");
                tempList.add(tempString + "1");
            }
            queue.addAll(tempList);
            tempA++;
        }

        /**
         * iterate through the sequence in chunks of length A
         * Ex:
         * A = 2, B = 4
         * 01111010
         * ^^ first check (2) [01]
         * ^ ^ next check (3) [011]
         * ^  ^ last check (4) [0111]
         * next iteration
         *  ^^ first check (2) [11]
         *  ^ ^ next check (3) [111]
         *  ^  ^ last check (4) [1111]
         */
        for(int index = 0; index <= sequence.length()-A; index++){
            //get minimum pattern, increment corresponding object
            String section = sequence.substring(index, index+A);
            patterns.get(section).increment();
            //check each subsequent length <= B
            inner : for(int next = A; next < B; next++){
                //avoid outofbounds exception
                if(index+next >= sequence.length()){
                    break inner;
                }else {
                    //update String section depending on next char
                    if (sequence.charAt(index + next) == '0') {
                        section = nextPatterns.get(section)[0];
                    }else{
                        section = nextPatterns.get(section)[1];
                    }
                    //increment corresponding object
                    patterns.get(section).increment();
                }
            }
        }

        //sorting all patterns by their count
        Collections.sort(allPatterns, new Comparator<pattern>() {
            public int compare(pattern obj1, pattern obj2) {
                //"-" used to get it in descending order
                return -(obj1.getCount() - obj2.getCount());
            }
        });

        //using StringBuilder
        //formatting output
        StringBuilder rtPatterns = new StringBuilder();
        int curFreq = Integer.MAX_VALUE;
        int numFreq = 0;
        int numLine = 0;
        for(int i = 0; i < allPatterns.size(); i++){
            //only put N or less frequencies of counts for patterns
            if(numFreq < N){
                pattern temp = allPatterns.get(i);
                int tempCount = temp.getCount();
                //don't record patterns with counts of 0
                if(tempCount == 0){
                    break;
                }
                //multiple patterns can share a line if they have the same counts
                if(curFreq != tempCount){
                    if(i != 0){
                        rtPatterns.append("\n");
                    }
                    rtPatterns.append(tempCount + "\n" + temp.getPattern());
                    curFreq = tempCount;
                    numFreq++;
                    numLine = 1;
                }else{
                    if(numLine%6 == 0){
                        rtPatterns.append("\n");
                    }else{
                        rtPatterns.append(" ");
                    }
                    rtPatterns.append(temp.getPattern());
                    numLine++;
                }
            }else{
                //checking to see that the last frequency gets ALL patterns that have that frequency
                pattern temp = allPatterns.get(i);
                while(i < allPatterns.size() && temp.getCount() == curFreq){
                    if(numLine%6 == 0){
                        rtPatterns.append("\n");
                    }else{
                        rtPatterns.append(" ");
                    }
                    rtPatterns.append(temp.getPattern());
                    numLine++;
                    i++;
                    temp = allPatterns.get(i);
                }
            }
        }
        rt = rtPatterns.toString();
        return rt;
    }

}
