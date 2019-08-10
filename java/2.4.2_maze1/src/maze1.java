/*
ID: andrew.83
LANG: JAVA
TASK: maze1
*/

import java.io.*;
import java.util.*;
import java.time.*;

/**
 * Overfencing (2.4.2)
 *
 * given a maze
 *  guaranteed to have two unique exit points
 *  guaranteed that all points are connected to at least one other point (from any point there is a way out)
 * return the greatest possible distance obtained using the most efficient method
 *  starting from the worst possible point (greatest distance to AN exit), shortest distance to escape
 *
 * Ex:
 * 5 3 (5 is number of points wide, 3 is number of points tall)
 * +-+-+-+-+-+
 * |         |
 * +-+ +-+ + +
 * |     | | |
 * + +-+-+ + +
 * | |     |   <- exit
 * +-+ +-+-+-+
 *    ^- exit
 * +- +- +- +- +- +
 * |1  2  3  4  5 |
 * +- +  +- +  +  +
 * |6  7  8 |9 |10|
 * +  +- +- +  +  +
 * |11|12 13 14 15|
 * +- +  +- +- +- +
 * return 9           1 2 3 4 5 6 7  8  9
 *  from point 11, go 6,7,2,3,4,5,10,15,out
 *
 * First Successful Run:
 *    Test 1: TEST OK [0.147 secs, 23348 KB]
 *    Test 2: TEST OK [0.142 secs, 24024 KB]
 *    Test 3: TEST OK [0.105 secs, 23676 KB]
 *    Test 4: TEST OK [0.105 secs, 24008 KB]
 *    Test 5: TEST OK [0.161 secs, 29468 KB]
 *    Test 6: TEST OK [0.460 secs, 34352 KB]
 *    Test 7: TEST OK [0.289 secs, 31496 KB]
 *    Test 8: TEST OK [0.383 secs, 33268 KB]
 *    Test 9: TEST OK [0.380 secs, 33644 KB]
 *    Test 10: TEST OK [0.401 secs, 32468 KB]
 */

/**
 * Node represents a point and contains its connected points
 * connections is a list of the other points it can reach
 */
class Node{

    ArrayList<Integer> connections;

    public Node(){
        connections = new ArrayList<>();
    }

    public void setConnections(ArrayList<Integer> allConnect){
        connections.addAll(allConnect);
    }

    public ArrayList<Integer> getConnections(){
        return connections;
    }

}

public class maze1 {

    public static void main(String[] args){

        try{
            maze1 m1 = new maze1();
            //System.out.println(m1.work("maze1.in"));
            PrintWriter out = new PrintWriter("maze1.out");
            out.println(m1.work("maze1.in"));
            out.close();
        }catch(Exception e){
            System.out.println(e.toString());
        }

    }

