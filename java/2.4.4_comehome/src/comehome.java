/*
ID: andrew.83
LANG: JAVA
TASK: comehome
*/

import java.io.*;
import java.util.*;

/**
 * Bessie Come Home (2.4.4)
 *
 * given a series of connections between pastures
 * return the shortest distance to the barn (source point) and the closest pasture
 *
 * basically just application of Djikstra's algorithm
 * pastures are labelled as 'a'-'z' or 'A'-'Y'
 *  barn (source/destination) is always 'Z'
 *  cows always start in pastures with uppercase labels (1 cow per pasture)
 * Ex:
 * 5     (# of connections)
 * A d 6 (pasture A is connected to pasture d by a path with distance of 6)
 * B d 3
 * C e 9
 * d Z 8
 * e Z 3
 *
 * (cows start from A,B,C)
 * A-6-d-3-B
 *     |
 *     8
 *     |
 *     Z-3-e-9-C
 * return B 11
 * B->d->Z = 3+8
 * A->d->Z = 6+8
 * C->e->Z = 9+3
 *
 * First Successful Runs
 *    Test 1: TEST OK [0.091 secs, 23616 KB]
 *    Test 2: TEST OK [0.096 secs, 23548 KB]
 *    Test 3: TEST OK [0.096 secs, 23572 KB]
 *    Test 4: TEST OK [0.098 secs, 23912 KB]
 *    Test 5: TEST OK [0.138 secs, 24112 KB]
 *    Test 6: TEST OK [0.198 secs, 27004 KB]
 *    Test 7: TEST OK [0.233 secs, 30304 KB]
 *    Test 8: TEST OK [0.096 secs, 23836 KB]
 *    Test 9: TEST OK [0.093 secs, 23684 KB]
 */

/**
 * for implementation of Djikstra's algorithm
 *  in future, could simply use an array of Arraylist/HashSet, since all that's needed is storing child nodes
 *  HashSet used to store unique children (input can have multiple paths between pastures, don't want repeats)
 */
class Node{

    HashSet<Integer> connections;

    public Node(){ connections = new HashSet<>(); }

    public void addConnections(int newConnect){ connections.add(newConnect); }

    public HashSet<Integer> getConnections(){ return connections; }

}

public class comehome {

