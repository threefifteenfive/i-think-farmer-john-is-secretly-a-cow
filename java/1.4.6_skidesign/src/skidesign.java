/*
ID: andrew.83
LANG: JAVA
TASK: skidesign
*/

import java.util.*;
import java.io.*;

/**
 * math is like: x*x + y*y + ...
 * so it is parabola and we need to find bottom of it
 * there is no way to calculate exact mean to have min result, so we have to do multiple calculation and find min
 * there are two ways to get it done
 * 1. brute force
 * 2. directional aiming (left,itself,right result shows direction to move) based on nature of parabola (binary search)
 *
 * Results of 2nd method are much better than the results of first method.
 * Initial pure brute force method results:
 * (found average, and then moved left or right by one)
 *    Test 1: TEST OK [0.166 secs, 23576 KB]
 *    Test 2: TEST OK [0.194 secs, 23516 KB]
 *    Test 3: TEST OK [0.105 secs, 23580 KB]
 *    Test 4: TEST OK [0.117 secs, 23948 KB]
 *    Test 5: TEST OK [0.128 secs, 24156 KB]
 *    Test 6: TEST OK [0.124 secs, 24252 KB]
 *    Test 7: TEST OK [0.142 secs, 24160 KB]
 *    Test 8: TEST OK [0.133 secs, 24048 KB]
 *    Test 9: TEST OK [0.131 secs, 23988 KB]
 *    Test 10: TEST OK [0.243 secs, 24452 KB]
 *
 * Revised binary search result:
 * (directional aiming by comparing mid result, and results from left and right to guide down optimization parabola)
 * Executing...
 *    Test 1: TEST OK [0.096 secs, 23688 KB]
 *    Test 2: TEST OK [0.124 secs, 23888 KB]
 *    Test 3: TEST OK [0.107 secs, 23844 KB]
 *    Test 4: TEST OK [0.105 secs, 23696 KB]
 *    Test 5: TEST OK [0.126 secs, 24048 KB]
 *    Test 6: TEST OK [0.145 secs, 24096 KB]
 *    Test 7: TEST OK [0.147 secs, 24132 KB]
 *    Test 8: TEST OK [0.147 secs, 23812 KB]
 *    Test 9: TEST OK [0.142 secs, 24424 KB]
 *    Test 10: TEST OK [0.135 secs, 24560 KB]
 */

/*
stores the height of a hill (maybe we don't actually need this class?)
 */
class hill{

    private int height;

    public hill(int start){
        height = start;
    }

    public int getHeight(){
        return height;
    }

}

public class skidesign {

