/*
ID: andrew.83
LANG: JAVA
TASK: concom
*/

import java.io.*;
import java.util.*;

/**
 * Controlling Companies (2.3.5)
 *
 * given a list of company relations (how much one company owns another company)
 * return all of the companies that own other companies and who they own
 *  Company A controls B if:
 *      A = B (rarely used, don't care)
 *      A owns more than 50 stock of B
 *      A owns a series several companies that own stock in B that adds up to more than 50
 *          Ex:
 *          A owns C and D
 *          C owns 23 stock in B, D owns 40 stock in B
 *          Therefore A has 63 stock in B and therefore owns B
 *
 * Ex:
 * 1 2 80 (1 is a company name, 2 is the company that company 1 has stock in, 80 is the amount of stock 1 has in 2)
 * 2 3 80 (2 is a company name, 3 is the company that company 2 has stock in, 80 is the amount of stock 1 has in 3)
 * 3 1 20 (3 is a company name, 1 is the company that company 3 has stock in, 20 is the amount of stock 1 has in 1)
 *
 * return:
 *  1 2 (1 owns 2)
 *  1 3 (1 owns 3)
 *  2 3 (2 owns 3)
 * 1 has 80 (>50) stock in 2, therefore owns 2
 * 2 has 80 (>50) stock in 3, therefore owns 3
 * because 1 owns 2 and 2 owns 3, 1 owns 3
 *
 * Ex:
 * 2 3 25
 * 4 5 63
 * 2 1 48
 * 4 2 52
 * 5 3 30
 *
 * return:
 *  4 2 (4 owns 2)
 *  4 3 (4 owns 3)
 *  4 5 (4 owns 4)
 * 4 owns 2 and 5 directly (>50)
 * 2 has 25 stock of 3, 5 has 30 stock of 3, combined is 55 stock of 3: therefore 4 owns 3
 *
 * First successful run:
 *    Test 1: TEST OK [0.096 secs, 23012 KB]
 *    Test 2: TEST OK [0.121 secs, 23664 KB]
 *    Test 3: TEST OK [0.089 secs, 23664 KB]
 *    Test 4: TEST OK [0.093 secs, 23884 KB]
 *    Test 5: TEST OK [0.098 secs, 23772 KB]
 *    Test 6: TEST OK [0.112 secs, 23944 KB]
 *    Test 7: TEST OK [0.114 secs, 23908 KB]
 *    Test 8: TEST OK [0.173 secs, 26428 KB]
 *    Test 9: TEST OK [0.184 secs, 28352 KB]
 */
public class concom {

    public static void main(String[] args){

        try{
            concom cc = new concom();
            //System.out.println(cc.work("concom.in"));
            PrintWriter out = new PrintWriter("concom.out");
            out.print(cc.work("concom.in"));
            out.close();
        }catch(Exception e){
            System.out.println(e.toString());
        }

    }

    public String work(String filename) throws Exception{
        String allControl = "";

        BufferedReader in = new BufferedReader(new FileReader(filename));
        StringTokenizer st = new StringTokenizer(in.readLine());
        int numCompanies = Integer.parseInt(st.nextToken());

        //relations are stored as arrays of 3 int
        int[][] relations = new int[numCompanies][3];

        //need to track company names (numbers) and how many there are
        HashSet<Integer> uniqueCompanies = new HashSet<>();
        for(int rel = 0; rel < numCompanies; rel++){
            st = new StringTokenizer(in.readLine());
            //read a line of 3 ints, process each
            for(int sec = 0; sec < 3; sec++){
                int tempNum = Integer.parseInt(st.nextToken());
                //first two ints are company names, third int is not a name, don't add to uniqueCompanies
                if(sec != 2){
                    uniqueCompanies.add(tempNum);
                }
                //put the relation into relations array
                relations[rel][sec] = tempNum;
            }
        }

        //tracking & converting index arrays to company names
        int numUniqueCompanies = uniqueCompanies.size();
        ArrayList<Integer> sortCompany = new ArrayList<>(uniqueCompanies);
        Collections.sort(sortCompany);
        //companyIndex stores a company name and the index that represents it in an array
        HashMap<Integer, Integer> companyIndex = new HashMap<>();
        //revCompanyIndex is the opposite: the index holds the company name
        /**
         * Ex:
         * table = (4 companies named 5,6,7,8)
         *     5 6 7 8 (company names)
         *     0 1 2 3 (index in array)
         * (5)0
         * (6)1
         * (7)2
         * (8)3
         *
         */
        int[] revCompanyIndex = new int[numUniqueCompanies];
        int controlIndex = 0;
        for(Integer company : sortCompany){
            revCompanyIndex[controlIndex] = company;
            companyIndex.put(company, controlIndex);
            controlIndex++;
        }

        /**
         * companyArray store the amount of stock a company owns of another company
         *  row = first company (company that owns the stock)
         *  col = second company (company whose stock is owned)
         *  Ex:
         *      companyArray[0][2] = 80 >> company whose index is 0 owns 80 stock of company whose index is 2
         *  control is boolean array that stores whether or not a company owns another company
         *  Ex:
         *      boolean[0][2] = true >> company whose index is 0 owns company whose index is 2
         */
        int[][] companyArray = new int[numUniqueCompanies][numUniqueCompanies];
        boolean[][] control = new boolean[numUniqueCompanies][numUniqueCompanies];
        //using relations array, fill in companyArray stock relations
        for(int rel = 0; rel < relations.length; rel++){
            companyArray[companyIndex.get(relations[rel][0])][companyIndex.get(relations[rel][1])] = relations[rel][2];
        }

        //row is company index
        for(int row = 0; row < numUniqueCompanies; row++){
            //col is company owned by row
            for(int col = 0; col < numUniqueCompanies; col++) {
                /**
                 * conditions are:
                 * row != col
                 *  don't care about companies owning themselves
                 * !control[row][col]
                 *  only need to process company relationships that aren't already decided
                 *  (if a company owns another company already, no need to re-process, it can lead to wrong stock numbers)
                 * companyArray[row][col] > 50
                 *  don't really care if a company doesn't own the other company
                 */
                if((row != col) && (!control[row][col]) && (companyArray[row][col] > 50)){
                    //mark that company (row) owns company (col)
                    control[row][col] = true;
                    /**
                     * iterate through company that is being owned (col) and it's relations
                     *  add all of col's stock to row's stock
                     *  (here is where things can go wrong. if re-processing an owned company, can add multiple times)
                     * reset col to -1 so the loop for col goes again
                     *  adding owned companies stock to col could have revealed more companies that are owned
                     *  iterate again if this is the case
                     *  will stop iterating when there isn't an owned company that hasn't been recorded
                     */
                    for(int subCol = 0; subCol < numUniqueCompanies; subCol++){
                        companyArray[row][subCol] += companyArray[col][subCol];
                        //if col owns a company (subCol), then row owns subCol as well
                        if(control[col][subCol]){
                            control[row][subCol] = true;
                        }
                    }
                    col = -1;
                }
            }
        }

        /**
         * building return string
         * using control array, if a company owns another company
         *  add the relation to the return string
         */
        StringBuilder sb = new StringBuilder();
        for(int row = 0; row < numUniqueCompanies; row++){
            for(int col = 0; col < numUniqueCompanies; col++){
                if(col != row && control[row][col]){
                    sb.append(revCompanyIndex[row] + " " + revCompanyIndex[col] + "\n");
                }
            }
        }

        allControl = sb.toString();

        return allControl;
    }

}
