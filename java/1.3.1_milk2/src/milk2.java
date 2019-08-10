/*
ID: andrew.83
LANG: JAVA
TASK: milk2
*/

import java.io.*;
import java.util.*;


class range{

    int myStart;
    int myEnd;

    public range(int start, int end) {
        myStart = start;
        myEnd = end;
    }

    public int getStart() {
        return myStart;
    }

    public int getEnd() {
        return myEnd;
    }

}

public class milk2 {

    public static void main(String[] args) {

        try {
            milk2 m = new milk2();
            PrintWriter out = new PrintWriter("milk2.out");
            out.println(m.work("milk2.in"));
            out.close();
            System.out.println(m.work("milk2.in"));
        }catch(Exception e) {
            System.out.println(e.toString());
        }

    }

    public void insert(TreeMap<Integer, ArrayList<Integer>> order, int start, int end) {
        if(order.get(end) == null) {
            ArrayList<Integer> temp = new ArrayList<Integer>();
            temp.add(start);
            order.put(end, temp);
        }else {
            order.get(end).add(start);
        }
    }

    public String work(String filename) throws Exception{
        String rt = "";

        BufferedReader in = new BufferedReader(new FileReader(filename));
        StringTokenizer s0 = new StringTokenizer(in.readLine());
        int numFarmers = Integer.parseInt(s0.nextToken());

        //ordering
        TreeMap<Integer, ArrayList<Integer>> order = new TreeMap<Integer, ArrayList<Integer>>();
        for(int i = 0; i < numFarmers; i++) {
            String line = in.readLine();
            StringTokenizer st = new StringTokenizer(line);
            int start = Integer.parseInt(st.nextToken());
            int end = Integer.parseInt(st.nextToken());
            insert(order, start, end);

        }
        ArrayList<Integer> ends = new ArrayList<Integer>();
        for(Integer end : order.keySet()) {
            ends.add(end);
        }

        //maximum continous milking and one farmer case
        range[] farmList = new range[numFarmers];
        int index = 0;
        for(Map.Entry<Integer, ArrayList<Integer>> entry : order.entrySet()) {
            ArrayList<Integer> possStart = entry.getValue();
            int end = entry.getKey();
            int start = 1000000;
            for(Integer s : possStart) {
                start = Math.min(s, start);
            }
            int previousEnd = end;
            farmList[index] = new range(start, end);
            index++;
        }

        //case of one farmer
        if(numFarmers == 1) {
            rt += farmList[0].getEnd() - farmList[0].getStart() + " " + 0;
            return rt;
        }

        //for maximum period of no activity
        int maxNone = 0;
        int tStart  = getMinStart(order.get(ends.get(ends.size()-1)));
        for(int revEnd = ends.size()-1; revEnd > 0; revEnd--) {
            int prevEnd = ends.get(revEnd-1);
            if(prevEnd < tStart) {
                maxNone = Math.max(maxNone, tStart - prevEnd);
                tStart = getMinStart(order.get(ends.get(revEnd-1)));
            }else {
                int prevStart = getMinStart(order.get(ends.get(revEnd-1)));
                if(prevStart < tStart) {
                    tStart = prevStart;
                }
            }
        }

        //for maximum continuous milking
        int maxTime = 0;

        int testEnd = -1;
        int testStart = -1;
        for(int i = index - 1; i > 0; i--) {
            int currentEnd = farmList[i].getEnd();
            int currentStart = farmList[i].getStart();
            if(testEnd == -1) {
                testEnd = currentEnd;
                testStart = currentStart;
            }

            int prevEnd = farmList[i-1].getEnd();
            int prevStart = farmList[i-1].getStart();
            if(prevEnd < testStart) {
                maxTime = Math.max(maxTime, testEnd - testStart);
                testEnd = -1;
                testStart = -1;
            }else {
                if(prevEnd >= testStart) {
                    if(prevStart < testStart) {
                        testStart = prevStart;
                    }
                    maxTime = Math.max(maxTime, testEnd - testStart);
                }
            }
        }

        rt += maxTime + " " + maxNone;
        return rt;
    }

    public int getMinStart(ArrayList<Integer> posStart) {
        int min = posStart.get(0);
        for(Integer i : posStart) {
            min = Math.min(min, i);
        }
        return min;
    }

}