    public static void main(String[] args) {

        try {
            skidesign sd = new skidesign();
            System.out.println(sd.work("skidesign.in"));
            PrintWriter out = new PrintWriter("skidesign.out");
            out.println(sd.work("skidesign.in"));
            out.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }

    /**
     * returns the minimum sum of the squares of the NECESSARY changes to hills to make the maximum-minimum height <= 17
     * @param filename: name of input file
     * @throws Exception
     * @return:
     */
    public int work(String filename) throws Exception {
        int totalDiff = 0;

        //reading input
        BufferedReader in = new BufferedReader(new FileReader(filename));
        StringTokenizer s0 = new StringTokenizer(in.readLine());
        int numHills = Integer.parseInt(s0.nextToken());
        ArrayList<hill> hillList = new ArrayList<hill>();
        for (int i = 0; i < numHills; i++) {
            StringTokenizer st = new StringTokenizer(in.readLine());
            hillList.add(new hill(Integer.parseInt(st.nextToken())));
        }

        /**
         * Sorting hillList using .sort()
         */
        Collections.sort(hillList, new Comparator<hill>() {
            //inline compare method
            public int compare(hill obj1, hill obj2) {
                return obj1.getHeight() - obj2.getHeight();
            }
        });

        /**
         * "brute force" method. find the average of the data as a starting point.
         * "calculate" the answer using the average as the lower bound.
         * "calculate" the answer using the average+1 as the lower bound.
         * if answer of average+1 is greater, increasing the average is the wrong way on optimization parabola.
         *      decrease average and compute answer.
         *      if it becomes greater than the previous one, the previous one was optimal result.
         * if answer of average+1 is less, increasing the average is the right way on optimization parabola.
         *      increase average and compute answer.
         *      if it becomes greater than the previous one, the previous one is optimal result.
         * return the optimal result
         */

        /*
        //computing initial average
        int avg = 0;
        for (hill h : hillList) {
            avg += h.getHeight();
        }
        avg /= hillList.size();

        //tryResult is the computing method
        int avgResult = tryResult(hillList, avg);
        int nextResult = tryResult(hillList, avg + 1);

        //deciding which direction on optimization parabola to go.
        if (nextResult < avgResult) {
            int prevResult = avgResult;
            int curResult = nextResult;
            while (prevResult > curResult) {
                avg++;
                prevResult = curResult;
                curResult = tryResult(hillList, avg);
            }
            System.out.println(avg);
            totalDiff = curResult;
        } else {
            int prevResult = nextResult;
            int curResult = avgResult;
            while (prevResult > curResult) {
                avg--;
                prevResult = curResult;
                curResult = tryResult(hillList, avg);
            }
            System.out.println(avg);
            totalDiff = prevResult;
        }
        */

        //binary search method (2)
        /**
         * using binary search algo to find the vertex of the optimization parabola
         */
        int midResult = tryResult(hillList, hillList.get(numHills/2).getHeight());
        totalDiff = recursiveMin(hillList, hillList.get(0).getHeight(), hillList.get(numHills-1).getHeight(), midResult);

        return totalDiff;
    }

    /**
     * avg is the lower bound of the range of 17.
     * returns the sum of the squares of the NECESSARY changes to hills to make the maximum-minimum height <= 17
     * @param hillList
     * @param avg
     * @return
     */
    public int tryResult(ArrayList<hill> hillList, int avg) {
        int rt = 0;
        for (hill h : hillList) {
            //decides if change is necessary. Less than avg (low bound) or greather than avg+17 (upper bound)
            if (h.getHeight() < avg || h.getHeight() > (avg + 17)) {
                //decides whether subtracting or adding minimizes the difference.
                if (h.getHeight() <= avg + 9) {
                    rt += (int) Math.pow(avg - h.getHeight(), 2);
                } else {
                    rt += (int) Math.pow(avg + 17 - h.getHeight(), 2);
                }
            }
        }
        return rt;
    }

    /**
     * takes in low and high bounds of possible "lower boundaries" and calculates where to look
     * locates the vertex of optimization parabola quicker than initial method of increment/decrement brute force
     * @param hillList
     * @param low
     * @param high
     * @param curMin
     * @return
     */
    public int recursiveMin(ArrayList<hill> hillList, int low, int high, int curMin){
        //based on binary search, so locating midpoint
        int midPoint = (high-low)/2 + low;
        /**
         * calculates the results on either side of midResult to figure out direction.
         * when approaching minimum, only comparing midResult with one side can "jump" over the minimum
         * left and right results clearly show direction.
         */
        int midResult = tryResult(hillList, midPoint);
        int topResult = tryResult(hillList, midPoint+1);
        int botResult = tryResult(hillList, midPoint-1);
        /**
         * result of midPoint+1 is greater and midPoint-1 is less than midPoint, minimum is located lower than midPoint
         * because direction of minimum is towards 0, take lower half of initial range
         */
        if(topResult > midResult && botResult < midResult){
            return recursiveMin(hillList, low, midPoint, midResult);
        }
        /**
         * result of midPoint+1 is less and midPoint-1 is greater than midPoint, minimum is located higher than midPoint
         * because direction of minimum is towards infinity, take upper half of initial range
         */
        if(topResult < midResult && botResult > midResult){
            return recursiveMin(hillList, midPoint, high, curMin);
        }
        /**
         * termination condition:
         * if the result on the right AND left are greater than midResult, then midResult is the vertex.
         * return midResult
         */
        return midResult;
    }

}