/*
ID: andrew.83
LANG: JAVA
TASK: holstein
*/

import java.io.*;
import java.util.*;

/**
 * Healthy Holsteins (2.1.4)
 * given a number of types of vitamins and a minimum requirement for each vitamin and several feeds
 *  -each feed contains a certain number of units to fulfill the vitamin requirements
 *  -each feed can only be used once (one scoop)
 * return the least number of feeds to be used and the feeds with the lowest number to fulfill the vitamin requirements
 * Ex:
 *  4 (number of vitamin types)
 *  100 200 300 400 (requirement for each type of vitamin)
 *  3 (number of feeds)
 *  50   50  50  50 (feed 1 distribution)
 *  200 300 200 300 (feed 2 distribution)
 *  900 150 389 399 (feed 3 distribution)
 *  2 1 3 (2 feed types are used) (sorted list of the lowest numbered feeds used)
 *
 * first successful test results
 *    Test 1: TEST OK [0.168 secs, 23912 KB]
 *    Test 2: TEST OK [0.149 secs, 24020 KB]
 *    Test 3: TEST OK [0.103 secs, 24068 KB]
 *    Test 4: TEST OK [0.110 secs, 23532 KB]
 *    Test 5: TEST OK [0.100 secs, 23480 KB]
 *    Test 6: TEST OK [0.142 secs, 24972 KB]
 *    Test 7: TEST OK [0.138 secs, 24872 KB]
 *    Test 8: TEST OK [0.215 secs, 32160 KB]
 *    Test 9: TEST OK [0.126 secs, 24092 KB]
 *    Test 10: TEST OK [0.686 secs, 43628 KB]
 */

/**
 * stores the distribution towards the vitamin requirements for the feed
 */
class Feed{

    private ArrayList<Integer> vitaDistribution;

    public Feed(){
        vitaDistribution = new ArrayList<>();
    }

    public void addDistribution(int vita){
        vitaDistribution.add(vita);
    }

    public ArrayList<Integer> getVitaDistribution(){
        return vitaDistribution;
    }

}

/**
 * stores the combined distribution of vitamins (vitaDistribution)
 * stores the sorted list of feeds used to create the combo (comboList)
 * makeCopy() is used in the queue to "clone" and make a deep copy of the object
 * isSatisfied() tests the combo is enough to fulfill the requirements
 * compareCombo() sums the indexes of the feeds in the comboList for use in custom comparator
 */
class Combo implements Cloneable{

    private int[] vitaDistribution;
    private ArrayList<Integer> comboList;

    public Combo makeCopy(){
        Combo rt = new Combo();
        ArrayList<Integer> cList = (ArrayList<Integer>)comboList.clone();
        rt.setComboList(cList);
        int[] vDist = vitaDistribution.clone();
        rt.setVitaDistribution(vDist);
        return rt;
    }

    public Combo(){}

    public Combo(int numVita){
        vitaDistribution = new int[numVita];
        comboList = new ArrayList<>();
    }

    public void addFeed(Feed scoop, int feedID){
        ArrayList<Integer> tempList = scoop.getVitaDistribution();
        for(int i = 0; i < tempList.size(); i++){
            vitaDistribution[i] += tempList.get(i);
        }
        comboList.add(feedID+1);
        Collections.sort(comboList);

    }

    public boolean isSatisfied(int[] vitaRequirements){
        for(int i = 0; i < vitaDistribution.length; i++){
            if(vitaDistribution[i] < vitaRequirements[i]){
                return false;
            }
        }
        return true;
    }

    public ArrayList<Integer> getComboList(){
        return comboList;
    }

    public int compareCombo(){
        int sum = 0;
        for(Integer i : comboList){
            sum += i;
        }
        return sum;
    }

    public String toString(){
        String rt = "";
        rt += comboList.size();
        for(Integer id : comboList){
            rt += (" " + id);
        }
        return rt;
    }

    public void setVitaDistribution(int[] vDist){
        vitaDistribution = vDist;
    }

    public void setComboList(ArrayList<Integer> cList){
        comboList = cList;
    }

    public int getNumList(){
        return comboList.size();
    }

}