    /**
     * adjacencyMatrix store the direct connections between all paths
     *  charToIndex stores the indexes of each pasture location
     *  'a' = 0, 'b' = 1, 'c' = 2 ... 'A' = 26, 'B' = 27 ... 'Z' = 51
     *  include Z because it is still a point that needs to be calculated
     * adjacencyMatrix indexes [0,51] represents the 52 letter names of the pastures
     *  since there is limit on the number of pastures, simple to initialize constant array size
     */
    HashMap<Character, Integer> charToIndex;
    char[] letters={'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
            'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
    int[][] adjacencyMatrix;
    int MAX = Integer.MAX_VALUE;

    public comehome(){
        charToIndex = new HashMap<>();
        for(int i = 0; i < letters.length; i++){
            charToIndex.put(letters[i], i);
        }
        adjacencyMatrix = new int[letters.length][letters.length];
    }

    public static void main(String[] args){

        try{
            comehome ch = new comehome();
            //System.out.println(ch.work("comehome.in"));
            PrintWriter out = new PrintWriter("comehome.out");
            out.println(ch.work("comehome.in"));
            out.close();
        }catch(Exception e){
            System.out.println(e.toString());
        }

    }

    public String work(String filename) throws Exception{
        String rt = "";
        String cowName = "";
        int minTime = MAX;

        BufferedReader in = new BufferedReader(new FileReader(filename));
        StringTokenizer st = new StringTokenizer(in.readLine());
        int numPaths = Integer.parseInt(st.nextToken());
        /**
         * reading input
         * NOTE: (two important things)
         *  important to make sure paths are two ways
         *  Ex:
         *      A d 6
         *      adjacencyMatrix[26][3] = 6 BUT ALSO adjacencyMatrix[3][26] = 6
         *  also important to make sure paths are the minimum (multiple connections)
         *  Ex:
         *      input may have
         *      B l 17
         *      B l 14
         *      adjacencyMatrix[27][11] & adjacencyMatrix[11][27] = min(14,17)
         */
        for(int i = 0; i < numPaths; i++){
            st = new StringTokenizer(in.readLine());
            int row = charToIndex.get(st.nextToken().charAt(0));
            int col = charToIndex.get(st.nextToken().charAt(0));
            int val = Integer.parseInt(st.nextToken());
            if(adjacencyMatrix[row][col] == 0){
                adjacencyMatrix[row][col] = val;
            }else if(adjacencyMatrix[row][col] != 0 && val < adjacencyMatrix[row][col]){
                adjacencyMatrix[row][col] = val;
            }
        }
        /**
         * creating array of nodes to keep track of children
         *  run through each row of adjacencyMatrix, if there is a connection, record the connection
         *  NOTE: this is 2-way, so have to make sure that both nodes involved are updated
         *      this is why Node uses HashSet<>
         */
        Node[] nodeArray = new Node[52];
        for(int i = 0; i < nodeArray.length; i++){
            nodeArray[i] = new Node();
        }
        int numUniqueNodes = 0;
        for(int r = 0; r < adjacencyMatrix.length; r++){
            boolean used = false;
            for(int c = 0; c < adjacencyMatrix.length; c++){
                if(adjacencyMatrix[r][c] != 0){
                    used = true;
                    nodeArray[r].addConnections(c);
                    nodeArray[c].addConnections(r);
                    if(adjacencyMatrix[c][r] != 0 && adjacencyMatrix[c][r] < adjacencyMatrix[r][c]){
                        adjacencyMatrix[r][c] = adjacencyMatrix[c][r];
                    }else {
                        adjacencyMatrix[c][r] = adjacencyMatrix[r][c];
                    }
                }
            }
            if(used){
                numUniqueNodes++;
            }
        }

        /**
         * initializing arrays for Djikstra's algorithm
         * distance[] stores distance from each pasture to barn
         *  distance[0] = min distance from pasture 'a' to barn 'Z'
         * parent[] stores the parents for each pasture (only necessary for recreating paths, not necessary for this problem)
         *  parent[0] = parent node that gives the least distance to barn 'Z'
         * visited[] tells whether or not a node has already been visited
         * nodesVisited is marker to stop while loop
         */
        int[] distance = new int[52];
        Arrays.fill(distance, MAX);
        int[] parent = new int[52];
        Arrays.fill(parent, -1);
        boolean[] visited = new boolean[52];
        int nodesVisited = 1;

        //since source node is 'Z', disance[51] is set to 0
        /**
         * # distance(j) is distance from source vertex to vertex j
         * # parent(j) is the vertex that precedes vertex j in any shortest path
         * #                  (to reconstruct the path subsequently)
         *
         *  1 For all nodes i
         *  2     distance(i) = infinity         	# not reachable yet
         *  3     visited(i) = False
         *  4     parent(i) = nil	# no path to vertex yet
         *
         *  5 distance(source) = 0	# source -> source is start of all paths
         *  6 parent(source) = nil
         *  7   8 while (nodesvisited < graphsize)
         *  9     find unvisited vertex with min distance to source; call it vertex i
         * 10     assert (distance(i) != infinity, "Graph is not connected")
         *
         * 11     visited(i) = True	# mark vertex i as visited
         *
         *     # update distances of neighbors of i
         * 12     For all neighbors j of vertex i
         * 13         if distance(i) + weight(i,j) < distance(j) then
         * 14             distance(j) = distance(i) + weight(i,j)
         * 15             parent(j) = i
         */
        distance[51] = 0;
        parent[51] = 51;
        int markNode = 51; //initialization here but will be set to 51 again inside while loop
        while(nodesVisited < numUniqueNodes){
            int minDist = MAX;
            //first time around, since source node distance is 0, will start markNode at source node
            //will update all children, markNode will find next closest node that isn't visited
            for(int i = 0; i < distance.length; i++){
                if(!visited[i] && distance[i] < minDist){
                    markNode = i;
                    minDist = distance[i];
                }
            }
            //if there are no nodes that haven't been visited, break the loop because we're done
            if(minDist != MAX){
                //else update visited[markNode]
                visited[markNode] = true;
                //update all of markNode's children
                for(int connected : nodeArray[markNode].getConnections()){
                    //set distance of markNode's children to shortest path
                    /**
                     * should be (shortest path from source to markNode + shortest path from markNode to child)
                     * shortest path from markNode to child is already determined when reading input for adjacencyMatrix
                     * shortest path from source to markNode has already been calculated
                     */
                    if(distance[markNode] + adjacencyMatrix[connected][markNode] < distance[connected]){
                        distance[connected] = distance[markNode] + adjacencyMatrix[connected][markNode];
                        parent[connected] = markNode;
                    }
                }
            }else{
                break;
            }
        }

        /**
         * since cows can only start in uppercase pastures, only check distances from pastures 'A'(26) to 'Y'(50)
         *  simple for loop to find minimum distance and id of pasture
         */
        int pastureID = 51;
        for(int i = 50; i > 25; i--){
            if(distance[i] < minTime){
                minTime = distance[i];
                pastureID = i;
            }
        }
        cowName += letters[pastureID];


        rt = cowName + " " + minTime;
        return rt;
    }

}
