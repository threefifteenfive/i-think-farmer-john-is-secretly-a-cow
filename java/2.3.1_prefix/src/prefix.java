/*
ID: andrew.83
LANG: JAVA
TASK: prefix
*/

/**
 * Efficiency Note:
 * https://stackoverflow.com/questions/1532461/stringbuilder-vs-string-concatenation-in-tostring-in-java
 * The key is whether you are writing a single concatenation all in one place or accumulating it over time.
 * For the example you gave, there's no point in explicitly using StringBuilder. (Look at the compiled code for your first case.)
 * But if you are building a string e.g. inside a loop, use StringBuilder.
 * To clarify, assuming that hugeArray contains thousands of strings, code like this:
 * ...
 * String result = "";
 * for (String s : hugeArray) {
 *     result = result + s;
 * }
 *
 * is very time- and memory-wasteful compared with:
 *
 * ...
 * StringBuilder sb = new StringBuilder();
 * for (String s : hugeArray) {
 *     sb.append(s);
 * }
 * String result = sb.toString();
 */

import java.io.*;
import java.util.*;
import java.time.*;

/**
 * Longest Prefix (2.3.1)
 *
 * given a string of characters and a list of "primitives"
 *  primitives: unique strings (less than length of overall string)
 * return the length of the longest possible section (from beginning) that is a sequence of only of the primitives
 *
 * First successful runs:
 *    Test 1: TEST OK [0.100 secs, 23996 KB]
 *    Test 2: TEST OK [0.107 secs, 23868 KB]
 *    Test 3: TEST OK [0.107 secs, 24056 KB]
 *    Test 4: TEST OK [0.119 secs, 24224 KB]
 *    Test 5: TEST OK [0.107 secs, 24412 KB]
 *    Test 6: TEST OK [0.352 secs, 35596 KB]
 * NOTE:
 *  Algo was successfully written but input reading was inefficient and slow.
 *  Test case 6 first run was 2.3 secs (just because of inefficient input reading)
 *  Needed to combine 3000+ strings into one string.
 *      Initially used + operator (inefficient, Strings are immutable objects)
 *          in order to change Strings, the old one is destroyed and new memory is allocated (3000+ times is too much)
 *      Used StringBuilder object (much more flexible and efficient)
 *          in order to change String, the old memory still cleared but 2*current memory is allocated (far less memory)
 */
public class prefix {

    public static void main(String[] args){

        try{
            prefix p = new prefix();
            //System.out.println(p.work("prefix.in"));
            Instant start = Instant.now();
            PrintWriter out = new PrintWriter("prefix.out");
            out.println(p.work("prefix.in"));
            out.close();
            Instant end = Instant.now();
            Duration d = Duration.between(start, end);
            System.out.println(d.toMillis());
        }catch(Exception e){
            System.out.println(e.toString());
        }

    }

    public int work(String filename)throws Exception{
        int maxPrefix = 0;

        /**
         * reading input
         * first section is simple (primitives)
         * read lines until the period "."
         * store each primitive in HashMap<Char, String>
         *  key: first char in primitive | value: list of all primitives that begin with that letter
         */
        BufferedReader in = new BufferedReader(new FileReader(filename));
        StringTokenizer st = new StringTokenizer(in.readLine());
        HashMap<Character, ArrayList<String>> primitiveTokens = new HashMap<>();
        String tempToken = st.nextToken();
        while(!tempToken.equals(".")){
            //use addToken() method to store primitives into the list (value) linked to the first character (key)
            addToken(tempToken.charAt(0), primitiveTokens, tempToken);
            if(!st.hasMoreTokens()){
                st = new StringTokenizer(in.readLine());
            }
            tempToken = st.nextToken();
        }

        /**
         * reading input (cont.)
         * second section is more complicated (creating overall string)
         * each line only contains 76 characters
         *  Ex: test case 6 had 3000 LINES (200000+ characters)
         * using + operator to concatenate is too expensive and time-consuming
         * use StringBuilder instead
         */
        StringBuilder s = new StringBuilder();
        st = new StringTokenizer(in.readLine());
        while(st.hasMoreTokens()){
            s.append(st.nextToken());
            String next = in.readLine();
            if(next != null) {
                st = new StringTokenizer(next);
            }
        }
        String sequence = s.toString();

        /**
         * stores the indexes that are possible continuations of prefix in boolean array (markerPoints)
         *  true = this index is a possible continuation
         *  false = it is impossible to reach this point using any combination of primitives
         * index 0 is set to true, try every primitive that begins with correct character, set their end indexes to true
         * Ex:
         * A AB BA CA BBC (primitives)
         * ABABACABAAB C (string)
         * 01234567891011
         * markerPoints[0] (starting point)
         *  string(0) = 'A'
         *  possible primitives = A, AB
         *  try A, markerPoints[1] = true
         *  try AB, markerPoints[2] = true
         * update maxPrefix to the highest starting point found
         * continue to end of string
         */
        boolean[] markerPoints = new boolean[sequence.length()];
        markerPoints[0] = true;

        for(int point = 0; point < sequence.length(); point++){
            if(markerPoints[point]){
                //identifying character
                char letter = sequence.charAt(point);
                //trying all possible primitives given the character (if the character has primitives linked to it)
                if(primitiveTokens.containsKey(letter)) {
                    //trying all available primitives
                    for (String primitive : primitiveTokens.get(letter)) {
                        int len = primitive.length();
                        //checking to see if primitive is valid
                        if (sequence.regionMatches(point, primitive, 0, len)) {
                            int mPoint = point + len;
                            //avoid indexOutOfBounds
                            // (at the end, cannot set the next starting point to true because it is out of bounds)
                            if(point+len < sequence.length()) {
                                markerPoints[mPoint] = true;
                            }
                            //updating maxPrefix
                            if(mPoint > maxPrefix){
                                maxPrefix = mPoint;
                            }
                        }
                    }
                }
            }
        }

        return maxPrefix;
    }

    /**
     * stores a string (primitive) into a list inside a HashMap according to the string's first character
     * @param key
     * @param pTokens
     * @param value
     */
    public void addToken(char key, HashMap<Character, ArrayList<String>> pTokens, String value){
        if(pTokens.containsKey(key)){
            pTokens.get(key).add(value);
        }else{
            ArrayList<String> tempList = new ArrayList<>();
            tempList.add(value);
            pTokens.put(key, tempList);
        }
    }

}
