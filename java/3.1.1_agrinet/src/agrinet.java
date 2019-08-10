/*
ID: andrew.83
LANG: JAVA
TASK: agrinet
*/

import java.io.*;
import java.util.*;

/**
 * Agri-Net (USACO 3.1.1)
 *
 * given a series of connected points
 * return the minimum length to connect all points
 *  all points must be connected but do not necessarily need to be in order
 * Ex:
 *   A  B  C  D
 * A 0  4  9  21  (A is connected to B by distance 4, to C by 9, to D by 21)
 * B 4  0  8  17
 * C 9  8  0  16
 * D 21 17 16 0
 * return 28
 *  A-4->B-8->C-16->D
 * (Like shortest paths)
 * NOTE: diagonals are ignored (A is 0 away from A)
 *
 * First Successful Runs
 *    Test 1: TEST OK [0.093 secs, 23580 KB]
 *    Test 2: TEST OK [0.089 secs, 23832 KB]
 *    Test 3: TEST OK [0.091 secs, 23428 KB]
 *    Test 4: TEST OK [0.091 secs, 23764 KB]
 *    Test 5: TEST OK [0.096 secs, 23812 KB]
 *    Test 6: TEST OK [0.107 secs, 23904 KB]
 *    Test 7: TEST OK [0.124 secs, 24156 KB]
 *    Test 8: TEST OK [0.138 secs, 25232 KB]
 *    Test 9: TEST OK [0.152 secs, 25500 KB]
 *    Test 10: TEST OK [0.187 secs, 26560 KB]
 */
public class agrinet {

    static int MAX = Integer.MAX_VALUE;

    public static void main(String[] args){

        try{
            agrinet an = new agrinet();
            //System.out.println(an.work("agrinet.in"));
            PrintWriter out = new PrintWriter("agrinet.out");
            out.println(an.work("agrinet.in"));
            out.close();
        }catch(Exception e){
            System.out.println(e.toString());
        }

    }

    public int work(String filename) throws Exception{
        int minLength = 0;

        /**
         * reading input
         * store everything in a 2D array
         * instead of custom node class, just used an array of ArrayList
         * number nodes is the numFarms
         *  nodes named 0,1...numFarms-1
         *  matches indexes of connectionMatrix and nodeArray
         */
        BufferedReader in = new BufferedReader(new FileReader(filename));
        int numFarms = Integer.parseInt(in.readLine());
        int[][] connectionMatrix = new int[numFarms][numFarms];
        ArrayList<Integer>[] nodeArray = new ArrayList[numFarms];
        for(int i = 0; i < numFarms; i++){
            StringTokenizer st = new StringTokenizer(in.readLine());
            nodeArray[i] = new ArrayList<>();
            for(int j = 0; j < numFarms; j++){
                int d;
                try {
                    d = Integer.parseInt(st.nextToken());
                }catch(Exception NoSuchElementException){
                    st = new StringTokenizer(in.readLine());
                    d = Integer.parseInt(st.nextToken());
                }
                connectionMatrix[i][j] = d;
                if(j != i) {
                    nodeArray[i].add(j);
                }
            }
        }

        /**
         * similar to Dijkstra's initialization
         * distance[] stores min distance from the point to any other point
         *  distance[i] will store the lowest connection from i to any other point
         * source[] stores the source node that creates the shortest connection to the point
         *  source[i] stores the index of the point that has the lowest connection to i
         * intree[] records whether or not a point has been added to the tree (processed or not)
         */
        int[] distance = new int[numFarms];
        int[] source = new int[numFarms];
        boolean[] intree = new boolean[numFarms];
        Arrays.fill(distance, MAX);
        Arrays.fill(source, -1);

        /**
         * MINIMAL SPANNING TREE PSUEDOCODE
         *   # distance(j) is distance from tree to node j
         *   # source(j) is which node of so-far connected MST
         *   #                      is closest to node j
         *  1   For all nodes i
         *  2     distance(i) = infinity        # no connections
         *  3     intree(i) = False             # no nodes in tree
         *  4     source(i) = nil
         *
         *  5   treesize = 1                    # add node 1 to tree
         *  6   treecost = 0
         *  7   intree(1) = True
         *  8   For all neighbors j of node 1   # update distances
         *  9      distance(j) = weight(1,j)
         * 10     source(j) = 1
         *
         * 11   while (treesize < graphsize)
         * 12     find node with minimum distance to tree; call it node i
         * 13     assert (distance(i) != infinity, "Graph Is Not Connected")
         *
         *     # add edge source(i),i to MST
         * 14     treesize = treesize + 1
         * 15     treecost = treecost + distance(i)
         * 16     intree(i) = True              # mark node i as in tree
         *
         *     # update distance after node i added
         * 17     for all neighbors j of node i
         * 18       if (distance(j) > weight(i,j))
         * 19         distance(j) = weight(i,j)
         * 20         source(j) = i
         */

        int treesize = 1;
        int treecost = 0;
        intree[0] = true;
        for(Integer connected : nodeArray[0]){
            if(connected != 0) {
                distance[connected] = connectionMatrix[0][connected];
                source[connected] = 0;
            }
        }

        while(treesize < numFarms){
            int markNode = 0;
            for(int closestNode = 0; closestNode < numFarms; closestNode++){
                if(!intree[closestNode] && distance[closestNode] < distance[markNode]){
                    markNode = closestNode;
                }
            }
            if(distance[markNode] == MAX){
                break;
            }else{
                treesize++;
                treecost += distance[markNode];
                intree[markNode] = true;
                for(int neighbor : nodeArray[markNode]){
                    if(!intree[neighbor] && connectionMatrix[markNode][neighbor] < distance[neighbor]){
                        distance[neighbor] = connectionMatrix[markNode][neighbor];
                        source[neighbor] = markNode;
                    }
                }
            }
        }

        minLength = treecost;

        return minLength;
    }

}