/**
 * for sorting the resulting list of results
 *  prioritizes less number of feeds used first
 *  prioritizes lowest sum of feeds next
 */
class SortCombo implements Comparator<Combo>{

    public int compare(Combo c1, Combo c2){
        if(c1.getNumList() != c2.getNumList()){
            return c1.getNumList()-c2.getNumList();
        }
        return c1.compareCombo() - c2.compareCombo();
    }

}

public class holstein {

    public static void main(String[] args){

        try{
            holstein h = new holstein();
            //System.out.println(h.work("holstein.in"));
            PrintWriter out = new PrintWriter("holstein.out");
            out.println(h.work("holstein.in"));
            out.close();
        }catch(Exception e){
            System.out.println(e.toString());
        }

    }

    public String work(String filename)throws Exception{
        String rt = "";

        /**
         * reading input and storing the data
         * vitaRequirements (array for easy access) stores minimum requirements to fulfill
         * allFeeds (array for easy access) stores all of the Feed objects
         */
        BufferedReader in = new BufferedReader(new FileReader(filename));
        StringTokenizer st = new StringTokenizer(in.readLine());
        int numVitamins = Integer.parseInt(st.nextToken());
        int[] vitaRequirements = new int[numVitamins];
        st = new StringTokenizer(in.readLine());
        for(int v = 0; v < numVitamins; v++){
            vitaRequirements[v] = Integer.parseInt(st.nextToken());
        }
        st = new StringTokenizer(in.readLine());
        int numFeeds = Integer.parseInt(st.nextToken());
        Feed[] allFeeds = new Feed[numFeeds];
        for(int f = 0; f < numFeeds; f++){
            st = new StringTokenizer(in.readLine());
            Feed temp = new Feed();
            for(int v = 0; v < numVitamins; v++){
                temp.addDistribution(Integer.parseInt(st.nextToken()));
            }
            allFeeds[f] = temp;
        }

        /**
         * resultList contains the successful combos
         * queue is used to implement BFS to find minimum combination
         *  initial combos use each Feed object as a starting point
         */
        ArrayList<Combo> resultList = new ArrayList<>();
        LinkedList<Combo> queue = new LinkedList<Combo>();
        for(int f = 0; f < allFeeds.length; f++){
            Combo temp = new Combo(numVitamins);
            temp.addFeed(allFeeds[f], f);
            queue.add(temp);
        }

        /**
         * processing the queue. flag used to signal end of the successful tier
         *  any successful combinations in next tier don't matter because they will require more Feeds to be used
         * pop first object in queue, test if meets requirements
         *  store in resultList and signal that this tier is where processing stops
         *  if not, create the next combo by adding one the following possible feeds
         *      Ex:
         *      6 feeds (1,2,3,4,5,6)
         *      2(numfeeds) 1,3 (ids of feeds)-> 3 1,3,4 | (3 1,3,5) | 3 1,3,6
         *      grab the feeds after (3) the max (id) in order to avoid overlapping cases (avoid identical combos)
         *      Ex: (possible identical combo case without)
         *      2 1,5 -> 3 1,2,5 | (3 1,3,5) already covered by previous | 3 1,4,5 | 3 1,5,6
         *      -> 2 1,5 -> only grab 6 -> 3 1,5,6 (previous combos are already covered)
         */
        boolean flag = true;
        while(queue.size() != 0){
            Combo tempCombo = queue.removeFirst();
            String tempID = tempCombo.toString();
            if(tempCombo.isSatisfied(vitaRequirements)){
                resultList.add(tempCombo);
                flag = false;
            }
            if(flag){
                ArrayList<Integer> tempList = tempCombo.getComboList();
                for(int i = tempList.get(tempList.size()-1); i < numFeeds; i++){
                    if(!tempList.contains(i+1)){
                        //NEED to make a deep copy because of building off
                        Combo copy = tempCombo.makeCopy();
                        copy.addFeed(allFeeds[i], i);
                        queue.add(copy);
                    }
                }
            }else{
                break;
            }
        }

        //sorting using custom comparator, minimum will be at the beginning
        Collections.sort(resultList, new SortCombo());

        rt += resultList.get(0).toString();

        return rt;
    }

}