    public int work(String filename) throws Exception{
        int worstPath = 0;

        //PROBLEM HERE
        /*
        int[][] connections = new int[3801][1];
        System.out.println("a");
        int[] tempArray = new int[3801];
        for(int row = 0; row < 3801; row++){
            System.out.println(row);
            connections[row] = Arrays.copyOf(tempArray, 3801);
        }
         */
        //PROBLEM HERE

        /**
         * problem stated that limits were max width of 38 and max height is 100
         * therefore, size of nodes[] can be max of 3801 (38*100+1)
         *  since we're using points 1,2..., row 0 is left unused (greatest node number is 3800)
         * PROBLEM HERE statement above is previous problem
         *  initially created a 2D array (on later cases with max limits, array size was 3801x3801)
         *  my computer is fine but USACO computer ran out of memory (heap space error)
         *  since 2D array was used to convert input into simple connection table
         *      (row 1: all points that are connected to node 1 -> connections[row][connected point] = 1, else 0)
         *      SOLUTION: directly create nodes[] from messy input instead of converting (takes too much memory)
         */
        Node[] nodes = new Node[3801];

        Instant t1s = Instant.now();
        BufferedReader in = new BufferedReader(new FileReader(filename));
        StringTokenizer st = new StringTokenizer(in.readLine());
        int numCol = Integer.parseInt(st.nextToken());
        int numRow = Integer.parseInt(st.nextToken());
        //format of maze means that array dimension = 2*dimension+1
        int rowGrid = 2*numRow+1;
        int colGrid = 2*numCol+1;
        //storing characters in 2D array
        int[][] maze = new int[rowGrid][colGrid];
        int openPoints = 1;
        //storing endPoints (key = row, value = column)
        HashMap<Integer,ArrayList<Integer>> eP = new HashMap<>();
        //store later
        ArrayList<Integer> endPoints = new ArrayList<>();
        //traverse array
        for(int row = 0; row < rowGrid; row++){
            String tempRow = in.readLine();
            //switch statement to interpret symbols
            for(int col = 0; col < colGrid; col++){
                char ch;
                /**
                 * + +-+-+ + +
                 * | |     |<- line ends here
                 * +-+ +-+-+-+
                 * in this case, the exit is on the side, spaces don't extend to the end
                 *  try/catch avoids error
                 */
                try {
                    ch = tempRow.charAt(col);
                }catch(StringIndexOutOfBoundsException sioob){
                    ch = ' ';
                }
                switch (ch){
                    case '+':
                        maze[row][col] = -1;
                        break;
                    case '-':
                        maze[row][col] = -1;
                        break;
                    case '|':
                        maze[row][col] = -1;
                        break;
                    case ' ':
                        //since exits have to be on edges, check by modding by dimensions
                        if(row%(rowGrid-1) == 0 || col%(colGrid-1) == 0){
                            //storing the exit points
                            if(eP.containsKey(row)){
                                eP.get(row).add(col);
                            }else {
                                ArrayList<Integer> tempList = new ArrayList<>();
                                tempList.add(col);
                                eP.put(row, tempList);
                            }
                        }
                        //due to format, points can only exist in odd rows and columns
                        /**
                         * 012345678910
                         * +-+-+-+-+-+ 0
                         * |         | 1
                         * +-+ +-+ + + 2
                         * |     | | | 3
                         * + +-+-+ + + 4
                         * | |     |   5
                         * +-+ +-+-+-+ 6
                         */
                        if(row%2 == 1 && col%2 == 1) {
                            maze[row][col] = openPoints;
                            openPoints++;
                        }else{
                            maze[row][col] = 0;
                        }
                        break;
                }
            }
        }

        //if case:
        /**
         * 1 1
         * +-+
         *
         * +-+
         */
        if(openPoints == 2){
            return 1;
        }

        /**
         * convert the maze coordinates into numbers of the nodes that just before exit
         * +-+-+-+-+-+
         * |         |
         * +-+ +-+ + +
         * |     | | |
         * + +-+-+ + +
         * | |#    |# <-exit
         * +-+ +-+-+-+
         *    ^-exit
         * # is number of the node just before exit
         */
        for(Integer row : eP.keySet()){
            ArrayList<Integer> colList = eP.get(row);
            for(Integer col : colList) {
                if (row == 0) {
                    endPoints.add(maze[row + 1][col]);
                }
                if (row == rowGrid - 1) {
                    endPoints.add(maze[row - 1][col]);
                }
                if (col == 0) {
                    endPoints.add(maze[row][col + 1]);
                }
                if (col == colGrid - 1) {
                    endPoints.add(maze[row][col - 1]);
                }
            }
        }
        Instant t1e = Instant.now();
        Duration d1 = Duration.between(t1s, t1e);
        System.out.println("t1 " + d1.toMillis());
        Instant t2s = Instant.now();

        /**
         * filling out the nodes array and creating all connections
         */
        for(int row = 1; row < rowGrid; row+=2){
            for(int col = 1; col < colGrid; col+=2){
                int point = maze[row][col];
                nodes[point] = new Node();
                ArrayList<Integer> tempConnections = new ArrayList<>();
                if(maze[row][col] != -1){
                    if(row > 2 && maze[row-1][col] != -1){
                        tempConnections.add(maze[row-2][col]);
                    }
                    if(row < rowGrid-2 && maze[row+1][col] != -1){
                        tempConnections.add(maze[row+2][col]);
                    }
                    if(col > 2 && maze[row][col-1] != -1){
                        tempConnections.add(maze[row][col-2]);
                    }
                    if(col < colGrid-2 && maze[row][col+1] != -1){
                        tempConnections.add(maze[row][col+2]);
                    }
                }
                nodes[point].setConnections(tempConnections);
            }
        }

        Instant t2e = Instant.now();
        Duration d2 = Duration.between(t2s, t2e);
        System.out.println("t2 " + d2.toMillis());

        Instant t3s = Instant.now();


        /**
         * Djikstra's Algorithm
         *
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
         *
         * faster than Floyd-Warshall becuase there are only two loops in main body of code
         *  therefore operates in O(V^2) time
         *  in current problem, faster to run Djikstra twice instead of Floyd-Warshall once
         *  2 x O(V^2) still < O(V^3)
         */
        int[][] compareDist = new int[3][openPoints];
        //compareDist[0] are distances from first endpoint, compareDist[1] are distances from second endpoint
        //compareDist[2] is for comparison of first two arrays
        int cD = 0;
        //try each endpoint
        for(Integer endPoint : endPoints){
            int[] distance = new int[openPoints];
            //set the initial connections between everything to an unreachable number (openPoints is total points + 1)
            Arrays.fill(distance, openPoints);
            //have to have a visited[] array to track what has been visited
            boolean[] visited = new boolean[openPoints];
            //tracking parents for reconstruction (NOT required for this problem but usually important)
            int[] parent = new int[openPoints];

            //since tracking distances to the endPoint, the endPoint is starting point and set to 0, no parent either
            distance[endPoint] = 0;
            parent[endPoint] = 0;

            int visitedNodes = 1;
            int markNode = endPoint;
            //run until all nodes are officially visited
            while(visitedNodes < openPoints){
                //minDist is to find the point with minimum distance to source point but is also unvisited
                int minDist = openPoints;
                //find the next point (closest to source but also unvisited so far)
                for(int i = 1; i < openPoints; i++){
                    if(!visited[i] && distance[i] < minDist){
                        markNode = i;
                        minDist = distance[i];
                    }
                }
                //update visited node, set to true
                visited[markNode] = true;
                visitedNodes++;
                //update all connections of the marker node, see if markNode is an alternative and shorter path for them
                for(int node : nodes[markNode].getConnections()){
                    if(distance[markNode] + 1 < distance[node]) {
                        distance[node] = distance[markNode] + 1;
                        parent[node] = markNode;
                    }
                }
            }
            //store the array
            compareDist[cD] = Arrays.copyOf(distance, openPoints);
            cD++;
        }

        //compare the two arrays, for each point, take the minimum route to an exit
        /**
         * if point 4 needs 14 moves to get to escape point #1 but only 11 moves to get to escape point #2
         *  set distance to the minimum moves (11 in this case)
         * return the maximum of minimum moves to reach AN escape point
         */
        for(int dist = 1; dist < openPoints; dist++){
            worstPath = Math.max(Math.min(compareDist[0][dist],compareDist[1][dist]), worstPath);
        }
        Instant t3e = Instant.now();
        Duration d3 = Duration.between(t3s, t3e);
        System.out.println("t3 " + d3.toMillis());

        /**
         * Floyd-Warshall Algorithm
         * # dist(i,j) is "best" distance so far from vertex i to vertex j
         *
         * # Start with all single edge paths.
         * For i = 1 to n do
         *     For j = 1 to n do
         *         dist(i,j) = weight(i,j)
         *
         * For k = 1 to n do	# k is the `intermediate' vertex
         *     For i = 1 to n do
         *         For j = 1 to n do
         *             if (dist(i,k) + dist(k,j) < dist(i,j)) then	# shorter path?
         *                 dist(i,j) = dist(i,k) + dist(k,j)
         *
         * NOTE:
         * doesn't work because it operates in O(V^3) (3 loops)
         *  given the same case (625 points) it runs over by about 0.1 seconds (avg ~1100 ms)
         *  big improvement over initial BFS implementation but still grows too much
         * WHY:
         *  Floyd-Warshall finds the lengths of the shortest paths among ALL other points
         *      works when paths DON'T need to be recorded (not a problem here)
         *      stores the minimum distance between EVERY possible pair of point (here is the problem)
         *  we're recording all paths when trying to find only two

        int[][] distance = new int[openPoints][openPoints];
        for(int point = 1; point < openPoints; point++){
            for(int connect = 1; connect < openPoints; connect++){
                distance[point][connect] = connections[point][connect];
            }
        }
        for(int inter = 1; inter < openPoints; inter++){
            for(int point = 1; point < openPoints; point++){
                for(int connect = 1; connect < openPoints; connect++){
                    if(connect != point && (distance[point][inter]+distance[inter][connect]) < distance[point][connect]){
                        distance[point][connect] = distance[point][inter] + distance[inter][connect];
                    }
                }
            }
        }

        int firstEP = endPoints.get(0);
        int secondEP = endPoints.get(1);
        for(int row = 1; row < openPoints; row++){
            int tempDistance = Math.min(distance[row][firstEP], distance[row][secondEP]);
            worstPath = Math.max(worstPath, tempDistance);
        }

        */

        /**
         * BFS implementation
         * Since there is no weight on the edges, BFS also works
         *  however, BFS is strongest with many vertices and few edges (many points but few connections)
         *  given the a case with 25 rows, 25 cols and walls on the outside only
         *  there are 625 points, with over 1000 connections (BFS therefore runs over time) (avg ~2300 ms)

        LinkedList<Integer> queue = new LinkedList<>();
        int[][] endPointCompare = new int[3][openPoints];
        int ePC = 0;
        for(int endPoint : endPoints){
            boolean[] visitedPoints = new boolean[openPoints];
            int[] distance = new int[openPoints];
            int[] parent = new int[openPoints];
            distance[endPoint] = 0;
            parent[endPoint] = 0;
            queue.add(endPoint);
            while(!queue.isEmpty()){
                int point = queue.removeFirst();
                visitedPoints[point] = true;
                distance[point] = distance[parent[point]]+1;
                for(int connect = 1; connect < openPoints; connect++){
                    if(!visitedPoints[connect] && connections[point][connect] == 1) {
                        queue.add(connect);
                        parent[connect] = point;
                    }
                }
            }
            endPointCompare[ePC] = Arrays.copyOf(distance, openPoints);
            ePC++;
        }

        for(int point = 1; point < openPoints; point++){
            int close = Math.min(endPointCompare[0][point], endPointCompare[1][point]);
            endPointCompare[2][point] = close;
        }

        for(int dist : endPointCompare[2]){
            worstPath = Math.max(worstPath, dist);
        }

         */

        return worstPath+1;
    }

}
